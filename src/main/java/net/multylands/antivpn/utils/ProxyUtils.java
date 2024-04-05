package net.multylands.antivpn.utils;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.multylands.antivpn.AntiVPN;
import net.multylands.antivpn.listeners.JoinListener;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

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

    public static boolean isVPNorProxy(String ip, AntiVPN plugin) {
        try {
            String url = "https://proxycheck.io/v2/" + ip + "?key=" + plugin.apiKey + "?asn=1?vpn=1";
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            BufferedReader in = new BufferedReader(reader);
            String line = in.readLine();
            try (reader; in) {
                while (line != null) {
                    if (line.contains("proxy") && line.contains("yes")) {
                        return true;
                    }
                    line = in.readLine();
                }
            } finally {
                connection.disconnect();
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
