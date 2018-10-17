/*
 *
 * PlaceholderAPI
 * Copyright (C) 2018 Ryan McCarthy
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
package me.clip.placeholderapi.expansion.cloud;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.gson.reflect.TypeToken;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ExpansionCloudManager {

    private static final String API_URL = "http://api.extendedclip.com/v2/";
    private static final Gson GSON = new Gson();


    private final PlaceholderAPIPlugin plugin;
    private final File expansionsDir;

    private final List<String> downloading = new ArrayList<>();
    private final Map<Integer, CloudExpansion> remote = new TreeMap<>();


    public ExpansionCloudManager(PlaceholderAPIPlugin plugin) {
        this.plugin = plugin;

        expansionsDir = new File(plugin.getDataFolder(), "expansions");

        final boolean result = expansionsDir.mkdirs();
        if (result) {
            plugin.getLogger().info("Created Expansions Directory");
        }

    }


    public void clean() {
        remote.clear();
        downloading.clear();
    }


    public Map<Integer, CloudExpansion> getCloudExpansions() {
        return remote;
    }

    public CloudExpansion getCloudExpansion(String name) {
        return remote.values()
                     .stream()
                     .filter(ex -> ex.getName().equalsIgnoreCase(name))
                     .findFirst()
                     .orElse(null);
    }


    public int getCloudAuthorCount() {
        return remote.values()
                     .stream()
                     .collect(Collectors.groupingBy(CloudExpansion::getAuthor, Collectors.counting()))
                     .size();
    }

    public int getToUpdateCount() {
        return ((int) PlaceholderAPI.getExpansions()
                                    .stream()
                                    .filter(ex -> getCloudExpansion(ex.getName()) != null && getCloudExpansion(ex.getName()).shouldUpdate())
                                    .count());
    }


    public Map<Integer, CloudExpansion> getAllByAuthor(String author) {
        if (remote.isEmpty()) return new HashMap<>();

        Map<Integer, CloudExpansion> byAuthor = new TreeMap<>();

        for (CloudExpansion ex : remote.values()) {
            if (!ex.getAuthor().equalsIgnoreCase(author)) continue;

            byAuthor.put(byAuthor.size(), ex);
        }

        return byAuthor;
    }

    public Map<Integer, CloudExpansion> getAllInstalled() {
        if (remote.isEmpty()) return new HashMap<>();

        Map<Integer, CloudExpansion> has = new TreeMap<>();

        for (CloudExpansion ex : remote.values()) {
            if (!ex.hasExpansion()) continue;

            has.put(has.size(), ex);
        }

        return has;
    }


    public int getPagesAvailable(Map<Integer, CloudExpansion> map, int amount) {
        if (map == null) {
            return 0;
        }
        int pages = map.size() > 0 ? 1 : 0;
        if (pages == 0) {
            return pages;
        }
        if (map.size() > amount) {
            pages = map.size() / amount;
            if (map.size() % amount > 0) {
                pages++;
            }
        }
        return pages;
    }

    public Map<Integer, CloudExpansion> getPage(Map<Integer, CloudExpansion> map, int page, int size) {
        if (map == null || map.size() == 0 || page > getPagesAvailable(map, size)) {
            return new HashMap<>();
        }

        int end = size * page;
        int start = end - size;

        Map<Integer, CloudExpansion> ex = new TreeMap<>();
        IntStream.range(start, end).forEach(n -> ex.put(n, map.get(n)));

        return ex;
    }


    public void fetch(boolean allowUnverified) {

        plugin.getLogger().info("Fetching available expansion information...");

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {

            final String readJson = URLReader.read(API_URL);
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


    public boolean isDownloading(String expansion) {
        return downloading.contains(expansion);
    }

    private void download(URL url, String name) throws IOException {

        InputStream is = null;

        FileOutputStream fos = null;

        try {

            URLConnection urlConn = url.openConnection();

            is = urlConn.getInputStream();

            fos = new FileOutputStream(
                    expansionsDir.getAbsolutePath() + File.separator + "Expansion-" + name + ".jar");

            byte[] buffer = new byte[is.available()];

            int l;

            while ((l = is.read(buffer)) > 0) {
                fos.write(buffer, 0, l);
            }
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }
    }


    public void downloadExpansion(final String player, final CloudExpansion ex) {
        downloadExpansion(player, ex, ex.getLatestVersion());
    }

    public void downloadExpansion(final String player, final CloudExpansion ex, final String version) {

        if (downloading.contains(ex.getName())) {
            return;
        }

        final CloudExpansion.Version ver = ex.getVersions()
                                             .stream()
                                             .filter(v -> v.getVersion().equals(version))
                                             .findFirst()
                                             .orElse(null);

        if (ver == null) {
            return;
        }

        downloading.add(ex.getName());

        plugin.getLogger().info("Attempting download of expansion: " + ex.getName() + (player != null ? " by user: " + player : "") + " from url: " + ver.getUrl());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            try {

                download(new URL(ver.getUrl()), ex.getName());

                plugin.getLogger().info("Download of expansion: " + ex.getName() + " complete!");

            } catch (Exception e) {

                plugin.getLogger()
                      .warning("Failed to download expansion: " + ex.getName() + " from: " + ver.getUrl());

                Bukkit.getScheduler().runTask(plugin, () -> {

                    downloading.remove(ex.getName());

                    if (player != null) {
                        Player p = Bukkit.getPlayer(player);

                        if (p != null) {
                            Msg.msg(p, "&cThere was a problem downloading expansion: &f" + ex.getName());
                        }
                    }
                });

                return;
            }

            Bukkit.getScheduler().runTask(plugin, () -> {

                downloading.remove(ex.getName());

                if (player != null) {

                    Player p = Bukkit.getPlayer(player);

                    if (p != null) {
                        Msg.msg(p, "&aExpansion &f" + ex.getName() + " &adownload complete!");
                    }
                }
            });

        });
    }


    private static class URLReader {

        static String read(String url) {
            StringBuilder builder = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {

                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    builder.append(inputLine);
                }
            } catch (Exception ex) {
                builder.setLength(0);
            }

            return builder.toString();
        }
    }

}
