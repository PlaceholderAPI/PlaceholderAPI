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

    if (seconds < 60) {
      return seconds + "s";
    }

    int minutes = seconds / 60;
    int s = 60 * minutes;
    int secondsLeft = seconds - s;

    if (minutes < 60) {
      if (secondsLeft > 0) {
        return minutes + "m " + secondsLeft + "s";
      } else {
        return minutes + "m";
      }
    }

    if (minutes < 1440) {
      String time;
      int hours = minutes / 60;
      time = hours + "h";
      int inMins = 60 * hours;
      int leftOver = minutes - inMins;

      if (leftOver >= 1) {
        time = time + " " + leftOver + "m";
      }

      if (secondsLeft > 0) {
        time = time + " " + secondsLeft + "s";
      }

      return time;
    }

    String time;
    int days = minutes / 1440;
    time = days + "d";
    int inMins = 1440 * days;
    int leftOver = minutes - inMins;

    if (leftOver >= 1) {
      if (leftOver < 60) {
        time = time + " " + leftOver + "m";
      } else {
        int hours = leftOver / 60;
        time = time + " " + hours + "h";

        int hoursInMins = 60 * hours;
        int minsLeft = leftOver - hoursInMins;
        time = time + " " + minsLeft + "m";
      }
    }

    if (secondsLeft > 0) {
      time = time + " " + secondsLeft + "s";
    }

    return time;
  }
}
