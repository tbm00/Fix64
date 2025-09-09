package dev.tbm00.fix64.events;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.tbm00.fix64.Fix64;

public class StopRenaming implements Listener {
    private final Fix64 fix64;
    private FileConfiguration fileConfiguration;
    private boolean enabled;
    private HashSet<String> bannedNames = new HashSet<>();
    private EnumSet<Material> bannedMaterials = EnumSet.noneOf(Material.class);
    private boolean banAll;

    public StopRenaming(FileConfiguration fileConfiguration, Fix64 fix64) {
        this.fileConfiguration = fileConfiguration;
        this.fix64 = fix64;
        loadConfig();
    }

    public void loadConfig() {
        try { 
            this.enabled = fileConfiguration.getBoolean("enableStopRenaming");
            fix64.getLogger().info("enableStopRenaming is set to: " + enabled);
        } catch (Exception e) {
            fix64.getLogger().warning("Exception getting enableStopRenaming!");
			return;
        }

        this.banAll = fileConfiguration.getBoolean("banAllNames");
        List<String> bannedNames = fileConfiguration.getStringList("bannedNames");
        List<String> bannedMaterials = fileConfiguration.getStringList("bannedMaterials");
        
        for (String string : bannedNames) {
            this.bannedNames.add(string);
        }
        for (String string : bannedMaterials) {
            Material material = Material.getMaterial(string);
            this.bannedMaterials.add(material);
        }
    }

    public void reloadConfig() {
        this.fileConfiguration = fix64.getConfig();
        loadConfig();
    }

    @EventHandler
    public void onAnvil(InventoryClickEvent e) {
        if (enabled == false) return;
        if (!(e.getClickedInventory() instanceof AnvilInventory)) return;
        if (e.getSlotType() == InventoryType.SlotType.RESULT) {
            ItemStack resultItem = e.getCurrentItem();
            if (resultItem == null || !resultItem.hasItemMeta()) return;

            ItemMeta meta = resultItem.getItemMeta();
            String renameText = "";

            if (meta.hasDisplayName()) {
                renameText = meta.getDisplayName();
            }

            if (renameText.isEmpty()) return;
            if (banAll) e.setCancelled(true);
            else if (bannedNames.contains(renameText))
                e.setCancelled(true);
            else if (bannedMaterials.contains(resultItem.getType()))
                e.setCancelled(true);
            return;
        }
    }
}