package me.clip.placeholderapi.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.clip.placeholderapi.PAPIComponents;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.OfflinePlayer;

public class TestCommand implements BasicCommand {
    private static final MiniMessage MINI = MiniMessage.miniMessage();

    @Override
    public void execute(final CommandSourceStack commandSourceStack, final String[] strings) {
//        final Component component = Component.text("Woo! Test: %player_name%").color(TextColor.color(50, 168, 82)).hoverEvent(HoverEvent.showText(Component.text("OMG %player_gamemode%")));
        final Component component = Component.text("Woo! Test: %player_name%");

        String ser = MINI.serialize(component);
        System.out.println(ser);

        commandSourceStack.getSender().sendMessage(
                PAPIComponents.setPlaceholders((OfflinePlayer) commandSourceStack.getSender(), component)
        );

        long tmp = System.currentTimeMillis();
        for (int i = 0; i < 100000; ++i) {
            PAPIComponents.setPlaceholders((OfflinePlayer) commandSourceStack.getSender(), component);
        }
        commandSourceStack.getSender().sendMessage(String.valueOf(System.currentTimeMillis() - tmp));

        tmp = System.currentTimeMillis();
        for (int i = 0; i < 100000; ++i) {
            PlaceholderAPI.setPlaceholders((OfflinePlayer) commandSourceStack.getSender(), "Woo! Test: %player_name%");
        }
        commandSourceStack.getSender().sendMessage(String.valueOf(System.currentTimeMillis() - tmp));

        tmp = System.currentTimeMillis();
        for (int i = 0; i < 100000; ++i) {
            final String serr = MINI.serialize(component);
            final String repl = PlaceholderAPI.setPlaceholders((OfflinePlayer) commandSourceStack.getSender(), serr);
            MINI.deserialize(repl);
        }
        commandSourceStack.getSender().sendMessage(String.valueOf(System.currentTimeMillis() - tmp));


        Component.text()
                .append(Component.text().content("yes ").color(TextColor.color(50,50,50)))
                .append(Component.text("%player_name%"))
                .append(Component.text(" omg").color(TextColor.color(200,200,200)));


    }
}
