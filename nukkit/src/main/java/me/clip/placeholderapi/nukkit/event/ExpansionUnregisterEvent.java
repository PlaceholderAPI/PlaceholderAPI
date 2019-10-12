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
package me.clip.placeholderapi.nukkit.event;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import me.clip.placeholderapi.nukkit.expansion.PlaceholderExpansion;

public class ExpansionUnregisterEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private PlaceholderExpansion expansion;

    public ExpansionUnregisterEvent(PlaceholderExpansion expansion) {
        this.expansion = expansion;
    }

    public static HandlerList getHandlers() {
        return HANDLERS;
    }

    public PlaceholderExpansion getExpansion() {
        return expansion;
    }
}