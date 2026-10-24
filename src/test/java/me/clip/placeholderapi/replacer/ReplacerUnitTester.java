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

package me.clip.placeholderapi.replacer;

import static me.clip.placeholderapi.Values.MockPlayerPlaceholderExpansion.PLAYER_NAME;
import static me.clip.placeholderapi.Values.MockPlayerPlaceholderExpansion.PLAYER_X;
import static me.clip.placeholderapi.Values.MockPlayerPlaceholderExpansion.PLAYER_Y;
import static me.clip.placeholderapi.Values.MockPlayerPlaceholderExpansion.PLAYER_Z;
import static me.clip.placeholderapi.Values.MockPlayerPlaceholderExpansion.EMPTY_ARGUMENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import me.clip.placeholderapi.Values;
import java.util.concurrent.atomic.AtomicReference;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.junit.jupiter.api.Test;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ReplacerUnitTester {

    @Test
    void testCharsReplacerProducesExpectedSingleValue() {
        assertEquals(PLAYER_NAME,
                Values.CHARS_REPLACER.apply("%player_name%", null, Values.PLACEHOLDERS::get));
    }

    @Test
    void charsReplacersDoesNotParsePlaceholdersWithNoArguments() {
        assertEquals(Values.NO_ARGUMENTS_PLACEHOLDER,
                Values.CHARS_REPLACER.apply(Values.NO_ARGUMENTS_PLACEHOLDER, null, Values.PLACEHOLDERS::get));
    }

    @Test
    void charsReplacersParsesPlaceholdersWithOneArgumentThatIsEmpty() {
        assertEquals(EMPTY_ARGUMENT,
                Values.CHARS_REPLACER.apply(Values.EMPTY_ARGUMENT_PLACEHOLDER, null, Values.PLACEHOLDERS::get));
    }

    @Test
    void testCharsReplacerProducesExpectedSentence() {
        assertEquals(String.format(
                        "My name is %s and my location is (%s, %s, %s), this placeholder is invalid %%server_name%%",
                        PLAYER_NAME, PLAYER_X, PLAYER_Y, PLAYER_Z),
                Values.CHARS_REPLACER.apply(Values.LARGE_TEXT, null, Values.PLACEHOLDERS::get));
    }

    @Test
    void testResultsAreTheSameAsReplacement() {
        final String resultChars = Values.CHARS_REPLACER
                .apply("%player_name%", null, Values.PLACEHOLDERS::get);

        assertEquals(PLAYER_NAME, resultChars);
    }

    @Test
    void testCharsReplacerIgnoresMalformed() {
        final String text = "10% and %hello world 15%";

        assertEquals(text, Values.CHARS_REPLACER.apply(text, null, Values.PLACEHOLDERS::get));
    }

    @Test
    void testCharsReplacerKeepsPlaceholderWhenExpansionThrows() {
        final AtomicReference<Throwable> logged = new AtomicReference<>();
        final PlaceholderExpansion expansion = new PlaceholderExpansion() {
            @NotNull
            @Override
            public String getIdentifier() {
                return "broken";
            }

            @NotNull
            @Override
            public String getAuthor() {
                return "test";
            }

            @NotNull
            @Override
            public String getVersion() {
                return "1.0";
            }

            @Override
            public String onRequest(@Nullable final OfflinePlayer player, @NotNull final String params) {
                throw new IllegalStateException("broken expansion");
            }

            @Override
            public void severe(final String message, final Throwable throwable) {
                logged.set(throwable);
            }
        };

        assertEquals("%broken_value% and Sxtanna",
                Values.CHARS_REPLACER.apply("%broken_value% and %player_name%", null,
                        identifier -> "broken".equals(identifier) ? expansion : Values.PLACEHOLDERS.get(identifier)));
        assertSame(IllegalStateException.class, logged.get().getClass());
    }

}
