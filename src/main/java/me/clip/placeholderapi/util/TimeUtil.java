package me.clip.placeholderapi.util;

import java.time.Duration;

public class TimeUtil {

  /**
   * Retrieves the remaining time in the specified format.
   *
   * @param seconds the total number of seconds to convert
   * @param type    the desired time format (DAYS, HOURS, MINUTES, SECONDS)
   * @return a string representing the remaining value in the specified format
   */
  public static String getRemaining(final int seconds, final TimeFormat type) {
    return String.valueOf(type.convert(seconds));
  }

  /**
   * Formats the given duration into a human-readable string.
   *
   * @param duration the {@link Duration} to be formatted (e.g., Duration.of(20, ChronoUnit.SECONDS))
   * @return a formatted string representing the duration (e.g., "1d 3h 20m 15s")
   */
  public static String getTime(final Duration duration) {
    return TimeFormat.DAYS.formatTime(duration);
  }

  /**
   * Formats the given number of seconds into a human-readable string.
   *
   * @param seconds the total number of seconds to format
   * @return a formatted string representing the time (e.g., "1d 3h 20m 15s")
   */
  public static String getTime(final int seconds) {
    return TimeFormat.DAYS.formatTime(seconds);
  }

  /**
   * Formats the given number of seconds into a human-readable string.
   *
   * @param seconds the total number of seconds to format
   * @return a formatted string representing the time (e.g., "1d 3h 20m 15s")
   */
  public static String getTime(final long seconds) {
    return TimeFormat.DAYS.formatTime((int) seconds);
  }
}
