package me.clip.placeholderapi.events;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import me.clip.placeholderapi.replacer.Replacer;


/**
 * Fired when user requests for placeholder replacement via {@link Replacer}
 * */
public class PlaceholderRequestEvent extends Event
{

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final String input;
    private final OfflinePlayer player;
    private final Replacer defaultReplacer;
    private String output;

    public PlaceholderRequestEvent(String input, OfflinePlayer player, Replacer defaultReplacer) {
        this.input = input;
        this.player = player;
        this.defaultReplacer = defaultReplacer;
    }

    /**
     * @return Input string of the request with no replacements
     * */
    public @NotNull String getInput() {
        return input;
    }

    /**
     * @return Player that was used for replacement
     */
    public @Nullable OfflinePlayer getPlayer() {
        return player;
    }

    /**
     * Returns output that will be provided for this request.
     * <br>If the output is equal to null, replacer will be used to provide final result
     * @return Output that will be provided for this request
     */
    public @Nullable String getOutput() {
        return output;
    }

    /**
     * Sets output that will be provided for this request
     * <br>If the output is equal to null, replacer will be used to provide final result
     * @param output Output that will be provided for this request
     */
    public void setOutput(@Nullable String output) {
        this.output = output;
    }

    /**
     * @return {@link Replacer} that will be used to provide final result if {@link PlaceholderRequestEvent#getOutput()} is null
     */
    public Replacer getDefaultReplacer() {
        return defaultReplacer;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
