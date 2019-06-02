package me.geek.tom.ChatChannels.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChannelChangeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean isCancelled;

    private final Player player;

    public Player getPlayer() {
        return player;
    }

    public String getChannel() {
        return channel;
    }

    private final String channel;


    public ChannelChangeEvent(Player player, String channelName) {
        this.player = player;
        this.channel = channelName;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

}
