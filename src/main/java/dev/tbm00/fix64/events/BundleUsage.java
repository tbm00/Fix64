package dev.tbm00.fix64.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import dev.tbm00.fix64.Fix64;

public class BundleUsage implements Listener {
    private final Fix64 fix64;
    private FileConfiguration fileConfiguration;
    private boolean enabled;

    public BundleUsage(FileConfiguration fileConfiguration, Fix64 fix64) {
        this.fileConfiguration = fileConfiguration;
        this.fix64 = fix64;
        loadConfig();
    }

    public void loadConfig() {
        try { 
            this.enabled = fileConfiguration.getBoolean("fixBundleCrasher");
            fix64.getLogger().info("fixBundleCrasher is set to: " + enabled);
        } catch (Exception e) {
            fix64.getLogger().warning("Exception getting fixBundleCrasher!");
			return;
        }
    }

    public void reloadConfig() {
        fix64.reloadConfig();
        this.fileConfiguration = fix64.getConfig();
        loadConfig();
    }

    @EventHandler()
    public void onBundleUsage(PlayerInteractEvent  event) {
        if (!enabled) return;

        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.BUNDLE) {
            Player player = event.getPlayer();
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
