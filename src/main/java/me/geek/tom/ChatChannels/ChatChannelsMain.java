package me.geek.tom.ChatChannels;

import org.bukkit.plugin.java.JavaPlugin;

public class ChatChannelsMain extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("onEnable");
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable");
    }
}
