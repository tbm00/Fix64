package dev.tbm00.fix64;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ChunkUnloader {
    private final Fix64 fix64;
    private FileConfiguration fileConfiguration;
    private boolean unloaderEnabled;
    private int ticksBetween;
    private int chunkRadius;

    public ChunkUnloader(FileConfiguration fileConfiguration, Fix64 fix64) {
        this.fileConfiguration = fileConfiguration;
        this.fix64 = fix64;
        loadConfig();
        startChunkSchedule();
    }

    public void loadConfig() {
        try { 
            this.unloaderEnabled = fileConfiguration.getBoolean("chunkUnloader.enabled");
        } catch (Exception e) {
            fix64.getLogger().warning("Exception getting chunkUnloader.enabled!");
			return;
        }
        try { 
            this.ticksBetween = fileConfiguration.getInt("chunkUnloader.timer") * 20;
        } catch (Exception e) {
            fix64.getLogger().warning("Exception getting chunkUnloader.timer!");
			return;
        }
        try { 
            this.chunkRadius = fileConfiguration.getInt("chunkUnloader.radius");
        } catch (Exception e) {
            fix64.getLogger().warning("Exception getting chunkUnloader.radius!");
			return;
        }
    }

    private void startChunkSchedule() {
        if (!unloaderEnabled) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                //sfix64.getLogger().info("[auto] Unloading empty chunks...");
                try {
                    int chunksUnloaded = unloadChunks();
                    fix64.getLogger().info("[auto] Unloaded " + chunksUnloaded + " empty chunks!");
                } catch (Exception e) {
                    fix64.getLogger().warning("[auto] Exception... could not unload chunks!");
                    e.printStackTrace();
                }
            }
        }.runTaskTimer(fix64, 0L, ticksBetween);
        fix64.getLogger().info("Started ChunkUnloader!");
    }

    private int unloadChunks() {
        int count = 0;
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                if (!isChunkInUse(chunk)) {
                    chunk.unload(true);
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isChunkInUse(Chunk chunk) {
        int chunkX = chunk.getX(),
            chunkZ = chunk.getZ(),
            radiusSquared = chunkRadius*chunkRadius;
        World world = chunk.getWorld();
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getWorld().equals(world)) continue;

            Chunk playerChunk = player.getLocation().getChunk();
            int playerChunkX = playerChunk.getX(),
                playerChunkZ = playerChunk.getZ();
            int dx = chunkX - playerChunkX,
                dz = chunkZ - playerChunkZ;
            int distanceSquared = dx * dx + dz * dz;
            if (distanceSquared <= radiusSquared) return true;
        }

        return false;
    }

    public void reloadConfig() {
        fix64.reloadConfig();
        this.fileConfiguration = fix64.getConfig();
        loadConfig();
    }
}