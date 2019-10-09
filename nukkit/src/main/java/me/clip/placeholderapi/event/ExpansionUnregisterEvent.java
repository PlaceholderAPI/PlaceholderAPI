package me.clip.placeholderapi.event;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class ExpansionUnregisterEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private PlaceholderExpansion expansion;

    public ExpansionUnregisterEvent(PlaceholderExpansion expansion) {
        this.expansion = expansion;
    }

    public static HandlerList getHandlers() {
        return HANDLERS;
    }

    public PlaceholderExpansion getExpansion() {
        return expansion;
    }
}