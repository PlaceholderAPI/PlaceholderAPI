package me.clip.placeholderapi.commands.impl.local;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.manager.LocalExpansionManager;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public final class CommandExpansionRegister extends PlaceholderCommand
{

	public CommandExpansionRegister()
	{
		super("register");
	}

	@Override
	public void evaluate(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull @Unmodifiable final List<String> params)
	{
		if (params.size() < 1)
		{
			Msg.msg(sender,
					"&cYou must specify the name of an expansion file.");
			return;
		}


		final LocalExpansionManager manager = plugin.getLocalExpansionManager();

		final File file = new File(manager.getExpansionsFolder(), params.get(0));
		if (!file.exists())
		{
			Msg.msg(sender,
					"&cThe file &f" + file.getName() + "&c doesn't exist!");
			return;
		}

		manager.findExpansionsInFile(file).whenCompleteAsync((classes, exception) -> {
			if (exception != null)
			{
				Msg.msg(sender,
						"&cFailed to find expansion in file: &f" + file);

				plugin.getLogger().log(Level.WARNING, "failed to find expansion in file: " + file, exception);
				return;
			}

			if (classes.isEmpty())
			{
				Msg.msg(sender,
						"&cNo expansion class found in file: &f" + file);
				return;
			}

			final Optional<PlaceholderExpansion> expansion = manager.register(classes.get(0));
			if (!expansion.isPresent())
			{
				Msg.msg(sender,
						"&cFailed to register expansion from &f" + params.get(0));
				return;
			}

			Msg.msg(sender,
					"&aSuccessfully registered expansion: &f" + expansion.get().getName());
		});
	}

	@Override
	public void complete(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions)
	{
		if (params.size() > 1)
		{
			return;
		}

		final String[] fileNames = plugin.getLocalExpansionManager().getExpansionsFolder().list((dir, name) -> name.endsWith(".jar"));
		if (fileNames == null || fileNames.length == 0)
		{
			return;
		}

		suggestByParameter(Arrays.stream(fileNames), suggestions, params.isEmpty() ? null : params.get(0));
	}

}
