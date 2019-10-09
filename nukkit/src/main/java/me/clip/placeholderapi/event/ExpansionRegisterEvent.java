package me.clip.placeholderapi.event;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class ExpansionRegisterEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private PlaceholderExpansion expansion;
    private boolean isCancelled;

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
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}