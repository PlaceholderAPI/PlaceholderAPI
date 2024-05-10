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
 * Classes implementing this interface will have a {@link #clear() clear void} that is called
 * by PlaceholderAPI whenever the {@link me.clip.placeholderapi.expansion.PlaceholderExpansion PlaceholderExpansion}
 * is unregistered.
 * 
 * <p>This allows you to execute things such as clearing internal caches, saving data to files, etc.
 *
 * @author Ryan McCarthy
 */
public interface Cacheable {

  /**
   * Called when the implementing class is unregistered from PlaceholderAPI
   */
  void clear();
}
