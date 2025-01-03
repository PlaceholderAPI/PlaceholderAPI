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
