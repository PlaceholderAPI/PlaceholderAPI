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
package me.clip.placeholderapi.common.util.logging;

public abstract class LoggerBase {
    private boolean debug;

    public abstract void info(String message);

    public abstract void error(String message);

    public void debug(String message) {
        if (debug) {
            info(message);
        }
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
        if (debug) {
            debug("Debug logging is enabled!");
        }
    }
}