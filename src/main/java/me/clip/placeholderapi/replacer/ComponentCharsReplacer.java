/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.clip.placeholderapi.replacer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A renderer performing a replacement on every {@link TextComponent} element of a component tree.
 */
final class ComponentCharsReplacer implements ComponentRenderer<ComponentCharsReplacer.State> {
    //static final TextReplacementRenderer INSTANCE = new TextReplacementRenderer();
    private final OfflinePlayer player;
    private final Function<String, @Nullable PlaceholderExpansion> lookup;
    private static final Closure CLOSURE = Closure.PERCENT;

    enum Closure {
        BRACKET('{', '}'),
        PERCENT('%', '%');


        public final char head, tail;

        Closure(final char head, final char tail) {
            this.head = head;
            this.tail = tail;
        }
    }

    public ComponentCharsReplacer(@Nullable final OfflinePlayer player,
                                  @NotNull final Function<String, @Nullable PlaceholderExpansion> lookup) {
        this.player = player;
        this.lookup = lookup;
    }

    @Override
    public Component render(final Component component, final State state) {
        if (!state.running) return component;
        final boolean prevFirstMatch = state.firstMatch;
        state.firstMatch = true;

        final List<Component> oldChildren = component.children();
        final int oldChildrenSize = oldChildren.size();
        Style oldStyle = component.style();
        List<Component> children = null;
        Component modified = component;
        // replace the component itself
        if (component instanceof TextComponent) {
            TextComponent tc = (TextComponent) component;
            final String content = tc.content();

            final char[] chars = content.toCharArray();
            final StringBuilder identifier = new StringBuilder();
            final StringBuilder parameters = new StringBuilder();

            final Map<List<Integer>, Component> replacements = new HashMap<>();

            for (int i = 0; i < chars.length; i++) {
                final char l = chars[i];

                if (l != CLOSURE.head || i + 1 >= chars.length) {
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

                    if (p == CLOSURE.tail) {
                        invalid = false;
                        break;
                    }

                    if (p == '_' && !identified) {
                        identified = true;
                        break;
                    }

                    if (identified) {
                        parameters.append(p);
                    } else {
                        identifier.append(p);
                    }
                }

                final String identifierString = identifier.toString();
                final String lowerIdentifiedString = identifierString.toLowerCase();
                final String parametersString = parameters.toString();

                identifier.setLength(0);
                parameters.setLength(0);

                if (invalid) {
                    continue;
                }

                replacements.put(Lists.newArrayList(i, i + identifierString.length() + parametersString.length()), lookup.apply(lowerIdentifiedString).onPlaceholderComponentRequest(player, parametersString));


            }


            // do something with our replacements

            


            final Matcher matcher = state.pattern.matcher(content);
            int replacedUntil = 0; // last index handled
            while (matcher.find()) {
                final PatternReplacementResult result = state.continuer.shouldReplace(matcher, ++state.matchCount, state.replaceCount);

                if (matcher.start() == 0) {
                    // if we're a full match, modify the component directly
                    if (matcher.end() == content.length()) {
                        final ComponentLike replacement = state.replacement.apply(matcher, Component.text().content(matcher.group())
                                .style(component.style()));

                        modified = replacement == null ? Component.empty() : replacement.asComponent();

                        if (modified.style().hoverEvent() != null) {
                            oldStyle = oldStyle.hoverEvent(null); // Remove original hover if it has been replaced completely
                        }

                        // merge style of the match into this component to prevent unexpected loss of style
                        modified = modified.style(modified.style().merge(component.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET));

                        if (children == null) { // Prepare children
                            children = new ArrayList<>(oldChildrenSize + modified.children().size());
                            children.addAll(modified.children());
                        }
                    } else {
                        // otherwise, work on a child of the root node
                        modified = Component.text("", component.style());
                        final ComponentLike child = state.replacement.apply(matcher, Component.text().content(matcher.group()));
                        if (child != null) {
                            if (children == null) {
                                children = new ArrayList<>(oldChildrenSize + 1);
                            }
                            children.add(child.asComponent());
                        }
                    }
                } else {
                    if (children == null) {
                        children = new ArrayList<>(oldChildrenSize + 2);
                    }
                    if (state.firstMatch) {
                        // truncate parent to content before match
                        modified = ((TextComponent) component).content(content.substring(0, matcher.start()));
                    } else if (replacedUntil < matcher.start()) {
                        children.add(Component.text(content.substring(replacedUntil, matcher.start())));
                    }
                    final ComponentLike builder = state.replacement.apply(matcher, Component.text().content(matcher.group()));
                    if (builder != null) {
                        children.add(builder.asComponent());
                    }
                }
                state.replaceCount++;
                state.firstMatch = false;
                replacedUntil = matcher.end();
            }
            if (replacedUntil < content.length()) {
                // append trailing content
                if (replacedUntil > 0) {
                    if (children == null) {
                        children = new ArrayList<>(oldChildrenSize);
                    }
                    children.add(Component.text(content.substring(replacedUntil)));
                }
                // otherwise, we haven't modified the component, so nothing to change
            }
        } else if (modified instanceof TranslatableComponent) { // get TranslatableComponent with() args
            final List<TranslationArgument> args = ((TranslatableComponent) modified).arguments();
            List<TranslationArgument> newArgs = null;
            for (int i = 0, size = args.size(); i < size; i++) {
                final TranslationArgument original = args.get(i);
                final TranslationArgument replaced = original.value() instanceof Component ? TranslationArgument.component(this.render((Component) original.value(), state)) : original;
                if (replaced != original) {
                    if (newArgs == null) {
                        newArgs = new ArrayList<>(size);
                        if (i > 0) {
                            newArgs.addAll(args.subList(0, i));
                        }
                    }
                }
                if (newArgs != null) {
                    newArgs.add(replaced);
                }
            }
            if (newArgs != null) {
                modified = ((TranslatableComponent) modified).arguments(newArgs);
            }
        }
        // Only visit children if we're running
        if (state.running) {
            // hover event
            if (state.replaceInsideHoverEvents) {
                final HoverEvent<?> event = oldStyle.hoverEvent();
                if (event != null) {
                    final HoverEvent<?> rendered = event.withRenderedValue(this, state);
                    if (event != rendered) {
                        modified = modified.style(s -> s.hoverEvent(rendered));
                    }
                }
            }
            // Children
            boolean first = true;
            for (int i = 0; i < oldChildrenSize; i++) {
                final Component child = oldChildren.get(i);
                final Component replaced = this.render(child, state);
                if (replaced != child) {
                    if (children == null) {
                        children = new ArrayList<>(oldChildrenSize);
                    }
                    if (first) {
                        children.addAll(oldChildren.subList(0, i));
                    }
                    first = false;
                }
                if (children != null) {
                    children.add(replaced);
                    first = false;
                }
            }
        } else {
            // we're not visiting children, re-add original children if necessary
            if (children != null) {
                children.addAll(oldChildren);
            }
        }

        state.firstMatch = prevFirstMatch;
        // Update the modified component with new children
        if (children != null) {
            return modified.children(children);
        }
        return modified;
    }

    static final class State {
        final Pattern pattern;
        final BiFunction<MatchResult, TextComponent.Builder, @Nullable ComponentLike> replacement;
        final TextReplacementConfig.Condition continuer;
        final boolean replaceInsideHoverEvents;
        boolean running = true;
        int matchCount = 0;
        int replaceCount = 0;
        boolean firstMatch = true;

        State(final Pattern pattern, final BiFunction<MatchResult, TextComponent.Builder, @Nullable ComponentLike> replacement, final TextReplacementConfig.Condition continuer, final boolean replaceInsideHoverEvents) {
            this.pattern = pattern;
            this.replacement = replacement;
            this.continuer = continuer;
            this.replaceInsideHoverEvents = replaceInsideHoverEvents;
        }
    }
}