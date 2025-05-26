package dev.tbm00.fix64.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import dev.tbm00.fix64.ChunkUnloader;
import dev.tbm00.fix64.events.*;

public class Fix64Command implements TabExecutor {

    public String[] subCommands = new String[]{"reload"};
    private Rename rename;
    private Portal portal;
    private Spawner spawner;
    private CrafterL crafter;
    private ChunkUnloader chunkUnloader;
    private RedstonePlace redstonePlace;

    public Fix64Command(Rename rename, Portal portal, Spawner spawner, CrafterL crafter, ChunkUnloader chunkUnloader, RedstonePlace redstonePlace) {
        this.rename = rename;
        this.portal = portal;
        this.spawner = spawner;
        this.crafter = crafter;
        this.chunkUnloader = chunkUnloader;
        this.redstonePlace = redstonePlace;
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
                    portal.reloadConfig();
                    spawner.reloadConfig();
                    crafter.reloadConfig();
                    chunkUnloader.reloadConfig();
                    redstonePlace.reloadConfig();
                    sender.sendMessage(ChatColor.GREEN + "You successfully reloaded Fix64!");
                }
                else {
                    sender.sendMessage(ChatColor.RED + "No permission!");
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
