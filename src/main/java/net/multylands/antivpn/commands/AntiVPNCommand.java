package net.multylands.antivpn.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.multylands.antivpn.AntiVPN;
import net.multylands.antivpn.utils.ChatUtils;

public class AntiVPNCommand extends Command implements TabExecutor {
    private AntiVPN plugin;

    public AntiVPNCommand(String name, AntiVPN plugin) {
        //this name doesnt really matter because we arent actually registering this command
        super(name);
        this.plugin = plugin;
    }

    @SuppressWarnings({"deprecation"})
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("antivpn.use")) {
            sender.sendMessage(ChatUtils.color(plugin.config.getString("antivpn.no-perm")));
            return;
        }
        if (args.length == 0 || !AntiVPN.commands.containsKey(args[0])) {
            for (String message : plugin.config.getStringList("antivpn.help-message")) {
                sender.sendMessage(ChatUtils.color(message));
            }
            return;
        }
        Command subCommand = AntiVPN.commands.get(args[0]);
        subCommand.execute(sender, args);
    }
    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> subCommands = new ArrayList<>();
        subCommands.add("add");
        subCommands.add("remove");
        subCommands.add("reload");
        Iterator<String> subCommandsIterator = subCommands.iterator();
        while (subCommandsIterator.hasNext()) {
            String subCommand = subCommandsIterator.next();
            if (subCommand.startsWith(args[0])) {
                continue;
            }
            subCommandsIterator.remove();
        }
        return subCommands;
    }
}
