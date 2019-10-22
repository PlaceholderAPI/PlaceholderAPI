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
package me.clip.placeholderapi.common.expansion.cloud;

import com.google.gson.Gson;
import me.clip.placeholderapi.common.PlaceholderAPI;
import me.clip.placeholderapi.common.PlaceholderAPIPlugin;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExpansionCloudManager {
    public static final String API_URL = "http://api.extendedclip.com/v2/";
    public static final Gson GSON = new Gson();
    public static final Map<Integer, CloudExpansion> remote = new TreeMap<>();
    public final List<String> downloading = new ArrayList<>();
    private final PlaceholderAPIPlugin plugin;
    private final File expansionsDir;

    public ExpansionCloudManager(PlaceholderAPIPlugin plugin) {
        this.plugin = plugin;

        expansionsDir = new File(plugin.getPluginDataFolder(), "expansions");
        final boolean result = expansionsDir.mkdirs();

        if (result) {
            plugin.getMainLogger().info("Created expansions directory!");
        }
    }

    public static CloudExpansion getCloudExpansion(String name) {
        return remote.values().stream().filter(ex -> ex.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static int getToUpdateCount() {
        return ((int) PlaceholderAPI.getExpansions().stream().filter(ex -> getCloudExpansion(ex.getName()) != null && getCloudExpansion(ex.getName()).shouldUpdate()).count());
    }

    public void clean() {
        remote.clear();
        downloading.clear();
    }

    public Map<Integer, CloudExpansion> getCloudExpansions() {
        return remote;
    }

    public int getCloudAuthorCount() {
        return remote.values().stream().collect(Collectors.groupingBy(CloudExpansion::getAuthor, Collectors.counting())).size();
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

    public boolean isDownloading(String expansion) {
        return downloading.contains(expansion);
    }

    protected void download(URL url, String name) throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            URLConnection urlConn = url.openConnection();

            is = urlConn.getInputStream();
            fos = new FileOutputStream(expansionsDir.getAbsolutePath() + File.separator + "Expansion-" + name + ".jar");

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

    public static class URLReader {
        public static String read(String url) {
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