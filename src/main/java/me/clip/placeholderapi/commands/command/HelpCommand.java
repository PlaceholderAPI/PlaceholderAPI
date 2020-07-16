package me.clip.placeholderapi.commands.command;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;

public final class HelpCommand extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Msg.msg(sender, "PlaceholderAPI &aHelp &e(&f" + PlaceholderAPIPlugin.getInstance().getDescription().getVersion() + "&e)",
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
                "&b/papi cmdparse <(playername)/me> <...args>",
                "&fParse a String with placeholders and execute it as command",
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
    }
}
