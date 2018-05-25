/*
 *
 * PlaceholderAPI
 * Copyright (C) 2018 Ryan McCarthy
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
package me.clip.placeholderapi.util;

import org.bukkit.plugin.java.JavaPlugin;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.configuration.PlaceholderAPIConfig;
import me.clip.placeholderapi.util.TimeFormat;

public class TimeUtil {
	
	public static final String getRemaining(int seconds, TimeFormat type) {
		final PlaceholderAPIPlugin plugin = (PlaceholderAPIPlugin) JavaPlugin.getProvidingPlugin(PlaceholderAPIPlugin.class);
		final PlaceholderAPIConfig config = plugin.getPlaceholderAPIConfig();
		final String time = config.getTime(type);
		final int days = seconds / 86400; seconds = seconds - (days * 86400);
		final int hours = seconds / 3600; seconds = seconds - (hours * 3600);
		final int minutes = seconds / 60; seconds = seconds - (minutes * 60);
		
		switch (type) {
		case DAYS : return days + time;
		case HOURS : return hours + time;
		case MINUTES : return minutes + time;
		case SECONDS : return seconds + time;
		default : return null;
		}
	}

	public static final String getTime(int seconds) {
		final PlaceholderAPIPlugin plugin = (PlaceholderAPIPlugin) JavaPlugin.getProvidingPlugin(PlaceholderAPIPlugin.class);
		final PlaceholderAPIConfig config = plugin.getPlaceholderAPIConfig();
		final String time = config.getTime(type);
		final String timeDays = getTime(TimeFormat.DAYS);
		final String timeHours = getTime(TimeFormat.HOURS);
		final String timeMinutes = getTime(TimeFormat.MINUTES);
		final String timeSeconds = getTime(TimeFormat.SECONDS);
		final StringBuilder timeBuilder = new StringBuilder();
		final int days = seconds / 86400; seconds = seconds - (days * 86400);
		final int hours = seconds / 3600; seconds = seconds - (hours * 3600);
		final int minutes = seconds / 60; seconds = seconds - (minutes * 60);
		
		if (days > 0) {
			timeBuilder.append(days).append(timeDays).append(" ");
			timeBuilder.append(hours).append(timeHours).append(" ");
			timeBuilder.append(minutes).append(timeMinutes).append(" ");
			timeBuilder.append(seconds).append(timeSeconds);
		} else if (hours > 0) {
			timeBuilder.append(hours).append(timeHours).append(" ");
			timeBuilder.append(minutes).append(timeMinutes).append(" ");
			timeBuilder.append(seconds).append(timeSeconds);
		} else if (minutes > 0) {
			timeBuilder.append(minutes).append(timeMinutes).append(" ");
			timeBuilder.append(seconds).append(timeSeconds);
		} else {
			timeBuilder.append(seconds).append(timeSeconds);
		}
		
		return timeBuilder.toString();
	}
}
