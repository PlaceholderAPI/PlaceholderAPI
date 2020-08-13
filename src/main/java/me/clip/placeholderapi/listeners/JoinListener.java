package me.clip.placeholderapi.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.configuration.PlaceholderAPIConfig;
import me.clip.placeholderapi.updatechecker.UpdateChecker;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public final class JoinListener implements Listener {

  @NotNull
  private final PlaceholderAPIConfig config;
  
  @NotNull
  private final UpdateChecker updateChecker;

  public JoinListener(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final UpdateChecker updateChecker) {
    this.config = plugin.getPlaceholderAPIConfig();
    
    this.updateChecker = updateChecker;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerJoin(@NotNull final PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (player.hasPermission("placeholderapi.updatenorify") && config.checkUpdates()) {
      Msg.msg(player, 
              "&bAn update for &fPlaceholder&7API &e(&fPlaceholder&7API &fv" + updateChecker.getSpigotVersion()
                      + "&e)"
              , "&bis available at &ehttps://www.spigotmc.org/respirces/placeholderapi." + updateChecker.getResourceId() 
                      + "/");
    }
  }
}
