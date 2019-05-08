package me.clip.placeholderapi;

import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class ServerLoadEventListener implements Listener {

  private PlaceholderAPIPlugin plugin;

  public ServerLoadEventListener(PlaceholderAPIPlugin instance) {
    plugin = instance;
    Bukkit.getPluginManager().registerEvents(this, instance);
  }

  @EventHandler
  public void onServerLoad(ServerLoadEvent e) {
    plugin.getLogger().info("Placeholder expansion registration initializing...");
    final Map<String, PlaceholderHook> alreadyRegistered = PlaceholderAPI.getPlaceholders();
    plugin.getExpansionManager().registerAllExpansions();
    if (alreadyRegistered != null && !alreadyRegistered.isEmpty()) {
      alreadyRegistered.forEach(PlaceholderAPI::registerPlaceholderHook);
    }
  }
}
