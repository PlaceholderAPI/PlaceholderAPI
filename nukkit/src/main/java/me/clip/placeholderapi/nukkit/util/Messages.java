package me.clip.placeholderapi.nukkit.util;

import cn.nukkit.Server;
import me.clip.placeholderapi.common.util.Msg;

import java.util.Arrays;

public class Messages {
    public void broadcast(String... message) {
        Arrays.stream(message).map(Msg::color).forEach(Messages::broadcast);
    }

    public static Integer broadcast(String message) {
        return Server.getInstance().broadcastMessage(message);
    }
}
