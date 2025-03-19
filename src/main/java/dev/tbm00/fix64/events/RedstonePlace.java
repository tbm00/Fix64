package dev.tbm00.fix64.events;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import dev.tbm00.fix64.Fix64;

public class RedstonePlace implements Listener {
    private final Fix64 fix64;
    private FileConfiguration fileConfiguration;
    private boolean enabled;
    private Set<Material> TRAPDOORS = EnumSet.noneOf(Material.class);

    public RedstonePlace(FileConfiguration fileConfiguration, Fix64 fix64) {
        this.fileConfiguration = fileConfiguration;
        this.fix64 = fix64;
        loadConfig();
    }

    public void loadConfig() {
        try { 
            this.enabled = fileConfiguration.getBoolean("fixLightTrapdoorDupe");
            fix64.getLogger().info("fixLightTrapdoorDupe is set to: " + enabled);
        } catch (Exception e) {
            fix64.getLogger().warning("Exception getting fixLightTrapdoorDupe!");
			return;
        }
        if (enabled) {
            for (Material material : Material.values()) {
                if (material.name().endsWith("_TRAPDOOR")) {
                    TRAPDOORS.add(material);
                }
                TRAPDOORS.add(Material.LIGHT);
            }
        }
    }

    public void reloadConfig() {
        fix64.reloadConfig();
        this.fileConfiguration = fix64.getConfig();
        loadConfig();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRedstonePlace(BlockPlaceEvent event) {
        if (event.getBlock().getType()!=Material.REDSTONE_WIRE) return;
        if (!enabled) return;

        Block blockUnder = event.getBlock().getRelative(BlockFace.DOWN, 1);
        if (TRAPDOORS.contains(blockUnder.getType())) {
            event.setCancelled(true);
        }
    }
}
