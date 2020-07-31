/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2020 PlaceholderAPI Team
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

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexReplacer implements Replacer
{

  @NotNull
  private final Pattern pattern;

  public RegexReplacer(@NotNull final Closure closure)
  {
    this.pattern = Pattern.compile(String.format("\\%s((?<identifier>[a-zA-Z0-9]+)_)(?<parameters>[^%s%s]+)\\%s", closure.head, closure.head, closure.tail, closure.tail));
  }


  @NotNull
  @Override
  public String apply(@NotNull final String text, @Nullable final OfflinePlayer player, @NotNull final Function<String, @Nullable PlaceholderExpansion> lookup)
  {
    final Matcher matcher = pattern.matcher(text);
    if (!matcher.find())
    {
      return text;
    }

    final StringBuffer builder = new StringBuffer();

    do
    {
      final String identifier = matcher.group("identifier");
      final String parameters = matcher.group("parameters");

      final PlaceholderExpansion expansion = lookup.apply(identifier);
      if (expansion == null)
      {
        continue;
      }

      final String requested = expansion.onRequest(player, parameters);
      matcher.appendReplacement(builder, requested != null ? requested : matcher.group(0));
    }
    while (matcher.find());

    return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(builder).toString());
  }

}
