package dev.tbm00.fix64.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import dev.tbm00.fix64.Fix64;

public class CrafterCrashFix implements Listener {
    private final Fix64 fix64;
    private FileConfiguration fileConfiguration;
    private boolean enabled;

    public CrafterCrashFix(FileConfiguration fileConfiguration, Fix64 fix64) {
        this.fileConfiguration = fileConfiguration;
        this.fix64 = fix64;
        loadConfig();
    }

    public void loadConfig() {
        try { 
            this.enabled = fileConfiguration.getBoolean("fixCrafterMapCrash");
            fix64.getLogger().info("fixCrafterMapCrash is set to: " + enabled);
        } catch (Exception e) {
            fix64.getLogger().warning("Exception getting fixCrafterMapCrash!");
			return;
        }
    }

    public void reloadConfig() {
        this.fileConfiguration = fix64.getConfig();
        loadConfig();
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        if (enabled == false) return;

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.CRAFTER) return;

        BlockState state = block.getState();
        if (!(state instanceof InventoryHolder holder)) return;
        Inventory inv = holder.getInventory(); 

        for (int slot = 0; slot < 10; slot++) {
            try {
                ItemStack item = inv.getItem(slot);
                if (item != null && (item.getType() == Material.FILLED_MAP || item.getType() == Material.MAP)) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.RED + "This crafter has been booby-trapped!");
                    return;
                }
            } catch (Exception ignored) {}
        }
    }
}
