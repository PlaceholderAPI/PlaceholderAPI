package at.helpch.placeholderapi;

import at.helpch.placeholderapi.commands.PlaceholderCommandRouter;
import at.helpch.placeholderapi.configuration.ConfigManager;
import at.helpch.placeholderapi.expansion.manager.CloudExpansionManager;
import at.helpch.placeholderapi.expansion.manager.LocalExpansionManager;
import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.event.events.BootEvent;
import com.hypixel.hytale.server.core.event.events.PrepareUniverseEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIPlugin extends JavaPlugin {
    private final ConfigManager configManager = new ConfigManager(this);
    private final LocalExpansionManager localExpansionManager = new LocalExpansionManager(this);
    private final CloudExpansionManager cloudExpansionManager = new CloudExpansionManager(this);

    private static PlaceholderAPIPlugin instance;

    public static PlaceholderAPIPlugin instance() {
        return instance;
    }

    public PlaceholderAPIPlugin(@NotNull final JavaPluginInit init) {
        super(init);

        instance = this;
    }

    @Override
    protected void setup() {
        configManager.setup();
        getEventRegistry().register(PlayerDisconnectEvent.class, localExpansionManager::onQuit);
        getEventRegistry().register(EventPriority.LAST, BootEvent.class, this::onServerLoad);

        if (configManager.config().cloudEnabled()) {
            cloudExpansionManager.load();
        }
    }

    @Override
    protected void start() {
        getCommandRegistry().registerCommand(new PlaceholderCommandRouter(this));

//        localExpansionManager().load(ConsoleSender.INSTANCE);

        super.start();
    }

    @Override
    protected void shutdown() {
        super.shutdown();
    }

    public void reloadPlugin(@NotNull final CommandSender sender) {
        localExpansionManager.kill();
//        configManager.save();
        configManager.setup();

        localExpansionManager.load(sender);

        if (configManager.config().cloudEnabled()) {
            cloudExpansionManager.load();
        } else {
            cloudExpansionManager.kill();
        }
    }

    public LocalExpansionManager localExpansionManager() {
        return localExpansionManager;
    }

    public ConfigManager configManager() {
        return configManager;
    }

    public CloudExpansionManager cloudExpansionManager() {
        return cloudExpansionManager;
    }

    private void onServerLoad(BootEvent event) {
        localExpansionManager.load(ConsoleSender.INSTANCE);
    }
}
