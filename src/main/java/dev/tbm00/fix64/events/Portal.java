package dev.tbm00.fix64.events;

import dev.tbm00.fix64.Fix64;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

import java.util.ArrayList;
import java.util.List;

public class Portal implements Listener {
    private final List<EntityType> bannedEntities = new ArrayList<>();
    private FileConfiguration fileConfiguration;
    private Fix64 fix64;
    
    public Portal(FileConfiguration fileConfiguration, Fix64 fix64) {
        this.fileConfiguration = fileConfiguration;
        this.fix64 = fix64;
        loadConfig();
    }

    public void loadConfig() {
        // Load banned entities type array
        List<String> entityTypes = fileConfiguration.getStringList("bannedPortalEntities");
        if (entityTypes == null) {
			fix64.getLogger().warning("Your PortalGuard config seems to be missing the 'bannedPortalEntities' config!");
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

	@EventHandler
	public void onPortalUse(EntityPortalEvent event) {
		if (bannedEntities.contains(event.getEntityType())) {
			event.setCancelled(true);
		}
	}
}
