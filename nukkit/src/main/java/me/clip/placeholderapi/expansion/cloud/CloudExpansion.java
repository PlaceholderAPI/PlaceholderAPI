package me.clip.placeholderapi.expansion.cloud;

import me.clip.placeholderapi.util.TimeUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CloudExpansion {
    private String name, author, latest_version, description, source_url, dependency_url;
    private Boolean hasExpansion, shouldUpdate, verified;
    private Long last_update, rating_count;
    private Double average_ratings;
    private List<String> placeholders;
    private List<Version> versions;

    public CloudExpansion() {
    }

    public String getTimeSinceLastUpdate() {
        long time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - getLastUpdate());
        return TimeUtil.getTime((int) time);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Version getVersion() {
        return getLatestVersion() == null ? null : getVersion(getLatestVersion());
    }

    public Version getVersion(String version) {
        return versions == null ? null : versions.stream().filter(v -> v.getVersion().equals(version)).findFirst().orElse(null);
    }

    public List<String> getAvailableVersions() {
        return versions.stream().map(Version::getVersion).collect(Collectors.toList());
    }

    public String getLatestVersion() {
        return latest_version;
    }

    public void setLatestVersion(String latest_version) {
        this.latest_version = latest_version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSourceURL() {
        return source_url;
    }

    public void setSourceURL(String source_url) {
        this.source_url = source_url;
    }

    public String getDependencyURL() {
        return dependency_url;
    }

    public void setDependencyURL(String dependency_url) {
        this.dependency_url = dependency_url;
    }

    public Boolean hasExpansion() {
        return hasExpansion;
    }

    public void setHasExpansion(Boolean hasExpansion) {
        this.hasExpansion = hasExpansion;
    }

    public Boolean shouldUpdate() {
        return shouldUpdate;
    }

    public void setShouldUpdate(Boolean shouldUpdate) {
        this.shouldUpdate = shouldUpdate;
    }

    public Boolean isVerified() {
        return verified;
    }

    public long getLastUpdate() {
        return last_update;
    }

    public void setLastUpdate(Long last_update) {
        this.last_update = last_update;
    }

    public Long getRatingsCount() {
        return rating_count;
    }

    public Double getAverageRating() {
        return average_ratings;
    }

    public List<String> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(List<String> placeholders) {
        this.placeholders = placeholders;
    }

    public List<Version> getVersions() {
        return versions;
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    public class Version {
        private String url, version, release_notes;

        public String getURL() {
            return url;
        }

        public void setURL(String url) {
            this.url = url;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getReleaseNotes() {
            return release_notes;
        }

        public void setReleaseNotes(String release_notes) {
            this.release_notes = release_notes;
        }
    }
}