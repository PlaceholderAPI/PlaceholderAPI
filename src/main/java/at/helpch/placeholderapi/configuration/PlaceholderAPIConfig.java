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

package at.helpch.placeholderapi.configuration;

import com.google.gson.annotations.JsonAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class PlaceholderAPIConfig {
    private boolean cloudEnabled;
    private boolean debugMode;
    private ExpansionSort cloudSorting;
    private BooleanValue booleanValue;
    private String dateFormat;
    private Map<String, Object> expansions;

    public PlaceholderAPIConfig(boolean cloudEnabled, boolean debugMode, @NotNull ExpansionSort cloudSorting,
                                @NotNull BooleanValue booleanValue, @NotNull String dateFormat) {
        this.cloudEnabled = cloudEnabled;
        this.debugMode = debugMode;
        this.cloudSorting = cloudSorting;
        this.booleanValue = booleanValue;
        this.dateFormat = dateFormat;
        this.expansions = new HashMap<>();
    }

    public PlaceholderAPIConfig(boolean cloudEnabled, boolean debugMode, @NotNull ExpansionSort cloudSorting,
                                @NotNull BooleanValue booleanValue, @NotNull String dateFormat, Map<String, Object> expansions) {
        this.cloudEnabled = cloudEnabled;
        this.debugMode = debugMode;
        this.cloudSorting = cloudSorting;
        this.booleanValue = booleanValue;
        this.dateFormat = dateFormat;
        this.expansions = expansions;
    }

    public boolean cloudEnabled() {
        return cloudEnabled;
    }

    public void cloudEnabled(final boolean value) {
        cloudEnabled = value;
    }

    public boolean debugMode() {
        return debugMode;
    }

    public void debugMode(final boolean value) {
        debugMode = value;
    }

    @NotNull
    public ExpansionSort cloudSorting() {
        return cloudSorting;
    }

    public void cloudSorting(@NotNull final ExpansionSort value) {
        cloudSorting = value;
    }

    @NotNull
    public BooleanValue booleanValue() {
        return booleanValue;
    }

    public void booleanValue(@NotNull final BooleanValue value) {
        booleanValue = value;
    }

    @NotNull
    public String dateFormat() {
        return dateFormat;
    }

    public void dateFormat(@NotNull final String value) {
        dateFormat = value;
    }

    @NotNull
    public Map<String, Object> expansions() {
        return expansions;
    }

    public void expansions(@NotNull final Map<String, Object> value) {
        expansions = value;
    }
}
