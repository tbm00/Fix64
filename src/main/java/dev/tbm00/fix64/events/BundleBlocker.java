package dev.tbm00.fix64.events;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
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
import org.bukkit.inventory.ItemStack;

import dev.tbm00.fix64.Fix64;

public class BundleBlocker implements Listener {
    private final Fix64 fix64;
    private FileConfiguration fileConfiguration;
    private boolean fixBundleCrashEnabled;
    private boolean disableBundlesEnabled;
    private long msgCooldown = 10_000L;
    private final Map<UUID, Long> lastMsgMap = new ConcurrentHashMap<>();
    private static Set<Material> BUNDLES = EnumSet.noneOf(Material.class);

    public BundleBlocker(FileConfiguration fileConfiguration, Fix64 fix64) {
        this.fileConfiguration = fileConfiguration;
        this.fix64 = fix64;
        loadConfig();
    }

    public void loadConfig() {
        try { 
            this.fixBundleCrashEnabled = fileConfiguration.getBoolean("fixBundleCrash", fileConfiguration.getBoolean("fixBundleCrasher"));
            fix64.getLogger().info("fixBundleCrash is set to: " + fixBundleCrashEnabled);
        } catch (Exception e) {
            fix64.getLogger().warning("Exception getting fixBundleCrash!");
        }
        try { 
            this.disableBundlesEnabled = fileConfiguration.getBoolean("disableBundles");
            fix64.getLogger().info("disableBundles is set to: " + disableBundlesEnabled);
        } catch (Exception e) {
            fix64.getLogger().warning("Exception getting disableBundles!");
        }
        if (disableBundlesEnabled || fixBundleCrashEnabled) {
            for (Material material : Material.values()) {
                if (material.name().endsWith("_BUNDLE")) {
                    BUNDLES.add(material);
                }
                BUNDLES.add(Material.BUNDLE);
            }
        }
    }

    public void reloadConfig() {
        this.fileConfiguration = fix64.getConfig();
        loadConfig();
    }

    private final void cancelAndMessage(Cancellable event, HumanEntity player) {
        event.setCancelled(true);
        justMessage(player);
    }

    private final void justMessage(HumanEntity offender) {
        if (shouldNotify(offender)) {
            offender.sendMessage("§cDue to 1.21.4 bundle dupes and crashes, bundles are disabled until we update to 1.21.5+!");
            fix64.log("§c" + offender.getName() + " tried using a bundle!");
        }

        // Notify staff individually, each with their own cooldown
        for (Player staff : fix64.getServer().getOnlinePlayers()) {
            if (!hasStaffPerms(staff)) continue;
            if (!shouldNotify(staff)) continue;

            staff.sendMessage("§c" + offender.getName() + " has or is using a bundle!");
            if (disableBundlesEnabled) {
                staff.sendMessage(
                    "§4!!! §eTo staff, §6please do this: §4!!!" +
                    "\n§6 - take it out of their inv," +
                    "\n§6 - empty it and return the contained items," +
                    "\n§6 - put their bundle in a chest inside their claim, &" +
                    "\n§6 - tell them not to touch it until we update" +
                    "\n§cBundles are disabled to prevent 1.21.4 dupes; carrying one can break pickup/drop behavior and more!" +
                    "\n§c   - forCashmoney"
                );
            }
        }

    }

    private boolean shouldNotify(HumanEntity recipient) {
        if (recipient == null) return false;
        long now = System.currentTimeMillis();
        UUID id = recipient.getUniqueId();
        Long last = lastMsgMap.get(id);
        if (last != null && (now - last) < msgCooldown) {
            return false; // still on cooldown
        }
        lastMsgMap.put(id, now);
        return true;
    }

    private static boolean isBundle(ItemStack item) {
        return item !=null && BUNDLES.contains(item.getType());
    }

    private static boolean isCarryingBundle(Player p) {
        for (int i = 0; i < p.getInventory().getSize(); i++) {
            if (isBundle(p.getInventory().getItem(i))) return true;
        }
        return isBundle(p.getInventory().getItemInOffHand());
    }

    private static boolean hasBundlePerms(HumanEntity entity) {
        return entity.hasPermission("fix64.usebundles");
    }

    private static boolean hasStaffPerms(HumanEntity entity) {
        return entity.hasPermission("fix64.staff");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBundleUsage(PlayerInteractEvent event) {
        if (!fixBundleCrashEnabled && !disableBundlesEnabled) return;

        if (isBundle(event.getItem()) && !hasBundlePerms(event.getPlayer())) {
            Player player = event.getPlayer();

            // disableBundlesEnabled
            if (disableBundlesEnabled) {
                cancelAndMessage(event, player);
            } 

            // fixBundleCrashEnabled
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
                    cancelAndMessage(event, player);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInvClick(InventoryClickEvent event) {
        if (disableBundlesEnabled && !hasBundlePerms(event.getWhoClicked()) && (isBundle(event.getCursor()) || isBundle(event.getCurrentItem()))) {
            cancelAndMessage(event, event.getWhoClicked());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInvDrag(InventoryDragEvent event) {
        if (disableBundlesEnabled && !hasBundlePerms(event.getWhoClicked())) {
            if (isBundle(event.getOldCursor())) {
                cancelAndMessage(event, event.getWhoClicked());
                return;
            } else {
                for (ItemStack item : event.getNewItems().values()) {
                    if (isBundle(item)) {
                        cancelAndMessage(event, event.getWhoClicked());
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
            if (viewer != null) justMessage(viewer);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityPickup(EntityPickupItemEvent event) {
        if (!disableBundlesEnabled) return;
        if (!(event.getEntity() instanceof Player)) return;

        if (event.getItem().getItemStack().getType() == Material.BUNDLE && !hasBundlePerms((Player) event.getEntity())) {
            cancelAndMessage(event, ((Player) event.getEntity()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        if (!disableBundlesEnabled || hasBundlePerms(event.getPlayer())) return;

        final Player player = event.getPlayer();

        boolean tryingToDropBundle =
            event.getItemDrop() != null &&
            event.getItemDrop().getItemStack() != null &&
            isBundle(event.getItemDrop().getItemStack());

        if (tryingToDropBundle || isCarryingBundle(player)) {
            cancelAndMessage(event, player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSwap(PlayerSwapHandItemsEvent event) {
        if (!disableBundlesEnabled) return;

        if ((isBundle(event.getMainHandItem()) || isBundle(event.getOffHandItem())) && !hasBundlePerms(event.getPlayer())) {
            cancelAndMessage(event, event.getPlayer());
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
                    justMessage(event.getViewers().get(0));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCraft(CraftItemEvent event) {
        if (!disableBundlesEnabled) return;

        final ItemStack current = event.getCurrentItem();
        if (isBundle(current) && !hasBundlePerms(event.getWhoClicked())) {
            cancelAndMessage(event, event.getWhoClicked());
        }
    }
}
