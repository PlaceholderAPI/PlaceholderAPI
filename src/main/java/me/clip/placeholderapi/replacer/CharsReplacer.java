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

package me.clip.placeholderapi.replacer;

import java.util.Locale;
import java.util.function.Function;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CharsReplacer implements Replacer {

  @NotNull
  private final Closure closure;

  public CharsReplacer(@NotNull final Closure closure) {
    this.closure = closure;
  }


  @NotNull
  @Override
  public String apply(@NotNull final String text, @Nullable final OfflinePlayer player,
      @NotNull final Function<String, @Nullable PlaceholderExpansion> lookup) {
    final char[] chars = text.toCharArray();
    final StringBuilder builder = new StringBuilder(text.length());

    final StringBuilder identifier = new StringBuilder();
    final StringBuilder parameters = new StringBuilder();

    for (int i = 0; i < chars.length; i++) {
      final char l = chars[i];

      if (l != closure.head || i + 1 >= chars.length) {
        builder.append(l);
        continue;
      }

      boolean identified = false;
      boolean invalid = true;
      boolean hadSpace = false;

      while (++i < chars.length) {
        final char p = chars[i];

        if (p == ' ' && !identified) {
          hadSpace = true;
          break;
        }
        if (p == closure.tail) {
          invalid = false;
          break;
        }

        if (p == '_' && !identified) {
          identified = true;
          continue;
        }

        if (identified) {
          parameters.append(p);
        } else {
          identifier.append(p);
        }
      }

      final String identifierString = identifier.toString();
      final String lowercaseIdentifierString = identifierString.toLowerCase(Locale.ROOT);
      final String parametersString = parameters.toString();

      identifier.setLength(0);
      parameters.setLength(0);

      if (invalid) {
        builder.append(closure.head).append(identifierString);

        if (identified) {
          builder.append('_').append(parametersString);
        }

        if (hadSpace) {
          builder.append(' ');
        }
        continue;
      }

      final PlaceholderExpansion placeholder = lookup.apply(lowercaseIdentifierString);
      if (placeholder == null) {
        builder.append(closure.head).append(identifierString);

        if (identified) {
          builder.append('_');
        }

        builder.append(parametersString).append(closure.tail);
        continue;
      }

      final String replacement = placeholder.onRequest(player, parametersString);
      if (replacement == null) {
        builder.append(closure.head).append(identifierString);

        if (identified) {
          builder.append('_');
        }

        builder.append(parametersString).append(closure.tail);
        continue;
      }

      builder.append(replacement);
    }

    return builder.toString();
  }

}
