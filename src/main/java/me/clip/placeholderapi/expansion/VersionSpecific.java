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

package me.clip.placeholderapi.expansion;

/**
 * Placeholder expansions which use NMS code should be version specific. Implementing this class
 * allows you to perform checks based on the version the server is running. The isCompatibleWith
 * method will be passed the server version and allow you to return if your expansion is compatible
 * with that version.
 *
 * @author Ryan McCarthy
 *
 * @deprecated Will be removed in a future release.
 */
@Deprecated
public interface VersionSpecific {

  /**
   * This method is called before the expansion is attempted to be registered The server version
   * will be passed to this method so you know what version the server is currently running.
   *
   * @param v The {@link Version} to check against
   *
   * @return true if your expansion is compatible with the version the server is running.
   */
  boolean isCompatibleWith(Version v);
}
