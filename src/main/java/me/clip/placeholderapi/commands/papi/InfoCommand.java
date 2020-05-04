package me.clip.placeholderapi.commands.papi;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class InfoCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public InfoCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("info");
        options.permissions("placeholderapi.info").requiredArgs(1);

        this.plugin = plugin;
    }

    @Override
    public boolean execute(final @NotNull CommandSender sender, final String[] args) {
        PlaceholderExpansion ex = plugin.getExpansionManager().getRegisteredExpansion(args[1]);
        if (ex == null) {
            Msg.msg(sender, "&cThere is no expansion loaded with the identifier: &f" + args[1]);

            return true;
        }

        Msg.msg(sender, "&7Placeholder expansion info for: &f" + ex.getName());
        Msg.msg(sender, "&7Status: " + (ex.isRegistered() ? "&aRegistered" : "&cNot registered"));

        if (ex.getAuthor() != null) {
            Msg.msg(sender, "&7Created by: &f" + ex.getAuthor());
        }

        if (ex.getVersion() != null) {
            Msg.msg(sender, "&7Version: &f" + ex.getVersion());
        }

        if (ex.getRequiredPlugin() != null) {
            Msg.msg(sender, "&7Requires plugin: &f" + ex.getRequiredPlugin());
        }

        if (ex.getPlaceholders() != null) {
            Msg.msg(sender, "&8&m-- &r&7Placeholders &8&m--");

            for (String placeholder : ex.getPlaceholders()) {
                Msg.msg(sender, placeholder);
            }
        }

        return true;
    }

}
