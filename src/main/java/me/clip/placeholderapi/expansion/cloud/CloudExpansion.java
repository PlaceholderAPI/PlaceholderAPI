package me.clip.placeholderapi.expansion.cloud;

import java.util.concurrent.TimeUnit;

import me.clip.placeholderapi.util.TimeUtil;


public class CloudExpansion {

	private String name, author, version, description, link, releaseNotes;
	
	private boolean hasExpansion, shouldUpdate;
	
	private long lastUpdate;

	public CloudExpansion(String name, String author, String version, String description, String link) {
		this.name = name;
		this.author = author;
		this.version = version;
		this.description = description;
		this.link = link;
	}
	
	public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}

	public String getVersion() {
		return version;
	}

	public String getDescription() {
		return description;
	}

	public String getLink() {
		return link;
	}

	public boolean hasExpansion() {
		return hasExpansion;
	}

	public void setHasExpansion(boolean hasExpansion) {
		this.hasExpansion = hasExpansion;
	}

	public boolean shouldUpdate() {
		return shouldUpdate;
	}

	public void setShouldUpdate(boolean shouldUpdate) {
		this.shouldUpdate = shouldUpdate;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getReleaseNotes() {
		return releaseNotes;
	}

	public void setReleaseNotes(String releaseNotes) {
		this.releaseNotes = releaseNotes;
	}
	
	public String getTimeSinceLastUpdate() {
		int time = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - getLastUpdate());
		return TimeUtil.getTime(time);
	}
}
