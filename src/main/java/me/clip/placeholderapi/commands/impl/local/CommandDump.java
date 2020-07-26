package me.clip.placeholderapi.commands.impl.local;

import com.google.common.io.CharStreams;
import com.google.gson.JsonParser;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class CommandDump extends PlaceholderCommand
{

	@NotNull
	private static final String URL = "https://paste.helpch.at/";

	@NotNull
	private static final JsonParser        JSON_PARSER = new JsonParser();
	@NotNull
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)
																		  .withLocale(Locale.US)
																		  .withZone(ZoneId.of("UTC"));


	public CommandDump()
	{
		super("dump");
	}

	@Override
	public void evaluate(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull @Unmodifiable final List<String> params)
	{
		postDump(makeDump(plugin)).whenComplete((key, exception) -> {
			if (exception != null)
			{
				plugin.getLogger().log(Level.WARNING, "failed to post dump details", exception);

				Msg.msg(sender,
						"&cFailed to post dump details, check console.");
				return;
			}

			Msg.msg(sender,
					"&aSuccessfully posted dump: " + URL + key);
		});
	}

	@NotNull
	private CompletableFuture<String> postDump(@NotNull final String dump)
	{
		return CompletableFuture.supplyAsync(() -> {
			try
			{
				final HttpURLConnection connection = ((HttpURLConnection) new URL(URL + "documents").openConnection());
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
				connection.setDoOutput(true);

				connection.connect();

				try (final OutputStream stream = connection.getOutputStream())
				{
					stream.write(dump.getBytes(StandardCharsets.UTF_8));
				}

				try (final InputStream stream = connection.getInputStream())
				{
					//noinspection UnstableApiUsage
					final String json = CharStreams.toString(new InputStreamReader(stream, StandardCharsets.UTF_8));
					return JSON_PARSER.parse(json).getAsJsonObject().get("key").getAsString();
				}
			}
			catch (final IOException ex)
			{
				throw new CompletionException(ex);
			}
		});
	}

	@NotNull
	private String makeDump(@NotNull final PlaceholderAPIPlugin plugin)
	{
		final StringBuilder builder = new StringBuilder();

		builder.append("Generated: ")
			   .append(DATE_FORMAT.format(Instant.now()))
			   .append("\n\n");

		builder.append("PlaceholderAPI: ")
			   .append(plugin.getDescription().getVersion())
			   .append("\n\n");

		builder.append("Expansions Registered:")
			   .append('\n');


		final Map<String, List<PlaceholderExpansion>> expansions = plugin.getLocalExpansionManager()
																		 .getExpansions()
																		 .stream()
																		 .collect(Collectors.groupingBy(PlaceholderExpansion::getAuthor));

		for (final Map.Entry<String, List<PlaceholderExpansion>> expansionsByAuthor : expansions.entrySet())
		{
			builder.append("  ")
				   .append(expansionsByAuthor.getKey())
				   .append(": ")
				   .append('\n');

			for (final PlaceholderExpansion expansion : expansionsByAuthor.getValue())
			{
				builder.append("    ")
					   .append(expansion.getName())
					   .append(':')
					   .append(expansion.getVersion())
					   .append('\n');
			}
		}

		builder.append('\n');

		builder.append("Expansions Directory:")
			   .append('\n');

		final String[] jars = plugin.getLocalExpansionManager()
									.getExpansionsFolder()
									.list((dir, name) -> name.toLowerCase().endsWith(".jar"));

		for (final String jar : jars)
		{
			builder.append("  ")
				   .append(jar)
				   .append('\n');
		}

		builder.append('\n');


		builder.append("Server Info: ")
			   .append(plugin.getServer().getBukkitVersion())
			   .append('/')
			   .append(plugin.getServer().getVersion())
			   .append("\n\n");

		builder.append("Plugin Info:")
			   .append('\n');

		for (final Plugin other : plugin.getServer().getPluginManager().getPlugins())
		{
			builder.append("  ")
				   .append(other.getName())
				   .append(": ")
				   .append(other.getDescription().getVersion())
				   .append('\n');
		}

		return builder.toString();
	}

}
