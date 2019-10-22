/*
 * PlaceholderAPI
 * Copyright (C) 2019 Ryan McCarthy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.clip.placeholderapi.bukkit;

import me.clip.placeholderapi.bukkit.commands.PlaceholderAPICommands;
import me.clip.placeholderapi.bukkit.events.ExpansionRegisterEvent;
import me.clip.placeholderapi.bukkit.events.ExpansionUnregisterEvent;
import me.clip.placeholderapi.bukkit.expansion.BukkitExpansionManager;
import me.clip.placeholderapi.bukkit.expansion.cloud.BukkitExpansionCloudManager;
import me.clip.placeholderapi.bukkit.updater.UpdateChecker;
import me.clip.placeholderapi.common.PlaceholderAPI;
import me.clip.placeholderapi.common.PlaceholderAPIPlugin;
import me.clip.placeholderapi.common.PlaceholderHook;
import me.clip.placeholderapi.common.config.PlaceholderAPIConfig;
import me.clip.placeholderapi.common.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.common.util.PlatformUtil;
import me.clip.placeholderapi.common.util.TimeUtil;
import me.clip.placeholderapi.common.util.logging.LoggerBase;
import me.clip.placeholderapi.common.util.logging.PlaceholderAPILogger;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PlaceholderAPIBukkitPlugin extends JavaPlugin implements PlaceholderAPIPlugin {
    private static PlaceholderAPIBukkitPlugin instance;
    private static SimpleDateFormat dateFormat;
    private static String booleanTrue, booleanFalse, platform;
    private PlaceholderAPIConfig config;
    private BukkitExpansionManager expansionManager;
    private BukkitExpansionCloudManager expansionCloud;
    private Long startTime;

    private static String getVersion() {
        String platform = "Bukkit";
        String platformVersion = "unknown";
        boolean spigot = false;

        try {
            platformVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        if (PlatformUtil.isSpigotCompat()) {
            spigot = true;
            platform = "Spigot/Paper";
        } else {
            return "Bukkit";
        }

        return platform + " v" + platformVersion + ". Spigot: " + spigot;
    }

    public static PlaceholderAPIBukkitPlugin getClassInstance() {
        return instance;
    }

    public static SimpleDateFormat getDateFormat() {
        return dateFormat != null ? dateFormat : new SimpleDateFormat("MM/dd/yy HH:mm:ss");
    }

    public static String booleanTrue() {
        return booleanTrue != null ? booleanTrue : "true";
    }

    public static String booleanFalse() {
        return booleanFalse != null ? booleanFalse : "false";
    }

    @Override
    public PlatformUtil.Platform getPlatform() {
        return PlatformUtil.Platform.BUKKIT;
    }

    @Override
    public LoggerBase getMainLogger() {
        return new PlaceholderAPILogger(getLogger());
    }

    @Override
    public File getPluginDataFolder() {
        return getDataFolder();
    }

    @Override
    public PlaceholderAPIBukkitPlugin getInstance() {
        return instance;
    }

    @Override
    public boolean registerExpansion(PlaceholderExpansion expansion) {
        ExpansionRegisterEvent ev = new ExpansionRegisterEvent(expansion);
        getServer().getPluginManager().callEvent(ev);
        if (ev.isCancelled()) {
            return false;
        }

        return PlaceholderAPI.registerPlaceholderHook(expansion.getIdentifier(), expansion);
    }

    @Override
    public Boolean unregisterExpansion(PlaceholderExpansion expansion) {
        if (PlaceholderAPI.unregisterPlaceholderHook(expansion.getIdentifier())) {
            getServer().getPluginManager().callEvent(new ExpansionUnregisterEvent(expansion));
            return true;
        }

        return false;
    }

    @Override
    public YamlConfiguration getConfiguration() {
        return YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
    }

    @Override
    public void onLoad() {
        startTime = System.currentTimeMillis();
        instance = this;
        config = new PlaceholderAPIConfig(this);
        expansionManager = new BukkitExpansionManager(this);
    }

    @Override
    public void onEnable() {
        config.loadDefConfig();
        setupOptions();

        getCommand("placeholderapi").setExecutor(new PlaceholderAPICommands(this));
        new PlaceholderListener(this);

        try {
            if (PlatformUtil.isServerLoadAvailable()) {
                new ServerLoadEventListener(this);
            } else {
                Bukkit.getScheduler().runTaskLater(this, () -> {
                    getLogger().info("Placeholder expansion registration initializing...");

                    // Fetch any hooks that may have registered externally onEnable first otherwise they will be lost
                    final Map<String, PlaceholderHook> alreadyRegistered = PlaceholderAPI.getPlaceholders();
                    getExpansionManager().registerAllExpansions();

                    if (alreadyRegistered != null && !alreadyRegistered.isEmpty()) {
                        alreadyRegistered.forEach(PlaceholderAPI::registerPlaceholderHook);
                    }
                }, 1);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (config.checkUpdates()) {
            new UpdateChecker(this).fetch();
        }

        if (config.isCloudEnabled()) {
            enableCloud();
        }

        setupMetrics();
    }

    @Override
    public void onDisable() {
        disableCloud();
        PlaceholderAPI.unregisterAll();
        expansionManager = null;
        Bukkit.getScheduler().cancelTasks(this);
        instance = null;
    }

    public void reloadConf(CommandSender s) {
        boolean cloudEnabled = this.expansionCloud != null;
        PlaceholderAPI.unregisterAllProvidedExpansions();
        reloadConfig();
        setupOptions();
        expansionManager.registerAllExpansions();

        if (!config.isCloudEnabled()) {
            disableCloud();
        } else if (!cloudEnabled) {
            enableCloud();
        }

        s.sendMessage(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.getRegisteredIdentifiers().size() + " &aplaceholder hooks successfully registered!"));
    }

    private void setupOptions() {
        booleanTrue = config.booleanTrue();
        if (booleanTrue == null) {
            booleanTrue = "true";
        }

        booleanFalse = config.booleanFalse();
        if (booleanFalse == null) {
            booleanFalse = "false";
        }

        try {
            dateFormat = new SimpleDateFormat(config.dateFormat());
        } catch (Exception e) {
            dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        }
    }

    private void setupMetrics() {
        Metrics m = new Metrics(this);
        m.addCustomChart(new Metrics.SimplePie("using_expansion_cloud", () -> getExpansionCloud() != null ? "yes" : "no"));
        m.addCustomChart(new Metrics.SimplePie("using_spigot", () -> PlatformUtil.isSpigotCompat() ? "yes" : "no"));
        m.addCustomChart(new Metrics.AdvancedPie("expansions_used", () -> {
            Map<String, Integer> map = new HashMap<>();
            Map<String, PlaceholderHook> p = PlaceholderAPI.getPlaceholders();

            if (!p.isEmpty()) {
                for (PlaceholderHook hook : p.values()) {
                    if (hook instanceof PlaceholderExpansion) {
                        PlaceholderExpansion ex = (PlaceholderExpansion) hook;
                        map.put(ex.getRequiredPlugin() == null ? ex.getIdentifier() : ex.getRequiredPlugin(), 1);
                    }
                }
            }

            return map;
        }));
    }

    public void enableCloud() {
        if (expansionCloud == null) {
            expansionCloud = new BukkitExpansionCloudManager(this);
        } else {
            expansionCloud.clean();
        }

        expansionCloud.fetch(config.cloudAllowUnverifiedExpansions());
    }

    public void disableCloud() {
        if (expansionCloud != null) {
            expansionCloud.clean();
            expansionCloud = null;
        }
    }

    /**
     * Obtain the configuration class for PlaceholderAPI.
     *
     * @return PlaceholderAPIConfig instance
     */
    public PlaceholderAPIConfig getPlaceholderAPIConfig() {
        return config;
    }

    public BukkitExpansionManager getExpansionManager() {
        return expansionManager;
    }

    public BukkitExpansionCloudManager getExpansionCloud() {
        return expansionCloud;
    }

    public String getUptime() {
        return TimeUtil.getTime((int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime));
    }

    public long getUptimeMillis() {
        return (System.currentTimeMillis() - startTime);
    }
}