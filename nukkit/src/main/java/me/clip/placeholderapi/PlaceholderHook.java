package me.clip.placeholderapi;

import cn.nukkit.IPlayer;
import cn.nukkit.OfflinePlayer;

public abstract class PlaceholderHook {
    public String onRequest(OfflinePlayer p, String params) {
        if (p != null && p.isOnline()) {
            return onPlaceholderRequest(p, params);
        }

        return onPlaceholderRequest(null, params);
    }

    public String onPlaceholderRequest(IPlayer p, String params) {
        return null;
    }
}