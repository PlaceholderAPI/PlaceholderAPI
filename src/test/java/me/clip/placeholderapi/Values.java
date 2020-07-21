package me.clip.placeholderapi;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import me.clip.placeholderapi.replacer.CharsReplacer;
import me.clip.placeholderapi.replacer.RegexReplacer;
import me.clip.placeholderapi.replacer.Replacer;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Function;

public interface Values
{

	String SMALL_TEXT = "My name is %player_name%";
	String LARGE_TEXT = "My name is %player_name% and my location is (%player_x%, %player_y%, %player_z%), this placeholder is invalid %server_name%";

	ImmutableMap<String, PlaceholderHook> PLACEHOLDERS = ImmutableMap.<String, PlaceholderHook>builder()
			.put("player", new MockPlayerPlaceholderHook())
			.build();


	Replacer CHARS_REPLACER = new CharsReplacer(Replacer.Closure.PERCENT);
	Replacer REGEX_REPLACER = new RegexReplacer(Replacer.Closure.PERCENT);
	Replacer TESTS_REPLACER = new Replacer()
	{
		private final Set<Character> COLOR_CODES = ImmutableSet.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
																		  'a', 'b', 'c', 'd', 'e', 'f', 'k', 'l', 'm', 'o', 'r', 'x');

		private final boolean colorize = true;
		private final Replacer.Closure closure = Replacer.Closure.PERCENT;

		@Override
		public @NotNull String apply(final @NotNull String text, final @Nullable OfflinePlayer player, final @NotNull Function<String, @Nullable PlaceholderHook> lookup)
		{
			char[]        chars   = text.toCharArray();
			StringBuilder builder = new StringBuilder(chars.length);

			// This won't cause memory leaks. It's inside a method. And we want to use setLength instead of
			// creating a new string builder to use the maximum capacity and avoid initializing new objects.
			StringBuilder   identifier = new StringBuilder(50);
			PlaceholderHook handler    = null;

			// Stages:
			//   Stage -1: Look for the color code in the next character.
			//   Stage 0: No closures detected, or the detected identifier is invalid. We're going forward while appending the characters normally.
			//   Stage 1: The closure has been detected, looking for the placeholder identifier...
			//   Stage 2: Detected the identifier and the parameter. Translating the placeholder...
			int stage = 0;

			for (char ch : chars)
			{
				if (stage == -1 && COLOR_CODES.contains(ch))
				{
					builder.append(ChatColor.COLOR_CHAR).append(ch);
					stage = 0;
					continue;
				}

				// Check if the placeholder starts or ends.
				if (ch == closure.head || ch == closure.tail)
				{
					// If the placeholder ends.
					if (stage == 2)
					{
						String parameter  = identifier.toString();
						String translated = handler.onRequest(player, parameter);

						if (translated == null)
						{
							String name = "";
							builder.append(closure.head).append(name).append('_').append(parameter).append(closure.tail);
						}
						else
						{
							builder.append(translated);
						}

						identifier.setLength(0);
						stage = 0;
						continue;
					}
					else if (stage == 1)
					{ // If it just started | Double closures | If it's still hasn't detected the indentifier, reset.
						builder.append(closure.head).append(identifier);
					}

					identifier.setLength(0);
					stage = 1;
					continue;
				}

				// Placeholder identifier started.
				if (stage == 1)
				{
					// Compare the current character with the idenfitier's.
					// We reached the end of our identifier.
					if (ch == '_')
					{
						handler = lookup.apply(identifier.toString());
						if (handler == null)
						{
							builder.append(closure.head).append(identifier).append('_');
							stage = 0;
						}
						else
						{
							identifier.setLength(0);
							stage = 2;
						}
						continue;
					}

					// Keep building the identifier name.
					identifier.append(ch);
					continue;
				}

				// Building the placeholder parameter.
				if (stage == 2)
				{
					identifier.append(ch);
					continue;
				}

				// Nothing placeholder related was found.
				if (colorize && ch == '&')
				{
					stage = -1;
					continue;
				}
				builder.append(ch);
			}

			if (identifier != null)
			{
				if (stage > 0)
				{
					builder.append(closure.tail);
				}
				builder.append(identifier);
			}
			return builder.toString();
		}
	};


	final class MockPlayerPlaceholderHook extends PlaceholderHook
	{

		public static final String PLAYER_X    = String.valueOf(10);
		public static final String PLAYER_Y    = String.valueOf(20);
		public static final String PLAYER_Z    = String.valueOf(30);
		public static final String PLAYER_NAME = "Sxtanna";


		@Override
		public String onRequest(final OfflinePlayer player, final String params)
		{
			final String[] parts = params.split("_");
			if (parts.length == 0)
			{
				return null;
			}

			switch (parts[0])
			{
				case "name":
					return PLAYER_NAME;
				case "x":
					return PLAYER_X;
				case "y":
					return PLAYER_Y;
				case "z":
					return PLAYER_Z;
			}

			return null;
		}

	}

}
