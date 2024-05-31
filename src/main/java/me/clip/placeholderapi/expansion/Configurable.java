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

package me.clip.placeholderapi.expansion;

import java.util.Map;

/**
 * Implementing this interface allows {@link me.clip.placeholderapi.expansion.PlaceholderExpansion PlaceholderExpansions}
 * to set a list of default configuration values through the {@link #getDefaults() getDefaults method}
 * that should be added to the config.yml of PlaceholderAPI.
 * 
 * <p>The entries will be added under {@code expansions} as their own section.
 * <h2>Example:</h2>
 * returning a Map with key {@code foo} and value {@code bar} will result in the following config entry:
 * 
 * <pre><code>
 * expansions:
 *   myexpansion:
 *     foo: "bar"
 * </code></pre>
 * 
 * <p><b>The configuration is set before the PlaceholderExpansion is registered!</b>
 *
 * @author Ryan McCarthy
 */
public interface Configurable {

  /**
   * The map returned by this method will be used to set config options in PlaceholderAPI's config.yml.
   * 
   * <p>The key and value pairs are set under a section named after your
   * {@link me.clip.placeholderapi.expansion.PlaceholderExpansion PlaceholderExpansion} in the
   * {@code expansions} section of the config.
   *
   * @return Map of config path / values which need to be added / removed from the PlaceholderAPI
   * config.yml file
   */
  Map<String, Object> getDefaults();
}
