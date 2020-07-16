package me.clip.placeholderapi.commands.command.ecloud;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.expansion.cloud.ExpansionCloudManager;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class EcloudDownloadCommand extends Command {
    public EcloudDownloadCommand() {
        super("ecloud download", options("&cAn expansion name must be specified!", 1));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        PlaceholderAPIPlugin plugin = PlaceholderAPIPlugin.getInstance();
        String input = args[0];
        CloudExpansion expansion = plugin.getExpansionCloud().getCloudExpansion(input);

        if (expansion == null) {
            Msg.msg(sender, "&cNo expansion found with the name: &f" + input);
            return;
        }

        PlaceholderExpansion loaded = plugin.getExpansionManager().getRegisteredExpansion(input);
        if (loaded != null && loaded.isRegistered()) {
            PlaceholderAPI.unregisterPlaceholderHook(loaded.getIdentifier());
        }

        String version = expansion.getLatestVersion();

        if (args.length == 2) {
            version = args[1];
            if (expansion.getVersion(version) == null) {
                Msg.msg(sender, "&cThe version you specified does not exist for &f" + expansion.getName());
                Msg.msg(sender, "&7Available versions: &f" + expansion.getVersions().size());
                Msg.msg(sender, String.join("&a, &f", expansion.getAvailableVersions()));

                return;
            }
        }

        Msg.msg(sender, "&aDownload starting for expansion: &f" + expansion.getName() + " &aversion: &f" + version);
        String player = ((sender instanceof Player) ? sender.getName() : null);
        ExpansionCloudManager cloud = plugin.getExpansionCloud();
        cloud.downloadExpansion(player, expansion, version);
        cloud.clean();
        cloud.fetch(plugin.getPlaceholderAPIConfig().cloudAllowUnverifiedExpansions());
    }

//    @Override
//    public List<String> handleCompletion(CommandSender sender, String[] args) {
//        List<String> downloads = new ArrayList<>();
//        if (!PlaceholderAPI.isRegistered("player")) downloads.add("player");
//
//        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
//            String identifier = plugin.getName();
//            if (!PlaceholderAPI.isRegistered(identifier)) downloads.add(identifier);
//        }
//
//        return downloads;
//    }
}
