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
package me.clip.placeholderapi.expansion;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.PlaceholderHook;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public abstract class PlaceholderExpansion extends PlaceholderHook {

    /**
     * The name of this expansion
     *
     * @return {@link #getIdentifier()} by default, name of this expansion if specified
     */
    public String getName() {
        return getIdentifier();
    }

    /**
     * The placeholder identifier of this expansion
     *
     * @return placeholder identifier that is associated with this expansion
     */
    public abstract String getIdentifier();

    /**
     * The author of this expansion
     *
     * @return name of the author for this expansion
     */
    public abstract String getAuthor();

    /**
     * The version of this expansion
     *
     * @return current version of this expansion
     */
    public abstract String getVersion();

    /**
     * The name of the plugin that this expansion hooks into. by default will return the deprecated
     * {@link #getPlugin()} method
     *
     * @return plugin name that this expansion requires to function
     */
    public String getRequiredPlugin() {
        return getPlugin();
    }

    /**
     * The placeholders associated with this expansion
     *
     * @return placeholder list that this expansion provides
     */
    public List<String> getPlaceholders() {
        return null;
    }

    /**
     * Expansions that do not use the ecloud and instead register from the dependency should set this
     * to true to ensure that your placeholder expansion is not unregistered when the papi reload
     * command is used
     *
     * @return if this expansion should persist through placeholder reloads
     */
    public boolean persist() {
        return false;
    }

    /**
     * Check if this placeholder identifier has already been registered
     *
     * @return true if the identifier for this expansion is already registered
     */
    public boolean isRegistered() {
        Validate.notNull(getIdentifier(), "Placeholder identifier can not be null!");
        return PlaceholderAPI.isRegistered(getIdentifier());
    }

    /**
     * If any requirements need to be checked before this expansion should register, you can check
     * them here
     *
     * @return true if this hook meets all the requirements to register
     */
    public boolean canRegister() {
        return getRequiredPlugin() == null
                || Bukkit.getPluginManager().getPlugin(getRequiredPlugin()) != null;
    }

    /**
     * Attempt to register this PlaceholderExpansion
     *
     * @return true if this expansion is now registered with PlaceholderAPI
     */
    public boolean register() {
        Validate.notNull(getIdentifier(), "Placeholder identifier can not be null!");
        return PlaceholderAPI.registerExpansion(this);
    }

    /**
     * Quick getter for the {@link PlaceholderAPIPlugin} instance
     *
     * @return {@link PlaceholderAPIPlugin} instance
     */
    public PlaceholderAPIPlugin getPlaceholderAPI() {
        return PlaceholderAPIPlugin.getInstance();
    }

    public String getString(String path, String def) {
        return getPlaceholderAPI().getConfig()
                .getString("expansions." + getIdentifier() + "." + path, def);
    }

    public int getInt(String path, int def) {
        return getPlaceholderAPI().getConfig()
                .getInt("expansions." + getIdentifier() + "." + path, def);
    }

    public long getLong(String path, long def) {
        return getPlaceholderAPI().getConfig()
                .getLong("expansions." + getIdentifier() + "." + path, def);
    }

    public double getDouble(String path, double def) {
        return getPlaceholderAPI().getConfig()
                .getDouble("expansions." + getIdentifier() + "." + path, def);
    }

    public List<String> getStringList(String path) {
        return getPlaceholderAPI().getConfig()
                .getStringList("expansions." + getIdentifier() + "." + path);
    }

    public Object get(String path, Object def) {
        return getPlaceholderAPI().getConfig().get("expansions." + getIdentifier() + "." + path, def);
    }

    public ConfigurationSection getConfigSection(String path) {
        return getPlaceholderAPI().getConfig()
                .getConfigurationSection("expansions." + getIdentifier() + "." + path);
    }

    public ConfigurationSection getConfigSection() {
        return getPlaceholderAPI().getConfig().getConfigurationSection("expansions." + getIdentifier());
    }

    public boolean configurationContains(String path) {
        return getPlaceholderAPI().getConfig().contains("expansions." + getIdentifier() + "." + path);
    }


    /**
     * @deprecated As of versions greater than 2.8.7, use {@link #getRequiredPlugin()}
     */
    @Deprecated
    public String getPlugin() {
        return null;
    }

    /**
     * @deprecated As of versions greater than 2.8.7, use the expansion cloud to show a description
     */
    @Deprecated
    public String getDescription() {
        return null;
    }

    /**
     * @deprecated As of versions greater than 2.8.7, use the expansion cloud to display a link
     */
    @Deprecated
    public String getLink() {
        return null;
    }
}
