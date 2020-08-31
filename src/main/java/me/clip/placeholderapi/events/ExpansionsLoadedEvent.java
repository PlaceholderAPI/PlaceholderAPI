package me.clip.placeholderapi.events;


import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is ran when all expansions are loaded.
 * This is done when reloading config, on plugin start and on server load.
 * @author jacobbordas (@ignissak)
 */
public class ExpansionsLoadedEvent extends Event {

    @NotNull
    private static final HandlerList HANDLERS = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
