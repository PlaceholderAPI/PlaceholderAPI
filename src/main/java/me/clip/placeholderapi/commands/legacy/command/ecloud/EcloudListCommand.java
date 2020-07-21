package me.clip.placeholderapi.commands.legacy.command.ecloud;

import com.google.common.collect.Sets;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.legacy.Command;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.Msg;
import me.rayzr522.jsonmessage.JSONMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static me.clip.placeholderapi.util.Msg.color;

public final class EcloudListCommand extends Command {
    private static final int MINIMUM_ARGUMENTS = 1;
    private static final Set<String> COMPLETIONS = Sets.newHashSet(
            "all",
            "author",
            "installed"
    );

    public EcloudListCommand() {
        super("ecloud list", options("&cIncorrect usage! &7/papi ecloud list <all/author/installed> (page)",
                MINIMUM_ARGUMENTS, "placeholderapi.ecloud"));
    }

    @Override
    public void execute(@NotNull final CommandSender sender, @NotNull final String[] args) {
        final PlaceholderAPIPlugin plugin = PlaceholderAPIPlugin.getInstance();
        int page = 1;

        String author;
        boolean installed = false;

        author = args[0];

        if (author.equalsIgnoreCase("all")) {
            author = null;
        } else if (author.equalsIgnoreCase("installed")) {
            author = null;
            installed = true;
        }

        if (args.length >= 2) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                Msg.msg(sender, "&cPage number must be an integer!");

                return;
            }
        }

        if (page < 1) {
            Msg.msg(sender, "&cPage must be greater than or equal to 1!");

            return;
        }

        int avail;
        Map<Integer, CloudExpansion> ex;

        if (installed) {
            ex = plugin.getExpansionCloud().getAllInstalled();
        } else if (author == null) {
            ex = plugin.getExpansionCloud().getCloudExpansions();
        } else {
            ex = plugin.getExpansionCloud().getAllByAuthor(author);
        }

        if (ex == null || ex.isEmpty()) {
            Msg.msg(sender, "&cNo expansions available" + (author != null ? " for author &f" + author : ""));

            return;
        }

        avail = plugin.getExpansionCloud().getPagesAvailable(ex, 10);
        if (page > avail) {
            Msg.msg(sender, "&cThere " + ((avail == 1) ? " is only &f" + avail + " &cpage available!"
                    : "are only &f" + avail + " &cpages available!"));

            return;
        }

        Msg.msg(sender, "&bShowing expansions for&7: &f" + (author != null ? author
                : (installed ? "all installed" : "all available")) + " &8&m--&r &bamount&7: &f" + ex
                .size() + " &bpage&7: &f" + page + "&7/&f" + avail);

        ex = plugin.getExpansionCloud().getPage(ex, page, 10);

        if (ex == null) {
            Msg.msg(sender, "&cThere was a problem getting the requested page...");

            return;
        }

        Msg.msg(sender, "&aGreen = Expansions you have");
        Msg.msg(sender, "&6Gold = Expansions which need updated");

        if (!(sender instanceof Player)) {
            final Map<String, CloudExpansion> expansions = new HashMap<>();

            for (CloudExpansion exp : ex.values()) {
                if (exp == null || exp.getName() == null) {
                    continue;
                }

                expansions.put(exp.getName(), exp);
            }

            final List<String> ce = expansions.keySet().stream().sorted().collect(Collectors.toList());

            int i = (int) ex.keySet().toArray()[0];

            for (String name : ce) {
                if (expansions.get(name) == null) {
                    continue;
                }

                final CloudExpansion expansion = expansions.get(name);

                Msg.msg(sender,
                        "&b" + i + "&7: " + (expansion.shouldUpdate() ? "&6"
                                : (expansion.hasExpansion() ? "&a" : "&7")) + expansion
                                .getName() + " &8&m-- &r" + expansion.getVersion().getUrl());
                i++;
            }

            return;
        }

        final Player p = (Player) sender;

        final Map<String, CloudExpansion> expansions = new HashMap<>();

        for (final CloudExpansion exp : ex.values()) {
            if (exp == null || exp.getName() == null) {
                continue;
            }

            expansions.put(exp.getName(), exp);
        }

        final List<String> ce = expansions.keySet().stream().sorted().collect(Collectors.toList());

        int i = page > 1 ? page * 10 : 0;

        for (String name : ce) {
            if (expansions.get(name) == null) {
                continue;
            }

            final CloudExpansion expansion = expansions.get(name);
            final StringBuilder sb = new StringBuilder();

            if (expansion.shouldUpdate()) {
                sb.append("&6Click to update to the latest version of this expansion\n\n");
            } else if (!expansion.hasExpansion()) {
                sb.append("&bClick to download this expansion\n\n");
            } else {
                sb.append("&aYou have the latest version of this expansion\n\n");
            }

            sb.append("&bAuthor&7: &f").append(expansion.getAuthor()).append("\n");
            sb.append("&bVerified&7: &f").append(expansion.isVerified()).append("\n");
            sb.append("&bLatest version&7: &f").append(expansion.getVersion().getVersion()).append("\n");
            sb.append("&bLast updated&7: &f").append(expansion.getTimeSinceLastUpdate()).append(" ago\n");
            sb.append("\n").append(expansion.getDescription());

            final String msg = color(
                    "&b" + (i + 1) + "&7: " + (expansion.shouldUpdate() ? "&6"
                            : (expansion.hasExpansion() ? "&a" : "")) + expansion.getName());

            final String hover = color(sb.toString());

            final JSONMessage line = JSONMessage.create(msg);
            line.tooltip(hover);

            if (expansion.shouldUpdate() || !expansion.hasExpansion()) {
                line.suggestCommand("/papi ecloud download " + expansion.getName());
            } else {
                line.suggestCommand("/papi ecloud info " + expansion.getName());
            }

            line.send(p);
            i++;
        }
    }

    @NotNull
    @Override
    public List<String> handleCompletion(@NotNull final CommandSender sender, @NotNull final String[] args) {
        if (args.length == MINIMUM_ARGUMENTS) {
            return StringUtil.copyPartialMatches(args[0], COMPLETIONS, new ArrayList<>(COMPLETIONS.size()));
        }

        if (args.length == MINIMUM_ARGUMENTS + 1) {
            return Collections.singletonList("Pages");
        }

        return super.handleCompletion(sender, args);
    }
}
