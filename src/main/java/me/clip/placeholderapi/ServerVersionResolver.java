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

import java.util.OptionalInt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ServerVersionResolver {

    private static final String PAPER_BUILD_INFO_PROVIDER =
            "me.clip.placeholderapi.PaperServerBuildInfoProvider";

    private ServerVersionResolver() {
    }

    @NotNull
    static ServerBuild resolve(@NotNull final String bukkitVersion) {
        return resolve(bukkitVersion, resolvePaperBuildInfo());
    }

    @NotNull
    static ServerBuild resolve(@NotNull final String bukkitVersion,
                               @Nullable final ServerBuild paperBuildInfo) {
        if (paperBuildInfo != null) {
            return paperBuildInfo;
        }

        return new ServerBuild(normalizeBukkitVersion(bukkitVersion), OptionalInt.empty());
    }

    @Nullable
    private static ServerBuild resolvePaperBuildInfo() {
        try {
            final Class<?> provider = Class.forName(
                    PAPER_BUILD_INFO_PROVIDER,
                    true,
                    ServerVersionResolver.class.getClassLoader()
            );
            final Object result = provider.getMethod("get").invoke(null);

            if (result instanceof ServerBuild) {
                return (ServerBuild) result;
            }
        } catch (final ReflectiveOperationException | LinkageError ignored) {
            // Paper is unavailable or exposes an incompatible ServerBuildInfo API.
        }

        return null;
    }

    @NotNull
    static String normalizeBukkitVersion(@NotNull final String bukkitVersion) {
        String version = bukkitVersion.split("-", 2)[0];

        // Some Paper development versions include build metadata in the Bukkit version.
        // Keep this fallback for Paper forks that do not expose ServerBuildInfo.
        final int paperBuildMetadataIndex = version.indexOf(".build.");
        if (paperBuildMetadataIndex != -1) {
            version = version.substring(0, paperBuildMetadataIndex);
        }

        return version;
    }

    static final class ServerBuild {

        @NotNull
        private final String minecraftVersionId;
        @NotNull
        private final OptionalInt buildNumber;

        ServerBuild(@NotNull final String minecraftVersionId,
                    @NotNull final OptionalInt buildNumber) {
            this.minecraftVersionId = minecraftVersionId;
            this.buildNumber = buildNumber;
        }

        @NotNull
        String getMinecraftVersionId() {
            return minecraftVersionId;
        }

        @NotNull
        OptionalInt getBuildNumber() {
            return buildNumber;
        }

        @NotNull
        String getLegacyVersion() {
            String version = minecraftVersionId;

            if (version.chars()
                    .filter(c -> c == '.')
                    .count() == 1) {
                return 'v' + version.replace('.', '_') + "_R1";
            }

            final String[] versionParts = version.split("\\.");
            int minor = 1;

            if (versionParts.length > 2 && !versionParts[2].isEmpty()) {
                try {
                    minor = Integer.parseInt(versionParts[2].charAt(0) + "");
                } catch (final NumberFormatException ignored) {
                    minor = 1;
                }
            }

            return 'v' + version.replace('.', '_').replace("_" + minor, "")
                    + "_R" + (minor - 1);
        }
    }
}
