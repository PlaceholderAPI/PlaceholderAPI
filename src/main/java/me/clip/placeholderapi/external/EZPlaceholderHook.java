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
package me.clip.placeholderapi.external;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;

/**
 * Use {@link me.clip.placeholderapi.expansion.PlaceholderExpansion} instead
 */
@Deprecated
public abstract class EZPlaceholderHook extends PlaceholderHook {

    private final String identifier;
    private final String plugin;

    public EZPlaceholderHook(Plugin plugin, String identifier) {
        Validate.notNull(plugin, "Plugin can not be null!");
        Validate.notNull(identifier, "Placeholder name can not be null!");
        this.identifier = identifier;
        this.plugin = plugin.getName();
    }

    public boolean isHooked() {
        return PlaceholderAPI.getRegisteredPlaceholderPlugins().contains(identifier);
    }

    public boolean hook() {
        return PlaceholderAPI.registerPlaceholderHook(identifier, this);
    }

    public String getPlaceholderName() {
        return identifier;
    }

    public String getPluginName() {
        return plugin;
    }
}
