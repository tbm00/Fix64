package dev.tbm00.fix64.events;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

import dev.tbm00.fix64.Fix64;

public class Portal implements Listener {
    private final Fix64 fix64;
    private FileConfiguration fileConfiguration;
    private List<EntityType> bannedEntities = new ArrayList<>();
    private boolean enabled;
    
    public Portal(FileConfiguration fileConfiguration, Fix64 fix64) {
        this.fileConfiguration = fileConfiguration;
        this.fix64 = fix64;
        loadConfig();
    }

    public void loadConfig() {
        try { 
            this.enabled = fileConfiguration.getBoolean("enablePortalGaurd");
            fix64.getLogger().info("enablePortalGaurd is set to: " + enabled);
        } catch (Exception e) {
            fix64.getLogger().warning("Exception getting enablePortalGaurd!");
			return;
        }

        // Load banned entities type array
        List<String> entityTypes = fileConfiguration.getStringList("bannedPortalEntities");
        if (entityTypes == null) {
			fix64.getLogger().warning("Your PortalGaurd config seems to be missing the 'bannedPortalEntities' config!");
			return;
		}
        for (String type : entityTypes) {
			try {
				final EntityType entityType = EntityType.valueOf(type);
				bannedEntities.add(entityType);
			} catch (Exception e){
				fix64.getLogger().warning("EntityType '" + type + "' does not exist!");
			}
        }
    }

    public void reloadConfig() {
        fix64.reloadConfig();
        this.fileConfiguration = fix64.getConfig();
        loadConfig();
    }

	@EventHandler
	public void onPortalUse(EntityPortalEvent event) {
        if (enabled == false) return;
		if (bannedEntities.contains(event.getEntityType())) {
            event.setCancelled(true);
		}
	}
}
