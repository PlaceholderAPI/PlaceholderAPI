/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2026 PlaceholderAPI Team
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

package me.clip.placeholderapi;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PlaceholderHook {
    @Nullable
    public String onRequest(final OfflinePlayer player, @NotNull final String params) {
        if (player != null && player.isOnline()) {
            return onPlaceholderRequest(player.getPlayer(), params);
        }

        return onPlaceholderRequest(null, params);
    }

    /**
     * Override this method when a placeholder should return a non-string value for type handling.
     * <br>The default delegates to {@link #onPlaceholderTypeHandledRequest(Player, String)} with an online
     * {@link Player}, matching the behavior of {@link #onRequest(OfflinePlayer, String)}.
     *
     * @param player Player to parse the placeholder against
     * @param params Placeholder parameters
     * @return Object to resolve through type handlers, or null to leave the placeholder unchanged
     */
    @Nullable
    public Object onTypeHandledRequest(final OfflinePlayer player, @NotNull final String params) {
        if (player != null && player.isOnline()) {
            return onPlaceholderTypeHandledRequest(player.getPlayer(), params);
        }

        return onPlaceholderTypeHandledRequest(null, params);
    }

    @Nullable
    public String onPlaceholderRequest(final Player player, @NotNull final String params) {
        return null;
    }

    /**
     * Override this method when a player placeholder should return a non-string value for type handling.
     * <br>The default returns null so legacy string placeholders are only resolved through
     * {@link #onRequest(OfflinePlayer, String)}.
     *
     * @param player Online player to parse the placeholder against
     * @param params Placeholder parameters
     * @return Object to resolve through type handlers, or null to leave the placeholder unchanged
     */
    @Nullable
    public Object onPlaceholderTypeHandledRequest(final Player player, @NotNull final String params) {
        return null;
    }
}
