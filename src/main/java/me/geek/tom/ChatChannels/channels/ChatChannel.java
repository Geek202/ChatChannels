package me.geek.tom.ChatChannels.channels;

import com.google.common.collect.Lists;
import me.geek.tom.ChatChannels.ChatChannelsMain;
import me.geek.tom.ChatChannels.util.Perms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ChatChannel {

    public String id;

    public String getDisplayName() {
        return displayName;
    }

    public ChatChannel setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    private String displayName;

    public List<String> getRequiredPerms() {
        return requirePerms;
    }

    private List<String> requirePerms = Lists.newArrayList();

    public String getOwner() {
        return ownerUuid;
    }

    private String ownerUuid;

    public List<String> getMembers() {
        return members;
    }

    private List<String> members;

    private boolean isConfig = false;

    protected ChatChannel() {
        members = Lists.newArrayList();
    }

    public static ChatChannel create(String id) {
        return new ChatChannel().setId(id).configChannel();
    }

    public void removeMember(String uuid) {
        while (members.contains(uuid)) {
            members.remove(uuid);
        }
    }

    public ChatChannel configChannel() {
        isConfig = true;
        return this;
    }

    public static ChatChannel create(String id, String ownerUuid) {
        return new ChatChannel().setId(id).setOwner(ownerUuid).notConfig();
    }

    protected ChatChannel setId(String id){
        this.id = id;
        return this;
    }

    protected ChatChannel setOwner(String owner) {
        if (isConfig) {
            return this;
        }
        ownerUuid = owner;
        return this;
    }

    public void invitePlayer(String playerUuid) {
        members.add(playerUuid);
    }

    public void sendMessageToPlayer(String message, String senderUuid) {
        if (isConfig) {
            messageConfigChannel(message, senderUuid);
        } else {
            sendMessageToPlayers(message, senderUuid);
        }
    }

    private void sendMessageToPlayers(String message, String sender) {
        ChatChannelsMain.plugin.getServer().getPlayer(UUID.fromString(ownerUuid)).sendMessage(message);
        for (int i = 0; i < members.size(); i++) {
            ChatChannelsMain.plugin.getServer().getPlayer(UUID.fromString(members.get(i))).sendMessage("[" + displayName + "] <" + Bukkit.getServer().getPlayer(UUID.fromString(sender)).getDisplayName() + "> " + message);
        }
    }

    private void messageConfigChannel(String message, String senderUuid) {
        List<Player> players = Bukkit.getServer().matchPlayer("");
        for (int i = 0; i < players.size(); i++) {
            for (int j = 0; j < requirePerms.size(); j++) {
                if (Perms.hasPerm(players.get(i), requirePerms.get(j))) {
                    players.get(i).sendMessage("[" + displayName + "] <" + Bukkit.getServer().getPlayer(UUID.fromString(senderUuid)).getDisplayName() + "> " + message);
                }
            }
        }
    }

    public ChatChannel addPerms(List<String> perms) {
        requirePerms.addAll(perms);
        return this;
    }

    public boolean requiresPerms() {
        return this.requirePerms.size() != 0;
    }

    public ChatChannel notConfig() {
        this.isConfig = false;
        this.requirePerms = Lists.newArrayList();
        return this;
    }
}
