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
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event indicates that a <b>single</b> {@link PlaceholderExpansion PlaceholderExpansion} has
 * been registered in PlaceholderAPI.
 * 
 * <p>To know when <b>all</b> Expansions have been registered, use the
 * {@link me.clip.placeholderapi.events.ExpansionsLoadedEvent ExpansionsLoadedEvent} instead.
 */
public final class ExpansionRegisterEvent extends Event implements Cancellable {

  @NotNull
  private static final HandlerList HANDLERS = new HandlerList();
  @NotNull
  private final PlaceholderExpansion expansion;
  private boolean cancelled;

  public ExpansionRegisterEvent(@NotNull final PlaceholderExpansion expansion) {
    this.expansion = expansion;
  }

  @NotNull
  public static HandlerList getHandlerList() {
    return HANDLERS;
  }
  
  /**
   * The {@link PlaceholderExpansion PlaceholderExpansion} that was registered in PlaceholderAPI.
   * <br>The PlaceholderExpansion will be available for use when the event
   * {@link #isCancelled() was not cancelled}!
   * 
   * @return Current instance of the registered {@link PlaceholderExpansion PlaceholderExpansion}
   */
  @NotNull
  public PlaceholderExpansion getExpansion() {
    return expansion;
  }

  /**
   * Indicates if this event was cancelled or not.
   * <br>A cancelled Event will result in the {@link #getExpansion() PlaceholderExpansion} NOT
   * being added to PlaceholderAPI's internal list and will therefore be considered not registered
   * anymore.
   * 
   * @return Whether the event has been cancelled or not.
   */
  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  @NotNull
  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

}
