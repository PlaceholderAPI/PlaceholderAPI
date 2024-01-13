---
description: Guide on how to use PlaceholderAPI in your own plugin.
---

# Using PlaceholderAPI

This page is about using PlaceholderAPI in your own plugin, to either let other plugins use your plugin, or just use placeholders from other plugins in your own.

Please note, that the examples in this page are only available for **PlaceholderAPI 2.10.0 or higher**!

## First steps

Before you can actually make use of PlaceholderAPI, you first have to import it into your project.

/// tab | :simple-apachemaven: Maven
```{ .xml title="pom.xml" data-md-component="api-version" }
    <repositories>
        <repository>
            <id>placeholderapi</id>
            <url>https://repo.extendedclip.com/content/repositories/placeholderapi/</url>
        </repository>
    </repositories>
    <dependencies>
        <dependency>
         <groupId>me.clip</groupId>
          <artifactId>placeholderapi</artifactId>
          <version>{version}</version>
         <scope>provided</scope>
        </dependency>
    </dependencies>
```
///

/// tab | :simple-gradle: Gradle
```{ .groovy title="build.gradle" data-md-component="api-version" }
repositories {
    maven {
        url = 'https://repo.extendedclip.com/content/repositories/placeholderapi/'
    }
}

dependencies {
    compileOnly 'me.clip:placeholderapi:{version}'
}
```
///

### Set PlaceholderAPI as (soft)depend

Next step is to go to your plugin.yml or paper-plugin.yml and add PlaceholderAPI as a depend or softdepend, depending (no pun intended) on if it is optional or not.

/// tab | :simple-spigotmc: plugin.yml

//// tab | Optional dependency
```yaml
name: ExamplePlugin
version: 1.0
author: author
main: your.main.path.Here

# This sets PlaceholderAPI as an optional dependency for your plugin.
softdepend: [PlaceholderAPI]
```
////

//// tab | Required dependency
```yaml
name: ExamplePlugin
version: 1.0
author: author
main: your.main.path.Here

# This sets PlaceholderAPI as a required dependency for your plugin.
depend: [PlaceholderAPI]
```
////

///

/// tab | :fontawesome-regular-paper-plane: paper-plugin.yml

//// tab | Optional dependency
```yaml
name: ExamplePlugin
version: 1.0
author: author
main: your.main.path.Here

dependencies:
  server:
    PlaceholderAPI:
      # Load order is relative to the dependency. So here PlaceholderAPI loads before our plugin.
      load: BEFORE
      required: false
```
////

//// tab | Required dependency
```yaml
name: ExamplePlugin
version: 1.0
author: author
main: your.main.path.Here

dependencies:
  server:
    PlaceholderAPI:
      # Load order is relative to the dependency. So here PlaceholderAPI loads before our plugin.
      load: BEFORE
      required: true
```
////

///

## Adding placeholders to PlaceholderAPI

A full guide on how to create expansions can be found on the [Creating a PlaceholderExpansion](creating-a-placeholderexpansion.md) page.

## Setting placeholders in your plugin

PlaceholderAPI offers the ability, to automatically parse placeholders from other plugins within your own plugin, giving the ability for your plugin to support thousands of other placeholders without depending on each plugin individually.  
To use placeholders from other plugins in our own plugin, we simply have to [(soft)depend on PlaceholderAPI](#set-placeholderapi-as-softdepend) and use the `setPlaceholders` method.

It is also important to point out, that any required plugin/dependency for an expansion has to be on the server and enabled, or the `setPlaceholders` method will just return the placeholder itself (do nothing).

**Example**:  
Let's assume we want to send a custom join message that shows the primary group a player has.  
To achieve this, we can do the following:
```java
package at.helpch.placeholderapi;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import me.clip.placeholderapi.PlaceholderAPI;

public class JoinExample extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
 
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            /*
             * We register the EventListener here, when PlaceholderAPI is installed.
             * Since all events are in the main class (this class), we simply use "this"
             */
            Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            /*
             * We inform about the fact that PlaceholderAPI isn't installed and then
             * disable this plugin to prevent issues.
             */
            getLogger().warn("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        String joinText = "%player_name% &ajoined the server! They are rank &f%vault_rank%";

        /*
         * We parse the placeholders using "setPlaceholders"
         * This would turn %vault_rank% into the name of the Group, that the
         * joining player has, assuming Vault and the Vault expansion are
         * on the server.
         */
        joinText = PlaceholderAPI.setPlaceholders(event.getPlayer(), joinText);

        event.setJoinMessage(joinText);
    }
}
```
