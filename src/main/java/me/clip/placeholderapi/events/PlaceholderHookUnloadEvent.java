package me.clip.placeholderapi.events;

import me.clip.placeholderapi.PlaceholderHook;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlaceholderHookUnloadEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private String plugin;
	private PlaceholderHook hook;

	public PlaceholderHookUnloadEvent(String plugin, PlaceholderHook placeholderHook) {
		this.plugin = plugin;
		this.hook = placeholderHook;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public String getHookName() {
		return plugin;
	}
	
	public PlaceholderHook getHook() {
		return hook;
	}
}
