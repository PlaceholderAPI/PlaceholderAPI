/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2021 PlaceholderAPI Team
 *
 * PlaceholderAPI is free software: you can redistribute it and/or modify
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

import java.util.Collections;
import java.util.List;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Any class extending this will be able to get registered as a PlaceholderExpansion.
 * <br>The registration either happens automatically when the jar file containing a
 * class extending this one is located under the {@code PlaceholderAPI/expansions}
 * directory or when the {@link #register()} method is called by said class.
 */
public abstract class PlaceholderExpansion extends PlaceholderHook {

  /**
   * The placeholder identifier of this expansion. May not contain {@literal %},
   * {@literal {}} or _
   *
   * @return placeholder identifier that is associated with this expansion
   */
  @NotNull
  public abstract String getIdentifier();

  /**
   * The author of this expansion
   *
   * @return name of the author for this expansion
   */
  @NotNull
  public abstract String getAuthor();

  /**
   * The version of this expansion
   *
   * @return current version of this expansion
   */
  @NotNull
  public abstract String getVersion();

  /**
   * The name of this expansion
   *
   * @return {@link #getIdentifier()} by default, name of this expansion if specified
   */
  @NotNull
  public String getName() {
    return getIdentifier();
  }

  /**
   * The name of the plugin that this expansion hooks into. by default will null
   *
   * @return plugin name that this expansion requires to function
   */
  @Nullable
  public String getRequiredPlugin() {
    return getPlugin();
  }

  /**
   * The placeholders associated with this expansion
   *
   * @return placeholder list that this expansion provides
   */
  @NotNull
  public List<String> getPlaceholders() {
    return Collections.emptyList();
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
  public final boolean isRegistered() {
    return getPlaceholderAPI().getLocalExpansionManager().findExpansionByIdentifier(getIdentifier())
        .map(it -> it.equals(this)).orElse(false);
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
    return getPlaceholderAPI().getLocalExpansionManager().register(this);
  }

  /**
   * Attempt to unregister this PlaceholderExpansion
   *
   * @return true if this expansion is now unregistered with PlaceholderAPI
   */
  public final boolean unregister() {
    return getPlaceholderAPI().getLocalExpansionManager().unregister(this);
  }


  /**
   * Quick getter for the {@link PlaceholderAPIPlugin} instance
   *
   * @return {@link PlaceholderAPIPlugin} instance
   */
  @NotNull
  public final PlaceholderAPIPlugin getPlaceholderAPI() {
    return PlaceholderAPIPlugin.getInstance();
  }

  // === Configuration ===
  
  /**
   * Gets the ConfigurationSection of the expansion located in the config.yml of PlaceholderAPI or
   * null when not specified.
   * <br>You may use the {@link Configurable} interface to define default values set
   * 
   * @return ConfigurationSection that this epxpansion has.
   */
  @Nullable
  public final ConfigurationSection getConfigSection() {
    return getPlaceholderAPI().getConfig().getConfigurationSection("expansions." + getIdentifier());
  }
  
  /**
   * Gets the ConfigurationSection relative to the {@link #getConfigSection() default one} set
   * by the expansion or null when the default ConfigurationSection is null
   * 
   * @param path The path to get the ConfigurationSection from. This is relative to the default section
   * @return ConfigurationSection relative to the default section
   */
  @Nullable
  public final ConfigurationSection getConfigSection(@NotNull final String path) {
    final ConfigurationSection section = getConfigSection();
    return section == null ? null : section.getConfigurationSection(path);
  }
  
  /**
   * Gets the Object relative to the {@link #getConfigSection() default ConfigurationSection} set
   * by the expansion or the provided Default Object, when the default ConfigurationSection is null
   * 
   * @param path The path to get the Object from. This is relative to the default section
   * @param def The default Object to return when the ConfigurationSection returns null
   * @return Object from the provided path or the default one provided
   */
  @Nullable
  @Contract("_, !null -> !null")
  public final Object get(@NotNull final String path, final Object def) {
    final ConfigurationSection section = getConfigSection();
    return section == null ? def : section.get(path, def);
  }
  
  /**
   * Gets the int relative to the {@link #getConfigSection() default ConfigurationSection} set
   * by the expansion or the provided Default int, when the default ConfigurationSection is null
   *
   * @param path The path to get the int from. This is relative to the default section
   * @param def The default int to return when the ConfigurationSection returns null
   * @return int from the provided path or the default one provided
   */
  public final int getInt(@NotNull final String path, final int def) {
    final ConfigurationSection section = getConfigSection();
    return section == null ? def : section.getInt(path, def);
  }
  
  /**
   * Gets the long relative to the {@link #getConfigSection() default ConfigurationSection} set
   * by the expansion or the provided Default long, when the default ConfigurationSection is null
   *
   * @param path The path to get the long from. This is relative to the default section
   * @param def The default long to return when the ConfigurationSection returns null
   * @return long from the provided path or the default one provided
   */
  public final long getLong(@NotNull final String path, final long def) {
    final ConfigurationSection section = getConfigSection();
    return section == null ? def : section.getLong(path, def);
  }
  
  /**
   * Gets the double relative to the {@link #getConfigSection() default ConfigurationSection} set
   * by the expansion or the provided Default double, when the default ConfigurationSection is null
   *
   * @param path The path to get the double from. This is relative to the default section
   * @param def The default double to return when the ConfigurationSection returns null
   * @return double from the provided path or the default one provided
   */
  public final double getDouble(@NotNull final String path, final double def) {
    final ConfigurationSection section = getConfigSection();
    return section == null ? def : section.getDouble(path, def);
  }
  
  /**
   * Gets the String relative to the {@link #getConfigSection() default ConfigurationSection} set
   * by the expansion or the provided Default String, when the default ConfigurationSection is null
   *
   * @param path The path to get the String from. This is relative to the default section
   * @param def The default String to return when the ConfigurationSection returns null. Can be null
   * @return String from the provided path or the default one provided
   */
  @Nullable
  @Contract("_, !null -> !null")
  public final String getString(@NotNull final String path, @Nullable final String def) {
    final ConfigurationSection section = getConfigSection();
    return section == null ? def : section.getString(path, def);
  }
  
  /**
   * Gets a String List relative to the {@link #getConfigSection() default ConfigurationSection} set
   * by the expansion or an empty List, when the default ConfigurationSection is null
   *
   * @param path The path to get the String list from. This is relative to the default section
   * @return String list from the provided path or an empty list
   */
  @NotNull
  public final List<String> getStringList(@NotNull final String path) {
    final ConfigurationSection section = getConfigSection();
    return section == null ? Collections.emptyList() : section.getStringList(path);
  }
  
  /**
   * Gets the boolean relative to the {@link #getConfigSection() default ConfigurationSection} set
   * by the expansion or the default boolean, when the default ConfigurationSection is null
   *
   * @param path The path to get the boolean from. This is relative to the default section
   * @param def The default boolean to return when the ConfigurationSection is null
   * @return boolean from the provided path or the default one provided
   */
  public final boolean getBoolean(@NotNull final String path, final boolean def) {
    final ConfigurationSection section = getConfigSection();
    return section == null ? def : section.getBoolean(path, def);
  }

  /**
   * Whether the {@link #getConfigSection() default ConfigurationSection} contains the provided path
   * or not. This will return {@code false} when either the default section is null, or doesn't
   * contain the provided path
   *
   * @param path The path to check
   * @return true when the default ConfigurationSection is not null and contains the path, false otherwise
   */
  public final boolean configurationContains(@NotNull final String path) {
    final ConfigurationSection section = getConfigSection();
    return section != null && section.contains(path);
  }
  
  /**
   * Whether the provided Object is an instance of this PlaceholderExpansion.
   * <br>This method will perform the following checks in order:
   * <br><ul>
   *     <li>Checks if Object equals the class. Returns true when equal and continues otherwise</li>
   *     <li>Checks if the Object is an instance of a PlaceholderExpansion. Returns false if not</li>
   *     <li>Checks if the Object's Identifier, Author and version equal the one of this class</li>
   * </ul>
   * 
   * @param o The Object to check
   * @return true or false depending on the above mentioned checks
   */
  @Override
  public final boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PlaceholderExpansion)) {
      return false;
    }

    final PlaceholderExpansion expansion = (PlaceholderExpansion) o;

    return getIdentifier().equals(expansion.getIdentifier()) &&
        getAuthor().equals(expansion.getAuthor()) &&
        getVersion().equals(expansion.getVersion());
  }
  
  /**
   * Returns a String containing the Expansion's name, author and version
   * 
   * @return String containing name, author and version of the expansion
   */
  @Override
  public final String toString() {
    return String.format("PlaceholderExpansion[name: '%s', author: '%s', version: '%s']", getName(),
        getAuthor(), getVersion());
  }

  // === Deprecated API ===

  /**
   * @deprecated As of versions greater than 2.8.7, use {@link #getRequiredPlugin()}
   *
   * @return The plugin name.
   */
  @Deprecated
  @ApiStatus.ScheduledForRemoval(inVersion = "2.11.0")
  public String getPlugin() {
    return null;
  }

  /**
   * @deprecated As of versions greater than 2.8.7, use the expansion cloud to show a description
   *
   * @return The description of the expansion.
   */
  @Deprecated
  @ApiStatus.ScheduledForRemoval(inVersion = "2.11.0")
  public String getDescription() {
    return null;
  }

  /**
   * @deprecated As of versions greater than 2.8.7, use the expansion cloud to display a link
   *
   * @return The link for the expansion.
   */
  @Deprecated
  @ApiStatus.ScheduledForRemoval(inVersion = "2.11.0")
  public String getLink() {
    return null;
  }
}
