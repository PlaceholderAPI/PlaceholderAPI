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
    private static Platform.BukkitType bukkitType;

    public PlatformUtil(Platform serverPlatform, String serverVersion) {
        platform = serverPlatform;
        version = serverVersion;
    }

    public static Platform getPlatform() {
        return platform;
    }

    public static Platform valueOf(String string) {
        return Platform.valueOf(string);
    }

    public static String getVersion() {
        return version;
    }

    public static Platform.BukkitType getBukkitType() {
        return bukkitType;
    }

    public static void setBukkitType(Platform.BukkitType bukkitType) {
        PlatformUtil.bukkitType = bukkitType;
    }

    public static boolean isSpigotCompat() {
        return bukkitType.isSpigotCompat();
    }

    public static boolean isServerLoadAvailable() throws ClassNotFoundException {
        return bukkitType.isServerLoadAvailable();
    }

    public enum Platform {
        BUKKIT,
        NUKKIT,
        // SPONGE,
        // Add more as needed
        UNKNOWN;

        public static Platform[] getPlatforms() {
            return values();
        }

        public static BukkitType isPaper() {
            try {
                if (isPaperServer()) {
                    return BukkitType.Paper;
                } else if (isSpigotServer()) {
                    return BukkitType.Spigot;
                } else {
                    return BukkitType.Bukkit;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return BukkitType.Unsupported;
        }

        public static BukkitType isSpigot() {
            try {
                if (isSpigotServer()) {
                    return BukkitType.Spigot;
                } else {
                    return BukkitType.Bukkit;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return BukkitType.Unsupported;
        }

        private static boolean isPaperServer() throws ClassNotFoundException {
            return Class.forName("com.destroystokyo.paper.PaperConfig") != null;
        }

        private static Boolean isSpigotServer() throws ClassNotFoundException {
            return Class.forName("org.spigotmc.SpigotConfig") != null;
        }

        public enum BukkitType {
            Bukkit(false),
            Spigot(true),
            Paper(true),
            Unsupported(false);

            boolean spigotCompat;

            BukkitType(boolean spigotCompatible) {
                spigotCompat = spigotCompatible;
            }

            public boolean isSpigotCompat() {
                return this.spigotCompat;
            }

            public boolean isServerLoadAvailable() throws ClassNotFoundException {
                return Class.forName("org.bukkit.event.server.ServerLoadEvent") != null;
            }
        }
    }
}