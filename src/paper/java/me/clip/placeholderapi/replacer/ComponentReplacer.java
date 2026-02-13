package me.clip.placeholderapi.replacer;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ComponentReplacer {
    @NotNull
    public static Component replace(@NotNull final Component component, @NotNull final Function<String, String> replacer, @Nullable final Function<String, Component> serializer) {
        return rebuild(component, replacer, serializer);
    }

    @NotNull
    private static Component rebuild(@NotNull final Component component, @NotNull final Function<String, String> replacer, @Nullable final Function<String, Component> serializer) {
        Component rebuilt;

        if (component instanceof TextComponent) {
            final TextComponent text = (TextComponent) component;
            final String replaced = replacer.apply(text.content());

            rebuilt = serializer == null ? Component.text(replaced) : serializer.apply(replaced);
        } else if (component instanceof TranslatableComponent) {
            final TranslatableComponent translatable = (TranslatableComponent) component;
            final List<Component> arguments = new ArrayList<>();

            for (final ComponentLike arg : translatable.arguments()) {
                arguments.add(rebuild(arg.asComponent(), replacer, serializer));
            }

            rebuilt = Component.translatable(translatable.key(), arguments);
        } else if (component instanceof KeybindComponent) {
            final KeybindComponent keybind = (KeybindComponent) component;
            rebuilt = Component.keybind(keybind.keybind());
        } else if (component instanceof ScoreComponent) {
            final ScoreComponent score = (ScoreComponent) component;
            rebuilt = Component.score(score.name(), score.objective());
        } else if (component instanceof SelectorComponent) {
            final SelectorComponent selector = (SelectorComponent) component;
            rebuilt = Component.selector(selector.pattern());
        } else {
            rebuilt = Component.empty();
        }

        rebuilt = rebuilt.style(rebuildStyle(component.style(), replacer, serializer));

        if (!component.children().isEmpty()) {
            final List<Component> children = new ArrayList<>();
            for (Component child : component.children()) {
                children.add(rebuild(child, replacer, serializer));
            }
            rebuilt = rebuilt.children(children);
        }

        return rebuilt;
    }

    @NotNull
    private static Style rebuildStyle(@NotNull final Style style, @NotNull final Function<String, String> replacer, @Nullable final Function<String, Component> serializer) {
        final Style.Builder builder = style.toBuilder();
        final ClickEvent click = style.clickEvent();

        if (click != null) {
            builder.clickEvent(rebuildClickEvent(click, replacer));
        }

        final HoverEvent<?> hover = style.hoverEvent();

        if (hover != null) {
            builder.hoverEvent(rebuildHoverEvent(hover, replacer, serializer));
        }

        return builder.build();
    }

    @NotNull
    private static ClickEvent rebuildClickEvent(@NotNull final ClickEvent click, @NotNull final Function<String, String> replacer) {
        final ClickEvent.Payload payload = click.payload();

        if (!(payload instanceof ClickEvent.Payload.Text)) {
            return click;
        }

        final String original = ((ClickEvent.Payload.Text) payload).value();
        final String replaced = replacer.apply(original);

        final ClickEvent.Action action = click.action();

        switch (action) {
            case OPEN_URL:
                return ClickEvent.openUrl(replaced);
            case OPEN_FILE:
                return ClickEvent.openFile(replaced);
            case RUN_COMMAND:
                return ClickEvent.runCommand(replaced);
            case SUGGEST_COMMAND:
                return ClickEvent.suggestCommand(replaced);
            case COPY_TO_CLIPBOARD:
                return ClickEvent.copyToClipboard(replaced);
            default:
                return click;
        }
    }

    @NotNull
    private static HoverEvent<?> rebuildHoverEvent(@NotNull final HoverEvent<?> hover, @NotNull final Function<String, String> replacer, @Nullable final Function<String, Component> serializer) {
        final Object value = hover.value();

        if (value instanceof Component) {
            final Component rebuilt = rebuild((Component) value, replacer, serializer);
            return HoverEvent.showText(rebuilt);
        }

        if (value instanceof HoverEvent.ShowItem) {
            return rebuildShowItem((HoverEvent.ShowItem) value, replacer);
        }

        if (value instanceof HoverEvent.ShowEntity) {
            final HoverEvent.ShowEntity entity = (HoverEvent.ShowEntity) value;

            Component rebuiltName = null;
            if (entity.name() != null) {
                rebuiltName = rebuild(entity.name(), replacer, serializer);
            }

            return HoverEvent.showEntity(entity.type(), entity.id(), rebuiltName);
        }

        return hover;
    }

    @NotNull
    private static HoverEvent<?> rebuildShowItem(@NotNull final HoverEvent.ShowItem item, @NotNull final Function<String, String> replacer) {
        final BinaryTagHolder nbt = item.nbt();

        if (nbt != null && !nbt.string().isEmpty()) {
            final String replaced = replacer.apply(nbt.string());

            return HoverEvent.showItem(item.item(), item.count(), BinaryTagHolder.binaryTagHolder(replaced));
        }

        //I'm not 100% sure this is how we're meant to support data components but let's give it a go and see if it causes any issues :)
        final Map<Key, DataComponentValue> components = item.dataComponents();

        if (!components.isEmpty()) {
            final Map<Key, DataComponentValue> rebuilt = new HashMap<>();

            for (final Map.Entry<Key, DataComponentValue> entry : components.entrySet()) {
                final DataComponentValue value = entry.getValue();

                if (!(value instanceof BinaryTagHolder)) {
                    rebuilt.put(entry.getKey(), value);
                    continue;
                }

                rebuilt.put(entry.getKey(), BinaryTagHolder.binaryTagHolder(replacer.apply(((BinaryTagHolder) value).string())));
            }

            return HoverEvent.showItem(item.item(), item.count(), rebuilt);
        }

        return HoverEvent.showItem(item);
    }
}