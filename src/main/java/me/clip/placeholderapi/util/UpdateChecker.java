/*
 *
 * PlaceholderAPI
 * Copyright (C) 2019 Ryan McCarthy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */
package me.clip.placeholderapi.updatechecker;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.util.Msg;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UpdateChecker implements Listener {
    private static final int RESOURCE_ID = 6245;
    private static final String SPIGOT_API = "https://api.spigotmc.org/legacy/update.php?resource=" + RESOURCE_ID;
    private final PlaceholderAPIPlugin plugin;
    private String spigotVersion;
    private final String pluginVersion;
    private boolean updateAvailable;

    public UpdateChecker(PlaceholderAPIPlugin i) {
        plugin = i;
        pluginVersion = i.getDescription().getVersion();
    }

    public boolean hasUpdateAvailable() {
        return updateAvailable;
    }

    public String getSpigotVersion() {
        return spigotVersion;
    }

    public void fetch() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                HttpsURLConnection con = (HttpsURLConnection) new URL(SPIGOT_API).openConnection();

                // Prevents the server from freezing with bad internet connection.
                con.setRequestMethod("GET");
                con.setConnectTimeout(2000);
                con.setReadTimeout(2000);

                spigotVersion = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8)).readLine();
            } catch (Exception ex) {
                plugin.getLogger().info("Failed to check for updates on spigot.");
                return;
            }

            if (spigotVersion == null || spigotVersion.isEmpty()) return;
            updateAvailable = spigotIsNewer();
            if (!updateAvailable) return;

            Bukkit.getScheduler().runTask(plugin, () -> {
                plugin.getLogger()
                        .info("An update for PlaceholderAPI (v" + spigotVersion + ") is available at:");
                plugin.getLogger()
                        .info("https://www.spigotmc.org/resources/" + RESOURCE_ID + '/');
                Bukkit.getPluginManager().registerEvents(this, plugin);
            });
        });
    }

    private boolean spigotIsNewer() {
        if (spigotVersion == null || spigotVersion.isEmpty()) return false;

        String plV = toReadable(pluginVersion);
        String spV = toReadable(spigotVersion);
        return plV.compareTo(spV) < 0;
    }

    private String toReadable(String version) {
        if (version.contains("-DEV-")) version = StringUtils.split(version, "-DEV-")[0];
        return StringUtils.remove(version, '.');
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("placeholderapi.updatenotify")) {
            Msg.msg(player,
                    "&bAn update for &fPlaceholder&7API &e(&fPlaceholder&7API &fv" + getSpigotVersion() + "&e)",
                    "&bis available at &ehttps://www.spigotmc.org/resources/placeholderapi." + RESOURCE_ID + '/');
        }
    }
}
