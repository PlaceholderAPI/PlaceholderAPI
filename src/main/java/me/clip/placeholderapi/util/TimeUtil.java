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
package me.clip.placeholderapi.util;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class TimeUtil {

    public static String getRemaining(int seconds, TimeFormat type) {
        if (seconds < 60) {
            switch (type) {
                case DAYS:
                case HOURS:
                case MINUTES:
                    return "0";
                case SECONDS:
                    return String.valueOf(seconds);
            }

            return String.valueOf(seconds);
        }

        int minutes = seconds / 60;
        int s = 60 * minutes;
        int secondsLeft = seconds - s;

        if (minutes < 60) {
            switch (type) {
                case DAYS:
                case HOURS:
                    return "0";
                case MINUTES:
                    return String.valueOf(minutes);
                case SECONDS:
                    return String.valueOf(secondsLeft);
            }

            return String.valueOf(seconds);
        }

        if (minutes < 1440) {
            int hours = minutes / 60;
            int inMins = 60 * hours;
            int leftOver = minutes - inMins;

            switch (type) {
                case DAYS:
                    return "0";
                case HOURS:
                    return String.valueOf(hours);
                case MINUTES:
                    return String.valueOf(leftOver);
                case SECONDS:
                    return String.valueOf(secondsLeft);
            }

            return String.valueOf(seconds);
        }

        int days = minutes / 1440;
        int inMins = 1440 * days;
        int leftOver = minutes - inMins;

        if (leftOver < 60) {
            switch (type) {
                case DAYS:
                    return String.valueOf(days);
                case HOURS:
                    return String.valueOf(0);
                case MINUTES:
                    return String.valueOf(leftOver);
                case SECONDS:
                    return String.valueOf(secondsLeft);
            }

            return String.valueOf(seconds);

        } else {
            int hours = leftOver / 60;
            int hoursInMins = 60 * hours;
            int minsLeft = leftOver - hoursInMins;

            switch (type) {
                case DAYS:
                    return String.valueOf(days);
                case HOURS:
                    return String.valueOf(hours);
                case MINUTES:
                    return String.valueOf(minsLeft);
                case SECONDS:
                    return String.valueOf(secondsLeft);
            }

            return String.valueOf(seconds);
        }
    }

    public static String getTime(int seconds) {
        return getTime(Duration.ofSeconds(seconds));
    }

    /**
     * Format the given value with s, m, h and d (seconds, minutes, hours and days)
     *
     * @param duration {@link Duration} (eg, Duration.of(20, {@link ChronoUnit#SECONDS}) for 20 seconds)
     * @return formatted time
     */
    public static String getTime(final Duration duration) {
        final StringBuilder builder = new StringBuilder();

        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        seconds %= 60;
        minutes %= 60;
        hours %= 60;
        days %= 24;

        if (seconds > 0) {
            builder.insert(0, seconds + "s");
        }

        if (minutes > 0) {
            if (builder.length() > 0) {
                builder.insert(0, ' ');
            }

            builder.insert(0, minutes + "m");
        }

        if (hours > 0) {
            if (builder.length() > 0) {
                builder.insert(0, ' ');
            }

            builder.insert(0, hours + "h");
        }

        if (days > 0) {
            if (builder.length() > 0) {
                builder.insert(0, ' ');
            }

            builder.insert(0, days + "d");
        }

        return builder.toString();
    }
}
