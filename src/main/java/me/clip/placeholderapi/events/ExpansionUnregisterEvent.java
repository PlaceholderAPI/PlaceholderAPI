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
package me.clip.placeholderapi.events;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class ExpansionUnregisterEvent extends Event
{

	@NotNull
	private static final HandlerList HANDLERS = new HandlerList();


	@NotNull
	private final PlaceholderExpansion expansion;

	public ExpansionUnregisterEvent(@NotNull final PlaceholderExpansion expansion)
	{
		this.expansion = expansion;
	}


	@NotNull
	public PlaceholderExpansion getExpansion()
	{
		return expansion;
	}


	@NotNull
	@Override
	public HandlerList getHandlers()
	{
		return HANDLERS;
	}


	@NotNull
	public static HandlerList getHandlerList()
	{
		return HANDLERS;
	}

}
