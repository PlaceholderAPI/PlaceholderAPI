[placeholderexpansion]: https://github.com/PlaceholderAPI/PlaceholderAPI/blob/master/src/main/java/me/clip/placeholderapi/expansion/PlaceholderExpansion.java

[playerexpansion]: https://github.com/PlaceholderAPI/Player-Expansion
[serverexpansion]: https://github.com/PlaceholderAPI/Server-Expansion
[mathexpansion]: https://github.com/Andre601/Math-expansion

[relational]: https://github.com/PlaceholderAPI/PlaceholderAPI/blob/master/src/main/java/me/clip/placeholderapi/expansion/Relational.java

## Overview
This page will cover how you can create your own [`PlaceholderExpansion`][placeholderexpansion] which you can either [[Upload to the eCloud|Expansion cloud]] or integrate into your own plugin.

It's worth noting that PlaceholderAPI relies on expansions being installed. PlaceholderAPI only acts as the core replacing utility while the expansions allow other plugins to use any installed placeholder in their own messages.
You can download Expansions either directly from the eCloud yourself, or download them through the [[download command of PlaceholderAPI|Commands#papi-ecloud-download]].

## Table of Contents

- [Getting started](#getting-started)
  - [Common Parts](#common-parts)
- [Without a Plugin](#without-a-plugin)
- [With a Plugin (External Jar)](#with-a-plugin-external-jar)
- [With a Plugin (Internal Class)](#with-a-plugin-internal-class)
  - [Register the Expansion](#register-the-expansion)
- [Relational Placeholders](#relational-placeholders)
  - [Notes about Relational Placeholders](#notes-about-relational-placeholders)

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

You must choose between one of these two methods for handling the actual parsing of placeholders:

- #### onRequest(OfflinePlayer, String)
  If not explicitly set, this will automatically call [`onPlaceholderRequest(Player, String)`](#onplaceholderrequestplayer-string).
  This method is recommended as it allows the usage of offline players, meaning the player doesn't need to be online to get certain information (i.e. name).
- #### onPlaceholderRequest(Player, String)
  If not set, this method will return `null` which PlaceholderAPI sees as an invalid placeholder.  
  The `Player` can be `null`, so keep that in mind when handling your placeholders.

----
## Without a Plugin
An expansion does not always need a plugin to rely on. If the placeholders it provides can return values from just the server itself or some other source (i.e. Java itself), then it can work independently.

Common examples of such Expansions are:

- [Player Expansion][playerexpansion]
- [Server Expansion][serverexpansion]
- [Math Expansion][mathexpansion]

These kinds of expansions don't require any additional plugins to function.  
When creating such an expansion is it recommended to use [`onRequest(OfflinePlayer, String)`](#onrequestofflineplayer-string).

#### Full Example
Please see the [Common parts](#common-parts) section for info on the other methods.

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
    
    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("name")) {
            return player == null ? null : player.getName(); // "name" requires the player to be valid
        }
        
        if(params.equalsIgnoreCase("placeholder1")) {
            return "Placeholder Text 1";
        }
        
        if(params.equalsIgnoreCase("placeholder2")) {
            return "Placeholder Text 2";
        }
        
        return null; // Placeholder is unknown by the Expansion
    }
}
```

----
## With a Plugin (External Jar)
If your expansion relies on a plugin to provide its placeholder values, you will need to override a few more methods to make sure everything will work correctly.

Your expansion will need to override the `getRequiredPlugin()` method to return the name of the plugin your expansion depends on.  
PlaceholderAPI automatically checks if this method will either return null, or if the name defined results in a non-null plugin.

It is worth noting that it is a bit more difficult to make a separate jar file that depends on a plugin, as it will require the plugin to have some sort of accessible API in order to get the required values.
One way to bypass this is to override the `canRegister()` method with the following code:

```java
private SomePlugin plugin = null; // This would be the plugin your expansion depends on

@Override
public boolean canregister() {
    // This sets plugin to the SomePlugin instance you get through the PluginManager
    return (plugin = (SomePlugin) Bukkit.getPluginManager().getPlugin(getRequiredPlugin())) != null;
}
```
Using this code-snippet, you can get a direct instance of the plugin and access things such as config values.  
With that said, it is recommended instead to use an API if one is available, as this kind of plugin access is a relatively poor approach.

#### Full Example
Please see the [Common parts](#common-parts) section for info on the other methods.

```java
package at.helpch.placeholderapi.example.expansions;

import at.helpch.placeholderapi.example.SomePlugin;
import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class SomeExpansion extends PlaceholderExpansion {

    private SomePlugin plugin; // This instance is assigned in canRegister()
    
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
    
    @Override
    public String getRequiredPlugin() {
        return "SomePlugin";
    }
    
    @Override
    public boolean canRegister() {
        return (plugin = (SomePlugin) Bukkit.getPluginManager().getPlugin(getRequiredPlugin())) != null;
    }
    
    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("placeholder1")){
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }
        
        if(params.equalsIgnoreCase("placeholder2")){
            return plugin.getConfig().getString("placeholders.placeholder2", "default2");
        }
        
        return null; // Placeholder is unknown by the expansion
    }
}
```

----
## With a Plugin (Internal Class)
The way expansions are handled when they are part of the plugin itself is fairly similar to when you [make an expansion without a plugin dependency](#without-a-plugin).

In fact, you don't even have to override the `getRequiredPlugin()` and `canRegister()` methods as it is always guaranteed that the plugin is available.  
Something worth noting, however, is that you need to override the `persist()` method and make it return true. This ensures that the expansion won't be unregistered by PlaceholderAPI whenever it is reloaded.

Finally, you can also use dependency injection as an easier way to access a plugin's methods.
Here is a small code example of how dependency injection may look:

```java
public class SomeExpansion extends PlaceholderExpansion {
    private final SomePlugin plugin; // The instance is created in the constructor and won't be modified, so it can be final
    
    public SomeExpansion(SomePlugin plugin) {
        this.plugin = plugin;
    }
}
```

#### Full Example
Please see the [Common parts](#common-parts) section for info on the other methods.


```java
package at.helpch.placeholderapi.example.expansions;

import at.helpch.placeholderapi.example.SomePlugin;
import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class SomeExpansion extends PlaceholderExpansion {

    private final SomePlugin plugin;
    
    public SomeExpansion(SomePlugin plugin) {
        this.plugin = plugin;
    }
    
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
    
    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }
    
    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("placeholder1")){
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }
        
        if(params.equalsIgnoreCase("placeholder2")) {
            return plugin.getConfig().getString("placeholders.placeholder2", "default2");
        }
        
        return null; // Placeholder is unknown by the Expansion
    }
}
```

### Register the Expansion
To register the expansion, you will need to call the `register()` method yourself.
This should be done in your plugin's `onEnable()` method after you make sure that PlaceholderAPI is installed and enabled.

```java
package at.helpch.placeholderapi.example

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SomePlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        // Small check to make sure that PlaceholderAPI is installed
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
              new SomeExpansion(this).register();
        }
    }
}
```

----
## Relational Placeholders
Relational Placeholders are a bit more specific compared to the previous examples.  
While they do use the same [common parts](#common-parts) that the other examples do, they have a different method to return placeholders.

In order to use the relational placeholders feature, you will need to implement the [`Relational`][relational] interface, which in return adds the `onPlaceholderRequest(Player, Player, String)` method to use.

#### Full Example
Please see the [Common parts](#common-parts) section for info on the other methods.

In this example, we use the [Internal class setup](#with-a-plugin-internal-jar) and `SomePlugin` has an `areFriends(Player, Player)` method that returns true or false based on if the given players are friends.

```java
package at.helpch.placeholderapi.example.expansions;

import at.helpch.placeholderapi.example.SomePlugin;
import org.bukkit.ChatColor;
import org.bukkit.Player;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;

public class SomeExpansion extends PlaceholderExpansion implements Relational {

    private final SomePlugin plugin;
    
    public SomeExpansion(SomePlugin plugin) {
        this.plugin = plugin;
    }
    
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
    
    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
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

### Notes about Relational Placeholders
Relational placeholders will always start with `%rel_` to properly identify them.  
So in the above example, the full placeholder will look like `%rel_example_friend%`.
