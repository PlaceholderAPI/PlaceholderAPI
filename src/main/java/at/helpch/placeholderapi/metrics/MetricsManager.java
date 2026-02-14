package at.helpch.placeholderapi.metrics;

import at.helpch.placeholderapi.PlaceholderAPIPlugin;
import at.helpch.placeholderapi.expansion.PlaceholderExpansion;
import at.helpch.placeholderapi.expansion.manager.CloudExpansionManager;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hypixel.hytale.common.util.java.ManifestUtil;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.universe.Universe;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MetricsManager {
    private static final String METRICS_URL = "https://ecloud.placeholderapi.com/api/metrics/";
    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    private final PlaceholderAPIPlugin main;

    public MetricsManager(@NotNull final PlaceholderAPIPlugin main) {
        this.main = main;
    }

    public void send() {
        final String pluginVersion = main.getManifest().getVersion().toString();
        final String serverVersion = ManifestUtil.getImplementationVersion();
        final boolean usingEcloud = main.configManager().config().cloudEnabled();
        final List<String> expansionsUsed = main.localExpansionManager().getExpansions().stream()
                .map(PlaceholderExpansion::getName)
                .toList();
        final Map<String, String> expansionVersions = main.localExpansionManager().getExpansions().stream()
                .collect(Collectors.toUnmodifiableMap(PlaceholderExpansion::getName, PlaceholderExpansion::getVersion));
        final String javaVersion = System.getProperty("java.version");
        final String serverOs = System.getProperty("os.name");
        final int playerCount = Universe.get().getPlayerCount();
        final UUID serverUuid = main.configManager().config().metricsUuid();

        final MetricsPayload payload = new MetricsPayload(pluginVersion, serverVersion, usingEcloud, expansionsUsed,
                expansionVersions, javaVersion, serverOs, playerCount, serverUuid);

        final String json = GSON.toJson(payload);

        final HttpRequest request = HttpRequest.newBuilder(URI.create(METRICS_URL))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .header("User-Agent", CloudExpansionManager.USER_AGENT)
                .build();

        CLIENT.sendAsync(request, HttpResponse.BodyHandlers.discarding());
    }
}
