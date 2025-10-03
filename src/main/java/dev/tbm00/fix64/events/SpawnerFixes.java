package dev.tbm00.fix64.events;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import dev.tbm00.fix64.Fix64;

public class SpawnerFixes implements Listener {
    private final Fix64 fix64;
    private FileConfiguration fileConfiguration;
    private boolean trialBlockEnabled;
    private boolean expBlockEnabled;
    private boolean eggBlockEnabled;
    private Set<Material> SPAWN_EGGS = EnumSet.noneOf(Material.class);

    public SpawnerFixes(FileConfiguration fileConfiguration, Fix64 fix64) {
        this.fileConfiguration = fileConfiguration;
        this.fix64 = fix64;
        loadConfig();
    }

    public void loadConfig() {
        try { 
            this.expBlockEnabled = fileConfiguration.getBoolean("disableSpawnerEXP", fileConfiguration.getBoolean("enableBlockSpawnerEXP"));
            fix64.getLogger().info("disableSpawnerEXP is set to: " + expBlockEnabled);
        } catch (Exception e) {
            fix64.getLogger().warning("Exception getting disableSpawnerEXP!");
			return;
        }
        try { 
            this.eggBlockEnabled = fileConfiguration.getBoolean("disableSpawnerConversion", fileConfiguration.getBoolean("enableBlockSpawnerConversion"));
            fix64.getLogger().info("disableSpawnerConversion is set to: " + eggBlockEnabled);
        } catch (Exception e) {
            fix64.getLogger().warning("Exception getting disableSpawnerConversion!");
			return;
        }
        try { 
            this.trialBlockEnabled = fileConfiguration.getBoolean("disableTrialSpawnerBreak", fileConfiguration.getBoolean("enableBlockTrialSpawnerBreak"));
            fix64.getLogger().info("disableTrialSpawnerBreak is set to: " + trialBlockEnabled);
        } catch (Exception e) {
            fix64.getLogger().warning("Exception getting disableTrialSpawnerBreak!");
			return;
        }
        if (eggBlockEnabled) {
            for (Material material : Material.values()) {
                if (material.name().endsWith("_SPAWN_EGG")) {
                    SPAWN_EGGS.add(material);
                }
            }
        }
    }

    public void reloadConfig() {
        this.fileConfiguration = fix64.getConfig();
        loadConfig();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (trialBlockEnabled && block.getType() == Material.TRIAL_SPAWNER) {
            event.setCancelled(true);
        }
        if (expBlockEnabled == false) return;
        else if (
            block.getType() == Material.SPAWNER ||
            block.getType() == Material.TRIAL_SPAWNER ||
            block.getType() == Material.LEGACY_MOB_SPAWNER ) {
            event.setExpToDrop(0);
        }
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        if (eggBlockEnabled == false) return;

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.LEFT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.SPAWNER) return;

        if (event.getPlayer().hasPermission("fix64.eggconversion")) return;
        
        ItemStack itemInHand = event.getItem();
        if (itemInHand != null && itemInHand.hasItemMeta()) {
            ItemMeta meta = itemInHand.getItemMeta();
            if ((meta instanceof SpawnEggMeta)||itemInHand.getType().toString().contains("SPAWN_EGG"))
                event.setCancelled(true);
        }
    }
}
