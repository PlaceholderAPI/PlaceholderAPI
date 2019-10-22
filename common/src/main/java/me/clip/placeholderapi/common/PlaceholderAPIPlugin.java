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
package me.clip.placeholderapi.common;

import me.clip.placeholderapi.common.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.common.util.PlatformUtil;
import me.clip.placeholderapi.common.util.logging.LoggerBase;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.File;

public interface PlaceholderAPIPlugin {
    PlatformUtil.Platform getPlatform();

    LoggerBase getMainLogger();

    File getPluginDataFolder();

    PlaceholderAPIPlugin getInstance();

    boolean registerExpansion(PlaceholderExpansion expansion);

    Boolean unregisterExpansion(PlaceholderExpansion expansion);

    YamlConfiguration getConfiguration();

    void reloadConfig();

    void saveDefaultConfig();
}