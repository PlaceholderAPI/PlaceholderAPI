---
description: Comprehensive guide on how to create a PlaceholderExpansion for other plugins to use through PlaceholderAPI.
---

# Creating a PlaceholderExpansion

This page will cover how you can create your own [`PlaceholderExpansion`][placeholderexpansion] which you can either integrate into your own plugin (Recommended) or [upload to the eCloud](expansion-cloud.md).

It's worth noting that PlaceholderAPI relies on expansions being installed. PlaceholderAPI only acts as the core replacing utility while the expansions allow other plugins to use any installed placeholder in their own messages.  
You can download expansions either directly from the eCloud yourself, or download them through the [download command of PlaceholderAPI](../users/commands.md#papi-ecloud-download).

## Table of contents

- [Getting started](#getting-started)
    - [Common Expansion Parts](#common-expansion-parts)
- [Making an Internal Expansion](#making-an-internal-expansion)
    - [Full Example](#full-example-internal)
    - [Register your Expansion](#register-your-expansion)
- [Making an External Expansion](#making-an-external-expansion)
    - [Full Example (Without Dependency)](#full-example-external-no-dependency)
    - [Full Example (With Dependency)](#full-example-external-dependency)
- [Making a relational Expansion](#making-a-relational-expansion)
    - [Full Example](#full-example-relational)

## Getting started

For starters, you need to decide what type of [`PlaceholderExpansion`][placeholderexpansion] you want to create. There are various ways to create an expansion. This page will cover the most common ones.

### Common Expansion Parts

All shown examples will share the same common parts that belong the the [`PlaceholderExpansion`][placeholderexpansion] class.  
In order to not repeat the same basic info for each method throughout this page, and to greatly reduce the overall length, we will cover the most basic/necessary ones here.

#### Basic PlaceholderExpansion Structure

/// note |
Tab the :material-plus-circle: icons in the code block below for additional information.
///

```java { .annotate title="SomeExpansion.java" }
package at.helpch.placeholderapi.example.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class SomeExpansion extends PlaceholderExpansion {
    
    @Override
    @NotNull
    public String getAuthor() {
        return "Author"; // (1)
    }
    
    @Override
    @NotNull
    public String getIdentifier() {
        return "example"; // (2)
    }
    
    @Override
    @NotNull
    public String getVersion() {
        return "1.0.0"; // (3)
    }

    // These methods aren't overriden by default.
    // You have to override one of them.
    
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        // (4)
    }
    
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        // (5)
    }
}
```

1.  This method allows you to set the name of the expansion's author. May not be null.

2.  The identifier is the part in the placeholder that is between the first `%` (or `{` for bracket placeholders) and the first `_`.  
    The identifier may not be null nor contain `%`, `{`, `}` or `_`.
    
    If you still want to use them in your expansion name, override the `getName()` method.

3.  This method returns the version of the expansion. May not be null.  
    Due to it being a string are you not limited to numbers alone, but it is recommended to stick with a number pattern.
    
    PlaceholderAPI uses this String to compare with the latest version on the eCloud (if uploaded to it) to see if a new version is available.  
    If your expansion is included in a plugin, this does not matter.

4.  Called by PlaceholderAPI to have placeholder values parsed.  
    When not overriden will call `onPlaceholderRequest(Player, String)`, converting the OfflinePlayer to a Player if possible or else providing `null`.
    
    Using this method is recommended for the usage of the OfflinePlayer, allowing to use data from a player without their precense being required.
    
    **Parameters**:
    
    - `player` - Nullable OfflinePlayer instance to parse placeholders against.
    - `params` - Non-null String representing the part of the placeholder after the first `_` and before the closing `%` (or `}` for bracket placeholders).

5.  Called by PlaceholderAPI through `onRequest(OfflinePlayer, String)` to have placeholder values parsed.  
    When not overriden will return `null`, which PlaceholderAPI will understand as an invalid Placeholder.
    
    **Parameters**:
    
    - `player` - Nullable Player instance to parse placeholders against.
    - `params` - Non-null String representing the part of the placeholder after the first `_` and before the closing `%` (or `}` for bracket placeholders).

/// note
Overriding `onRequest(OfflinePlayer, String)` or `onPlaceholderRequest(Player, String)` is not required if you [create relational placeholders](#making-a-relational-expansion).
///

----

## Making an Internal Expansion

Internal PlaceholderExpansions are classes directly integrated in the plugin they depend on.  
This method of creating a PlaceholderExpansion is recommended as it has the following benefits:

- No `canRegister()` method override required. Since your expansion is part of the plugin it depends on is this override not required.
- Easier access to plugin data. Using dependency injection, you can more easily access data of your plugin such as config values.

/// warning | Important!
Internal PlaceholderExpansions are not automatically registered by PlaceholderAPI, due to them not being a separate jar file located in the expansion folder.  
Please see the [Regsister your Expansion](#register-your-expansion) section for more details.

You are also required to override and set `persist()` to `true`. This tells PlaceholderAPI to not unload your expansion during plugin reload, as it would otherwise unregister your expansion, making it no longer work.
///

/// details | Full Example
    attrs: { id: full-example-internal }
    type: example

//// note |
Please see the [Basic PlaceholderExpansion Structure](#basic-placeholderexpansion-structure) section for an explanation of all common methods in this example.

Tab the :material-plus-circle: icons in the code block below for additional information.
////

```java { .annotate title="SomeExpansion.java" }
package at.helpch.placeholderapi.example.expansion;

import at.helpch.placeholderapi.example.SomePlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class SomeExpansion extends PlaceholderExpansion {
    
    private final SomePlugin plugin; // (1)
    
    public SomeExpansion(SomePlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    @NotNull
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors()); // (2)
    }
    
    @Override
    @NotNull
    public String getIdentifier() {
        return "example";
    }
    
    @Override
    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion(); // (3)
    }
    
    @Override
    public boolean persist() {
        return true; // (4)
    }
    
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("placeholder1")) {
            return plugin.getConfig().getString("placeholders.placeholder1", "default1"); // (5)
        }
        
        if (params.equalsIgnoreCase("placeholder2")) {
            return plugin.getConfig().getString("placeholders.placeholder1", "default1"); // (6)
        }
        
        return null; // (7)
    }
}
```

1.  Mockup plugin used to showcase the use of dependency injection to access specific plugin related data.

2.  We can use the authors set in the plugin's `plugin.yml` file as the authors of this expansion.

3.  Since our expansion is internal can this version be the same as the one defined in the plugin's `plugin.yml` file.

4.  This needs to be set, or else will PlaceholderAPI unregister our expansion during a plugin reload.

5.  Example of accessing data of the plugin's `config.yml` file.

6.  Example of accessing data of the plugin's `config.yml` file.

7.  Reaching this means that an invalid params String was given, so we return `null` to tell PlaceholderAPI that the placeholder was invalid.
///

### Register your Expansion

Due to the PlaceholderExpansion being internal, PlaceholderAPI does not load it automatically, we'll need to do it manually.  
This is being done by creating a new instance of your PlaceholderExpansion class and calling the `register()` method of it.

Here is a quick example:

```java { .annotate title="SomePlugin.java" }
package at.helpch.placeholderapi.example;

import at.helpch.placeholderapi.example.expansion.SomeExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SomePlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { // (1)
            new SomeExpansion(this).register(); // (2)
        }
    }
}
```

1.  We check that PlaceholderAPI is present and enabled on the server, or else we would get Exceptions.  
    Also, make sure you [set PlaceholderAPI as depend or softdepend](using-placeholderapi.md#set-placeholderapi-as-softdepend) in your plugin's `plugin.yml` file!

2.  This registers our expansion in PlaceholderAPI. It also gives the Plugin class as dependency injection to the Expansion class, so that we can use it.

----

## Making an External Expansion

External Expansions are separate Jar files located inside PlaceholderAPI's `expansions` folder, that contain the [`PlaceholderExpansion`][placeholderexpansion] extending class.  
It is recommended to only make external Expansions for the following situations.

- Your expansion does not rely on a plugin.
- Your expansion depends on a plugin and you can't directly include it (Plugin is not your own).

Should the above cases not match your situation, meaning your expansion is for a plugin you maintain, is the creation of an [internal Expansion](#making-an-internal-expansion) recommended.

Some benefits of an external expansion include automatic (re)loading of your expansion by PlaceholderAPI and having the option to [upload it to the eCloud](expansion-cloud.md) allowing the download of it through the [`/papi ecloud download` command](../users/commands.md#papi-ecloud-download).  
Downsides include a more tedious setup in terms of checking for a required plugin being present.

/// details | Full Example (Without Dependency)
    attrs: { id: full-example-external-no-dependency }
    type: example

//// note |
Please see the [Basic PlaceholderExpansion Structure](#basic-placeholderexpansion-structure) section for an explanation of all common methods in this example.

Tab the :material-plus-circle: icons in the code block below for additional information.
////

This is an example expansion without any plugin dependency.

```java { .annotate title="SomeExpansion.java" }
package at.helpch.placeholderapi.example.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class SomeExpansion extends PlaceholderExpansion {
    
    @Override
    @NotNull
    public String getAuthor() {
        return "Author";
    }
    
    @Override
    @NotNull
    public String getIdentifier() {
        return "example";
    }
    
    @Override
    @NotNull
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("placeholder1")) {
            return "text1";
        }
        
        if (params.equalsIgnoreCase("placeholder2")) {
            return "text2";
        }
        
        return null; // (1)
    }
}
```

1.  Reaching this means that an invalid params String was given, so we return `null` to tell PlaceholderAPI that the placeholder was invalid.
///

/// details | Full Example (With Dependency)
    attrs: { id: full-example-external-dependency }
    type: example

//// note |
Please see the [Basic PlaceholderExpansion Structure](#basic-placeholderexpansion-structure) section for an explanation of all common methods in this example.

Tab the :material-plus-circle: icons in the code block below for additional information.
////

This is an example expansion with a plugin dependency.

```java { .annotate title="SomeExpansion.java" }
package at.helpch.placeholderapi.example.expansion;

import at.helpch.placeholderapi.example.SomePlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class SomeExpansion extends PlaceholderExpansion {
    
    private SomePlugin plugin; // (1)
    
    @Override
    @NotNull
    public String getAuthor() {
        return "Author";
    }
    
    @Override
    @NotNull
    public String getIdentifier() {
        return "example";
    }
    
    @Override
    @NotNull
    public String getVersion() {
        return "1.0.0"
    }
    
    @Override
    public String getRequiredPlugin() {
        return "SomePlugin"; // (2)
    }
    
    @Override
    public boolean canRegister() { // (3)
        return (plugin = (SomePlugin) Bukkit.getPluginManager().getPlugin(getRequiredPlugin())) != null;
    }
    
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("placeholder1")) {
            return plugin.getConfig().getString("placeholders.placeholder1", "default1"); // (4)
        }
        
        if (params.equalsIgnoreCase("placeholder2")) {
            return plugin.getConfig().getString("placeholders.placeholder1", "default1"); // (5)
        }
        
        return null; // (6)
    }
}
```

1.  We set the value of this instance in the `canRegister()` method, which means that it can't be set to be final.

2.  The name of the plugin this expansion depends on.  
    It is recommended to set this, as it would result in PlaceholderAPI reporting any missing plugin for your expansion.
    
3.  This does two things:
    
    1. It sets the `plugin` instance to `SomePlugin` using Bukkit's PluginManager to retrieve a JavaPlugin instance that is cast to `SomePlugin`.
    2. It checks if the retrieved instance is not null. If it is will this result in `canRegister()` returning false, resulting in PlaceholderAPI not loading our expansion.

4.  Example of accessing data of the plugin's `config.yml` file.

5.  Example of accessing data of the plugin's `config.yml` file.

6.  Reaching this means that an invalid params String was given, so we return `null` to tell PlaceholderAPI that the placeholder was invalid.
///

----

## Making a relational Expansion

/// note
Relational Placeholders always start with `rel_` to properly identify them. This means that if you make a relational placeholder called `friends_is_friend` would the full placeholder be `%rel_friends_is_friend%`.
///

Relational PlaceholderExpansions are special in that they take two players as input, allowing you to give outputs based on their relation to each other.

To create a relational expansion you will need to implement the [`Relational`][relational] interface into your expansion. You also still need to extend the [`PlaceholderExpansion`][placeholderexpansion] class.  
Implementing this interface will add the `onPlaceholderRequest(Player, Player, String)` with the first two arguments being the first and second player to use and the third argument being the content after the second `_` and before the final `%` (Or `}` if bracket placeholders are used) in the placeholder.

/// details | Full Example
    attrs: { id: full-example-relational }
    type: example

//// note |
Please see the [Basic PlaceholderExpansion Structure](#basic-placeholderexpansion-structure) section for an explanation of all common methods in this example.

Tab the :material-plus-circle: icons in the code block below for additional information.
////

This is a complete example of using relational placeholders.  
For the sake of simplicity are we using the [internal Expansion setup](#making-an-internal-expansion) here and assume that `SomePlugin` offers a `areFriends(Player, Player)` method that returns true or false based on if the players are friends or not.

```{ .java .annotate title="SomeExpansion.java" }
package at.helpch.placeholderapi.example.expansion;

import at.helpch.placeholderapi.example.SomePlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational
import org.bukkit.ChatColor;
import org.bukkit.Player;
import org.jetbrains.annotations.NotNull;

public class SomeExpansion extends PlaceholderExpansion implements Relational {
    
    private final SomePlugin plugin; // (1)
    
    public SomeExpansion(SomePlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    @NotNull
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors()); // (2)
    }
    
    @Override
    @NotNull
    public String getIdentifier() {
        return "example";
    }
    
    @Override
    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion(); // (3)
    }
    
    @Override
    public boolean persist() {
        return true; // (4)
    }
    
    @Override
    public String onPlaceholderRequest(Player one, Player two, String identifier) {
        if (one == null || two == null) {
            return null; // (5)
        }
        
        if (identifier.equalsIgnoreCase("friends")) { // (6)
            if (plugin.areFriends(one, two)) {
                return ChatColor.GREEN + one.getName() + " and " + two.getName() + " are friends!";
            } else {
                return ChatColor.RED + one.getName() + " and " + two.getName() + " are not friends!";
            }
        }
        
        return null; // (7)
    }
}
```

1.  Mockup plugin used to showcase the use of dependency injection to access specific plugin related data.

2.  We can use the authors set in the plugin's `plugin.yml` file as the authors of this expansion.

3.  Since our expansion is internal can this version be the same as the one defined in the plugin's `plugin.yml` file.

4.  This needs to be set, or else will PlaceholderAPI unregister our expansion during a plugin reload.

5.  Our placeholder requires both players to be present, so if either one is not will this return null.

6.  In case the identifier matches (Meaning the placeholder is `%rel_example_friends%` or `{rel_example_friends}`) will we check if Player one and two are friends through our plugin's `areFriends(Player, Player)` method.  
    Should they be friends, return green text saying they are and else return red text saying they aren't.

7.  Reaching this means that an invalid params String was given, so we return `null` to tell PlaceholderAPI that the placeholder was invalid.

Don't forget to [register your expansion](#register-your-expansion).
///

[placeholderexpansion]: https://github.com/PlaceholderAPI/PlaceholderAPI/blob/master/src/main/java/me/clip/placeholderapi/expansion/PlaceholderExpansion.java
[relational]: https://github.com/PlaceholderAPI/PlaceholderAPI/blob/master/src/main/java/me/clip/placeholderapi/expansion/Relational.java
