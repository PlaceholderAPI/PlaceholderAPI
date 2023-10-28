/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2021 PlaceholderAPI Team
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

package me.clip.placeholderapi.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import java.util.Arrays;
import java.util.List;

public class ValidateUtil {

    private static final List<String> vulnerableExpansions = Arrays.asList(
            "JavaScript",
            "StaffFacilities",
            "Groopi",
            "Minepacks",
            "fetch",
            "Spigotlobby"
    );

    private static final List<String> expansionVersions = Arrays.asList(
            "2.1.2",
            "1.4.4",
            "ALL",
            "1.0.7",
            "ALL",
            "ALL"
    );

    public static boolean checkExpansion(final PlaceholderExpansion expansion) {
        final String expansionName = expansion.getIdentifier();
        if(!vulnerableExpansions.contains(expansionName)) {
            return false;
        }

        final String expansionVersion = expansionVersions.get(vulnerableExpansions.indexOf(expansionName));
        return expansionVersion.equals("ALL") || !expansionVersion.equals(expansion.getVersion());
    }
}
