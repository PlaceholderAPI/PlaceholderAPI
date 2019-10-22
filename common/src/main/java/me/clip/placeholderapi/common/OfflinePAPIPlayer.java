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
package me.clip.placeholderapi.common;

import me.clip.placeholderapi.common.util.PlatformUtil;

import java.util.UUID;

public interface OfflinePAPIPlayer {
    /**
     * Determines whether the player is online or not.
     *
     * @return true if online; false if not
     */
    boolean isOnline();

    /**
     * This method returns the player object that represents a player
     * on any of the platforms, for seamless player usage
     *
     * @return player The player object
     */
    PAPIPlayer getPAPIPlayer();

    /**
     * This method returns the name of the player on a per-platform basis
     * If the name is not implemented, it will return null.
     *
     * @return name The name of the player
     */
    String getName();

    /**
     * This method returns the uuid of the player on a per-platform basis
     * This method is much more reliable than {@link #getName()} for data
     *
     * @return uuid The unique id of the player
     */
    UUID getUniqueId();

    /**
     * This method returns the platform on which this player object is on.
     *
     * @return platform The platform this player object is on
     */
    PlatformUtil.Platform getPlayerPlatform();
}
