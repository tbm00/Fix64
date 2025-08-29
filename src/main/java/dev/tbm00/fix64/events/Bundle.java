package dev.tbm00.fix64.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import dev.tbm00.fix64.Fix64;

public class Bundle implements Listener {
    private final Fix64 fix64;
    private FileConfiguration fileConfiguration;
    private boolean fixBundleCrasherEnabled;
    private boolean disableBundlesEnabled;
    private final String disabledBundlesStr = "§cDue to a new bundle dupe, bundles are disabled until we update to 1.21.5+!";

    public Bundle(FileConfiguration fileConfiguration, Fix64 fix64) {
        this.fileConfiguration = fileConfiguration;
        this.fix64 = fix64;
        loadConfig();
    }

    public void loadConfig() {
        try { 
            this.fixBundleCrasherEnabled = fileConfiguration.getBoolean("fixBundleCrasher");
            fix64.getLogger().info("fixBundleCrasher is set to: " + fixBundleCrasherEnabled);
        } catch (Exception e) {
            fix64.getLogger().warning("Exception getting fixBundleCrasher!");
        }
        try { 
            this.disableBundlesEnabled = fileConfiguration.getBoolean("disableBundles");
            fix64.getLogger().info("disableBundles is set to: " + disableBundlesEnabled);
        } catch (Exception e) {
            fix64.getLogger().warning("Exception getting disableBundles!");
        }
    }

    public void reloadConfig() {
        fix64.reloadConfig();
        this.fileConfiguration = fix64.getConfig();
        loadConfig();
    }

    private static boolean isBundle(ItemStack item) {
        return item != null && item.getType() == Material.BUNDLE;
    }

    private static boolean hasBundlePerms(HumanEntity entity) {
        return entity.hasPermission("fix64.usebundles");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBundleUsage(PlayerInteractEvent event) {
        if (!fixBundleCrasherEnabled && !disableBundlesEnabled) return;

        if (isBundle(event.getItem()) && !hasBundlePerms(event.getPlayer())) {
            Player player = event.getPlayer();

            // disableBundlesEnabled
            if (disableBundlesEnabled) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(disabledBundlesStr);
            } 

            // fixBundleCrasherEnabled
            else {
                int current_play_ticks;
                try {
                    current_play_ticks = player.getStatistic(Statistic.valueOf("PLAY_ONE_MINUTE"));
                } catch (Exception e) {
                    try {
                        current_play_ticks = player.getStatistic(Statistic.valueOf("PLAY_ONE_TICK"));
                    } catch (Exception e2) {
                        fix64.log(ChatColor.RED +  "Caught exception getting player statistic PLAY_ONE_MINUTE: " + e.getMessage());
                        fix64.log(ChatColor.RED +  "Caught exception getting player statistic PLAY_ONE_TICK: " + e2.getMessage());
                        current_play_ticks = 0;
                    }
                } if (current_play_ticks < 288000) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("§cDue to assholes crashing the server with a Mojang bundle bug, bundles are disabled for newbies!");
                    fix64.log(ChatColor.RED + "Newbie tried to use a bundle: " + player.getName());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInvClick(InventoryClickEvent event) {
        if (disableBundlesEnabled && !hasBundlePerms(event.getWhoClicked()) && (isBundle(event.getCursor()) || isBundle(event.getCurrentItem()))) {
            event.setCancelled(true);
            event.getWhoClicked().sendMessage(disabledBundlesStr);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInvDrag(InventoryDragEvent event) {
        if (disableBundlesEnabled && !hasBundlePerms(event.getWhoClicked())) {
            if (isBundle(event.getOldCursor())) {
                event.setCancelled(true);
                event.getWhoClicked().sendMessage(disabledBundlesStr);
            } else {
                for (ItemStack item : event.getNewItems().values()) {
                    if (isBundle(item)) {
                        event.setCancelled(true);
                        event.getWhoClicked().sendMessage(disabledBundlesStr);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInvMove(InventoryMoveItemEvent event) {
        if (disableBundlesEnabled && isBundle(event.getItem())) {

            HumanEntity viewer = event.getInitiator().getViewers().isEmpty()
                                ? null
                                : event.getInitiator().getViewers().get(0);

            if (viewer != null && hasBundlePerms(viewer)) return;
            event.setCancelled(true);
            if (viewer != null) viewer.sendMessage(disabledBundlesStr);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityPickup(EntityPickupItemEvent event) {
        if (!disableBundlesEnabled) return;
        if (!(event.getEntity() instanceof Player)) return;

        if (event.getItem().getItemStack().getType() == Material.BUNDLE && !hasBundlePerms((Player) event.getEntity())) {
            event.setCancelled(true);
            ((Player) event.getEntity()).sendMessage(disabledBundlesStr);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        if (!disableBundlesEnabled) return;

        if (event.getItemDrop().getItemStack().getType() == Material.BUNDLE && !hasBundlePerms(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(disabledBundlesStr);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSwap(PlayerSwapHandItemsEvent event) {
        if (!disableBundlesEnabled) return;

        if ((isBundle(event.getMainHandItem()) || isBundle(event.getOffHandItem())) && !hasBundlePerms(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(disabledBundlesStr);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        if (!disableBundlesEnabled) return;

        final ItemStack result = event.getInventory().getResult();
        if (isBundle(result)) {
            if (!event.getViewers().isEmpty() && hasBundlePerms(event.getViewers().get(0))) {
                return;
            } else {
                event.getInventory().setResult(null);
                if (!event.getViewers().isEmpty()) {
                    event.getViewers().get(0).sendMessage(disabledBundlesStr);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCraft(CraftItemEvent event) {
        if (!disableBundlesEnabled) return;

        final ItemStack current = event.getCurrentItem();
        if (isBundle(current) && !hasBundlePerms(event.getWhoClicked())) {
            event.setCancelled(true);
            event.getWhoClicked().sendMessage(disabledBundlesStr);
        }
    }
}
