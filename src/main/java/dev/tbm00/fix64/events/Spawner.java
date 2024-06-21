package dev.tbm00.fix64.events;

import dev.tbm00.fix64.Fix64;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class Spawner implements Listener {
    Fix64 fix64;
    FileConfiguration fileConfiguration;
    boolean enabled;

    public Spawner(FileConfiguration fileConfiguration, Fix64 fix64) {
        this.fileConfiguration = fileConfiguration;
        this.fix64 = fix64;
        loadConfig();
    }

    public void loadConfig() {
        // Check if enabled
        try { 
            this.enabled = fileConfiguration.getBoolean("enableBlockSpawnerEXP");
        } catch (Exception e) {
            fix64.getLogger().warning("Exception with enableBlockSpawnerEXP!");
			return;
        }
    }

    public void reloadConfig() {
        fix64.reloadConfig();
        this.fileConfiguration = fix64.getConfig();
        loadConfig();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (enabled == false) return;
        Block block = event.getBlock();
        if (block.getType() == Material.SPAWNER ||
            block.getType() == Material.LEGACY_MOB_SPAWNER ) {
            event.setExpToDrop(0);
        }
    }



}
