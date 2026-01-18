---
description: Guide on how to use PlaceholderAPI in your own plugin.
---

# Using PlaceholderAPI

This page is about using PlaceholderAPI in your own plugin, to either let other plugins use your plugin, or just use placeholders from other plugins in your own.

Please note, that the examples in this page are only available for **PlaceholderAPI 2.10.0 or higher**!

## First steps

### Add PlaceholderAPI to your Project

Before you can actually make use of PlaceholderAPI, you first have to import it into your project.  
Use the below code example matching your dependency manager.

/// tab | :simple-apachemaven: Maven
```{ .xml .annotate title="pom.xml" data-md-component="api-version" }
    <repositories>
        <repository>
            <id>placeholderapi</id>
            <url>https://repo.extendedclip.com/releases/</url>
        </repository>
    </repositories>
    <dependencies>
        <dependency>
            <groupId>me.clip</groupId>
            <artifactId>placeholderapi</artifactId>
            <version>{version}</version>
            <scope>provided</scope>
        </dependency>

        <!-- Optional: Component support on Paper Servers (Since TBD) -->
        <dependency>
          <groupId>me.clip</groupId>
          <artifactId>placeholderapi-paper</artifactId>
          <version>{version}</version>
          <scope>provided</scope>
    </dependencies>
```
///

/// tab | :simple-gradle: Gradle
```{ .groovy title="build.gradle" data-md-component="api-version" }
repositories {
    maven {
        url = 'https://repo.extendedclip.com/releases/'
    }
}

dependencies {
    compileOnly 'me.clip:placeholderapi:{version}'

    // Optional: Component support on Paper Servers (Since TBD)
    compileOnly 'me.clip:placeholderapi-paper:{version}'
}
```
///

/// details | What is `{version}`?
    type: question

Using Javascript, `{version}` is replaced with the latest available API version of PlaceholderAPI.  
Should you see the placeholder as-is does it mean that you either block Javascript, or that the version couldn't be obtained in time during page load.

You can always find the latest version matching the API version on the [releases tab](https://github.com/PlaceholderAPI/PlaceholderAPI/releases) of the GitHub Repository.
///

### Set PlaceholderAPI as (soft)depend

Next step is to go to your plugin.yml or paper-plugin.yml and add PlaceholderAPI as a depend or softdepend, depending (no pun intended) on if it is optional or not.

/// tab | :simple-spigotmc: plugin.yml

//// tab | Optional dependency

///// note |
Tab the :material-plus-circle: icons in the code block below for additional information.
/////

```{ .yaml .annotate title="plugin.yml" }
name: ExamplePlugin
version: 1.0
author: author
main: your.main.path.Here

softdepend: ["PlaceholderAPI"] # (1)
```

1.  This sets PlaceholderAPI as an optional dependency for your plugin.
////

//// tab | Required dependency

///// note |
Tab the :material-plus-circle: icons in the code block below for additional information.
/////

```{ .yaml .annotate title="plugin.yml" }
name: ExamplePlugin
version: 1.0
author: author
main: your.main.path.Here

depend: ["PlaceholderAPI"] # (1)
```

1.  This sets PlaceholderAPI as a required dependency for your plugin.
////

///

/// tab | :fontawesome-regular-paper-plane: paper-plugin.yml

//// tab | Optional dependency

///// note |
Tab the :material-plus-circle: icons in the code block below for additional information.
/////

```{ .yaml .annotate title="paper-plugin.yml" }
name: ExamplePlugin
version: 1.0
author: author
main: your.main.path.Here

dependencies:
  server:
    PlaceholderAPI:
      load: BEFORE # (1) 
      required: false
```

1.  Load order is relative to the Dependency.  
    This means that in this example, PlaceholderAPI is loaded **before** your plugin.
////

//// tab | Required dependency

///// note |
Tab the :material-plus-circle: icons in the code block below for additional information.
/////

```{ .yaml .annotate title="paper-plugin.yml" }
name: ExamplePlugin
version: 1.0
author: author
main: your.main.path.Here

dependencies:
  server:
    PlaceholderAPI:
      load: BEFORE # (1)
      required: true
```

1.  Load order is relative to the Dependency.  
    This means that in this example, PlaceholderAPI is loaded **before** your plugin.
////

///

## Adding placeholders to PlaceholderAPI

A full guide on how to create expansions can be found on the [Creating a PlaceholderExpansion](creating-a-placeholderexpansion.md) page.

## Setting placeholders in your plugin

PlaceholderAPI offers the ability, to automatically parse placeholders from other plugins within your own plugin, giving the ability for your plugin to support thousands of other placeholders without depending on each plugin individually.  
To use placeholders from other plugins in your own plugin, you simply have to [(soft)depend on PlaceholderAPI](#set-placeholderapi-as-softdepend) and use the `setPlaceholders` method.

It is also important to point out, that any required plugin/dependency for an expansion has to be on the server and enabled, or the `setPlaceholders` method will just return the placeholder itself (do nothing).

/// info | New since TBD
Starting with version TBD is it now possible to provide Components from the Adventure library to have placeholders parsed in.

In order to use this new feature are the following things required to be true:

- You depend on `placeholderapi-papi` and not just `placeholderapi`
- Your plugin runs on a Paper-based Server. Spigot-based servers will not work!
- You use `PAPIComponent` instead of `PlaceholderAPI` to parse Components.
///

/// details | Example
    type: example

Let's assume we want to send a custom join message that shows the primary group a player has.  
To achieve this, we can do the following:

//// note |
The below example assumes a **soft dependency** on PlaceholderAPI to handle PlaceholderAPI not being present more decently.

Tab the :material-plus-circle: icons in the code block below for additional information.
////

```{ .java .annotate title="JoinExample.java" }
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
 
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Bukkit.getPluginManager().registerEvents(this, this); // (1)
        } else {
            getLogger().warn("Could not find PlaceholderAPI! This plugin is required."); // (2)
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        String joinText = "%player_name% joined the server! They are rank %vault_rank%";

        joinText = PlaceholderAPI.setPlaceholders(event.getPlayer(), joinText); // (3)

        event.setJoinMessage(joinText);
    }
}
```

1.  We check that PlaceholderAPI is present and enabled to then register events to handle (See below).
2.  In case PlaceholderAPI is not present are we reporting this issue and disable the plugin.
3.  Using `PlaceholderAPI.setPlaceholders(Player, String)` we can parse `%placeholder%` text in the provided String, should they have a matching expansion and said expansion return a non-null String.  
    In our example are we providing a text containing `%player_name%` and `%vault_rank%` to be parsed, which require the Player and Vault expansion respectively.
    
    Example output: `Notch joined the server! They are rank Admin`

    //// info | New since TBD
    Using `placeholderapi-papi` and `PAPIComponents` instead of `PlaceholderAPI` allows you to parse placeholders inside Adventure Components.
    ////
///
