package dev.tbm00.fix64.commands;

import dev.tbm00.fix64.events.Rename;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class Fix64Command implements CommandExecutor, TabExecutor {

    public String[] subCommands = new String[]{"reload"};
    private Rename rename;

    public Fix64Command(Rename rename) {
        this.rename = rename;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "You need to enter a subcommand!");
        }
        if (args.length == 1) {
            if ("reload".equals(args[0])) {
                if (sender.hasPermission("fix64.reload")) {
                    rename.reloadConfig();
                    sender.sendMessage(ChatColor.AQUA + "You successfully reloaded the plugin!");
                }
                else {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command!");
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            for (String subCommand : subCommands) {
                if (subCommand.startsWith(args[0])) {
                    list.add(subCommand);
                }
            }
        }
        return list;
    }
}
