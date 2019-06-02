package me.geek.tom.ChatChannels.channels.invites;

import me.geek.tom.ChatChannels.channels.ChannelManager;
import me.geek.tom.ChatChannels.channels.ChatChannel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Invite extends BukkitRunnable {

    public Player getInviter() {
        return inviter;
    }

    public Player getInvitee() {
        return invitee;
    }

    public ChatChannel getChannel() {
        return channel;
    }

    public Player inviter;
    public Player invitee;
    public ChatChannel channel;
    public ChannelManager manager;
    public InviteManager inviteManager;

    public Invite(Player inviter, Player invitee, ChatChannel channel, ChannelManager manager, InviteManager inviteManager) {
        this.invitee = invitee;
        this.inviter = inviter;
        this.channel = channel;
        this.manager = manager;
        this.inviteManager= inviteManager;
    }

    public void run() {
        inviter.sendMessage(ChatColor.DARK_PURPLE + "Your invite to " + ChatColor.LIGHT_PURPLE + inviter.getDisplayName() + ChatColor.DARK_PURPLE + " was not accepted and expired");
        invitee.sendMessage(ChatColor.DARK_PURPLE + "Your invite from " + ChatColor.LIGHT_PURPLE + inviter.getDisplayName() + ChatColor.DARK_PURPLE + " has expired.");
        inviteManager.inviteExpired(this);
    }
}
