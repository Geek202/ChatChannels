package me.geek.tom.ChatChannels.channels.invites;

import com.google.common.collect.Lists;
import me.geek.tom.ChatChannels.ChatChannelsMain;
import me.geek.tom.ChatChannels.channels.ChannelManager;
import me.geek.tom.ChatChannels.channels.ChatChannel;
import me.geek.tom.ChatChannels.events.ChannelJoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;

public class InviteManager {

    private List<Invite> activeInvites;
    private ChannelManager channelManager;

    public InviteManager(ChannelManager channelManager) {
        this.activeInvites = Lists.newArrayList();
        this.channelManager = channelManager;
    }

    public void inviteExpired(Invite invite) {
        activeInvites.remove(invite);
    }

    public void inviteAccepted(Invite invite) {
        Event event = new ChannelJoinEvent(invite.invitee, invite.channel.id);
        Bukkit.getPluginManager().callEvent(event);
    }
    public void invite(Player inviter, Player invitee, ChatChannel channel) {
        Invite invite = new Invite(inviter, invitee, channel, channelManager, this);
        activeInvites.add(invite);
        invite.runTaskLater(ChatChannelsMain.plugin, 1200);
    }
}
