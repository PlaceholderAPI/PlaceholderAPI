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
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public abstract class PlaceholderExpansion extends PlaceholderHook
{

	/**
	 * The placeholder identifier of this expansion
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

	@Nullable
	@Override /* override for now >:) */
	public String onRequest(@Nullable final OfflinePlayer player, @NotNull final String params)
	{
		return super.onRequest(player, params);
	}


	/**
	 * The name of this expansion
	 *
	 * @return {@link #getIdentifier()} by default, name of this expansion if specified
	 */
	@NotNull
	public String getName()
	{
		return getIdentifier();
	}

	/**
	 * The name of the plugin that this expansion hooks into. by default will null
	 *
	 * @return plugin name that this expansion requires to function
	 */
	@Nullable
	public String getRequiredPlugin()
	{
		return null;
	}

	/**
	 * The placeholders associated with this expansion
	 *
	 * @return placeholder list that this expansion provides
	 */
	@NotNull
	public List<String> getPlaceholders()
	{
		return Collections.emptyList();
	}


	/**
	 * Expansions that do not use the ecloud and instead register from the dependency should set this
	 * to true to ensure that your placeholder expansion is not unregistered when the papi reload
	 * command is used
	 *
	 * @return if this expansion should persist through placeholder reloads
	 */
	public boolean persist()
	{
		return false;
	}


	/**
	 * Check if this placeholder identifier has already been registered
	 *
	 * @return true if the identifier for this expansion is already registered
	 */
	public final boolean isRegistered()
	{
		return PlaceholderAPI.isRegistered(getIdentifier());
	}


	/**
	 * If any requirements need to be checked before this expansion should register, you can check
	 * them here
	 *
	 * @return true if this hook meets all the requirements to register
	 */
	public boolean canRegister()
	{
		return getRequiredPlugin() == null || Bukkit.getPluginManager().getPlugin(getRequiredPlugin()) != null;
	}

	/**
	 * Attempt to register this PlaceholderExpansion
	 *
	 * @return true if this expansion is now registered with PlaceholderAPI
	 */
	public boolean register()
	{
		return canRegister() && getPlaceholderAPI().getLocalExpansionManager().register(this);
	}


	/**
	 * Quick getter for the {@link PlaceholderAPIPlugin} instance
	 *
	 * @return {@link PlaceholderAPIPlugin} instance
	 */
	@NotNull
	public final PlaceholderAPIPlugin getPlaceholderAPI()
	{
		return PlaceholderAPIPlugin.getInstance();
	}


	// === Configuration ===

	@Nullable
	public final ConfigurationSection getConfigSection()
	{
		return getPlaceholderAPI().getConfig().getConfigurationSection("expansions." + getIdentifier());
	}

	@Nullable
	public final ConfigurationSection getConfigSection(@NotNull final String path)
	{
		final ConfigurationSection section = getConfigSection();
		return section == null ? null : section.getConfigurationSection(path);
	}

	@Nullable
	@Contract("_, !null -> !null")
	public final Object get(@NotNull final String path, final Object def)
	{
		final ConfigurationSection section = getConfigSection();
		return section == null ? def : section.get(path, def);
	}

	public final int getInt(@NotNull final String path, final int def)
	{
		final ConfigurationSection section = getConfigSection();
		return section == null ? def : section.getInt(path, def);
	}

	public final long getLong(@NotNull final String path, final long def)
	{
		final ConfigurationSection section = getConfigSection();
		return section == null ? def : section.getLong(path, def);
	}

	public final double getDouble(@NotNull final String path, final double def)
	{
		final ConfigurationSection section = getConfigSection();
		return section == null ? def : section.getDouble(path, def);
	}

	@Nullable
	@Contract("_, !null -> !null")
	public final String getString(@NotNull final String path, @Nullable final String def)
	{
		final ConfigurationSection section = getConfigSection();
		return section == null ? def : section.getString(path, def);
	}

	@NotNull
	public final List<String> getStringList(@NotNull final String path)
	{
		final ConfigurationSection section = getConfigSection();
		return section == null ? Collections.emptyList() : section.getStringList(path);
	}

	public final boolean configurationContains(@NotNull final String path)
	{
		final ConfigurationSection section = getConfigSection();
		return section != null && section.contains(path);
	}


	// === Deprecated API ===

	/**
	 * @deprecated As of versions greater than 2.8.7, use {@link #getRequiredPlugin()}
	 */
	@Deprecated
	public final String getPlugin()
	{
		return null;
	}

	/**
	 * @deprecated As of versions greater than 2.8.7, use the expansion cloud to show a description
	 */
	@Deprecated
	public final String getDescription()
	{
		return null;
	}

	/**
	 * @deprecated As of versions greater than 2.8.7, use the expansion cloud to display a link
	 */
	@Deprecated
	public final String getLink()
	{
		return null;
	}

}
