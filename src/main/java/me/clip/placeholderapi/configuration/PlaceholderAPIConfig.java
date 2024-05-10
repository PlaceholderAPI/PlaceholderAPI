/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2024 PlaceholderAPI Team
 *
 * PlaceholderAPI free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlaceholderAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.clip.placeholderapi.configuration;

import java.util.Optional;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.jetbrains.annotations.NotNull;

public final class PlaceholderAPIConfig {

  @NotNull
  private final PlaceholderAPIPlugin plugin;

  public PlaceholderAPIConfig(@NotNull final PlaceholderAPIPlugin plugin) {
    this.plugin = plugin;
  }


  public boolean checkUpdates() {
    return plugin.getConfig().getBoolean("check_updates");
  }


  public boolean isCloudEnabled() {
    return plugin.getConfig().getBoolean("cloud_enabled");
  }

  public void setCloudEnabled(boolean state) {
    plugin.getConfig().set("cloud_enabled", state);
    plugin.saveConfig();
  }


  public boolean isDebugMode() {
    return plugin.getConfig().getBoolean("debug", false);
  }


  public Optional<ExpansionSort> getExpansionSort() {
    final String option = plugin.getConfig()
        .getString("cloud_sorting", ExpansionSort.LATEST.name());

    try {
      //noinspection ConstantConditions (bad spigot annotation)
      return Optional.of(ExpansionSort.valueOf(option.toUpperCase()));
    } catch (final IllegalArgumentException ignored) {
      return Optional.empty();
    }
  }


  @NotNull
  public String dateFormat() {
    //noinspection ConstantConditions (bad spigot annotation)
    return plugin.getConfig().getString("date_format", "MM/dd/yy HH:mm:ss");
  }


  @NotNull
  public String booleanTrue() {
    //noinspection ConstantConditions (bad spigot annotation)
    return plugin.getConfig().getString("boolean.true", "true");
  }

  @NotNull
  public String booleanFalse() {
    //noinspection ConstantConditions (bad spigot annotation)
    return plugin.getConfig().getString("boolean.false", "false");
  }

}
