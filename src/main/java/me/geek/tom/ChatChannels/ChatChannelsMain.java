package me.geek.tom.ChatChannels;

import me.geek.tom.ChatChannels.channels.ChannelManager;
import me.geek.tom.ChatChannels.commands.CommandMain;
import me.geek.tom.ChatChannels.util.Perms;
import me.geek.tom.ChatChannels.util.PluginLogger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatChannelsMain extends JavaPlugin {

    public static ChatChannelsMain plugin;

    public static ConfigurationSection channels;

    public static PluginLogger logger;

    public ChannelManager getManager() {
        return manager;
    }

    private ChannelManager manager;

    private FileConfiguration config;

    private void loadConfig() {
        config = getConfig();
        if (!config.contains("channels")) {
            config.createSection("channels");
        }

        if (!config.contains("debug")) {
            config.set("debug", false);
        }

        config.options().copyDefaults(true);

        saveConfig();

        reloadConfig();

        config = getConfig();

        logger = new PluginLogger(Bukkit.getLogger(), config.getBoolean("debug"), "[ChatChannels]");
    }

    @Override
    public void onEnable() {
        getLogger().info("onEnable:enter");

        plugin = this;

        getLogger().info("Loading config");
        loadConfig();
        getLogger().info("Done!");

        logger.debug("Loading channel manager");
        manager = new ChannelManager();
        logger.debug("Loading channels from config");
        manager.loadFromSection(config.getConfigurationSection("channels"));
        logger.debug("Done!");

        logger.debug("Loading simple perms wrapper");
        Perms.load();
        logger.debug("Loaded!");

        logger.debug("Loading event handler");
        getServer().getPluginManager().registerEvents(manager, this);
        logger.debug("Registering commands");
        getCommand("chatchannels").setExecutor(new CommandMain());
        logger.debug("Done!");

        logger.info("onEnable:exit");
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable:enter");
        getLogger().info("onDisable:exit");
    }

    public void refreshConfig() {
        reloadConfig();
        channels = getConfig().getConfigurationSection("channels");
    }
}
