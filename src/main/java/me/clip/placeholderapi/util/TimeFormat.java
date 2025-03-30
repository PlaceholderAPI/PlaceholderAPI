/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2024 PlaceholderAPI Team
 *
 * PlaceholderAPI free software: you can redistribute it and/or modify
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


package me.clip.placeholderapi.util;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.StringJoiner;
import java.util.function.IntUnaryOperator;

public enum TimeFormat {

  DAYS(seconds -> seconds / 86400),
  HOURS(seconds -> (seconds / 3600) % 24),
  MINUTES(seconds -> (seconds / 60) % 60),
  SECONDS(seconds -> seconds % 60);

  private final IntUnaryOperator convert;

  TimeFormat(IntUnaryOperator convert) {
    this.convert = convert;
  }

  public int convert(int seconds) {
    return convert.applyAsInt(seconds);
  }

  public String formatTime(Duration duration) {
    return formatTime((int) duration.getSeconds());
  }

  public String formatTime(int seconds) {
    final StringJoiner joiner = new StringJoiner(" ");

    int remainingSeconds = seconds;
    int days = DAYS.convert(remainingSeconds);
    remainingSeconds -= days * 86400;

    int hours = HOURS.convert(remainingSeconds);
    remainingSeconds -= hours * 3600;

    int minutes = MINUTES.convert(remainingSeconds);
    remainingSeconds -= minutes * 60;

    if (days > 0) joiner.add(days + "d");
    if (hours > 0) joiner.add(hours + "h");
    if (minutes > 0) joiner.add(minutes + "m");
    if (remainingSeconds > 0) joiner.add(remainingSeconds + "s");

    return joiner.toString();
  }
}
