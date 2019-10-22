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
package me.clip.placeholderapi.nukkit.expansion.cloud;

import cn.nukkit.Server;
import com.google.common.reflect.TypeToken;
import me.clip.placeholderapi.common.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.common.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.common.expansion.cloud.ExpansionCloudManager;
import me.clip.placeholderapi.common.util.Msg;
import me.clip.placeholderapi.nukkit.NukkitPAPIPlayer;
import me.clip.placeholderapi.nukkit.PlaceholderAPINukkitPlugin;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class NukkitExpansionCloudManager extends ExpansionCloudManager {
    private final PlaceholderAPINukkitPlugin plugin;

    public NukkitExpansionCloudManager(PlaceholderAPINukkitPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public void fetch(boolean allowUnverified) {
        plugin.getLogger().info("Fetching available expansion information...");

        plugin.getServer().getScheduler().scheduleTask(plugin, () -> {
            final String readJson = ExpansionCloudManager.URLReader.read(API_URL);
            final Map<String, CloudExpansion> data = GSON.fromJson(readJson, new TypeToken<Map<String, CloudExpansion>>() {
            }.getType());
            final List<CloudExpansion> unsorted = new ArrayList<>();

            data.forEach((name, cexp) -> {
                if ((allowUnverified || cexp.isVerified()) && cexp.getLatestVersion() != null && cexp.getVersion(cexp.getLatestVersion()) != null) {
                    cexp.setName(name);

                    PlaceholderExpansion ex = plugin.getExpansionManager().getRegisteredExpansion(cexp.getName());

                    if (ex != null && ex.isRegistered()) {
                        cexp.setHasExpansion(true);
                        if (!ex.getVersion().equals(cexp.getLatestVersion())) {
                            cexp.setShouldUpdate(true);
                        }
                    }

                    unsorted.add(cexp);
                }
            });

            unsorted.sort(Comparator.comparing(CloudExpansion::getLastUpdate).reversed());

            int count = 0;
            for (CloudExpansion e : unsorted) {
                remote.put(count++, e);
            }

            plugin.getLogger().info(count + " placeholder expansions are available on the cloud.");

            long updates = getToUpdateCount();

            if (updates > 0) {
                plugin.getLogger().info(updates + " installed expansions have updates available.");
            }
        });
    }

    public void downloadExpansion(final String player, final CloudExpansion ex) {
        downloadExpansion(player, ex, ex.getLatestVersion());
    }

    public void downloadExpansion(final String player, final CloudExpansion ex, final String version) {
        if (downloading.contains(ex.getName())) {
            return;
        }

        final CloudExpansion.Version ver = ex.getVersions().stream().filter(v -> v.getVersion().equals(version)).findFirst().orElse(null);

        if (ver == null) {
            return;
        }

        downloading.add(ex.getName());
        plugin.getLogger().info("Attempting download of expansion: " + ex.getName() + (player != null ? " by user: " + player : "") + " from url: " + ver.getUrl());

        Server.getInstance().getScheduler().scheduleTask(plugin, () -> {
            try {
                download(new URL(ver.getUrl()), ex.getName());

                plugin.getLogger().info("Download of expansion: " + ex.getName() + " complete!");

            } catch (Exception e) {
                plugin.getLogger().warning("Failed to download expansion: " + ex.getName() + " from: " + ver.getUrl());

                Server.getInstance().getScheduler().scheduleTask(plugin, () -> {
                    downloading.remove(ex.getName());

                    if (player != null) {
                        NukkitPAPIPlayer p = (NukkitPAPIPlayer) Server.getInstance().getPlayer(player);

                        if (p != null) {
                            Msg.msg(p, "&cThere was a problem downloading expansion: &f" + ex.getName());
                        }
                    }
                });

                return;
            }

            Server.getInstance().getScheduler().scheduleTask(plugin, () -> {
                downloading.remove(ex.getName());

                if (player != null) {
                    NukkitPAPIPlayer p = (NukkitPAPIPlayer) Server.getInstance().getPlayer(player);

                    if (p != null) {
                        Msg.msg(p, "&aExpansion &f" + ex.getName() + " &adownload complete!");
                        Msg.msg(p, "&aMake sure to run &f/papi reload &ato enable it!");
                    }
                }
            });
        }, true);
    }
}
