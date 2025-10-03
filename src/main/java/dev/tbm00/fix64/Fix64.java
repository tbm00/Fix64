package dev.tbm00.fix64;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import dev.tbm00.fix64.commands.Fix64Command;
import dev.tbm00.fix64.events.*;

public final class Fix64 extends JavaPlugin {
    @Override
    public void onEnable() {
        // Startup Message
        final PluginDescriptionFile pdf = this.getDescription();
		log(
            ChatColor.DARK_PURPLE + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-",
            pdf.getName() + " v" + pdf.getVersion() + " by tbm00",
            "- BlockSpawnerEXP by SainttX",
            "- PortalGaurd by MetallicGoat",
            "- StopRenaming by Xemor_",
            ChatColor.DARK_PURPLE + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"
		);

        // Load Config
        this.saveDefaultConfig();
        FileConfiguration fileConfiguration = this.getConfig();
        log("Configuration loaded.");

        StopRenaming stopRenaming = new StopRenaming(fileConfiguration, this);
        PortalGaurd portalGuard = new PortalGaurd(fileConfiguration, this);
        SpawnerFixes spawnerFixes = new SpawnerFixes(fileConfiguration, this);
        VaultProtection vaultProtection = new VaultProtection(fileConfiguration, this);
        CrafterCrashFix crafterCrashFix = new CrafterCrashFix(fileConfiguration, this);
        RedstoneTrapdoors redstoneTrapdoors = new RedstoneTrapdoors(fileConfiguration, this);
        BundleBlocker bundleBlocker = new BundleBlocker(fileConfiguration, this);
        TripwirePistons tripwirePistons = new TripwirePistons(fileConfiguration, this);
        ChunkUnloader chunkUnloader = new ChunkUnloader(fileConfiguration, this);
        log("Listeners initialized.");

        // Register Events
        this.getServer().getPluginManager().registerEvents(stopRenaming, this);
        this.getServer().getPluginManager().registerEvents(portalGuard, this);
        this.getServer().getPluginManager().registerEvents(spawnerFixes, this);
        this.getServer().getPluginManager().registerEvents(vaultProtection, this);
        this.getServer().getPluginManager().registerEvents(crafterCrashFix, this);
        this.getServer().getPluginManager().registerEvents(redstoneTrapdoors, this);
        this.getServer().getPluginManager().registerEvents(bundleBlocker, this);
        this.getServer().getPluginManager().registerEvents(tripwirePistons, this);
        log("Listeners registered.");

        // Register Commands
        Fix64Command fix64Command = new Fix64Command(this, stopRenaming, portalGuard, spawnerFixes, crafterCrashFix, chunkUnloader, redstoneTrapdoors, bundleBlocker, tripwirePistons);
        PluginCommand fix64 = this.getCommand("fix64");
        fix64.setTabCompleter(fix64Command);
        fix64.setExecutor(fix64Command);
        log("Commands registered.");

        // Disable recipes
        if (fileConfiguration.getBoolean("disableBundles", false)) {
            try {
                getServer().removeRecipe(NamespacedKey.minecraft("bundle"));
                log("Bundle recipe disabled.");
            } catch (Throwable thrown) {
                getLogger().warning("Failed to remove bundle recipe: " + thrown.getMessage());
            }
        }
    }

    public void log(String... strings) {
		for (String s : strings)
            getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + s);
	}
}