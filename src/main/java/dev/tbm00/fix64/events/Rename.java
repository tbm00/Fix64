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


import dev.tbm00.fix64.Fix64;

public class Rename implements Listener {
    Fix64 fix64;
    FileConfiguration fileConfiguration;
    boolean enabled;
    HashSet<String> bannedNames = new HashSet<>();
    EnumSet<Material> bannedMaterials = EnumSet.noneOf(Material.class);
    boolean banAll;

    public Rename(FileConfiguration fileConfiguration, Fix64 fix64) {
        this.fileConfiguration = fileConfiguration;
        this.fix64 = fix64;
        loadConfig();
    }

    public void loadConfig() {
        try { 
            this.enabled = fileConfiguration.getBoolean("enableStopRenaming");
        } catch (Exception e) {
            fix64.getLogger().warning("Exception with enableStopRenaming!");
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
        fix64.reloadConfig();
        this.fileConfiguration = fix64.getConfig();
        loadConfig();
    }

    @EventHandler
    public void onAnvil(InventoryClickEvent e) {
        if (enabled == false) return;
        if (e.getClickedInventory() instanceof AnvilInventory) {
            AnvilInventory anvil = (AnvilInventory) e.getClickedInventory();
            if (e.getSlotType() == InventoryType.SlotType.RESULT) {
                if (anvil.getRenameText().isEmpty()) {
                    return;
                }
                if (banAll) {
                    e.setCancelled(true);
                }
                else if (bannedNames.contains(anvil.getRenameText())) {
                    e.setCancelled(true);
                }
                else if (bannedMaterials.contains(e.getCurrentItem().getType())) {
                    e.setCancelled(true);
                }
                return;
            }
        }
    }

}
