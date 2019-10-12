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
package me.clip.placeholderapi.nukkit.util;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;

import java.util.Arrays;

public class Msg {
    public static void msg(CommandSender s, String... msg) {
        Arrays.stream(msg).map(Msg::color).forEach(s::sendMessage);
    }

    public static void broadcast(String... msg) {
        Arrays.stream(msg).map(Msg::color).forEach(Msg::broadcastMessage);
    }

    public static String color(String text) {
        return TextFormat.colorize(text);
    }

    private static int broadcastMessage(String message) {
        return Server.getInstance().broadcastMessage(message);
    }
}