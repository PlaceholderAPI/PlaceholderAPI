package me.clip.placeholderapi.commands.papi;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HelpCommand extends Command {
    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public HelpCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("help", 0);
        options.def(true).permissions("placeholderapi.ecloud");

        this.plugin = plugin;
    }

    @Override
    public boolean execute(final @NotNull CommandSender sender, final String[] args) {
        Msg.msg(sender, "PlaceholderAPI &aHelp &e(&f" + plugin.getDescription().getVersion() + "&e)",
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

        if (sender.hasPermission("placeholderapi.ecloud")) {
            Msg.msg(sender, "&b/papi disablecloud",
                    "&fDisable the expansion cloud",
                    "&b/papi ecloud",
                    "&fView ecloud command usage",
                    "&b/papi enablecloud",
                    "&fEnable the expansion cloud");

        }

        return true;
    }

}
