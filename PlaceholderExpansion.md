[placeholderexpansion]: https://github.com/PlaceholderAPI/PlaceholderAPI/blob/master/src/main/java/me/clip/placeholderapi/expansion/PlaceholderExpansion.java
[relational]: https://github.com/PlaceholderAPI/PlaceholderAPI/blob/master/src/main/java/me/clip/placeholderapi/expansion/Relational.java

## Overview

This page will cover how you can create your own [`PlaceholderExpansion`][placeholderexpansion] which you can either [[Upload to the eCloud|Expansion cloud]] or integrate into your own plugin.

It's worth noting that PlaceholderAPI relies on expansions being installed. PlaceholderAPI only acts as the core replacing utility while the expansions allow other plugins to use any installed placeholder in their own messages.
You can download Expansions either directly from the eCloud yourself, or download them through the [[download command of PlaceholderAPI|Commands#papi-ecloud-download]].

## Table of Contents

- [Getting started](#getting-started)
  - [Common Parts](#common-parts)
- [Making an internal Expansion](#making-an-internal-expansion)
  - [Full Example](#full-example)
  - [Register your Expansion](#register-your-expansion)
- [Making an external Expansion](#making-an-external-expansion)
  - [Full Example (Without Dependency)](#full-example-without-dependency)
  - [Full Example (With Dependency)](#full-example-with-dependency)
- [Relational Placeholders](#relational-placeholders)
  - [Quick Notes](#quick-notes)
  - [Adding Relational Placeholders](#adding-relational-placeholders)
  - [Full Example](#full-example-1)

## Getting started

For starters, you need to decide what type of [`PlaceholderExpansion`][placeholderexpansion] you want to create. There are various ways to create an expansion. This page will cover the most common ones.

### Common Parts

All shown examples will share the same common parts that belong to the [`PlaceholderExpansion`][placeholderexpansion] class.  
In order to not repeat the same basic info for each method throughout this page, and to greatly reduce its overall length, we will cover the most basic/necessary ones here.

#### Basic PlaceholderExpansion Structure

```java
package at.helpch.placeholderapi.example.expansions;

import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class SomeExpansion extends PlaceholderExpansion {

    @Override
    public String getAuthor() {
        return "someauthor";
    }
    
    @Override
    public String getIdentifier() {
        return "example";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
}
```
Let's quickly break down the different methods you have to implement.

- #### getAuthor
  This method allows you to set the name of the expansion's author.
- #### getIdentifier
  The identifier is the part in the placeholder that is between the first `%` (Or `{` if bracket placeholders are used) and the first `_`.  
  Because of that can you not use `%`, `{`, `}` or `_` in your identifier.
  
  If you still want to use those symbols can you override the `getName()` method to display a different name.
- #### getVersion
  This is a string, which means it can contain more than just a number. This is used to determine if a new update is available or not when the expansion is shared on the eCloud.
  For expansions that are part of a plugin, this does not really matter.

Those are all the neccessary parts for your PlaceholderExpansion.  
Any other methods that are part of the [`PlaceholderExpansion`][placeholderexpansion] class are optional and will usually not be used, or will default to a specific value. Please read the Javadoc comments of those methods for more information.

You must choose between one of these two methods for handling the actual parsing of placeholders (Unless you're using [Relational Placeholders](#relational-placeholders)):

- #### onRequest(OfflinePlayer, String)
  If not explicitly set, this will automatically call [`onPlaceholderRequest(Player, String)`](#onplaceholderrequestplayer-string).
  This method is recommended as it allows the usage of offline players, meaning the player doesn't need to be online to obtain certain data from them like name or UUID.
- #### onPlaceholderRequest(Player, String)
  If not set, this method will return `null` which PlaceholderAPI sees as an invalid placeholder.  
  The `Player` can be `null`, so keep that in mind when handling your placeholders.

PlaceholderAPI will **always** call `onRequest(OfflinePlayer, String)` in an expansion.

----

## Making an internal Expansion

Internal PlaceholderExpansions are classes directly implemented into the plugin they depend on. The main benefit is, that you're not required to do any `canRegister()` check for your own plugin, as it is loaded within it.  
Another benefit is that you can more easily access plugin data using Dependency Injection should an API not be accessible for the plugin.

It is important to note that PlaceholderExpansions loaded manually through the `register()` method (Therefore not being loaded by PlaceholderAPI itself) **require** to have the `persist()` method return `true` to avoid the expansion being unloaded during a `/papi reload`.

Depending on what info you try to access from your plugin (i.e. through the main class or a dedicated API) can you use Dependency Injection to obtain an instance of whatever you need.  
Keep in mind that as mentioned earlier, the Expansion class won't be reloaded when `persist()` is set to `true`, so you have to refresh any info that may get outdated during a reload yourself.

Here is an example of a possible Dependency Injection:  
```java
package at.helpch.placeholderapi.example.expansion;

import at.helpch.placeholderapi.example.SomePlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class SomeExpansion extends PlaceholderExpansion {
    
    private final SomePlugin plugin;
    
    public SomeExpansion(SomePlugin plugin) {
        this.plugin = plugin;
    }
    
    // Imported methods from PlaceholderExpansion
}
```

### Full Example

> Please read the [`Common Parts` Section](#common-parts) for details on all the methods.

Below is a full example of an internal Expansion class. Please note the override of the `persist()` method to guarantee the Expansion isn't unloaded during a `/papi reload` operation.  
We also use the provided `SomePlugin` instance for information such as the version and authors. This allows us to keep the code clean while not having to deal with updating the expansion's information every time we make changes to the plugin.

```java
package at.helpch.placeholderapi.example.expansion;

import at.helpch.placeholderapi.example.SomePlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class SomeExpansion extends PlaceholderExpansion {
    
    private final SomePlugin plugin;
    
    public SomeExpansion(SomePlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }
    
    @Override
    public String getIdentifier() {
        return "example";
    }
    
    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
    
    // This override is required or PlaceholderAPI will unregister your expansion on /papi reload
    @Override
    public boolean persist() {
        return true;
    }
    
    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("placeholder1")) {
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }
        
        if (params.equalsIgnoreCase("placeholder2")) {
            return plugin.getConfig().getString("placeholders.placeholder2", "default2");
        }
        
        return null; // Unknown Placeholder provided
    }
}
```

### Register your expansion

The main downside of an internal expansion is, that it can't be loaded automatically by PlaceholderAPI and instead requires you to manually register it.  
To do that, create a new instance of your Expansion class and call the `register()` method of it.

Below is an example of loading the Expansion in the Plugin's main class inside the `onEnable()` method.

```java
package at.helpch.placeholderapi.example;

import at.helpch.placeholderapi.example.expansion.SomeExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SomePlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        // Make sure PlaceholderAPI is installed and enabled
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            // Register the expansion
            new SomeExpansion(this).register();
        }
    }
}
```

----

## Making an external Expansion

External expansions are separate Jar files containing the PlaceholderExpansion extending class.  
They are recommended for the following scenarios:

- Your Expansion doesn't depend on any Plugin.
- Your Expansion depends on a Plugin AND you can't directly implement the Class into it.

If you'd like to implement PlaceholderAPI expansions for your own plugin consider using the [internal expansion setup](#making-an-internal-expansion) instead.

Benefits of this type of expansion are 1) automatic loading through PlaceholderAPI by adding them to your `expansions` folder and 2) having the option to upload them on the eCloud, allowing it to be downloaded through [[`/papi ecloud download <expansion>`|Commands#papi-ecloud-download]] automatically (After it has been verified).

Downsides can be a more tedious setup to make sure any required plugin/dependency is loaded before registering the Expansion.

### Full Example (Without Dependency)

> Please read the [`Common Parts` Section](#common-parts) for details on all the methods.

Below is a full example of an external PlaceholderExpansion without any dependencies such as plugins.

```java
package at.helpch.placeholderapi.example.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class SomeExpansion extends PlaceholderExpansion {
    
    @Override
    public String getAuthor() {
        return "SomeAuthor";
    }
    
    @Override
    public String getIdentifier() {
        return "example";
    }
    
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("player_name")) {
            // player_name requires a valid OfflinePlayer
            return player == null ? null : player.getName();
        }
        
        if (params.equalsIgnoreCase("placeholder1")) {
            return "Placeholder Text 1";
        }
        
        if (params.equalsIgnoreCase("placeholder2")) {
            return "Placeholder Text 2";
        }
        
        return null; // Unknown Placeholder provided
    }
}
```

### Full example (With Dependency)

> Please read the [`Common Parts` Section](#common-parts) for details on all the methods.

The below example shows a possible setup of an external PlaceholderExpansion that depends on a Plugin to be present.

```java
package at.helpch.placeholderapi.example.expansion;

import at.helpch.placeholderapi.example.SomePlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class SomeExpansion extends PlaceholderExpansion {
    
    // This instance is assigned in canRegister() and can therefore not be final
    private SomePlugin plugin;
    
    @Override
    public String getAuthor() {
        return "SomeAuthor";
    }
    
    @Override
    public String getIdentifier() {
        return "example";
    }
    
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    // Allows us to define a plugin the expansion depends on
    @Override
    public String getRequiredPlugin() {
        return "SomePlugin";
    }
    
    /*
     * This method needs to be overriden if your Expansion requires
     * a plugin or other dependency to be present to work.
     *
     * Returning false will cancel the Expansion registration.
     */
    @Override
    public boolean canRegister() {
        /*
         * We do 2 things here:
         *   1. Check if the required plugin is present and enabled
         *   2. Obtain an instance of the plugin to use
         */
        return (plugin = (SomePlugin) Bukkit.getPluginManager().getPlugin(getRequiredPlugin())) != null;
    }
        
    
    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("placeholder1")) {
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }
        
        if (params.equalsIgnoreCase("placeholder2")) {
            return plugin.getConfig().getString("placeholders.placeholder2", "default2");
        }
        
        return null; // Unknown Placeholder provided
    }
}
```

----

## Relational Placeholders

Relational Placeholders are a special kind of placeholder that allow the usage of 2 Players for whatever you like to do. The most common use is to evaluate their relation to each other (i.e. check if Player one is in the same team as Player two) and return an output based on that.

### Quick Notes

Relational Placeholders are always prefixed with `rel_`, meaning that a relational placeholder with identifier `example` and value `friend` looks like `%rel_example_friend%` when used.

### Adding Relational Placeholders

To add relational placeholders will you need to implement the [`Relational`][relational] interface into your Expansion class and override the `onPlaceholderRequest(Player, Player, String)` method.

### Full Example

> Please read the [`Common Parts` Section](#common-parts) for details on all the methods.

Below is a full example of using Relational Placeholders.  
For the sake of simplicity are we using parts of the [internal Expansion Example](#making-an-internal-expansion) here and assume that the `SomePlugin` class offers a `areFriends(Player, Player)` method that returns a boolean value.

```java
package at.helpch.placeholderapi.example.expansions;

import at.helpch.placeholderapi.example.SomePlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.ChatColor;
import org.bukkit.Player;

public class SomeExpansion extends PlaceholderExpansion implements Relational {

    private final SomePlugin plugin;
    
    public SomeExpansion(SomePlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }
    
    @Override
    public String getIdentifier() {
        return "example";
    }
    
    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
    
    // This override is required or PlaceholderAPI will unregister your expansion on /papi reload
    @Override
    public boolean persist() {
        return true;
    }
    
    @Override
    public String onPlaceholderRequest(Player one, Player two, String identifier) {
        if(one == null || two == null)
            return null; // We require both Players to be online
            
        if(params.equalsIgnoreCase("friend")) {
            if(plugin.areFriends(one, two)) {
                return ChatColor.GREEN + one.getName() + " and " + two.getName() + " are friends!";
            } else {
                return ChatColor.GREEN + one.getName() + " and " + two.getName() + " are not friends!";
            }
        }
        
        return null; // Placeholder is unknown by the Expansion
    }
}
```
