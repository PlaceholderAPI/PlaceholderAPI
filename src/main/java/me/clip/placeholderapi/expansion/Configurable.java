package me.clip.placeholderapi.expansion;

import java.util.Map;

/**
 * Any {@link PlaceholderExpansion} class which implements configurable will
 * have any options listed in the getDefaults map automatically added to the PlaceholderAPI config.yml file
 * @author Ryan McCarthy
 *
 */
public interface Configurable {

	/**
	 * This method will be called before the implementing class is registered
	 * to obtain a map of configuration options that the implementing class needs
	 * These paths and values will be added to the PlaceholderAPI config.yml in the configuration section
	 * expansions.(placeholder identifier).(your key): (your value)
	 * @return Map of config path / values which need to be added / removed from the PlaceholderAPI config.yml file
	 */
	Map<String, Object> getDefaults();
}
