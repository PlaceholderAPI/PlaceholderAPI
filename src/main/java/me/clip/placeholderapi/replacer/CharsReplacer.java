/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2021 PlaceholderAPI Team
 *
 * PlaceholderAPI is free software: you can redistribute it and/or modify
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
      
      if (l == '&' && ++i < chars.length) {
        final char c = Character.toLowerCase(chars[i]);

        if (c != '0' && c != '1' && c != '2' && c != '3' && c != '4' && c != '5' && c != '6'
            && c != '7' && c != '8' && c != '9' && c != 'a' && c != 'b' && c != 'c' && c != 'd'
            && c != 'e' && c != 'f' && c != 'k' && c != 'l' && c != 'm' && c != 'n' && c != 'o' && c != 'r'
            && c != 'x') {
          builder.append(l).append(chars[i]);
        } else {
          builder.append(ChatColor.COLOR_CHAR);

          if (c != 'x') {
            builder.append(chars[i]);
            continue;
          }

          if ((i > 1 && chars[i - 2] == '\\') /*allow escaping &x*/) {
            builder.setLength(builder.length() - 2);
            builder.append('&').append(chars[i]);
            continue;
          }

          builder.append(c);

          int j = 0;
          while (++j <= 6) {
            if (i + j >= chars.length) {
              break;
            }

            final char x = chars[i + j];
            builder.append(ChatColor.COLOR_CHAR).append(x);
          }

          if (j == 7) {
            i += 6;
          } else {
            builder.setLength(builder.length() - (j * 2)); // undo &x parsing
          }
        }
        continue;
      }

      if (l != closure.head || i + 1 >= chars.length) {
        builder.append(l);
        continue;
      }

      boolean identified = false;
      boolean oopsitsbad = true;
      boolean hadSpace = false;

      while (++i < chars.length) {
        final char p = chars[i];

        if (p == ' ' && !identified) {
          hadSpace = true;
          break;
        }
        if (p == closure.tail) {
          oopsitsbad = false;
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

      final String identifierString = identifier.toString().toLowerCase();
      final String parametersString = parameters.toString();

      identifier.setLength(0);
      parameters.setLength(0);

      if (oopsitsbad) {
        builder.append(closure.head).append(identifier);

        if (identified) {
          builder.append('_').append(parametersString);
        }

        if (hadSpace) {
          builder.append(' ');
        }
        continue;
      }

      final PlaceholderExpansion placeholder = lookup.apply(identifierString);
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

      builder.append(ChatColor.translateAlternateColorCodes('&', replacement));
    }

    return builder.toString();
  }

}
