package me.clip.placeholderapi.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public final class CompletionHandler implements TabCompleter {
    private static String[] splitArguments(String[] args, String command) {
        int skip = StringUtils.split(command).length;
        return Arrays.stream(args).skip(skip).toArray(String[]::new);
    }

    // it makes me physically cringe trying to understand why bukkit uses a list instead of a set for this
    // It's because of the list order. Even if they wanted to change that, they couldn't for the sake of backward compatibility. ~Crypto
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command bukkitCommand, @NotNull String name, String[] args) {
        String joined = String.join(" ", args).toLowerCase(Locale.ENGLISH);

        if (args.length > 1) {
            return CommandHandler.COMMANDS.stream()
                    .filter(command -> sender.hasPermission(command.getPermission()) && joined.startsWith(command.getMatch()))
                    .findFirst()
                    .map(command -> command.handleCompletion(sender, splitArguments(args, command.getMatch())))
                    .orElse(Collections.emptyList());
        }
        return CommandHandler.COMMANDS.stream()
                .filter(command -> sender.hasPermission(command.getPermission()) && (args[0].isEmpty() || command.getMatch().startsWith(joined)))
                .map(Command::getMatch).collect(Collectors.toList());
    }
}
