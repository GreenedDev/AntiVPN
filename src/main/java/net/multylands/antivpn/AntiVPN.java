package net.multylands.antivpn;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.multylands.antivpn.commands.AddWhitelistCommand;
import net.multylands.antivpn.commands.AntiVPNCommand;
import net.multylands.antivpn.commands.ReloadCommand;
import net.multylands.antivpn.commands.RemoveWhitelistCommand;
import net.multylands.antivpn.listeners.JoinListener;

public class AntiVPN extends Plugin {
    public File dir = getDataFolder();

    public File data = new File(dir, "whitelist.yml");
    public File configFile = new File(dir, "config.yml");
    public static HashMap<String, Command> commands = new HashMap<>();
    public static List<String> blacklistedNames = new ArrayList<>();
    public static List<String> whitelistedIPs = new ArrayList<>();
    public Configuration whitelistConfig;
    public Configuration config;
    public String blacklistedNameMessage;
    public String apiKey = "";
    public String blockMessage;

    @Override
    public void onEnable() {
        try {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new AntiVPNCommand("antivpn", this));
            ProxyServer.getInstance().getPluginManager().registerListener(this, new JoinListener(this));
            if (!dir.exists()) {
                dir.mkdir();
            }
            if (!data.exists()) {
                data.createNewFile();
            }
            if (!configFile.exists()) {
                InputStream in = getResourceAsStream("config.yml"); // This file must exist in the jar resources folder
                Files.copy(in, configFile.toPath());
                in.close();
            }
            whitelistConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(data);
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            reloadKeys();
            commands.put("add", new AddWhitelistCommand(this));
            commands.put("remove", new RemoveWhitelistCommand(this));
            commands.put("reload", new ReloadCommand(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadWhitelists() {
        try {
            data = new File(dir, "whitelist.yml");
            whitelistConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveAdded() {
        try {
            data = new File(dir, "whitelist.yml");
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(whitelistConfig, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void reloadAntiVPNConfig() {
        try {
            configFile = new File(dir, "config.yml");
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            reloadKeys();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void reloadKeys() {
        blacklistedNames = config.getStringList("antivpn.blacklisted-names");
        blockMessage = config.getString("antivpn.vpnmessage");
        blacklistedNameMessage = config.getString("antivpn.blacklisted-name-kick");
        whitelistedIPs = whitelistConfig.getStringList("whitelisted");
        apiKey = config.getString("antivpn.apikey");
    }
}
