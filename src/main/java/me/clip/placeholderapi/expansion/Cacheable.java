package me.clip.placeholderapi.expansion;

/**
 * This interface allows a class which extends a {@link PlaceholderExpansion}
 * to have the clear method called when the implementing expansion is unregistered
 * from PlaceholderAPI.
 * This is useful if we want to do things when the implementing hook is unregistered
 * @author Ryan McCarthy
 *
 */
public interface Cacheable {

	/**
	 * Called when the implementing class is unregistered from PlaceholderAPI
	 */
	void clear();
}
