package at.helpch.placeholderapi.configuration;

import at.helpch.placeholderapi.PlaceholderAPIPlugin;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Optional;

public final class ConfigManager {
    private static final Yaml YAML = new Yaml();
    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
//            .registerTypeAdapter()

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
            e.printStackTrace();
            return;
        }

        final Map<String, Object> data = YAML.load(content);
        config = GSON.fromJson(GSON.toJsonTree(data), PlaceholderAPIConfig.class);
    }

    public PlaceholderAPIConfig config() {
        return config;
    }

    public void save() {

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
            logger.atSevere().log("Something went wrong when trying to craete ", file);

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
