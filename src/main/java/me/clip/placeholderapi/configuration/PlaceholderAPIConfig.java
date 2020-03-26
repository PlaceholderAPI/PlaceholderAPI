/*
 *
 * PlaceholderAPI
 * Copyright (C) 2019 Ryan McCarthy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */
package me.clip.placeholderapi.configuration;

import me.clip.placeholderapi.PlaceholderAPIPlugin;

public class PlaceholderAPIConfig {

  private final PlaceholderAPIPlugin plugin;

  public PlaceholderAPIConfig(PlaceholderAPIPlugin i) {
    plugin = i;
  }

  public void loadDefConfig() {
    plugin.saveDefaultConfig();
    plugin.reloadConfig();
  }

  public boolean checkUpdates() {
    return plugin.getConfig().getBoolean("check_updates");
  }

  public boolean cloudAllowUnverifiedExpansions() {
    return plugin.getConfig().getBoolean("cloud_allow_unverified_expansions");
  }

  public boolean isCloudEnabled() {
    return plugin.getConfig().getBoolean("cloud_enabled");
  }

  public boolean isDebugMode() {
    return plugin.getConfig().getBoolean("debug", false);
  }

  public void setCloudEnabled(boolean b) {
    plugin.getConfig().set("cloud_enabled", b);
    plugin.reloadConfig();
  }

  public String booleanTrue() {
    return plugin.getConfig().getString("boolean.true");
  }

  public String booleanFalse() {
    return plugin.getConfig().getString("boolean.false");
  }

  public String dateFormat() {
    return plugin.getConfig().getString("date_format");
  }
}
