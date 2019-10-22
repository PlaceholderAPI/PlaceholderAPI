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
package me.clip.placeholderapi.common.expansion;

import me.clip.placeholderapi.common.util.PlatformUtil;

/**
 * Placeholder expansions which use platform/version specific code should implement this interface.
 * Implementing this class allows you to perform checks based on the platform the server software is.
 * The {@link #isCompatibleWith(PlatformUtil.Platform)} method will be passed the platform and allow you
 * to return if your expansion is compatible with that platform.
 *
 * @author Ryan McCarthy
 * @author Garrett Koleda
 */
public interface PlatformSpecific {
    /**
     * This method is called before the expansion is attempted to be registered.
     * The server type will be passed to this method so you know what platform the plugin is currently running on.
     *
     * @return true if your expansion is compatible with the platform the plugin is running.
     */
    Boolean isCompatibleWith(PlatformUtil.Platform platform);

    /**
     * This method is called before the expansion is attempted to be registered.
     * The server version will be passed to this method so you know what version the server is currently running.
     *
     * @return true if your expansion is compatible with the version the server is running.
     * @deprecated Use {@link #isCompatibleWith(PlatformUtil.Platform)} for more extensive usage
     */
    @Deprecated
    boolean isCompatibleWith(String version);
}