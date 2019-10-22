/*
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
 */
package me.clip.placeholderapi.common.util;

import me.clip.placeholderapi.common.PAPIPlayer;

import java.util.Arrays;

public class Msg {
    public static void msg(PAPIPlayer sender, String... msg) {
        Arrays.stream(msg).map(Msg::color).forEach(sender::sendMessage);
    }

    public static String color(String msg) {
        return msg.replace('&', '§');
    }
}