package net.multylands.antivpn.utils;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.multylands.antivpn.AntiVPN;

public class ProxyUtils {
    public static boolean isWhitelisted(String ip) {
        for (String whitelistedIp : AntiVPN.whitelistedIPs) {
            if (!ip.contains(whitelistedIp)) {
                continue;
            }
            return true;
        }
        return false;
    }
    public static boolean isBlacklisted(String name) {
        for (String blacklistedName : AntiVPN.blacklistedNames) {
            if (!name.contains(blacklistedName)) {
                continue;
            }
            return true;
        }
        return false;
    }
    public static void disconnect(ProxiedPlayer player, String message, AntiVPN plugin) {
        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            player.disconnect(ChatUtils.color(message));
        });
    }
}
