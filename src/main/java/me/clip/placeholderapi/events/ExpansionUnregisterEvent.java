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

package me.clip.placeholderapi.events;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event indicates that a {@link PlaceholderExpansion PlaceholderExpansion} has been
 * unregistered by PlaceholderAPI.
 * 
 * <p>Note that this event is triggered <b>before</b> the PlaceholderExpansion is completely
 * removed.
 * <br>This includes removing any Listeners, stopping active tasks and clearing the cache of
 * the PlaceholderExpansion.
 */
public final class ExpansionUnregisterEvent extends Event {

  @NotNull
  private static final HandlerList HANDLERS = new HandlerList();
  
  @NotNull
  private final PlaceholderExpansion expansion;

  public ExpansionUnregisterEvent(@NotNull final PlaceholderExpansion expansion) {
    this.expansion = expansion;
  }

  @NotNull
  public static HandlerList getHandlerList() {
    return HANDLERS;
  }
  
  /**
   * The {@link PlaceholderExpansion PlaceholderExpansion} that was unregistered.
   * 
   * @return The {@link PlaceholderExpansion PlaceholderExpansion} instance.
   */
  @NotNull
  public PlaceholderExpansion getExpansion() {
    return expansion;
  }

  @NotNull
  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

}
