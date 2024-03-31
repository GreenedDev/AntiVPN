package net.multylands.antivpn.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.multylands.antivpn.AntiVPN;
import net.multylands.antivpn.utils.ChatUtils;
import net.multylands.antivpn.utils.ProxyUtils;

public class JoinListener implements Listener {
    public List<String> cachedVPNIps = new ArrayList<>();
    public AntiVPN plugin;

    public JoinListener(AntiVPN plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PostLoginEvent event) {
        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            try {
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
                String url = "https://proxycheck.io/v2/" + ip + "?key=" + plugin.apiKey + "?asn=1?vpn=1";
                HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                BufferedReader in = new BufferedReader(reader);
                String line = in.readLine();
                try {
                    while (line != null) {
                        if (line.contains("proxy") && line.contains("yes")) {
                            ProxyUtils.disconnect(player, plugin.blockMessage, plugin);
                            cachedVPNIps.add(ip);
                            break;
                        }
                        line = in.readLine();
                    }
                } finally {
                    in.close();
                    reader.close();
                    connection.disconnect();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }
}
