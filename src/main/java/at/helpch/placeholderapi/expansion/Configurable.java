/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2026 PlaceholderAPI Team
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

package at.helpch.placeholderapi.expansion;

import at.helpch.placeholderapi.PlaceholderAPIPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Implementing this interface allows {@link at.helpch.placeholderapi.expansion.PlaceholderExpansion PlaceholderExpansions}
 * allows you to map an object to a section of yaml in the main config.yml.
 *
 * <p>The entries will be added under {@code expansions} as their own section.
 * <h2>Example:</h2>
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
public interface Configurable<T> {

    @NotNull
    Class<T> provideConfigType();

    @NotNull
    T provideDefault();

    @NotNull
    default T getConfig() {
        if (this instanceof PlaceholderExpansion exp) {
            return (T) PlaceholderAPIPlugin.instance().configManager().config().expansions().getOrDefault(exp.getIdentifier(), provideDefault());
        }

        return provideDefault();
    }

//    /**
//     * The map returned by this method will be used to set config options in PlaceholderAPI's config.yml.
//     *
//     * <p>The key and value pairs are set under a section named after your
//     * {@link at.helpch.placeholderapi.expansion.PlaceholderExpansion PlaceholderExpansion} in the
//     * {@code expansions} section of the config.
//     *
//     * @return Map of config path / values which need to be added / removed from the PlaceholderAPI
//     * config.yml file
//     */
//    Map<String, Object> getDefaults();
}
