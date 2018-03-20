package me.clip.placeholderapi.expansion;

public class Version {

	private boolean isSpigot;
	
	private String version;
	
	public Version(String version, boolean isSpigot) {
		this.version = version;
		this.isSpigot = isSpigot;
	}
	
	public String getVersion() {
		return version == null ? "unknown" : version;
	}
	
	public boolean isSpigot() {
		return isSpigot;
	}
}
