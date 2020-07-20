package me.clip.placeholderapi.replacer;

import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public final class CharsReplacer implements Replacer
{

	@Override
	public @NotNull String apply(@NotNull final String text, @Nullable final OfflinePlayer player, @NotNull final Function<String, @Nullable PlaceholderHook> lookup)
	{
		final char[]        chars   = text.toCharArray();
		final StringBuilder builder = new StringBuilder(text.length());

		final StringBuilder identifier = new StringBuilder();
		final StringBuilder parameters = new StringBuilder();

		for (int i = 0; i < chars.length; i++)
		{
			final char l = chars[i];

			if (l == '&' && ++i < chars.length)
			{
				final char c = chars[i];

				if (c != '0' && c != '1' && c != '2' && c != '3' && c != '4' && c != '5' && c != '6' && c != '7' && c != '8' && c != '9' && c != 'a' && c != 'b' && c != 'c' && c != 'd' && c != 'e' && c != 'f' && c != 'k' && c != 'l' && c != 'm' && c != 'o' && c != 'r' && c != 'x')
				{
					builder.append(l).append(c);
				}
				else
				{
					builder.append('§').append(c);
				}
				continue;
			}

			if (l != '%')
			{
				builder.append(l);
				continue;
			}

			boolean identified = false;

			while (i++ < chars.length)
			{
				final char p = chars[i];

				if (p == '%')
				{
					break;
				}

				if (p == '_' && !identified)
				{
					identified = true;
					continue;
				}

				if (identified)
				{
					parameters.append(p);
				}
				else
				{
					identifier.append(p);
				}
			}

			final String identifierString = identifier.toString();
			final String parametersString = parameters.toString();

			identifier.setLength(0);
			parameters.setLength(0);

			final PlaceholderHook placeholder = lookup.apply(identifierString);
			if (placeholder == null)
			{
				builder.append('%').append(identifierString).append('_').append(parametersString).append('%');
				continue;
			}

			final String replacement = placeholder.onRequest(player, parametersString);
			if (replacement == null)
			{
				builder.append('%').append(identifierString).append('_').append(parametersString).append('%');
				continue;
			}

			builder.append(replacement);
		}

		return builder.toString();
	}

}
