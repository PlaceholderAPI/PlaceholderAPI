package me.clip.placeholderapi.commands.papi.ecloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.rayzr522.jsonmessage.JSONMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.clip.placeholderapi.util.Msg.color;
import static me.clip.placeholderapi.util.Msg.msg;

public class ListCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public ListCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("ecloud list");
        options.permissions("placeholdera.ecloud");

        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String[] args) {
        int page = 1;

        String author;
        boolean installed = false;

        if (args.length < 3) {
            msg(sender, "&cIncorrect usage! &7/papi ecloud list <all/author/installed> (page)");
            return true;
        }

        author = args[2];

        if (author.equalsIgnoreCase("all")) {
            author = null;
        } else if (author.equalsIgnoreCase("installed")) {
            author = null;
            installed = true;
        }

        if (args.length >= 4) {
            try {
                page = Integer.parseInt(args[3]);
            } catch (NumberFormatException ex) {
                msg(sender, "&cPage number must be an integer!");

                return true;
            }
        }

        if (page < 1) {
            msg(sender, "&cPage must be greater than or equal to 1!");

            return true;
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
            msg(sender, "&cNo expansions available" + (author != null ? " for author &f" + author : ""));

            return true;
        }

        avail = plugin.getExpansionCloud().getPagesAvailable(ex, 10);
        if (page > avail) {
            msg(sender, "&cThere " + ((avail == 1) ? " is only &f" + avail + " &cpage available!"
                    : "are only &f" + avail + " &cpages available!"));

            return true;
        }

        msg(sender, "&bShowing expansions for&7: &f" + (author != null ? author
                : (installed ? "all installed" : "all available")) + " &8&m--&r &bamount&7: &f" + ex
                .size() + " &bpage&7: &f" + page + "&7/&f" + avail);

        ex = plugin.getExpansionCloud().getPage(ex, page, 10);

        if (ex == null) {
            msg(sender, "&cThere was a problem getting the requested page...");

            return true;
        }

        msg(sender, "&aGreen = Expansions you have");
        msg(sender, "&6Gold = Expansions which need updated");

        if (!(sender instanceof Player)) {
            Map<String, CloudExpansion> expansions = new HashMap<>();

            for (CloudExpansion exp : ex.values()) {
                if (exp == null || exp.getName() == null) {
                    continue;
                }

                expansions.put(exp.getName(), exp);
            }

            List<String> ce = expansions.keySet().stream().sorted().collect(Collectors.toList());

            int i = (int) ex.keySet().toArray()[0];

            for (String name : ce) {
                if (expansions.get(name) == null) {
                    continue;
                }

                CloudExpansion expansion = expansions.get(name);

                msg(sender,
                        "&b" + i + "&7: " + (expansion.shouldUpdate() ? "&6"
                                : (expansion.hasExpansion() ? "&a" : "&7")) + expansion
                                .getName() + " &8&m-- &r" + expansion.getVersion().getUrl());
                i++;
            }

            return true;
        }

        Player p = (Player) sender;

        Map<String, CloudExpansion> expansions = new HashMap<>();

        for (CloudExpansion exp : ex.values()) {
            if (exp == null || exp.getName() == null) {
                continue;
            }

            expansions.put(exp.getName(), exp);
        }

        List<String> ce = expansions.keySet().stream().sorted().collect(Collectors.toList());

        int i = page > 1 ? page * 10 : 0;

        for (String name : ce) {
            if (expansions.get(name) == null) {
                continue;
            }

            CloudExpansion expansion = expansions.get(name);
            StringBuilder sb = new StringBuilder();

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

            String msg = color(
                    "&b" + (i + 1) + "&7: " + (expansion.shouldUpdate() ? "&6"
                            : (expansion.hasExpansion() ? "&a" : "")) + expansion.getName());

            String hover = color(sb.toString());

            JSONMessage line = JSONMessage.create(msg);
            line.tooltip(hover);

            if (expansion.shouldUpdate() || !expansion.hasExpansion()) {
                line.suggestCommand("/papi ecloud download " + expansion.getName());
            } else {
                line.suggestCommand("/papi ecloud info " + expansion.getName());
            }

            line.send(p);
            i++;
        }

        return true;
    }
}
