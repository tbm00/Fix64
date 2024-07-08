# Fix64  
A spigot plugin that blocks a few exploits.

Thank you to the original developers; this is a combination of the following three open source plugins:

## Original Plugins
#### BlockSpawnerEXP  
- **Author:** SainttX @ https://github.com/sainttx/  
- **Link:** https://www.spigotmc.org/threads/disable-xp-from-breaking-mob-spawners.19277/
  
#### PortalGaurd  
- **Author:** MetallicGoat @ https://github.com/MetallicGoat/  
- **Link:** https://github.com/MetallicGoat/PortalGaurd/
  
#### StopRenaming  
- **Author:** Xemor_ @ https://github.com/Xemorr/  
- **Link:** https://www.spigotmc.org/resources/stoprenaming.80430/

## Dependencies
- **Java 17+**: REQUIRED
- **Spigot 1.18.1+**: UNTESTED ON OLDER VERSIONS

## Commands
#### Admin Commands
- `/fix64 reload` Reload the plugin's config

## Permissions
#### Admin Permissions
- `fix64.reload` Ability to use reload the config

## Config
```
### BlockSpawnerEXP by SainttX
### https://www.spigotmc.org/threads/disable-xp-from-breaking-mob-spawners.19277/
enableBlockSpawnerEXP: true

### PortalGaurd by MetallicGoat
### https://github.com/MetallicGoat/PortalGaurd/
### Misspelling guard is by the original author
enablePortalGaurd: true
bannedPortalEntities:
  - "BOAT"
  - "CHEST_BOAT"
  - "MINECART"
  - "MINECART_CHEST"
  - "MINECART_COMMAND"
  - "MINECART_FURNACE"
  - "MINECART_TNT"
  - "MINECART_HOPPER"
  - "MINECART_MOB_SPAWNER"
  - "DROPPED_ITEM"
  - "ITEM_DISPLAY"
  - "BLOCK_DISPLAY"
  # https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/EntityType.html

### StopRenaming by Xemor_
### https://www.spigotmc.org/resources/stoprenaming.80430/
enableStopRenaming: true
banAllNames: false
bannedMaterials:
  - TRIPWIRE_HOOK
bannedNames:
  - "&4BadWord"
```
