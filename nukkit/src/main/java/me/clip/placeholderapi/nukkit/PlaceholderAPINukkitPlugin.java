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
package me.clip.placeholderapi.nukkit;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import me.clip.placeholderapi.common.PlaceholderAPI;
import me.clip.placeholderapi.common.PlaceholderAPIPlugin;
import me.clip.placeholderapi.common.PlaceholderHook;
import me.clip.placeholderapi.common.config.PlaceholderAPIConfig;
import me.clip.placeholderapi.common.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.common.expansion.cloud.ExpansionCloudManager;
import me.clip.placeholderapi.common.util.PlatformUtil;
import me.clip.placeholderapi.common.util.TimeUtil;
import me.clip.placeholderapi.nukkit.commands.PlaceholderAPICommands;
import me.clip.placeholderapi.nukkit.event.ExpansionRegisterEvent;
import me.clip.placeholderapi.nukkit.event.ExpansionUnregisterEvent;
import me.clip.placeholderapi.nukkit.expansion.NukkitExpansionManager;
import me.clip.placeholderapi.nukkit.expansion.cloud.NukkitExpansionCloudManager;
import me.clip.placeholderapi.nukkit.util.logger.NukkitLogger;
import org.bstats.nukkit.Metrics;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PlaceholderAPINukkitPlugin extends PluginBase implements PlaceholderAPIPlugin {
    private static PlaceholderAPINukkitPlugin instance;
    private static SimpleDateFormat dateFormat;
    private static String booleanTrue, booleanFalse;
    private PlaceholderAPIConfig config;
    private NukkitExpansionManager expansionManager;
    private NukkitExpansionCloudManager expansionCloud;
    private Long startTime;

    @Override
    public PlatformUtil.Platform getPlatform() {
        return null;
    }

    @Override
    public NukkitLogger getMainLogger() {
        return new NukkitLogger(getLogger());
    }

    @Override
    public File getPluginDataFolder() {
        return getDataFolder();
    }

    public PlaceholderAPINukkitPlugin getInstance() {
        return instance;
    }

    @Override
    public YamlConfiguration getConfiguration() {
        return YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
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
    public void onLoad() {
        startTime = System.currentTimeMillis();
        instance = this;
        config = new PlaceholderAPIConfig(this);
        expansionManager = new NukkitExpansionManager(this);
    }

    @Override
    public void onEnable() {
        config.loadDefConfig();
        setupOptions();
        PluginCommand<PlaceholderAPINukkitPlugin> cs = (PluginCommand<PlaceholderAPINukkitPlugin>) getServer().getPluginCommand("placeholderapi");
        cs.setExecutor(new PlaceholderAPICommands(this));
        new PlaceholderListener(this);
        getServer().getScheduler().scheduleDelayedTask(this, () -> {
            getLogger().info("Placeholder expansion registration initializing...");
            final Map<String, PlaceholderHook> alreadyRegistered = PlaceholderAPI.getPlaceholders();
            getExpansionManager().registerAllExpansions();
            if (alreadyRegistered != null && !alreadyRegistered.isEmpty()) {
                alreadyRegistered.forEach(PlaceholderAPI::registerPlaceholderHook);
            }
        }, 1);
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
        Server.getInstance().getScheduler().cancelTask(this);
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

        s.sendMessage(TextFormat.colorize(PlaceholderAPI.getRegisteredIdentifiers().size() + " &aplaceholder hooks successfully registered!"));
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
            expansionCloud = new NukkitExpansionCloudManager(this);
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

    public PlaceholderAPIConfig getPlaceholderAPIConfig() {
        return config;
    }

    public NukkitExpansionManager getExpansionManager() {
        return expansionManager;
    }

    public NukkitExpansionCloudManager getExpansionCloud() {
        return expansionCloud;
    }

    public String getUptime() {
        return TimeUtil.getTime((int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime));
    }

    public long getUptimeMillis() {
        return (System.currentTimeMillis() - startTime);
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
}