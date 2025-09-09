package dev.tbm00.fix64.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import dev.tbm00.fix64.ChunkUnloader;
import dev.tbm00.fix64.Fix64;
import dev.tbm00.fix64.events.*;

public class Fix64Command implements TabExecutor {

    public String[] subCommands = new String[]{"reload"};
    private Fix64 fix64;
    private StopRenaming rename;
    private PortalGaurd portal;
    private SpawnerFixes spawner;
    private CrafterCrashFix crafter;
    private ChunkUnloader chunkUnloader;
    private RedstoneTrapdoors redstonePlace;
    private BundleBlocker bundleBlocker;
    private TripwirePistons tripwirePistons;

    public Fix64Command(Fix64 fix64, StopRenaming rename, PortalGaurd portal, SpawnerFixes spawner, CrafterCrashFix crafter, ChunkUnloader chunkUnloader, RedstoneTrapdoors redstonePlace, BundleBlocker bundleBlocker, TripwirePistons tripwirePistons) {
        this.fix64 = fix64;
        this.rename = rename;
        this.portal = portal;
        this.spawner = spawner;
        this.crafter = crafter;
        this.chunkUnloader = chunkUnloader;
        this.redstonePlace = redstonePlace;
        this.bundleBlocker = bundleBlocker;
        this.tripwirePistons = tripwirePistons;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "You need to enter a subcommand!");
        }
        if (args.length == 1) {
            if ("reload".equals(args[0])) {
                if (sender.hasPermission("fix64.reload")) {
                    fix64.reloadConfig();
                    rename.reloadConfig();
                    portal.reloadConfig();
                    spawner.reloadConfig();
                    crafter.reloadConfig();
                    chunkUnloader.reloadConfig();
                    redstonePlace.reloadConfig();
                    bundleBlocker.reloadConfig();
                    tripwirePistons.reloadConfig();
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
