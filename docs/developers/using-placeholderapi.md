---
description: Guide on how to use PlaceholderAPI in your own plugin.
---

# Using PlaceholderAPI

This page is about using PlaceholderAPI in your own plugin, to either let other plugins use your plugin, or just use placeholders from other plugins in your own.

Please note, that the examples in this page are only available for **PlaceholderAPI 2.10.0 (1.0.0 for Hytale version) or newer**!

## First steps

### Add PlaceholderAPI to your Project

Before you can actually make use of PlaceholderAPI, you first have to import it into your project.  
Use the below code example matching your project type and dependency manager.

/// tab | Minecraft (Spigot, Paper, ...)
//// tab | :simple-apachemaven: Maven
```{ .xml title="pom.xml" data-md-component="api-version" }
    <repositories>
        <repository>
            <id>placeholderapi</id>
            <url>https://repo.helpch.at/releases/</url>
        </repository>
    </repositories>
    <dependencies>
        <dependency>
            <groupId>me.clip</groupId>
            <artifactId>placeholderapi</artifactId>
            <version>{papiVersion}</version>
            <scope>provided</scope>
        </dependency>

        <!-- Optional: Component support on Paper Servers (Since 2.12.0) -->
        <dependency>
          <groupId>me.clip</groupId>
          <artifactId>placeholderapi-paper</artifactId>
          <version>{papiVersion}</version>
          <scope>provided</scope>
    </dependencies>
```
////

//// tab | :simple-gradle: Gradle
```{ .groovy title="build.gradle" data-md-component="api-version" }
repositories {
    maven {
        url = 'https://repo.extendedclip.com/releases/'
    }
}

dependencies {
    compileOnly 'me.clip:placeholderapi:{papiVersion}'

    // Optional: Component support on Paper Servers (Since 2.12.0)
    compileOnly 'me.clip:placeholderapi-paper:{papiVersion}'
}
```
////
///

/// tab | Hytale
//// tab | :simple-apachemaven: Maven
```{ .xml title="pom.xml" data-md-component="api-version" }
    <repositories>
        <repository>
          <id>hytale</id>
          <url>https://repo.codemc.io/repository/hytale/</url>
        </repository>
        <repository>
            <id>placeholderapi</id>
            <url>https://repo.helpch.at/releases/</url>
        </repository>
    </repositories>
    <dependencies>
        <dependency>
            <!-- Replace {hytaleVersion} with the version you need -->
            <groupId>com.hypixel.hytale</groupId>
            <artifactId>Server</artifactId>
            <version>{hytaleVersion}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>at.helpch</groupId>
            <artifactId>placeholderapi-hytale</artifactId>
            <version>{papiHytaleVersion}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
```
////

//// tab | :simple-gradle: Gradle
```{ .groovy title="build.gradle" data-md-component="api-version" }
repositories {
    maven {
        url = 'https://repo.codemc.io/repository/hytale/'
        url = 'https://repo.helpch.at/releases/'
    }
}

dependencies {
    // Replace {hytaleVersion} with the version you need.
    compileOnly 'com.hypixel.hytale:Server:{hytaleVersion}'
    compileOnly 'at.helpch:placeholderapi-hytale:{papiHytaleVersion}'
}
```
////
///

/// details | What is `{papiVersion}`/`{papiHytaleVersion}`?
    type: question

Using Javascript, `{papiVersion}` and `{papiHytaleVersion}` is replaced with the latest available API version of PlaceholderAPI for Minecraft and Hytale respectively.  
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
main: com.example.plugin.ExamplePlugin

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
main: com.example.plugin.ExamplePlugin

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
main: com.example.plugin.ExamplePlugin

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
main: com.example.plugin.ExamplePlugin

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

/// tab | manifest.json (Hytale)

//// tab | Optional dependency

```{ .json .annotate title="manifest.json" }
{
    "Group": "com.example",
    "Name": "ExamplePlugin",
    "Version": "1.0",
    "Main": "com.example.plugin.ExamplePlugin",
    "OptionalDependencies": {
        "HelpChat:PlaceholderAPI": ">= 1.0.2"
    }
}
```

////

//// tab | Required dependency

```{ .json .annotate title="manifest.json" }
{
    "Group": "com.example",
    "Name": "ExamplePlugin",
    "Version": "1.0",
    "Main": "com.example.plugin.ExamplePlugin",
    "Dependencies": {
        "HelpChat:PlaceholderAPI": ">= 1.0.2"
    }
}
```

////

///

## Adding placeholders to PlaceholderAPI

A full guide on how to create expansions can be found on the [Creating a PlaceholderExpansion](creating-a-placeholderexpansion.md) page.

## Setting placeholders in your plugin

PlaceholderAPI offers the ability, to automatically parse placeholders from other plugins within your own plugin, giving the ability for your plugin to support thousands of other placeholders without depending on each plugin individually.  
To use placeholders from other plugins in your own plugin, you simply have to [(soft)depend on PlaceholderAPI](#set-placeholderapi-as-softdepend) and use the `setPlaceholders` method.

It is also important to point out, that any required plugin/dependency for an expansion has to be on the server and enabled, or the `setPlaceholders` method will just return the placeholder itself (do nothing).

/// info | New since 2.12.0
Starting with version 2.12.0 is it now possible to provide Components from the Adventure library to have placeholders parsed in.

In order to use this new feature are the following things required to be true:

- You depend on `placeholderapi-paper` and not just `placeholderapi`
- Your plugin runs on a Paper-based Server. Spigot-based servers will not work!
- You use `PAPIComponent` instead of `PlaceholderAPI` to parse Components.
///

/// tab | Spigot, Paper, ...

The following is an example plugin that sends `%player_name% joined the server! They are rank %vault_rank%` as the Join message, having the placeholders be replaced by PlaceholderAPI.

//// note |
The below example assumes a **soft dependency** on PlaceholderAPI to handle PlaceholderAPI not being present more decently.

Tab the :material-plus-circle: icons in the code block below for additional information.
////

```{ .java .annotate title="JoinExample.java" }
package com.example.plugin;

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

    //// info | New since 2.12.0
    Using `placeholderapi-papi` and `PAPIComponents` instead of `PlaceholderAPI` allows you to parse placeholders inside Adventure Components.
    ////
///

/// tab | Hytale

The following is an example plugin that sends `Welcome %player_name%!` as the Join message, having the placeholders be replaced by PlaceholderAPI.

``` { .java .annotate title="JoinExample.java" }
packate com.example.plugin;

import at.helpch.placeholderapi.PlaceholderAPI;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;

public class JoinExample extends JavaPlugin {

    public JoinExample(JavaPluginInit init) {
        super(init)
    }

    @Override
    protected void setup() {
        // (1)
        Universe.get().getWorlds().keySet().forEach(name -> getEventRegistry().register(PlayerReadyEvent.class, name, this::onPlayerReady));
    }

    public void onPlayerReady(PlayerReadyEvent event) {
        Player player = event.getPlayer();
        // (2)
        player.sendMessage(PlaceholderAPI.setPlaceholders(Message.raw("Welcome %player_name%!"), player))
        
    }
}
```

1.  We tell the server to call `onPlayerReady` whenever a `PlayerReadyEvent` fires.
2.  PlaceholderAPI offers multiple `setPlaceholders` methods that can either return a `String` or a `Message` object, depending on your needs.  
    Note that these methods require input of the same type: `setPlaceholders(String, PlayerRef)` for String and `setPlaceholders(Message, PlayerRef)` for Messages.

///
