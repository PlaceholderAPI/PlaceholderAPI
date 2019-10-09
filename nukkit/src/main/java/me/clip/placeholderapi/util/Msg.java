package me.clip.placeholderapi.util;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;

import java.util.Arrays;

public class Msg {
    public static void msg(CommandSender s, String... msg) {
        Arrays.stream(msg).map(Msg::color).forEach(s::sendMessage);
    }

    public static void broadcast(String... msg) {
        Arrays.stream(msg).map(Msg::color).forEach(Msg::broadcastMessage);
    }

    public static String color(String text) {
        return TextFormat.colorize(text);
    }

    private static int broadcastMessage(String message) {
        return Server.getInstance().broadcastMessage(message);
    }
}