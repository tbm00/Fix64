package dev.tbm00.fix64.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class Spawner implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.SPAWNER ||
            block.getType() == Material.LEGACY_MOB_SPAWNER ) {
            event.setExpToDrop(0);
        }
    }
}
