package at.helpch.placeholderapi.metrics;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class MetricsPayload {
    private final String pluginVersion;
    private final String serverVersion;
    private final String platform = "HYTALE";
    private final boolean usingEcloud;
    private final List<String> expansionsUsed;
    private final Map<String, String> expansionVersions;
    private final String javaVersion;
    private final String serverOs;
    private final int playerCount;
    private final UUID serverUuid;
    private final String pluginName = "PlaceholderAPI";

    public MetricsPayload(final String pluginVersion, final String serverVersion, final boolean usingEcloud,
                          final List<String> expansionsUsed, final Map<String, String> expansionVersions,
                          final String javaVersion, final String serverOs, final int playerCount,
                          final UUID serverUuid) {
        this.pluginVersion = pluginVersion;
        this.serverVersion = serverVersion;
        this.usingEcloud = usingEcloud;
        this.expansionsUsed = expansionsUsed;
        this.expansionVersions = expansionVersions;
        this.javaVersion = javaVersion;
        this.serverOs = serverOs;
        this.playerCount = playerCount;
        this.serverUuid = serverUuid;
    }
}
