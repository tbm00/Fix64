package dev.tbm00.fix64;

import org.bukkit.ChatColor;
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
        Rename rename = new Rename(fileConfiguration, this);
        Portal portal = new Portal(fileConfiguration, this);
        Spawner spawner = new Spawner(fileConfiguration, this);

        // Register Events
        this.getServer().getPluginManager().registerEvents(rename, this);
        this.getServer().getPluginManager().registerEvents(portal, this);
        this.getServer().getPluginManager().registerEvents(spawner, this);

        // Register Commands
        Fix64Command fix64Command = new Fix64Command(rename, portal, spawner);
        PluginCommand fix64 = this.getCommand("fix64");
        fix64.setTabCompleter(fix64Command);
        fix64.setExecutor(fix64Command);
    }

    private void log(String... strings) {
		for (String s : strings)
            getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + s);
	}
}
