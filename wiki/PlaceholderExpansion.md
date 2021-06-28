[placeholderexpansion]: https://github.com/PlaceholderAPI/PlaceholderAPI/blob/master/src/main/java/me/clip/placeholderapi/expansion/PlaceholderExpansion.java

[playerexpansion]: https://github.com/PlaceholderAPI/Player-Expansion
[serverexpansion]: https://github.com/PlaceholderAPI/Server-Expansion
[mathexpansion]: https://github.com/Andre601/Math-expansion

[relational]: https://github.com/PlaceholderAPI/PlaceholderAPI/blob/master/src/main/java/me/clip/placeholderapi/expansion/Relational.java

## Overview
This page will cover how you can create your own [`PlaceholderExpansion`][placeholderexpansion] which you can either [[Upload to the eCloud|Expansion cloud]] or integrate into your own plugin.

Something to note here is, that PlaceholderAPI relies on Expansions to be installed. PlaceholderAPI is acting as the core while the Expansions are what allows other plugins to use placeholders in various messages.  
You can download Expansions either directly from the eCloud yourself, or download them through the [[download command of PlaceholderAPI|Commands#papi-ecloud-download]].

## Table of Contents

- [Getting started](#getting-started)
  - [Common Parts](#common-parts)
- [Without a Plugin](#without-a-plugin)
- [With a Plugin (External Jar)](#with-a-plugin-external-jar)
- [With a Plugin (Internal Jar)](#with-a-plugin-internal-jar)
  - [Register the Expansion](#register-the-expansion)
- [Relational Placeholders](#relational-placeholders)
  - [Notes about Relational Placeholders](#notes-about-relational-placeholders)

## Getting started
To get started, first choose what type of [`PlaceholderExpansion`][placeholderexpansion] you want to create. There are various ways you can create a [`PlaceholderExpansion`][placeholderexpansion] from which this page will cover the most common ones.

### Common Parts
All shown examples will share the same, common parts that belong to the [`PlaceholderExpansion`][placeholderexpansion] class.  
To not repeat the same basic info for each method throughout this page will we cover the most basic/neccessary ones here.

#### Basic PlaceholderExpansion Structure
```java
package at.helpch.placeholderapi.example.expansions;

import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class SomeExpansion extends PlaceholderExpansion {

    @Override
    public String getAuthor(){
        return "someauthor";
    }
    
    @Override
    public String getIdentifier(){
        return "example";
    }

    @Override
    public String getVersion(){
        return "1.0.0";
    }
}
```
Let's quickly break down the different methods you had to import.

- #### getAuthor
  Through this method can you set the name of who created the expansion.
- #### getIdentifier
  The name that should be used to identify the placeholders for this expansion.  
  The Identifier is the first text after the `%` and before the first `_` (`%identifier_values%`) and can therefore not contain any `_` in it.
  
  If you want to use `_` in your Expansion's name can you override the optional `getName()` method.
- #### getVersion
  This is a String, which means it doesn't has to be a number in itself. The String is used to determine if a new update is available or not, when the expansion is shared on the eCloud.  
  For Expansions that are part of a plugin does this not really matter.

Those are all the neccessary parts for your PlaceholderExpansion.  
Any other methods that are part of the [`PlaceholderExpansion`][placeholderexpansion] class are optional and will usually not be used, or default to a specific value. Please read the Javadoc comments of those methods for more information.

----
## Without a Plugin
A PlaceholderExpansion may not need a plugin to rely on, if the placeholders it provides can return values from just the server itself or some other source (i.e. Java itself).

Common examples of such Expansions are:

- [Player Expansion][playerexpansion]
- [Server Expansion][serverexpansion]
- [Math Expansion][mathexpansion]

These kinds of Expansions don't need much more to properly work.  
In fact is the only thing they need to have added the `onRequest(OfflinePlayer, String)` or `onPlaceholderRequest(Player, String)` methods.

It is recommended to use the `onRequest(OfflinePlayer, String)` method as OfflinePlayers can also be used for normal players, but also allow to have no Player provided.

#### Full Example
Please see the [Common parts](#common-parts) section for info on the other methods.

```java
package at.helpch.placeholderapi.example.expansions;

import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class SomeExpansion extends PlaceholderExpansion {

    @Override
    public String getAuthor(){
        return "someauthor";
    }
    
    @Override
    public String getIdentifier(){
        return "example";
    }

    @Override
    public String getVersion(){
        return "1.0.0";
    }
    
    @Override
    public String onRequest(OfflinePlayer player, String params){
        if(params.equalsIgnoreCase("name")){
            return player == null ? null : player.getName(); // "name" requires the player to be valid
    
        if(params.equalsIgnoreCase("placeholder1")){
            return "Placeholder Text 1";
    
        if(params.equalsIgnoreCase("placeholder2")){
            return "Placeholder Text 2";
        
        return null; // Placeholder is unknown by the Expansion
    }
}
```

----
## With a Plugin (External Jar)
If your Expansion relies on a Plugin to provide its placeholder values will you need to override a few more methods to make everything work as it should.

Your expansion will need to override the `getRequiredPlugin()` method to return the name of the plugin your expansion depends on.  
PlaceholderAPI does by default check, if this method either returns null, or the name defined results in a Not-null plugin being available.

Something worth noting is, that it is a bit more difficult to make a separate Jar file that depends on a plugin, as it will require the plugin to have some sort of accessible API to use in order to get the values needed.  
One way to bypass this is to override the `canRegister()` method with the following code:

```java
SomePlugin plugin; // This would be the plugin your expansion depends on

@Override
public boolean canregister(){
    // This sets plugin to the SomePlugin instance you get through the PluginManager
    return (plugin = (SomePlugin) Bukkit.getPluginManager().getPlugin(getRequiredPlugin())) != null;
}
```
With this code-snippet can you get a direct instance of the Plugin and access things such as config values.  
With that said is it recommended to instead use an API if available, as this kind of plugin access is not really a good approach.

#### Full Example
Please see the [Common parts](#common-parts) section for info on the other methods.

```java
package at.helpch.placeholderapi.example.expansions;

import at.helpch.placeholderapi.example.SomePlugin;
import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class SomeExpansion extends PlaceholderExpansion {

    SomePlugin plugin; // This instance is assigned in canRegister()
    
    @Override
    public String getAuthor(){
        return "someauthor";
    }
    
    @Override
    public String getIdentifier(){
        return "example";
    }

    @Override
    public String getVersion(){
        return "1.0.0";
    }
    
    @Override
    public String getRequiredPlugin(){
        return "SomePlugin";
    }
    
    @Override
    public boolean canRegister(){
        return (plugin = (SomePlugin) Bukkit.getPluginManager().getPlugin(getRequiredPlugin())) != null;
    }
    
    @Override
    public String onRequest(OfflinePlayer player, String params){
        if(params.equalsIgnoreCase("placeholder1")){
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
    
        if(params.equalsIgnoreCase("placeholder2")){
            return plugin.getConfig().getString("placeholders.placeholder2", "default2");
        
        return null; // Placeholder is unknown by the Expansion
    }
}
```

----
## With a Plugin (Internal Jar)
The way Expansions are handled when they are part of the plugin itself is fairly similar to when you [make a Jar without a plugin dependency](#without-a-plugin)

In fact, you don't even have to override the `getRequiredPlugin()` and `canRegister()` methods as it is always guaranteed that the plugin is available.  
Something worth noting is, that you need to override the `persist()` method and make it return true. This ensures that the Expansion won't be unregistered by PlaceholderAPI whenever it is reloaded.

Finally, can you also use Dependency Injection for a more easy way to access a plugin's methods.  
Here is a small code example of how this Dependency Injection may look like:

```java
public class SomeExpansion extends PlaceholderExpansion {
    final SomePlugin plugin; // The instance is created in the constructor and won't be modified, so it can be final
    
    public SomeExpansion(SomePlugin plugin){
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

    SomePlugin plugin;
    
    public SomeExpansion(SomePlugin plugin){
        this.plugin = plugin;
    }
    
    @Override
    public String getAuthor(){
        return "someauthor";
    }
    
    @Override
    public String getIdentifier(){
        return "example";
    }

    @Override
    public String getVersion(){
        return "1.0.0";
    }
    
    @Override
    public boolean persist(){
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }
    
    @Override
    public String onRequest(OfflinePlayer player, String params){
        if(params.equalsIgnoreCase("placeholder1")){
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
    
        if(params.equalsIgnoreCase("placeholder2")){
            return plugin.getConfig().getString("placeholders.placeholder2", "default2");
        
        return null; // Placeholder is unknown by the Expansion
    }
}
```

### Register the Expansion
To now register the expansion will you need to call the `register()` method yourself.  
This should be done in your plugin's `onEnable()` void after you made sure that PlaceholderAPI is installed and enabled.

```java
package at.helpch.placeholderapi.example

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SomePlugin extends JavaPlugin{
    
    @Override
    public void onEnable(){
        // Small check to make sure that PlaceholderAPI is installed
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
              new SomeExpansion(this).register();
        }
    }
}
```

----
## Relational Placeholders
Relational Placeholders are a bit more specific compared to the previous examples.  
While they do use the same [common parts](#common-parts) like the other examples do they have a different method to return placeholders.

In order to use the relational placeholders feature will you need to implement the [`Relational`][relational] interface, which in return adds the `onPlaceholderRequest(Player, Player, String)` method to use.

#### Full Example
Please see the [Common parts](#common-parts) section for info on the other methods.

In this Example do we use the [Internal class setup](#with-a-plugin-internal-jar) and `SomePlugin` has a `areFriends(Player, Player)` method that returns true or false based on if the Players are friends.

```java
package at.helpch.placeholderapi.example.expansions;

import at.helpch.placeholderapi.example.SomePlugin;
import org.bukkit.ChatColor;
import org.bukkit.Player;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;

public class SomeExpansion extends PlaceholderExpansion implements Relational {

    SomePlugin plugin;
    
    public SomeExpansion(SomePlugin plugin){
        this.plugin = plugin;
    }
    
    @Override
    public String getAuthor(){
        return "someauthor";
    }
    
    @Override
    public String getIdentifier(){
        return "example";
    }

    @Override
    public String getVersion(){
        return "1.0.0";
    }
    
    @Override
    public boolean persist(){
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }
    
    @Override
    public String onPlaceholderRequest(Player one, Player two, String identifier){
        if(one == null || two == null)
            return null; // We require both Players to be online
            
        if(params.equalsIgnoreCase("friend")){
            if(plugin.areFriends(one, two))
                return ChatColor.GREEN + one.getName() + " and " + two.getName() + " are friends!";
            else
                return ChatColor.GREEN + one.getName() + " and " + two.getName() + " are not friends!";
        }
        
        return null; // Placeholder is unknown by the Expansion
    }
}
```

### Notes about Relational Placeholders
Relational Placeholders will always start with `%rel_` to properly identify them as such.  
So for the above example to work will the full placeholder need to look like `%rel_example_friend%`.
