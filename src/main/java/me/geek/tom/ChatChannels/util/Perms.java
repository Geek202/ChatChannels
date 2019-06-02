package me.geek.tom.ChatChannels.util;

import me.geek.tom.ChatChannels.ChatChannelsMain;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Perms {

    public static Permission perms;

    public static boolean useBukkit = false;

    public static void load() {
        if (!setupPermissions()) {
            ChatChannelsMain.plugin.getLogger().info("Vault not found, using Bukkit permissions instead");
            useBukkit = true;
        }
    }

    private static boolean setupPermissions() {
        if (ChatChannelsMain.plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = ChatChannelsMain.plugin.getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public static boolean hasPerm(Player player, String perm) {
        if (useBukkit) {
            if (perm.equals("op")) {
                return player.isOp();
            }
            return player.hasPermission(perm);
        } else {
            return perms.has(player, perm);
        }
    }
}
