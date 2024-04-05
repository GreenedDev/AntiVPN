package net.multylands.antivpn.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.multylands.antivpn.AntiVPN;
import net.multylands.antivpn.utils.ChatUtils;

import java.util.List;

public class RemoveWhitelistCommand extends Command {
    AntiVPN plugin;

    public RemoveWhitelistCommand(AntiVPN plugin) {
        //this name doesnt really matter because we arent actually registering this command
        super("removewhitelistantivpn");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatUtils.color(plugin.config.getString("antivpn.command-usage").replace("%cmd%", "remove <address>")));
            return;
        }
        List<String> IPAddresses = plugin.whitelistConfig.getStringList("whitelisted");
        if (!IPAddresses.contains(args[1])) {
            sender.sendMessage(ChatUtils.color(plugin.config.getString("antivpn.unable-to-remove")));
            return;
        }
        IPAddresses.remove(args[1]);
        plugin.whitelistConfig.set("whitelisted", IPAddresses);
        plugin.saveAdded();
        sender.sendMessage(ChatUtils.color(plugin.config.getString("antivpn.remove-success").replace("%ip%", args[1])));
    }
}
