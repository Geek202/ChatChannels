package me.geek.tom.ChatChannels.channels;

import com.google.common.collect.Lists;
import me.geek.tom.ChatChannels.ChatChannelsMain;
import me.geek.tom.ChatChannels.events.ChannelChangeEvent;
import me.geek.tom.ChatChannels.events.CreateChannelEvent;
import me.geek.tom.ChatChannels.util.Perms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class ChannelManager implements Listener {

    private List<ChatChannel> tempChannels;

    private List<ChatChannel> configChannels;

    private ChatChannel global;

    private HashMap<Player,ChatChannel> playerChannels = new HashMap<Player,ChatChannel>();

    public ChannelManager() {
        configChannels = Lists.newArrayList();
        tempChannels = Lists.newArrayList();
    }

    public void loadFromSection(ConfigurationSection section) {
        List<ConfigurationSection> subSections = Lists.newArrayList();
        Set<String> subKeys = section.getKeys(false);
        Iterator<String> subKeysIter = subKeys.iterator();
        while (subKeysIter.hasNext()) {
            String path = subKeysIter.next();
            if (section.get(path) instanceof ConfigurationSection) {
                subSections.add((ConfigurationSection) section.get(path));
            } else {
                ChatChannelsMain.logger.info("[Config] Invalid config section, removing...");
                section.set(path, null);
            }
        }

        Bukkit.getLogger().info(subSections.toString());

        Iterator<ConfigurationSection> subSectionIter = subSections.iterator();

        if (subSections.size() > 0) {
            while (subSectionIter.hasNext()) {
                ConfigurationSection sec = subSectionIter.next();
                ChatChannelsMain.logger.debug(sec.getName());
                if (sec.isSet("name")) {
                    ChatChannelsMain.logger.debug("Contains display name");
                    if (sec.isSet("requiredPerms")) {
                        ChatChannelsMain.logger.debug("Contains perms");
                        configChannels.add(ChatChannel.create(sec.getName()).configChannel().setDisplayName(sec.getString("name")).addPerms(sec.getStringList("requiredPerms")));
                        ChatChannelsMain.logger.debug("created a channel!");
                    }
                }
            }
        }
        loadOther();
    }

    private ChatChannel getChannelFromName(String name) {
        if (name.equals(global.id)) {
            return global;
        }
        for (int i = 0; i < configChannels.size(); i++) {
            ChatChannelsMain.logger.debug( "channel: " + configChannels.get(i).id);
            if (configChannels.get(i).id.equals(name)) {
                return configChannels.get(i);
            }
        }
        for (int i = 0; i < tempChannels.size(); i++) {
            ChatChannelsMain.logger.debug("channel: " + tempChannels.get(i).id);
            if (tempChannels.get(i).id.equals(name)) {
                return tempChannels.get(i);
            }
        }
        ChatChannelsMain.logger.debug("No channel found");
        return null;
    }

    private void loadOther() {
        global = new ChatChannel().setId("global").configChannel();
    }

    @EventHandler
    public void PlayerLogin(PlayerLoginEvent event) {
        playerChannels.put(event.getPlayer(), global);
    }

    @EventHandler
    public void changePlayerChannel(ChannelChangeEvent event) {
        ChatChannel channel = getChannelFromName(event.getChannel());

        Player player = event.getPlayer();

        if (channel == null) {
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot connect to that channel, it doesn't exist!");
            return;
        }

        if(channel.equals(global)) {
            playerChannels.put(player, channel);
            player.sendMessage(ChatColor.GREEN + "Connected to new channel!");
            return;
        }

        if (channel.requiresPerms()) {
            List<String> perms = channel.getRequiredPerms();
            for (int i = 0; i < perms.size(); i++) {
                ChatChannelsMain.logger.debug("checking player perm: " + perms.get(i));
                if (Perms.hasPerm(player, perms.get(i))) {
                    ChatChannelsMain.logger.debug("has perm, moving to channel");
                    playerChannels.put(player, channel);
                    player.sendMessage(ChatColor.GREEN + "Connected to new channel!");
                    return;
                }
            }
        } else {
            if (channel.getMembers().contains(player.getUniqueId().toString())) {
                playerChannels.put(player, channel);
                player.sendMessage(ChatColor.GREEN + "Connected to channel!");
            } else if (channel.getOwner().equals(player.getUniqueId().toString())) {
                playerChannels.put(player, channel);
                player.sendMessage(ChatColor.GREEN + "Connected to channel!");
            } else {
                player.sendMessage(ChatColor.RED + "You are not a member of this channel, try asking its owner to join.");
            }
        }
    }

    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event) {
        playerChannels.remove(event.getPlayer());
        for (int i = 0; i < tempChannels.size(); i++) {
            if (tempChannels.get(i).getMembers().contains(event.getPlayer().getUniqueId().toString())) {
                tempChannels.get(i).removeMember(event.getPlayer().getUniqueId().toString());
            }
        }
    }

    @EventHandler
    public void createChannel(CreateChannelEvent event) {
        tempChannels.add(new ChatChannel().setId(event.getChannel()).setOwner(event.getPlayer().getUniqueId().toString()));
    }

    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent event) {
        if (playerChannels.get(event.getPlayer()).equals(global)) {
            return;
        }
        event.setCancelled(true);

        playerChannels.get(event.getPlayer()).sendMessageToPlayer(event.getMessage(), event.getPlayer().getUniqueId().toString());
    }
}
