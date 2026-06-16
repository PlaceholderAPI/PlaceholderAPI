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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.OptionalInt;

import org.junit.jupiter.api.Test;

public final class ServerVersionResolverTest {

    @Test
    void usesPaperBuildInfoWhenAvailable() {
        final ServerVersionResolver.ServerBuild paperBuild =
                new ServerVersionResolver.ServerBuild("26.2", OptionalInt.of(10));

        final ServerVersionResolver.ServerBuild result =
                ServerVersionResolver.resolve("26.2.build.10-alpha", paperBuild);

        assertSame(paperBuild, result);
        assertEquals("26.2", result.getMinecraftVersionId());
        assertEquals(OptionalInt.of(10), result.getBuildNumber());
        assertEquals("v26_2_R1", result.getLegacyVersion());
    }

    @Test
    void stripsPaperBuildMetadataWhenServerBuildInfoIsUnavailable() {
        final ServerVersionResolver.ServerBuild result =
                ServerVersionResolver.resolve("26.2.build.10-alpha", null);

        assertEquals("26.2", result.getMinecraftVersionId());
        assertEquals(OptionalInt.empty(), result.getBuildNumber());
        assertEquals("v26_2_R1", result.getLegacyVersion());
    }

    @Test
    void preservesTraditionalBukkitVersions() {
        final ServerVersionResolver.ServerBuild result =
                ServerVersionResolver.resolve("1.21.11-R0.1-SNAPSHOT", null);

        assertEquals("1.21.11", result.getMinecraftVersionId());
        assertEquals(OptionalInt.empty(), result.getBuildNumber());
    }
}
