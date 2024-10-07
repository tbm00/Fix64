# Fix64  
A spigot plugin that blocks a few exploits.

Thank you to the original developers; this is a combination of EventHandlers from three open source plugins, plus some of my own.

## Included
#### Fix64 *- Base Plugin*
- Prevents players from changing spawner mob types with spawn eggs
- **Author:** tbm00 @ https://github.com/tbm00/  
- **Link:** https://github.com/tbm00/Fix64/

#### BlockSpawnerEXP  
- Prevents spawners from dropping exp when broken, thus disabling exp dupes
- Updated for 1.21's trial spawners by tbm00
- **Author:** SainttX @ https://github.com/sainttx/  
- **Link:** https://www.spigotmc.org/threads/disable-xp-from-breaking-mob-spawners.19277/
  
#### PortalGaurd  
- Prevents entities from teleporting in portals, thus disabling some chunk loaders
- **Author:** MetallicGoat @ https://github.com/MetallicGoat/  
- **Link:** https://github.com/MetallicGoat/PortalGaurd/
  
#### StopRenaming  
- Prevents players from renaming of items in anvil based on item's name & material
- Updated for 1.21+ by tbm00 (fix deprecated functions)
- **Author:** Xemor_ @ https://github.com/Xemorr/  
- **Link:** https://www.spigotmc.org/resources/stoprenaming.80430/

## Dependencies
- **Java 17+**: REQUIRED
- **Spigot 1.20.4+**: UNTESTED ON OLDER VERSIONS

## Commands
#### Admin Commands
- `/fix64 reload` Reload the plugin's config

## Permissions
#### Admin Permissions
- `fix64.reload` Ability to use reload the config *(default: OP)*
- `fix64.eggconversion` Ability to convert spawners with spawn eggs *(default: OP)*

## Config
```
### Fix64 v1.4 by tbm00
### https://github.com/tbm00/Fix64/
### Prevents players from changing spawner mob types with spawn eggs
enableBlockSpawnerConversion: true

### BlockSpawnerEXP by SainttX
### https://www.spigotmc.org/threads/disable-xp-from-breaking-mob-spawners.19277/
### Prevents spawners from dropping exp when broken, thus disabling exp dupes
### Updated for 1.21's trial spawners by tbm00
enableBlockSpawnerEXP: true

### PortalGaurd by MetallicGoat
### https://github.com/MetallicGoat/PortalGaurd/
### Prevents entities from teleporting in portals, thus disabling some chunk loaders
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
### Prevents players from renaming of items in anvil based on item's name & material
### Updated for 1.21+ by tbm00 (fix deprecated functions)
enableStopRenaming: true
banAllNames: false
bannedMaterials:
  - TRIPWIRE_HOOK
bannedNames:
  - "&4BadWord"
```
