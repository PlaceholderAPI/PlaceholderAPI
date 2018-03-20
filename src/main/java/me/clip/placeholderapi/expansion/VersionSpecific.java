package me.clip.placeholderapi.expansion;

/**
 * Placeholder expansions which use NMS code should be version specific.
 * Implementing this class allows you to perform checks based on the version the server is running. 
 * The isCompatibleWith method will be passed the server version and allow you to return if your expansion is compatible with that version.
 * @author Ryan McCarthy
 *
 */
public interface VersionSpecific {

	/**
	 * This method is called before the expansion is attempted to be registered
	 * The server version will be passed to this method so you know what version the server is currently running.
	 * 
	 * @return true if your expansion is compatible with the version the server is running.
	 */
	boolean isCompatibleWith(Version v);
}
