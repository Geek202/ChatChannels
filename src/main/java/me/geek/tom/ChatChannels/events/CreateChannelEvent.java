package me.geek.tom.ChatChannels.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CreateChannelEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private boolean isCancelled;

    private final Player player;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    private boolean isSuccess;

    public Player getPlayer() {
        return player;
    }

    public String getChannel() {
        return channel;
    }

    private final String channel;


    public CreateChannelEvent(Player player, String channelName) {
        this.player = player;
        this.channel = channelName;
        this.isSuccess = false;
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
