package dev.tbm00.fix64;

import dev.tbm00.fix64.commands.Fix64Command;
import dev.tbm00.fix64.events.Rename;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Fix64 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        FileConfiguration fileConfiguration = this.getConfig();
        Rename rename = new Rename(fileConfiguration, this);
        this.getServer().getPluginManager().registerEvents(rename, this);
        Fix64Command fix64Command = new Fix64Command(rename);
        PluginCommand stopRenaming = this.getCommand("stoprenaming");
        stopRenaming.setTabCompleter(fix64Command);
        stopRenaming.setExecutor(fix64Command);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
