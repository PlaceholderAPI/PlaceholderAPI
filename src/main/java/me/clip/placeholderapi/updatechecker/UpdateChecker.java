/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2026 PlaceholderAPI Team
 *
 * PlaceholderAPI free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlaceholderAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.clip.placeholderapi.updatechecker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.scheduler.scheduling.schedulers.TaskScheduler;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateChecker implements Listener {
    private static final String MODRINTH_URL = "https://api.modrinth.com/v2/project/lKEzGugV/version";

    private static final int RESOURCE_ID = 6245;
    private final PlaceholderAPIPlugin plugin;
    private final TaskScheduler scheduler;
    private final String pluginVersion;
    private String modrinthVersion;
    private boolean updateAvailable;

    public UpdateChecker(PlaceholderAPIPlugin plugin) {
        this.plugin = plugin;
        scheduler = plugin.getScheduler();
        pluginVersion = plugin.getDescription().getVersion();
    }

    public boolean hasUpdateAvailable() {
        return updateAvailable;
    }

    public String getModrinthVersion() {
        return modrinthVersion;
    }

    public void fetch() {
        scheduler.runTaskAsynchronously(() -> {
            try {
                HttpsURLConnection con = (HttpsURLConnection) new URL(MODRINTH_URL).openConnection();
                con.setRequestMethod("GET");
                final JsonElement json = JsonParser.parseReader(new BufferedReader(new InputStreamReader(con.getInputStream())));
                modrinthVersion = json.getAsJsonArray().get(0).getAsJsonObject().get("version_number").getAsString();
            } catch (Exception ex) {
                plugin.getLogger().info("Failed to check for updates on modrinth.");
                return;
            }

            if (modrinthVersion == null || modrinthVersion.isEmpty()) {
                return;
            }

            updateAvailable = modrinthIsNewer();

            if (!updateAvailable) {
                return;
            }

            scheduler.runTask(() -> {
                plugin.getLogger()
                        .info("An update for PlaceholderAPI (v" + getModrinthVersion() + ") is available at:");
                plugin.getLogger()
                        .info("https://modrinth.com/plugin/placeholderapi");
                Bukkit.getPluginManager().registerEvents(this, plugin);
            });
        });
    }

    private boolean modrinthIsNewer() {
        if (modrinthVersion == null || modrinthVersion.isEmpty()) {
            return false;
        }

        int[] plV = toReadable(pluginVersion);
        int[] spV = toReadable(modrinthVersion);

        if (plV[0] < spV[0]) {
            return true;
        } else if ((plV[1] < spV[1])) {
            return true;
        } else {
            return plV[2] < spV[2];
        }
    }

    private int[] toReadable(String version) {
        if (version.contains("-DEV")) {
            version = version.split("-DEV")[0];
        }

        return Arrays.stream(version.split("\\.")).mapToInt(Integer::parseInt).toArray();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission("placeholderapi.updatenotify")) {
            Msg.msg(e.getPlayer(),
                    "&bAn update for &fPlaceholder&7API &e(&fPlaceholder&7API &fv" + getModrinthVersion()
                            + "&e)"
                    , "&bis available at &ehttps://modrinth.com/plugin/placeholderapi");
        }
    }
}
