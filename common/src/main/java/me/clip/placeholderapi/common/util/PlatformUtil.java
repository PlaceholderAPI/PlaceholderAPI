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
package me.clip.placeholderapi.common.util;

public class PlatformUtil {
    private static Platform platform;
    private static String version;

    public PlatformUtil(Platform serverPlatform) {
        platform = serverPlatform;
    }

    public static Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform serverPlatform) {
        platform = serverPlatform;
    }

    public enum Platform {
        BUKKIT,
        NUKKIT;
        // SPONGE,
        // Add more as needed

        public static Platform[] getPlatforms() {
            return values();
        }

        public Boolean isPaper() {
            try {
                Class.forName("com.destroystokyo.paper.PaperConfig");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        }

        public Boolean isSpigot() {
            try {
                Class.forName("org.spigotmc.SpigotConfig");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        }
    }
}