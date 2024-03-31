package net.multylands.antivpn.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.multylands.antivpn.AntiVPN;
import net.multylands.antivpn.utils.ChatUtils;

public class ReloadCommand extends Command {
    private AntiVPN plugin;

    public ReloadCommand(AntiVPN plugin) {
        //this name doesnt really matter because we arent actually registering this command
        super("reload");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatUtils.color(plugin.config.getString("antivpn.command-usage").replace("%cmd%", "reload")));
            return;
        }
        plugin.loadWhitelists();
        plugin.reloadAntiVPNConfig();
        sender.sendMessage(ChatUtils.color(plugin.config.getString("antivpn.reload-success")));
    }
}
