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

import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @deprecated This event is no longer used.
 */
@Deprecated
public final class PlaceholderHookUnloadEvent extends Event {

  @NotNull
  private static final HandlerList HANDLERS = new HandlerList();


  @NotNull
  private final String plugin;
  @NotNull
  private final PlaceholderHook placeholderHook;

  public PlaceholderHookUnloadEvent(@NotNull final String plugin,
      @NotNull final PlaceholderHook placeholderHook) {
    this.plugin = plugin;
    this.placeholderHook = placeholderHook;
  }

  @NotNull
  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  @NotNull
  public String getHookName() {
    return plugin;
  }

  @NotNull
  public PlaceholderHook getHook() {
    return placeholderHook;
  }

  @NotNull
  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

}
