package me.clip.placeholderapi.commands;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.Msg;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.stream.Collectors;

public class PlaceholderAPICommands implements CommandExecutor {
    private PlaceholderAPIPlugin plugin;
    private CommandExecutor eCloud;

    public PlaceholderAPICommands(PlaceholderAPIPlugin plugin) {
        this.plugin = plugin;
        this.eCloud = new ExpansionCloudCommands(plugin);
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            Msg.msg(s, "PlaceholderAPI &7version &b&o" + plugin.getDescription().getVersion(),
                    "&fCreated by&7: &b" + plugin.getDescription().getAuthors(),
                    "&fPAPI commands: &b/papi help",
                    "&fEcloud commands: &b/papi ecloud");
            return true;
        } else {
            if (args[0].equalsIgnoreCase("help")) {
                Msg.msg(s, "PlaceholderAPI &aHelp &e(&f" + plugin.getDescription().getVersion() + "&e)",
                        "&b/papi",
                        "&fView plugin info/version info",
                        "&b/papi list",
                        "&fList all placeholder expansions that are currently active",
                        "&b/papi info <placeholder name>",
                        "&fView information for a specific expansion",
                        "&b/papi parse <(playername)/me> <...args>",
                        "&fParse a String with placeholders",
                        "&b/papi bcparse <(playername)/me> <...args>",
                        "&fParse a String with placeholders and broadcast the message",
                        "&b/papi parserel <player one> <player two> <...args>",
                        "&fParse a String with relational placeholders",
                        "&b/papi register <fileName>",
                        "&fRegister an expansion by the name of the file",
                        "&b/papi unregister <Expansion name>",
                        "&fUnregister an expansion by name",
                        "&b/papi reload",
                        "&fReload the config settings");

                if (s.hasPermission("placeholderapi.ecloud")) {
                    if (plugin.getExpansionCloud() == null) {
                        Msg.msg(s, "&b/papi enablecloud", "&fEnable the expansion cloud");
                    } else {
                        Msg.msg(s, "&b/papi disablecloud",
                                "&fDisable the expansion cloud",
                                "&b/papi ecloud",
                                "&fView ecloud command usage");
                    }
                }

                return true;
            } else if (args[0].equalsIgnoreCase("ecloud")) {
                if (!s.hasPermission("placeholderapi.ecloud")) {
                    Msg.msg(s, "&cYou don't have permission to do that!");
                    return true;
                }

                if (plugin.getExpansionCloud() == null) {
                    Msg.msg(s, "&7The expansion cloud is not enabled!");
                    return true;
                }

                return eCloud.onCommand(s, cmd, label, args);
            } else if (args[0].equalsIgnoreCase("enablecloud")) {
                if (!s.hasPermission("placeholderapi.ecloud")) {
                    Msg.msg(s, "&cYou don't have permission to do that!");
                    return true;
                }

                if (plugin.getExpansionCloud() != null) {
                    Msg.msg(s, "&7The cloud is already enabled!");
                    return true;
                }

                plugin.enableCloud();
                plugin.getPlaceholderAPIConfig().setCloudEnabled(true);
                Msg.msg(s, "&aThe cloud has been enabled!");
                return true;
            } else if (args[0].equalsIgnoreCase("disablecloud")) {
                if (!s.hasPermission("placeholderapi.ecloud")) {
                    Msg.msg(s, "&cYou don't have permission to do that!");
                    return true;
                }

                if (plugin.getExpansionCloud() == null) {
                    Msg.msg(s, "&7The cloud is already disabled!");
                    return true;
                }

                plugin.disableCloud();
                plugin.getPlaceholderAPIConfig().setCloudEnabled(false);
                Msg.msg(s, "&aThe cloud has been disabled!");
                return true;
            } else if (args.length > 1 && args[0].equalsIgnoreCase("info")) {
                if (!s.hasPermission("placeholderapi.info")) {
                    Msg.msg(s, "&cYou don't have permission to do that!");
                    return true;
                }

                PlaceholderExpansion ex = plugin.getExpansionManager().getRegisteredExpansion(args[1]);

                if (ex == null) {
                    Msg.msg(s, "&cThere is no expansion loaded with the identifier: &f" + args[1]);
                    return true;
                }

                Msg.msg(s, "&7Placeholder expansion info for: &f" + ex.getName());
                Msg.msg(s, "&7Status: " + (ex.isRegistered() ? "&aRegistered" : "&cNot registered"));

                if (ex.getAuthor() != null) {
                    Msg.msg(s, "&7Created by: &f" + ex.getAuthor());
                }

                if (ex.getVersion() != null) {
                    Msg.msg(s, "&7Version: &f" + ex.getVersion());
                }

                if (ex.getRequiredPlugin() != null) {
                    Msg.msg(s, "&7Requires plugin: &f" + ex.getRequiredPlugin());
                }

                if (ex.getPlaceholders() != null) {
                    Msg.msg(s, "&8&m-- &r&7Placeholders &8&m--");
                    for (String placeholder : ex.getPlaceholders()) {
                        Msg.msg(s, placeholder);
                    }
                }

                return true;
            } else if (args.length > 2 && args[0].equalsIgnoreCase("parse") || args.length > 2 && args[0].equalsIgnoreCase("bcparse")) {
                if (!s.hasPermission("placeholderapi.parse")) {
                    Msg.msg(s, "&cYou don't have permission to do that!");
                    return true;
                }

                IPlayer pl;

                if (args[1].equalsIgnoreCase("me")) {
                    if (s instanceof Player) {
                        pl = (Player) s;
                    } else {
                        Msg.msg(s, "&cThis command must target a player when used by console");
                        return true;
                    }
                } else {
                    if (Server.getInstance().getPlayer(args[1]) != null) {
                        pl = Server.getInstance().getPlayer(args[1]);
                    } else {
                        pl = Server.getInstance().getOfflinePlayer(args[1]);
                    }
                }

                if (pl == null || !pl.hasPlayedBefore()) {
                    Msg.msg(s, "&cFailed to find player: &f" + args[1]);
                    return true;
                }

                String parse = StringUtils.join(args, " ", 2, args.length);

                if (args[0].equalsIgnoreCase("bcparse")) {
                    Msg.broadcast("&r" + PlaceholderAPI.setPlaceholders(pl, parse));
                } else {
                    Msg.msg(s, "&r" + PlaceholderAPI.setPlaceholders(pl, parse));
                }

                return true;
            } else if (args.length > 3 && args[0].equalsIgnoreCase("parserel")) {
                if (!(s instanceof Player)) {
                    Msg.msg(s, "&cThis command can only be used in game!");
                    return true;
                } else {
                    if (!s.hasPermission("placeholderapi.parse")) {
                        Msg.msg(s, "&cYou don't have permission to do that!");
                        return true;
                    }
                }

                Player one = Server.getInstance().getPlayer(args[1]);

                if (one == null) {
                    Msg.msg(s, args[1] + " &cis not online!");
                    return true;
                }

                Player two = Server.getInstance().getPlayer(args[2]);

                if (two == null) {
                    Msg.msg(s, args[2] + " &cis not online!");
                    return true;
                }

                String parse = StringUtils.join(args, " ", 3, args.length);
                Msg.msg(s, "&r" + PlaceholderAPI.setRelationalPlaceholders(one, two, parse));
                return true;
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (s instanceof Player) {
                    if (!s.hasPermission("placeholderapi.reload")) {
                        Msg.msg(s, "&cYou don't have permission to do that!");
                        return true;
                    }
                }

                Msg.msg(s, "&fPlaceholder&7API &bconfiguration reloaded!");
                plugin.reloadConf(s);
            } else if (args[0].equalsIgnoreCase("list")) {
                if (s instanceof Player) {
                    if (!s.hasPermission("placeholderapi.list")) {
                        Msg.msg(s, "&cYou don't have permission to do that!");
                        return true;
                    }
                }

                Set<String> registered = PlaceholderAPI.getRegisteredIdentifiers();

                if (registered.isEmpty()) {
                    Msg.msg(s, "&7There are no placeholder hooks currently registered!");
                    return true;
                }

                Msg.msg(s, registered.size() + " &7Placeholder hooks registered:");
                Msg.msg(s, registered.stream().sorted().collect(Collectors.joining(", ")));
            } else if (args.length > 1 && args[0].equalsIgnoreCase("register")) {
                if (s instanceof Player) {
                    if (!s.hasPermission("placeholderapi.register")) {
                        Msg.msg(s, "&cYou don't have permission to do that!");
                        return true;
                    }
                }

                String fileName = args[1].replace(".jar", "");
                PlaceholderExpansion ex = plugin.getExpansionManager().registerExpansion(fileName);

                if (ex == null) {
                    Msg.msg(s, "&cFailed to register expansion from " + fileName);
                    return true;
                }

                Msg.msg(s, "&aSuccessfully registered expansion: &f" + ex.getName());
            } else if (args.length > 1 && args[0].equalsIgnoreCase("unregister")) {
                if (s instanceof Player) {
                    if (!s.hasPermission("placeholderapi.register")) {
                        Msg.msg(s, "&cYou don't have permission to do that!");
                        return true;
                    }
                }

                PlaceholderExpansion ex = plugin.getExpansionManager().getRegisteredExpansion(args[1]);

                if (ex == null) {
                    Msg.msg(s, "&cFailed to find expansion: &f" + args[1]);
                    return true;
                }

                if (PlaceholderAPI.unregisterExpansion(ex)) {
                    Msg.msg(s, "&aSuccessfully unregistered expansion: &f" + ex.getName());
                } else {
                    Msg.msg(s, "&cFailed to unregister expansion: &f" + ex.getName());
                }
            } else {
                Msg.msg(s, "&cIncorrect usage! &7/papi help");
            }
        }

        return false;
    }
}