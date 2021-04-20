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
    
    final StringBuilder builder = new StringBuilder();
    final StringBuilder identifier = new StringBuilder();
    final StringBuilder parameters = new StringBuilder();
    
    for (int i = 0; i < text.length(); i++) {
      final char chr = text.charAt(i);
      
      // Character is not %, { or } or text is less than or only 5 characters long
      if (chr != closure.head || i + 5 >= text.length()) {
        builder.append(chr);

        continue;
      }
      
      boolean identified = false;
      boolean invalid = true;
      
      while (++i < text.length()) {
        final char p = text.charAt(i);
        
        if (p == closure.tail) {
          invalid = identifier.length() == 0 && parameters.length() == 0;
          break;
        }
        if (p == ' ' && !identified) {
          parameters.append(' ');
          break;
        }
        if (p == '_' && !identified) {
          if (identifier.length() == 0) {
            break; // Placeholder is %_<text>%
          }
          identified = true; // We got the complete identifier.
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
      
      if (invalid) {
        builder.append(closure.head).append(identifierString);
        
        if (identified) {
          builder.append('_').append(parametersString);
        }
        continue;
      }
      
      final PlaceholderExpansion placeholder = lookup.apply(identifierString);
      if (placeholder == null) {
        appendPlaceholder(builder, identifierString, parametersString, identified);
        continue;
      }
      
      final String replacement = placeholder.onRequest(player, parametersString);
      if (replacement == null) {
        appendPlaceholder(builder, identifierString, parametersString, identified);
        continue;
      }
      
      builder.append(replacement);
    }
    
    return builder.toString();
  }
  
  // convenience method to reduce duplicate code
  private void appendPlaceholder(StringBuilder builder, String identifier, String parameters,
      boolean identified) {
    builder.append(closure.head).append(identifier);
    
    if (identified) {
      builder.append('_');
    }
    
    builder.append(parameters).append(closure.tail);
  }
}
