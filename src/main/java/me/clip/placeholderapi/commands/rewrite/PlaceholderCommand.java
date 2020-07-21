package me.clip.placeholderapi.commands.rewrite;

import com.google.common.collect.Sets;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public abstract class PlaceholderCommand
{

	@NotNull
	private final String      label;
	@NotNull
	private final Set<String> alias;

	@Nullable
	private String permission;


	protected PlaceholderCommand(@NotNull final String label, @NotNull final String... alias)
	{
		this.label = label;
		this.alias = Sets.newHashSet(alias);

		setPermission("placeholderapi." + label);
	}


	@NotNull
	public final String getLabel()
	{
		return label;
	}

	@NotNull
	public final Set<String> getAlias()
	{
		return alias;
	}


	@Nullable
	public final String getPermission()
	{
		return permission;
	}

	public void setPermission(@NotNull final String permission)
	{
		this.permission = permission;
	}


	public void evaluate(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, final @NotNull String alias, @NotNull final List<String> params)
	{

	}

	public void complete(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, final @NotNull String alias, @NotNull final List<String> params, @NotNull final List<String> suggestions)
	{

	}

}
