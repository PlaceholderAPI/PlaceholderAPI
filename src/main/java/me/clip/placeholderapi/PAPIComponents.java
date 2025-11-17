package me.clip.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import me.clip.placeholderapi.replacer.ExactReplacer;
import me.clip.placeholderapi.replacer.Replacer;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.N;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class PAPIComponents {
    private static final Replacer EXACT_REPLACER = new ExactReplacer();

    @NotNull
    public static Component setPlaceholders(final OfflinePlayer player, @NotNull final Component component) {
        // TODO: explore a custom TextReplacementRenderer which doesn't use regex for performance benefits i.e. merge CharsReplacer with kyori TextReplacementRenderer
        return component.replaceText(config -> config.match(PlaceholderAPI.PLACEHOLDER_PATTERN).replacement((result, builder) ->
                builder.content(EXACT_REPLACER.apply(result.group(), player, PlaceholderAPIPlugin.getInstance().getLocalExpansionManager()::getExpansion))));
    }

    @NotNull
    public static List<Component> setPlaceholders(final OfflinePlayer player, @NotNull final List<Component> components) {
        return components.stream().map(component -> setPlaceholders(player, component)).collect(Collectors.toList());
    }

    @NotNull
    public static Component setPlaceholders(final Player player, @NotNull final Component component) {
        return setPlaceholders((OfflinePlayer) player, component);
    }

    @NotNull
    public static List<Component> setPlaceholders(final Player player, @NotNull final List<Component> components) {
        return setPlaceholders((OfflinePlayer) player, components);
    }

    @NotNull
    public static Component setBracketPlaceholders(final OfflinePlayer player, @NotNull final Component component) {
        return component.replaceText(config -> config.match(PlaceholderAPI.BRACKET_PLACEHOLDER_PATTERN).replacement((result, builder) ->
                builder.content(EXACT_REPLACER.apply(result.group(), player, PlaceholderAPIPlugin.getInstance().getLocalExpansionManager()::getExpansion))));
    }

    @NotNull
    public static List<Component> setBracketPlaceholders(final OfflinePlayer player, @NotNull final List<Component> components) {
        return components.stream().map(component -> setBracketPlaceholders(player, component)).collect(Collectors.toList());
    }

    @NotNull
    public static Component setBracketPlaceholders(final Player player, @NotNull final Component component) {
        return setBracketPlaceholders((OfflinePlayer) player, component);
    }

    @NotNull
    public static List<Component> setBracketPlaceholders(final Player player, @NotNull final List<Component> components) {
        return setBracketPlaceholders((OfflinePlayer) player, components);
    }

//  public static Component setRelationalPlaceholders(Player one, Player two, Component component) {
//    return component.replaceText(config -> config.match(PlaceholderAPI.RELATIONAL_PLACEHOLDER_PATTERN).replacement((result, builder) -> {
//
//      final String format = result.group(2);
//      final int index = format.indexOf("_");
//
//      if (index <= 0 || index >= format.length()) {
//        continue;
//      }
//
//      String identifier = format.substring(0, index).toLowerCase(Locale.ROOT);
//      String params = format.substring(index + 1);
//      final PlaceholderExpansion expansion = PlaceholderAPIPlugin.getInstance()
//              .getLocalExpansionManager().getExpansion(identifier);
//
//      if (!(expansion instanceof Relational)) {
//        continue;
//      }
//
//      final String value = ((Relational) expansion).onPlaceholderRequest(one, two, params);
//
//      if (value != null) {
//        text = text.replaceAll(Pattern.quote(matcher.group()), Matcher.quoteReplacement(value));
//      }
//
//
//    }));
//
//    final Matcher matcher = PlaceholderAPI.RELATIONAL_PLACEHOLDER_PATTERN.matcher(text);
//
//    while (matcher.find()) {
//      final String format = matcher.group(2);
//      final int index = format.indexOf("_");
//
//      if (index <= 0 || index >= format.length()) {
//        continue;
//      }
//
//      String identifier = format.substring(0, index).toLowerCase(Locale.ROOT);
//      String params = format.substring(index + 1);
//      final PlaceholderExpansion expansion = PlaceholderAPIPlugin.getInstance()
//              .getLocalExpansionManager().getExpansion(identifier);
//
//      if (!(expansion instanceof Relational)) {
//        continue;
//      }
//
//      final String value = ((Relational) expansion).onPlaceholderRequest(one, two, params);
//
//      if (value != null) {
//        text = text.replaceAll(Pattern.quote(matcher.group()), Matcher.quoteReplacement(value));
//      }
//    }
//
//    return text;
//  }
}
