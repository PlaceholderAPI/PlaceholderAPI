package me.clip.placeholderapi;

import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.plugin.PluginDisableEvent;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.event.ExpansionUnregisterEvent;
import me.clip.placeholderapi.expansion.Cacheable;
import me.clip.placeholderapi.expansion.Cleanable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Taskable;

import java.util.Map;
import java.util.Set;

public class PlaceholderListener implements Listener {
    private PlaceholderAPIPlugin plugin;

    public PlaceholderListener(PlaceholderAPIPlugin instance) {
        plugin = instance;
        Server.getInstance().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onExpansionUnregister(ExpansionUnregisterEvent e) {
        if (e.getExpansion() instanceof Listener) {
            HandlerList.unregisterAll((Listener) e.getExpansion());
        }

        if (e.getExpansion() instanceof Taskable) {
            ((Taskable) e.getExpansion()).stop();
        }

        if (e.getExpansion() instanceof Cacheable) {
            ((Cacheable) e.getExpansion()).clear();
        }

        if (plugin.getExpansionCloud() != null) {
            CloudExpansion ex = plugin.getExpansionCloud().getCloudExpansion(e.getExpansion().getName());

            if (ex != null) {
                ex.setHasExpansion(false);
                ex.setShouldUpdate(false);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPluginUnload(PluginDisableEvent e) {
        String n = e.getPlugin().getName();

        if (n == null) {
            return;
        }

        if (n.equals(plugin.getName())) {
            return;
        }

        Map<String, PlaceholderHook> hooks = PlaceholderAPI.getPlaceholders();

        for (Map.Entry<String, PlaceholderHook> hook : hooks.entrySet()) {
            PlaceholderHook i = hook.getValue();

            if (i instanceof PlaceholderExpansion) {
                PlaceholderExpansion ex = (PlaceholderExpansion) i;

                if (ex.getRequiredPlugin() == null) {
                    continue;
                }

                if (ex.getRequiredPlugin().equalsIgnoreCase(n)) {
                    if (PlaceholderAPI.unregisterExpansion(ex)) {
                        plugin.getLogger().info("Unregistered placeholder expansion: " + ex.getIdentifier());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Set<PlaceholderExpansion> expansions = PlaceholderAPI.getExpansions();

        if (expansions.isEmpty()) {
            return;
        }

        for (PlaceholderExpansion ex : expansions) {
            if (ex instanceof Cleanable) {
                ((Cleanable) ex).cleanup(e.getPlayer());
            }
        }
    }
}