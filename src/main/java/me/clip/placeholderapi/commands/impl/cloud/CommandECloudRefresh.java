package me.clip.placeholderapi.commands.impl.cloud;

import java.util.List;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandECloudRefresh extends PlaceholderCommand {

  public CommandECloudRefresh() {
    super("refresh");
  }

  @Override
  public void evaluate(
      @NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender,
      @NotNull final String alias,
      @NotNull @Unmodifiable final List<String> params) {
    plugin.getExpansionCloud().clean();
    plugin
        .getExpansionCloud()
        .fetch(plugin.getPlaceholderAPIConfig().cloudAllowUnverifiedExpansions());

    Msg.msg(sender, "&aThe eCloud Manager has been refreshed!");
  }
}
