package dev.tbm00.fix64.events;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import dev.tbm00.fix64.Fix64;

public class TripwirePistons implements Listener {
    private final Fix64 fix64;
    private FileConfiguration fileConfiguration;
    private boolean enabled;
    private static final Set<Material> PISTONS = EnumSet.of(Material.PISTON, Material.STICKY_PISTON);
    private static final Set<Material> TRIPWIRES = EnumSet.of(Material.TRIPWIRE, Material.TRIPWIRE_HOOK, Material.STRING);

    public TripwirePistons(FileConfiguration fileConfiguration, Fix64 fix64) {
        this.fileConfiguration = fileConfiguration;
        this.fix64 = fix64;
        loadConfig();
    }

    public void loadConfig() {
        try { 
            this.enabled = fileConfiguration.getBoolean("disablePistonsNearTripwire");
            fix64.getLogger().info("disablePistonsNearTripwire is set to: " + enabled);
        } catch (Exception e) {
            enabled = false;
            fix64.getLogger().warning("Exception getting disablePistonsNearTripwire!");
			return;
        }
    }

    public void reloadConfig() {
        this.fileConfiguration = fix64.getConfig();
        loadConfig();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!enabled) return;

        final Material placed = event.getBlock().getType();

        if (PISTONS.contains(placed)) {
            if (hasNearby(event.getBlockPlaced(), TRIPWIRES, 2)) {
                event.setCancelled(true);
                return;
            }
        }

        if (TRIPWIRES.contains(placed)) {
            if (hasNearby(event.getBlockPlaced(), PISTONS, 2)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        if (!enabled) return;
        Block pistonBase = event.getBlock();
        if (hasNearby(pistonBase, TRIPWIRES, 3)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if (!enabled) return;
        Block pistonBase = event.getBlock();
        if (hasNearby(pistonBase, TRIPWIRES, 3)) {
            event.setCancelled(true);
        }
    }


    private boolean hasNearby(Block center, Set<Material> targets, int r) {
        if (r <= 0) {
            return targets.contains(center.getType());
        }

        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -r; dy <= r; dy++) {
                for (int dz = -r; dz <= r; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;

                    Block b = center.getRelative(dx, dy, dz);
                    if (targets.contains(b.getType())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
