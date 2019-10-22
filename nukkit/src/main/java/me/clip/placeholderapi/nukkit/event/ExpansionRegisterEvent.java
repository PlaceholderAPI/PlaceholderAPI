package me.clip.placeholderapi.nukkit.event;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import me.clip.placeholderapi.common.expansion.PlaceholderExpansion;

public class ExpansionRegisterEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private PlaceholderExpansion expansion;
    private boolean cancelled;

    public ExpansionRegisterEvent(PlaceholderExpansion expansion) {
        this.expansion = expansion;
    }

    public static HandlerList getHandlers() {
        return HANDLERS;
    }

    public PlaceholderExpansion getExpansion() {
        return expansion;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}