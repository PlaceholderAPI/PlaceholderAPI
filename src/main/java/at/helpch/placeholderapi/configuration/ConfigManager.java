package at.helpch.placeholderapi.configuration;

import at.helpch.placeholderapi.PlaceholderAPIPlugin;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class ConfigManager {
    private static final Yaml YAML;
    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
//            .registerTypeAdapter()
    private static final Pattern LINE_DELIMITER = Pattern.compile("\n");

    static {
        final DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        YAML = new Yaml(options);
    }

    private final JavaPlugin main;
    private final HytaleLogger logger;
    private PlaceholderAPIConfig config;

    public ConfigManager(@NotNull final JavaPlugin main) {
        this.main = main;
        this.logger = main.getLogger();
    }

    public void setup() {
        final String content;

        try {
            final Path file = createFile("/config.yml", main.getDataDirectory().toString() + "/config.yml");

            if (file != null) {
                content = Files.readString(file);
            } else {
                return;
            }
        } catch (Exception e) {
            logger.atSevere().log("Something went wrong when getting the file content of config.yml", e);
            return;
        }

        final Map<String, Object> data = YAML.load(content);
        config = GSON.fromJson(GSON.toJsonTree(data), PlaceholderAPIConfig.class);

        if (config.metricsUuid() == null && config.metrics() == null) {
            config.metricsUuid(UUID.randomUUID());
            config.metrics(true);
            save();
        } else if (config.metricsUuid() == null && config.metrics()) {
            config.metricsUuid(UUID.randomUUID());
            save();
        }
    }

    public PlaceholderAPIConfig config() {
        return config;
    }

    public void save() {
        String headerString = null;

        try (final InputStream in = PlaceholderAPIPlugin.class.getResourceAsStream("/header.txt")) {
            if (in != null) {
                headerString = new BufferedReader(new InputStreamReader(in)).lines()
                        .collect(Collectors.joining("\n"));
            }
        } catch (IOException e) {
            logger.atWarning().log("Failed to write internal header.txt to config.yml.", e);
        }

        try {
            final Map<String, Object> map = GSON.fromJson(GSON.toJsonTree(config), new TypeToken<Map<String, Object>>(){}.getType());
            final String yaml = YAML.dump(map);
            final Path path = Paths.get(main.getDataDirectory().toString() + "/config.yml");
            Files.write(path, Arrays.asList(LINE_DELIMITER.split((headerString == null ? "" : headerString + '\n') + yaml)), StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            logger.atSevere().log("Something went wrong when saving config.yml: ", e);
        }
    }

    @NotNull
    public <T> T convertExpansion(@NotNull final Map<String, Object> expansionConfig, @NotNull final Class<T> type) {
        return GSON.fromJson(GSON.toJsonTree(expansionConfig), type);
    }

    @Nullable
    private Path createFile(@NotNull final String internalPath, @NotNull final String externalPath) {
        final Path file = Paths.get(externalPath);

        if (Files.exists(file)) {
            return file;
        }

        final Optional<Path> parent = Optional.ofNullable(file.getParent());

        try {
            if (parent.isPresent()) {
                Files.createDirectories(parent.get());
            }

            Files.createFile(file);
        } catch (IOException e) {
            logger.atSevere().log("Something went wrong when trying to create ", file);

            return null;
        }

        if (exportResource(internalPath, externalPath)) {
            return file;
        }

        return null;
    }

    private boolean exportResource(@NotNull final String internalPath, @NotNull final String externalPath) {
        try {
            Files.copy(PlaceholderAPIPlugin.class.getResourceAsStream(internalPath), Paths.get(externalPath),
                    StandardCopyOption.REPLACE_EXISTING);

            return true;
        } catch (Exception e) {
            logger.atSevere().log("Something went wrong when moving internal: ", internalPath, " to ", externalPath);
        }

        return false;
    }
}
