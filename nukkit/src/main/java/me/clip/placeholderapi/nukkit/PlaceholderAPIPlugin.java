/*
 *
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
 *
 *
 */
package me.clip.placeholderapi;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import me.clip.placeholderapi.commands.PlaceholderAPICommands;
import me.clip.placeholderapi.configuration.PlaceholderAPIConfig;
import me.clip.placeholderapi.util.TimeUtil;
import me.clip.placeholderapi.expansion.ExpansionManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.cloud.ExpansionCloudManager;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PlaceholderAPIPlugin extends PluginBase {
    private static PlaceholderAPIPlugin instance;
    private static SimpleDateFormat dateFormat;
    private static String booleanTrue, booleanFalse;
    private PlaceholderAPIConfig configuration;
    private ExpansionManager expansionManager;
    private ExpansionCloudManager expansionCloud;
    private Long startTime;

    public static PlaceholderAPIPlugin getInstance() {
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
    public void onLoad() {
        startTime = System.currentTimeMillis();
        instance = this;
        configuration = new PlaceholderAPIConfig(this);
        expansionManager = new ExpansionManager(this);
    }

    @Override
    public void onEnable() {
        configuration.loadDefConfig();
        setupOptions();
        PluginCommand<PlaceholderAPIPlugin> cs = (PluginCommand<PlaceholderAPIPlugin>) getServer().getPluginCommand("placeholderapi");
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
        // if (config.checkUpdates()) {
            // new UpdateChecker(this).fetch();
        // }

        if (configuration.isCloudEnabled()) {
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
        if (!configuration.isCloudEnabled()) {
            disableCloud();
        } else if (!cloudEnabled) {
            enableCloud();
        }

        s.sendMessage(TextFormat.colorize(PlaceholderAPI.getRegisteredIdentifiers().size() + " &aplaceholder hooks successfully registered!"));
    }

    private void setupOptions() {
        booleanTrue = configuration.booleanTrue();
        if (booleanTrue == null) {
            booleanTrue = "true";
        }

        booleanFalse = configuration.booleanFalse();
        if (booleanFalse == null) {
            booleanFalse = "false";
        }

        try {
            dateFormat = new SimpleDateFormat(configuration.dateFormat());
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
            expansionCloud = new ExpansionCloudManager(this);
            expansionCloud.fetch(configuration.cloudAllowUnverifiedExpansions());
        } else {
            expansionCloud.clean();
            expansionCloud.fetch(configuration.cloudAllowUnverifiedExpansions());
        }
    }

    public void disableCloud() {
        if (expansionCloud != null) {
            expansionCloud.clean();
            expansionCloud = null;
        }
    }

    public PlaceholderAPIConfig getPlaceholderAPIConfig() {
        return configuration;
    }

    public ExpansionManager getExpansionManager() {
        return expansionManager;
    }

    public ExpansionCloudManager getExpansionCloud() {
        return expansionCloud;
    }

    public String getUptime() {
        return TimeUtil.getTime((int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime));
    }

    public long getUptimeMillis() {
        return (System.currentTimeMillis() - startTime);
    }
}
