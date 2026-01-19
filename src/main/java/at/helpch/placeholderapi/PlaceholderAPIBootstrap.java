package at.helpch.placeholderapi;

import at.helpch.placeholderapi.configuration.BooleanValue;
import at.helpch.placeholderapi.configuration.ConfigManager;
import at.helpch.placeholderapi.configuration.ExpansionSort;
import at.helpch.placeholderapi.configuration.PlaceholderAPIConfig;
import at.helpch.placeholderapi.expansion.manager.LocalExpansionManager;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class PlaceholderAPIBootstrap extends JavaPlugin {
    private final ConfigManager configManager;
    private final LocalExpansionManager localExpansionManager;

    public PlaceholderAPIBootstrap(@NotNull final JavaPluginInit init) {
        super(init);

        configManager = new ConfigManager(this);
        localExpansionManager = new LocalExpansionManager(this);
    }

    @Override
    protected void setup() {
        configManager.setup();
    }

    @Override
    protected void start() {
        super.start();
    }

    @Override
    protected void shutdown() {
        super.shutdown();
    }

    public LocalExpansionManager localExpansionManager() {
        return localExpansionManager;
    }
}
