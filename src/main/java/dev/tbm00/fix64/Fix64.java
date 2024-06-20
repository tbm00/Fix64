package dev.tbm00.fix64;

import dev.tbm00.fix64.commands.Fix64Command;
import dev.tbm00.fix64.events.*;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public final class Fix64 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Startup Message
        final PluginDescriptionFile pdf = this.getDescription();
		log(
            "------------------------------",
            pdf.getName() + " compiled by tbm00",
            "BlockSpawnerEXP by SainttX",
            "PortalGuard by MetallicGoat",
            "StopRenaming by Xemor_",
            "------------------------------"
		);

        // Load Config
        this.saveDefaultConfig();
        FileConfiguration fileConfiguration = this.getConfig();
        Rename rename = new Rename(fileConfiguration, this);
        Portal portal = new Portal(fileConfiguration, this);

        // Register Events
        this.getServer().getPluginManager().registerEvents(rename, this);
        this.getServer().getPluginManager().registerEvents(portal, this);
        this.getServer().getPluginManager().registerEvents(new Spawner(), this);

        // Register Commands
        Fix64Command fix64Command = new Fix64Command(rename);
        PluginCommand fix64 = this.getCommand("fix64");
        fix64.setTabCompleter(fix64Command);
        fix64.setExecutor(fix64Command);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void log(String... strings) {
		for (String s : strings)
			getLogger().info(s);
	}
}
