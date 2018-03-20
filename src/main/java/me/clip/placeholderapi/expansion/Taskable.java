package me.clip.placeholderapi.expansion;


public interface Taskable {
	
	/**
	 * Called when the implementing class has successfully been registered to the placeholder map
	 * Tasks that need to be performed when this expansion is registered should go here
	 */
	void start();
	
	/**
	 * Called when the implementing class has been unregistered from PlaceholderAPI
	 * Tasks that need to be performed when this expansion has unregistered should go here
	 */
	void stop();
}
