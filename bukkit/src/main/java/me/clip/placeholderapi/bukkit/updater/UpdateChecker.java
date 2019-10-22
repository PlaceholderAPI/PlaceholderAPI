/*
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
 */
package me.clip.placeholderapi.bukkit.updater;

import me.clip.placeholderapi.bukkit.PlaceholderAPIBukkitPlugin;
import me.clip.placeholderapi.common.PAPIPlayer;
import me.clip.placeholderapi.common.util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateChecker implements Listener {
    private final int RESOURCE_ID = 6245;
    private final PlaceholderAPIBukkitPlugin plugin;
    private final String pluginVer;
    private String serverVer;
    private boolean updateAvailable;

    public UpdateChecker(PlaceholderAPIBukkitPlugin plugin) {
        this.plugin = plugin;
        pluginVer = plugin.getDescription().getVersion();
    }

    public boolean hasUpdateAvailable() {
        return updateAvailable;
    }

    public String getServerVersion() {
        return serverVer;
    }

    public void fetch() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                HttpsURLConnection con = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=" + RESOURCE_ID).openConnection();
                con.setRequestMethod("GET");
                serverVer = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            } catch (Exception ex) {
                plugin.getLogger().info("Failed to check for updates on spigot.");
                return;
            }

            if (serverVer == null || serverVer.isEmpty()) {
                return;
            }

            updateAvailable = spigotIsNewer();

            if (!updateAvailable) {
                return;
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                plugin.getLogger().info("An update for PlaceholderAPI (v" + getServerVersion() + ") is available at:");
                plugin.getLogger().info("https://www.spigotmc.org/resources/placeholderapi." + RESOURCE_ID + "/");
                Bukkit.getPluginManager().registerEvents(this, plugin);
            });
        });
    }

    private boolean spigotIsNewer() {
        if (serverVer == null || serverVer.isEmpty()) {
            return false;
        }

        String plV = toReadable(pluginVer);
        String spV = toReadable(serverVer);
        return plV.compareTo(spV) < 0;
    }

    private String toReadable(String version) {
        if (version.contains("-DEV-")) {
            version = version.split("-DEV-")[0];
        }

        return version.replaceAll("\\.", "");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission("placeholderapi.updatenotify")) {
            Msg.msg((PAPIPlayer) e.getPlayer(), "&bAn update for &fPlaceholder&7API &e(&fPlaceholder&7API &fv" + getServerVersion() + "&e)",
                    "&bis available at &ehttps://www.spigotmc.org/resources/placeholderapi." + RESOURCE_ID + "/");
        }
    }
}