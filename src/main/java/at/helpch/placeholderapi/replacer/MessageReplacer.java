package at.helpch.placeholderapi.replacer;

import at.helpch.placeholderapi.PlaceholderAPI;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class MessageReplacer {
    @NotNull
    public static Message replace(@NotNull final Message original, @NotNull final Function<String, String> setPlaceholders) {
        if (original.getFormattedMessage().rawText == null) {
            return original;
        }

        String replaced = setPlaceholders.apply(original.getFormattedMessage().rawText);
        String link = original.getFormattedMessage().link == null ? null : setPlaceholders.apply(original.getFormattedMessage().link);

        List<Message> newChildren = original.getChildren().stream()
                .filter(Objects::nonNull)
                .map(child -> replace(child, setPlaceholders))
                .toList();

        Message message = Message.raw(replaced);

        if (original.getColor() != null) {
            message = message.color(original.getColor());
        }

        if (link != null) {
            message = message.link(link);
        }

        int bold = original.getFormattedMessage().bold.getValue();
        if (bold != 0) {
            message = message.bold(bold != 1);
        }

        int italic = original.getFormattedMessage().italic.getValue();
        if (italic != 0) {
            message = message.italic(italic != 1);
        }

        return message.insertAll(newChildren);
    }
}
