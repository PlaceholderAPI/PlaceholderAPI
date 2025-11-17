package me.clip.placeholderapi.replacer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.format.Style;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public final class ComponentReplacer {
    public Component replace(Component component, OfflinePlayer player, Function<String, PlaceholderExpansion> function) {
        Component modified = component;

        final List<Component> oldChildren = component.children();
        final int oldChildrenSize = oldChildren.size();
        List<Component> children = null;

        if (component instanceof TextComponent) {
            TextComponent tc = (TextComponent) component;
            final String content = tc.content();

            final char[] chars = content.toCharArray();

            final StringBuilder identifier = new StringBuilder();
            final StringBuilder parameters = new StringBuilder();

            for (int i = 0; i < chars.length; i++) {
                final char l = chars[i];

                if (l != '%' || i + 1 >= chars.length) {
                    continue;
                }

                final int start = i;

                boolean identified = false;
                boolean invalid = true;

                while (++i < chars.length) {
                    final char p = chars[i];

                    if (p == ' ' && !identified) {
                        break;
                    }

                    if (p == '%') {
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
                    continue;
                }

                final PlaceholderExpansion expansion = function.apply(lowercaseIdentifierString);

                if (expansion == null) {
                    continue;
                }

                final String placeholderValue = expansion.onRequest(player, parametersString);

                if (placeholderValue == null) {
                    continue;
                }

                if (start == 0) {
                    // if we're a full match, modify the component directly
                    if (i == content.length() - 1) {
                        final ComponentLike replacement = Component.text(placeholderValue).style(component.style());

                        modified = replacement.asComponent();
                        Style modStyle = modified.style();

                        if (modStyle.hoverEvent() != null) {
                            Object hoverValue = modStyle.hoverEvent().value();

                            if (hoverValue instanceof Component) {
                                final Component replacedHoverComponent = replace((Component) hoverValue, player, function);

                                if (replacedHoverComponent != hoverValue) {
                                    modified.style();
                                }
                            }
                        }

                        if (modStyle.clickEvent() != null) {
                            String clickValue =
                        }
                    }
                }
            }

        } else if (component instanceof TranslatableComponent) {
            TranslatableComponent tc = (TranslatableComponent) component;
            final List<TranslationArgument> args = tc.arguments();
            List<TranslationArgument> newArgs = null;
            for (int i = 0, size = args.size(); i < size; i++) {
                final TranslationArgument original = args.get(i);
                TranslationArgument replacement = original instanceof Component ? TranslationArgument.component(replace((Component) original, player, function)) : original;

                if (original != replacement) {
                    if (newArgs == null) {
                        newArgs = new ArrayList<>(size);

                        if (i > 0) {
                            newArgs.addAll(args.subList(0, i));
                        }
                    }
                }

                if (newArgs != null) {
                    newArgs.add(replacement);
                }
            }

            if (newArgs != null) {
                modified = ((TranslatableComponent) modified).arguments(newArgs);
            }
        }

        return modified;
    }
}
