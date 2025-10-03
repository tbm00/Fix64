

# Fix64 <img align="left" src="icon.png" alt="Item64 Icon" width="40"/>
A spigot plugin that blocks exploits.

Thank you to the original developers; this is a combination of EventHandlers from three open source plugins, plus some of my own.


## Configurable Features
- **Disable Chunk Loaders:** unload unused chunks on an interval, and/or disable entity portal usage
- **Prevent Item Renaming:** blacklist specific items and/or names
- **Prevent Light Block Redstone Dupe:** prevent placing redstone on trapdoors with lights above it
- **Prevent Crafter Map Crash Booby Traps:** prevent opening crafters with maps inside
- **Prevent Bundle Crashing & Duping:** disable bundles for newbies or everyone
- **Prevent Breaking Trial Spawners & Vaults**
- **Prevent Tripwire+Piston Dupes**
- **Disable Spawner-Egg Conversion**
- **Disable Spawner Exp Orbs**


## Included Plugins
#### Fix64 *- Base Plugin*  
- **Author:** tbm00 @ https://github.com/tbm00/  
- **Link:** https://github.com/tbm00/Fix64/  
- Prevents players from breaking trial spawners & vaults
- Prevents players from changing spawner mob types with spawn eggs
- Prevents players from placing redstone on trapdoors, thus minimizing light block dupes
- Prevents players from using bundles, thus minimizing dupes and server crashes
- Prevents players from opening crafters with map(s) inside, thus preventing client-crashing booby traps
- Prevents players from placing tripwire & string nearby pistons, and vice-a-versa
- Prevents pistons from working nearby tripwire & string, thus disabling some dupe methods
- Unloads chunks without nearby players on interval, thus minimizing chunk loaders

#### BlockSpawnerEXP  
- **Author:** SainttX @ https://github.com/sainttx/  
- **Link:** https://www.spigotmc.org/threads/disable-xp-from-breaking-mob-spawners.19277/  
- Prevents spawners from dropping exp when broken, thus disabling exp dupes
- Updated for 1.21's trial spawners by tbm00
  
#### PortalGaurd  
- **Author:** MetallicGoat @ https://github.com/MetallicGoat/  
- **Link:** https://github.com/MetallicGoat/PortalGaurd/  
- Prevents entities from teleporting in portals, thus disabling some chunk loaders
  
#### StopRenaming  
- **Author:** Xemor_ @ https://github.com/Xemorr/  
- **Link:** https://www.spigotmc.org/resources/stoprenaming.80430/  
- Prevents players from renaming items in anvils based on item's name & material
- Updated for 1.21+ by tbm00 (fix deprecated functions)


## Dependencies
- **Java 17+**: REQUIRED
- **Spigot 1.21+**: UNTESTED ON OLDER VERSIONS


## Commands & Permissions
#### Commands
- `/fix64 reload` Reload the plugin's config
#### Permissions
- `fix64.staff` Get staff/admin notifications *(default: OP)*
- `fix64.reload` Ability to use reload the config *(default: OP)*
- `fix64.eggconversion` Ability to convert spawners with spawn eggs *(default: OP)*
- `fix64.usebundles` Ability to use bundles when disabled or prevented *(default: OP)*


## Config
```
# Fix64 v1.9.5 by @tbm00
# https://github.com/tbm00/Fix64/

### Prevents spawners from dropping exp when broken, thus disabling exp dupes (includes 1.21 trial spawners)
disableSpawnerEXP: true

### Prevents players from changing spawner mob types with spawn eggs
disableSpawnerConversion: true

### Prevents players from breaking trial spawners
disableTrialSpawnerBreak: true

### Prevents players from breaking vault blocks
disableVaultBreak: true

### Prevents players from placing redstone on trapdoors, thus minimizing light block dupes
fixLightTrapdoorDupe: true

### Prevents players from opening crafters with map inside, thus preventing client-crashing booby traps
fixCrafterMapCrash: true

### Prevents newbies (less than 4hr playtime) from using bundles (thus preventing 1.21.4 bundle crashes)
fixBundleCrash: true

### Disables bundles entirely (thus preventing 1.21.4 bundle dupes & crashes)
disableBundles: true

### Disables pistons from operating nearby tripwire & string, and prevents placement nearby one another (thus preventing some dupe methods)
disablePistonsNearTripwire: true

### Unloads all loaded chunks without nearby players on interval, thus minimizing chunk loaders
chunkUnloader:
  enabled: true
  timer: 480 # seconds
  radius: 16 # chunks around each player will not be unloaded

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

### Prevents players from renaming items in anvils based on item's name & material
enableStopRenaming: true
banAllNames: false
bannedMaterials:
  - "TRIPWIRE_HOOK"
bannedNames:
  - "&4BadWord"
```
