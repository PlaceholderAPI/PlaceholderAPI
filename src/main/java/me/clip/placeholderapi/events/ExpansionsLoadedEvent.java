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


import java.util.Collections;
import java.util.List;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event indicated that <b>all</b> {@link PlaceholderExpansion PlaceholderExpansions} have
 * been registered in PlaceholderAPI and can now be used.
 * <br>This even will also be triggered whenever PlaceholderAPI gets reloaded.
 * 
 * <p>All PlaceholderExpansions, except for those loaded by plugins, are loaded
 * after Spigot triggered its ServerLoadEvent (1.13+), or after PlaceholderAPI has been enabled.
 */
public class ExpansionsLoadedEvent extends Event {
    
    private final List<PlaceholderExpansion> expansions;
    
    public ExpansionsLoadedEvent(List<PlaceholderExpansion> expansions) {
        this.expansions = Collections.unmodifiableList(expansions);
    }

    /**
     * Returns a unmodifiable list of {@link PlaceholderExpansion PlaceholderExpansions} that
     * have been registered by PlaceholderAPI.
     * 
     * <p><b>This list does not include manually registered PlaceholderExpansions.</b>
     * 
     * @return List of {@link PlaceholderExpansion registered PlaceholderExpansions}.
     */
    @NotNull
    public final List<PlaceholderExpansion> getExpansions(){
        return expansions;
    }
    
    @NotNull
    private static final HandlerList HANDLERS = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
