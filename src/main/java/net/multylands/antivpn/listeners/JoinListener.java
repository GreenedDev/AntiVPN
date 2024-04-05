package net.multylands.antivpn.listeners;

import java.util.ArrayList;
import java.util.List;


import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.multylands.antivpn.AntiVPN;
import net.multylands.antivpn.utils.ProxyUtils;


public class JoinListener implements Listener {
    public static List<String> cachedVPNIps = new ArrayList<>();
    public AntiVPN plugin;

    public JoinListener(AntiVPN plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PostLoginEvent event) {
        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            ProxiedPlayer player = event.getPlayer();
            String name = player.getName();
            if (ProxyUtils.isBlacklisted(name)) {
                ProxyUtils.disconnect(player, plugin.blacklistedNameMessage, plugin);
            }
            String ip = player.getAddress().getHostString();
            if (ProxyUtils.isWhitelisted(ip)) {
                cachedVPNIps.remove(ip);
                return;
            }
            if (cachedVPNIps.contains(ip)) {
                ProxyUtils.disconnect(player, plugin.blockMessage, plugin);
                return;
            }
            if (ProxyUtils.isVPNorProxy(ip, plugin)) {
                ProxyUtils.disconnect(player, plugin.blockMessage, plugin);
                JoinListener.cachedVPNIps.add(ip);
                return;
            }
        });
    }
}
