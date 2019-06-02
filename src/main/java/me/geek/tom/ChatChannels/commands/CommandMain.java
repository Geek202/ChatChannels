package me.geek.tom.ChatChannels.commands;

import me.geek.tom.ChatChannels.ChatChannelsMain;
import me.geek.tom.ChatChannels.events.ChannelChangeEvent;
import me.geek.tom.ChatChannels.events.ChannelJoinEvent;
import me.geek.tom.ChatChannels.events.CreateChannelEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMain implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Can only be run by a player!");
            return true;
        }
        Player player = (Player) sender;

        if (args.length < 1) {
            return false;
        }
        switch (args[0]) {
            case ("create"): {
                if (args.length < 2) {
                    return false;
                }
                CreateChannelEvent event = new CreateChannelEvent(player, args[1]);
                Bukkit.getServer().getPluginManager().callEvent(event);
                return true;
            }
            case ("talk"): {
                if (args.length < 2) {
                    return false;
                }
                ChannelChangeEvent event = new ChannelChangeEvent(player, args[1]);
                Bukkit.getServer().getPluginManager().callEvent(event);
                return true;
            }
            case ("invite"): {
                if (args.length < 3) {
                    return false;
                }
                ChatChannelsMain.plugin.getManager().getInviteManager().invite(player, Bukkit.getPlayer(args[2]), ChatChannelsMain.plugin.getManager().getChannelFromName(args[1]));
            }
            default: {
                return false;
            }
        }
    }
}
