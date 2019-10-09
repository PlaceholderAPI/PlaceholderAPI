package me.clip.placeholderapi.expansion;

import cn.nukkit.IPlayer;

public interface Relational {
    String onPlaceholderRequest(IPlayer one, IPlayer two, String identifier);
}