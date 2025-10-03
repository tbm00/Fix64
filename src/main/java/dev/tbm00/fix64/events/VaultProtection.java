package dev.tbm00.fix64.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import dev.tbm00.fix64.Fix64;

public class VaultProtection implements Listener {
    private final Fix64 fix64;
    private FileConfiguration fileConfiguration;
    private boolean vaultBlockEnabled;

    public VaultProtection(FileConfiguration fileConfiguration, Fix64 fix64) {
        this.fileConfiguration = fileConfiguration;
        this.fix64 = fix64;
        loadConfig();
    } 

    public void loadConfig() {
        try { 
            this.vaultBlockEnabled = fileConfiguration.getBoolean("disableVaultBreak", fileConfiguration.getBoolean("disableVaultBreak"));
            fix64.getLogger().info("disableVaultBreak is set to: " + vaultBlockEnabled);
        } catch (Exception e) {
            fix64.getLogger().warning("Exception getting disableVaultBreak!");
			return;
        }
    }

    public void reloadConfig() {
        this.fileConfiguration = fix64.getConfig();
        loadConfig();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (vaultBlockEnabled == false) return;
        Block block = event.getBlock();
        if (block.getType() == Material.VAULT) {
            event.setExpToDrop(0);
            event.setCancelled(true);
        }
    }
}
