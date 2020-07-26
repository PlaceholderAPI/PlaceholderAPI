package me.clip.placeholderapi;

import com.google.common.collect.ImmutableMap;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.replacer.CharsReplacer;
import me.clip.placeholderapi.replacer.RegexReplacer;
import me.clip.placeholderapi.replacer.Replacer;
import org.bukkit.OfflinePlayer;

public interface Values
{

	String SMALL_TEXT = "My name is %player_name%";
	String LARGE_TEXT = "My name is %player_name% and my location is (%player_x%, %player_y%, %player_z%), this placeholder is invalid %server_name%";

	ImmutableMap<String, PlaceholderExpansion> PLACEHOLDERS = ImmutableMap.<String, PlaceholderExpansion>builder()
			.put("player", new MockPlayerPlaceholderExpansion())
			.build();


	Replacer CHARS_REPLACER = new CharsReplacer(Replacer.Closure.PERCENT);
	Replacer REGEX_REPLACER = new RegexReplacer(Replacer.Closure.PERCENT);


	final class MockPlayerPlaceholderExpansion extends PlaceholderExpansion
	{

		public static final String PLAYER_X    = String.valueOf(10);
		public static final String PLAYER_Y    = String.valueOf(20);
		public static final String PLAYER_Z    = String.valueOf(30);
		public static final String PLAYER_NAME = "Sxtanna";


		@Override
		public String getIdentifier()
		{
			return "player";
		}

		@Override
		public String getAuthor()
		{
			return "Sxtanna";
		}

		@Override
		public String getVersion()
		{
			return "1.0";
		}

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
