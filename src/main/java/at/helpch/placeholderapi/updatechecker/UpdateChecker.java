package at.helpch.placeholderapi.updatechecker;

import at.helpch.placeholderapi.PlaceholderAPIPlugin;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class UpdateChecker {
    private static final String CURSEFORGE_URL = "https://api.cfwidget.com/hytale/mods/placeholder-api";

    private static final int RESOURCE_ID = 6245;
    private final PlaceholderAPIPlugin plugin;
    private final String pluginVersion;
    private String curseforgeVersion;
    private boolean updateAvailable;

    public UpdateChecker(PlaceholderAPIPlugin plugin) {
        this.plugin = plugin;
        pluginVersion = plugin.getManifest().getVersion().toString();
    }

    public boolean hasUpdateAvailable() {
        return updateAvailable;
    }

    public String getCurseforgeVersion() {
        return curseforgeVersion;
    }

    public void fetch() {
        plugin.getTaskRegistry().registerTask(CompletableFuture.runAsync(() -> {
            try {
                HttpsURLConnection con = (HttpsURLConnection) new URI(CURSEFORGE_URL).toURL().openConnection();
                con.setRequestMethod("GET");
                final JsonElement json = new Gson().fromJson(new BufferedReader(new InputStreamReader(con.getInputStream())), JsonElement.class);
                final String jar = json.getAsJsonObject().get("download").getAsJsonObject().get("name").getAsString();
                final String[] parts = jar.split("-");

                if (parts.length >= 2) {
                    curseforgeVersion = parts[1];
                } else {
                    return;
                }
            } catch (Exception ex) {
                plugin.getLogger().atInfo().log("Failed to check for updates on curseforge.");
                return;
            }

            if (curseforgeVersion == null || curseforgeVersion.isEmpty()) {
                return;
            }

            updateAvailable = curseforgeIsNewer();

            if (!updateAvailable) {
                return;
            }

            plugin.getLogger().atInfo().log("An update for PlaceholderAPI (v" + getCurseforgeVersion() + ") is available at:");
            plugin.getLogger().atInfo().log("https://placeholderapi.com/download");
            plugin.getEventRegistry().registerGlobal(PlayerReadyEvent.class, this::onJoin);
        }));
    }

    private boolean curseforgeIsNewer() {
        if (curseforgeVersion == null || curseforgeVersion.isEmpty()) {
            return false;
        }

        int[] plV = toReadable(pluginVersion);
        int[] spV = toReadable(curseforgeVersion);

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

    public void onJoin(PlayerReadyEvent e) {
        if (e.getPlayer().hasPermission("placeholderapi.updatenotify")) {
            final Message message = Message.raw("An update for ").color(Color.CYAN)
                    .insert(Message.raw("Placeholder").color(Color.WHITE))
                    .insert(Message.raw("API").color(Color.LIGHT_GRAY))
                    .insert(Message.raw(" (").color(Color.YELLOW))
                    .insert(Message.raw("Placeholder").color(Color.WHITE))
                    .insert(Message.raw("API").color(Color.LIGHT_GRAY))
                    .insert(Message.raw(" v" + getCurseforgeVersion()).color(Color.WHITE))
                    .insert(Message.raw(")").color(Color.YELLOW))
                    .insert(Message.raw("\nis available at ").color(Color.CYAN))
                    .insert(Message.raw("https://placeholderapi.com/download").color(Color.YELLOW).link("https://www.curseforge.com/hytale/mods/placeholder-api").bold(true).italic(true));
            e.getPlayer().sendMessage(message);
        }
    }
}
