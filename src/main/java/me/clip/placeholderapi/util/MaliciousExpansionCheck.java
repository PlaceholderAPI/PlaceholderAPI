package me.clip.placeholderapi.util;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.configuration.PlaceholderAPIConfig;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class MaliciousExpansionCheck {
    private static final String MESSAGE =
            "\n###############################################\n" +
            "###############################################\n" +
            "PlaceholderAPI performs checks at startup and /papi reload for known malicious expansions. If you're seeing this message, there are the following malicious expansions in plugins/PlaceholderAPI/expansions.\n" +
            "%s" +
            "To prevent further infection PlaceholderAPI has stopped the server.\n" +
            "Best practice is a complete system wipe and reinstall of your server software and plugins to be safe.\n" +
            "###############################################\n" +
            "###############################################";

    private final PlaceholderAPIPlugin main;

    public MaliciousExpansionCheck(@NotNull final PlaceholderAPIPlugin main) {
        this.main = main;
    }

    public boolean runChecks() {
        if (!main.getPlaceholderAPIConfig().detectMaliciousExpansions()) {
            return false;
        }

        final File expansionsFolder = new File(main.getDataFolder(), "expansions");

        if (!expansionsFolder.exists()) {
            return false;
        }

        final Set<String> knownMalware;

        try {
            final String malware = Resources.toString(new URL("https://check.placeholderapi.com"), StandardCharsets.UTF_8);
            knownMalware = Arrays.stream(malware.split("\n")).collect(Collectors.toSet());
        } catch (Exception e) {
            main.getLogger().log(Level.SEVERE, "Failed to download anti malware hash check list from https://check.placeholderapi.com", e);
            return false;
        }

        final Set<String> malwarePaths = new HashSet<>();

        for (File file : expansionsFolder.listFiles()) {
            try {
                final String hash = Hashing.sha256().hashBytes(Files.asByteSource(file).read()).toString();

                if (knownMalware.contains(hash)) {
                    malwarePaths.add(file.getAbsolutePath());
                }
            } catch (Exception e) {
                main.getLogger().log(Level.SEVERE, "Error occurred while trying to read " + file.getAbsolutePath(), e);
            }
        }

        if (malwarePaths.isEmpty()) {
            return false;
        }

        main.getLogger().severe(String.format(MESSAGE, malwarePaths.stream().map(p -> "HASH OF " + p + " MATCHES KNOWN MALICIOUS EXPANSION DELETE IMMEDIATELY\n").collect(Collectors.joining())));

        main.getServer().shutdown();
        return true;
    }
}
