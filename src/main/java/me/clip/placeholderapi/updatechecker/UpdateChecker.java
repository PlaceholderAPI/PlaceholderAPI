/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2021 PlaceholderAPI Team
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
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateChecker implements Listener {

  private final int RESOURCE_ID = 6245;
  private final PlaceholderAPIPlugin plugin;
  private final String pluginVersion;
  private String spigotVersion;
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
    PlaceholderAPIPlugin.getScheduler().runTaskAsynchronously(() -> {
      try {
        HttpsURLConnection con = (HttpsURLConnection) new URL(
            "https://api.spigotmc.org/legacy/update.php?resource=" + RESOURCE_ID).openConnection();
        con.setRequestMethod("GET");
        spigotVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
      } catch (Exception ex) {
        plugin.getLogger().info("Failed to check for updates on spigot.");
        return;
      }

      if (spigotVersion == null || spigotVersion.isEmpty()) {
        return;
      }

      updateAvailable = spigotIsNewer();

      if (!updateAvailable) {
        return;
      }

      PlaceholderAPIPlugin.getScheduler().runTask(() -> {
        plugin.getLogger()
            .info("An update for PlaceholderAPI (v" + getSpigotVersion() + ") is available at:");
        plugin.getLogger()
            .info("https://www.spigotmc.org/resources/placeholderapi." + RESOURCE_ID + "/");
        Bukkit.getPluginManager().registerEvents(this, plugin);
      });
    });
  }

  private boolean spigotIsNewer() {
    if (spigotVersion == null || spigotVersion.isEmpty()) {
      return false;
    }

    int[] plV = toReadable(pluginVersion);
    int[] spV = toReadable(spigotVersion);

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
          "&bAn update for &fPlaceholder&7API &e(&fPlaceholder&7API &fv" + getSpigotVersion()
              + "&e)"
          , "&bis available at &ehttps://www.spigotmc.org/resources/placeholderapi." + RESOURCE_ID
              + "/");
    }
  }
}
