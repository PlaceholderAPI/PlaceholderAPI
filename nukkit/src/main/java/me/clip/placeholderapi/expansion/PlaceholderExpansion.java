package me.clip.placeholderapi.expansion;

import cn.nukkit.Server;
import cn.nukkit.utils.ConfigSection;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.PlaceholderHook;
import org.apache.commons.lang3.Validate;

import java.util.List;

public abstract class PlaceholderExpansion extends PlaceholderHook {
    public String getName() {
        return getIdentifier();
    }

    public abstract String getIdentifier();

    public abstract String getAuthor();

    public abstract String getVersion();

    public String getRequiredPlugin() {
        return null; // Not using deprecated methods
    }

    public List<String> getPlaceholders() {
        return null;
    }

    public Boolean persist() {
        return false;
    }

    public Boolean isRegistered() {
        Validate.notNull(getIdentifier(), "Placeholder Identifier cannot be null!");
        return PlaceholderAPI.isRegistered(getIdentifier());
    }

    public Boolean canRegister() {
        return getRequiredPlugin() == null || Server.getInstance().getPluginManager().getPlugin(getRequiredPlugin()) != null;
    }

    public Boolean register() {
        Validate.notNull(getIdentifier(), "Placeholder Identifier cannot be null!");
        return PlaceholderAPI.registerExpansion(this);
    }

    public PlaceholderAPIPlugin getPlaceholderAPI() {
        return PlaceholderAPIPlugin.getInstance();
    }

    public String getString(String path, String def) {
        return getPlaceholderAPI().getConfig()
                .getString("expansions." + getIdentifier() + "." + path, def);
    }

    public int getInt(String path, int def) {
        return getPlaceholderAPI().getConfig().getInt("expansions." + getIdentifier() + "." + path, def);
    }

    public long getLong(String path, long def) {
        return getPlaceholderAPI().getConfig().getLong("expansions." + getIdentifier() + "." + path, def);
    }

    public double getDouble(String path, double def) {
        return getPlaceholderAPI().getConfig().getDouble("expansions." + getIdentifier() + "." + path, def);
    }

    public List<String> getStringList(String path) {
        return getPlaceholderAPI().getConfig().getStringList("expansions." + getIdentifier() + "." + path);
    }

    public Object get(String path, Object def) {
        return getPlaceholderAPI().getConfig().get("expansions." + getIdentifier() + "." + path, def);
    }

    public ConfigSection getConfigSection(String path) {
        return getPlaceholderAPI().getConfig().getSection("expansions." + getIdentifier() + "." + path);
    }

    public ConfigSection getConfigSection() {
        return getPlaceholderAPI().getConfig().getSection("expansions." + getIdentifier());
    }

    public Boolean configurationContains(String path) {
        return getPlaceholderAPI().getConfig().exists("expansions." + getIdentifier() + "." + path);
    }
}