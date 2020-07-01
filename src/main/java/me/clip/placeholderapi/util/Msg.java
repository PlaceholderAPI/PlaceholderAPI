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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class Msg {

  public static void msg(CommandSender sender, String... messages) {
    for (String message : messages) {
      String msg = color(message);
      sender.sendMessage(msg);
    }
  }

  public static void broadcast(String... messages) {
    CommandSender sender = Bukkit.getConsoleSender();
    for (String message : messages) {
      String msg = color(message);
      sender.sendMessage(msg);
    }
  }

  public static String color(String text) {
    return ChatColor.translateAlternateColorCodes('&', text);
  }
}
