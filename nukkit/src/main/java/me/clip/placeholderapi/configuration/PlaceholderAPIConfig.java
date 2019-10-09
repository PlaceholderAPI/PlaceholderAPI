package me.clip.placeholderapi.configuration;

import me.clip.placeholderapi.PlaceholderAPIPlugin;

public class PlaceholderAPIConfig {
    private PlaceholderAPIPlugin plugin;

    public PlaceholderAPIConfig(PlaceholderAPIPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadDefConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
    }

    public boolean checkUpdates() {
        return plugin.getConfig().getBoolean("check_updates");
    }

    public Boolean cloudAllowUnverifiedExpansions() {
        return plugin.getConfig().getBoolean("cloud_allow_unverified_expansions");
    }

    public boolean isCloudEnabled() {
        return plugin.getConfig().getBoolean("cloud_enabled");
    }

    public void setCloudEnabled(boolean b) {
        plugin.getConfig().set("cloud_enabled", b);
        plugin.reloadConfig();
    }

    public String booleanTrue() {
        return plugin.getConfig().getString("boolean.true");
    }

    public String booleanFalse() {
        return plugin.getConfig().getString("boolean.false");
    }

    public String dateFormat() {
        return plugin.getConfig().getString("date_format");
    }
}
