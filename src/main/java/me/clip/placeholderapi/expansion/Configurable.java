/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2020 PlaceholderAPI Team
 *
 * PlaceholderAPI free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlaceholderAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.clip.placeholderapi.expansion;

import java.util.Map;

/**
 * Any {@link PlaceholderExpansion} class which implements configurable will have any options listed
 * in the getDefaults map automatically added to the PlaceholderAPI config.yml file
 *
 * @author Ryan McCarthy
 */
public interface Configurable {

    /**
     * This method will be called before the implementing class is registered to obtain a map of
     * configuration options that the implementing class needs These paths and values will be added to
     * the PlaceholderAPI config.yml in the configuration section expansions.(placeholder
     * identifier).(your key): (your value)
     *
     * @return Map of config path / values which need to be added / removed from the PlaceholderAPI
     * config.yml file
     */
    Map<String, Object> getDefaults();
}
