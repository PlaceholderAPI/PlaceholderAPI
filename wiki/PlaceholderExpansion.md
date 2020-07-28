## Overview
This page covers how you can use the `PlaceholderExpansion` to add own placeholders to PlaceholderAPI, which then can be used by other plugins.

PlaceholderAPI is using Expansions for its placeholders, with PlaceholderAPI providing the core. Users can download Expansions from the cloud server using commands in-game or by going [here](https://api.extendedclip.com/home/) if they want to use the placeholder.

## Note
You can either make a separate jar file, to upload it to the expansion-cloud (recommended) or have it as a local class inside your plugin.

## Examples
There are multiple methods and ways you can use the PlaceholderExpansion.  
Those depend on what you want to display through the placeholders in the end.

* [Without external plugin](#without-external-plugin)
* [With external plugin](#with-external-plugin)
  * [Separate jar](#separate-jar)
  * [Internal class](#internal-class)

### Without an external plugin
This part here covers how you create an expansion that doesn't require any external/additional plugins to function.  
Examples of such expansions are:
- [Player expansion](/PlaceholderAPI/Player-Expansion)
- [Math expansion](https://github.com/Andre601/Math-Expansion)
- [Statistics expansion](/PlaceholderAPI/Statistics-Expansion)

Since it would be weird (and also make no real sense) to have this inside your plugin, we assume you make a separate jar-file as an expansion.

To begin, first make the class extend the `PlaceholderExpansion` and add the required methods:  
```java
package at.helpch.placeholderapi.example.expansions;

import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

/**
 * This class will automatically register as a placeholder expansion 
 * when a jar including this class is added to the directory 
 * {@code /plugins/PlaceholderAPI/expansions} on your server.
 *
 * <p>If you create such a class inside your own plugin, you have to
 * register it manually in your plugins {@code onEnable()} by using 
 * {@code new YourExpansionClass().register();}
 */
public class SomeExpansion extends PlaceholderExpansion {

    /**
     * This method should always return true unless we
     * have a dependency we need to make sure is on the server
     * for our placeholders to work!
     *
     * @return always true since we do not have any dependencies.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     * 
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor(){
        return "someauthor";
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest 
     * method to obtain a value if a placeholder starts with our 
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier(){
        return "example";
    }

    /**
     * This is the version of this expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion(){
        return "1.0.0";
    }
  
    /**
     * This is the method called when a placeholder with our identifier 
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests (recommended).
     *
     * @param  player
     *         A {@link org.bukkit.OfflinePlayer OfflinePlayer}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return Possibly-null String of the requested identifier.
     */
    @Override
    public String onRequest(OfflinePlayer player, String identifier){
  
        // %example_placeholder1%
        if(identifier.equals("placeholder1")){
            return "placeholder1 works";
        }

        // %example_placeholder2%
        if(identifier.equals("placeholder2")){
            return "placeholder2 works";
        }

        // We return null if an invalid placeholder (f.e. %example_placeholder3%) 
        // was provided
        return null;
    }
}
```
----

### With external plugin
Those examples here applies to people who want to provide information from their own plugin through placeholders from PlaceholderAPI.  
There exists a repository showcasing an [example-expansion](/PlaceholderAPI/Example-Expansion) with what you can/should do.

In our examples do we have the plugin `SomePlugin` and want to show certain placeholders with it.

There are two ways to actually get information from your plugin and they only are different in if you have the expansion as a separate jar-file or as an internal class.

#### Separate jar
In our separate jar do we have to make some checks, to be sure, that the required plugin is installed and running.  
The below code shows how the class can look like in case the plugin doesn't have a public and easy to access API.

```java
package at.helpch.placeholderapi.example.expansions;

import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import at.helpch.placeholderapi.example.SomePlugin;

/**
 * This class will automatically register as a placeholder expansion 
 * when a jar including this class is added to the directory 
 * {@code /plugins/PlaceholderAPI/expansions} on your server.
 *
 * <p>If you create such a class inside your own plugin, you have to
 * register it manually in your plugins {@code onEbale()} by using 
 * {@code new YourExpansionClass().register();}
 */
public class SomeExpansion extends PlaceholderExpansion {

    // We get an instance of the plugin later.
    private SomePlugin plugin;

    /**
     * Since this expansion requires api access to the plugin "SomePlugin" 
     * we must check if said plugin is on the server or not.
     *
     * @return wether the required plugin is installed.
     */
    @Override
    public boolean canRegister(){
        return (plugin = (SomePlugin) Bukkit.getPluginManager().getPlugin(getRequiredPlugin())) != null;
    }

    /**
     * The name of the person who created this expansion should go here.
     * 
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor(){
        return "someauthor";
    }
 
    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest 
     * method to obtain a value if a placeholder starts with our 
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier(){
        return "someplugin";
    }
  
    /**
     * if the expansion requires another plugin as a dependency, the 
     * proper name of the dependency should go here.
     * <br>Set this to {@code null} if your placeholders do not require 
     * another plugin to be installed on the server for them to work.
     *
     * <p>This is extremely important to set your plugin here, since if 
     * you don't do it, your expansion will throw errors.
     *
     * @return The name of our dependency.
     */
    @Override
    public String getRequiredPlugin(){
        return "SomePlugin";
    }

    /**
     * This is the version of this expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion(){
        return "1.0.0";
    }
  
    /**
     * This is the method called when a placeholder with our identifier 
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests (recommended).
     *
     * @param  player
     *         A {@link org.bukkit.Player Player}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */
    @Override
    public String onRequest(OfflinePlayer player, String identifier){

        if(p == null){
            return "";
        }

        // %someplugin_placeholder1%
        if(identifier.equals("placeholder1")){
            return plugin.getConfig().getString("placeholder1", "value doesnt exist");
        }

        // %someplugin_placeholder2%
        if(identifier.equals("placeholder2")){
            return plugin.getConfig().getString("placeholder2", "value doesnt exist");
        }
 
        // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%) 
        // was provided
        return null;
    }
}
```

----
#### Internal class
You can have the class inside your plugin.  
This has some advantages but can also have some disadvantages.
If you include a PlaceholderExpansion class in your plugin, you MUST add an override the persist() method to return true
otherwise, if PlaceholderAPI is reloaded, your expansion will be unregistered and lost forever.


```java
package at.helpch.placeholderapi.example.expansions;

import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import at.helpch.placeholderapi.example.SomePlugin;

/**
 * This class will be registered through the register-method in the 
 * plugins onEnable-method.
 */
public class SomeExpansion extends PlaceholderExpansion {

    private SomePlugin plugin;

    /**
     * Since we register the expansion inside our own plugin, we
     * can simply use this method here to get an instance of our
     * plugin.
     *
     * @param plugin
     *        The instance of our plugin.
     */
    public SomeExpansion(SomePlugin plugin){
        this.plugin = plugin;
    }

    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }

    /**
     * Because this is a internal class, this check is not needed
     * and we can simply return {@code true}
     *
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     * <br>For convienience do we return the authors from the plugin.yml
     * 
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest 
     * method to obtain a value if a placeholder starts with our 
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier(){
        return "someplugin";
    }

    /**
     * This is the version of the expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * For convienience do we return the version from the plugin.yml
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    /**
     * This is the method called when a placeholder with our identifier 
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests (recommended).
     *
     * @param  player
     *         A {@link org.bukkit.Player Player}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */
    @Override
    public String onRequest(Player player, String identifier){

        if(player == null){
            return "";
        }

        // %someplugin_placeholder1%
        if(identifier.equals("placeholder1")){
            return plugin.getConfig().getString("placeholder1", "value doesnt exist");
        }

        // %someplugin_placeholder2%
        if(identifier.equals("placeholder2")){
            return plugin.getConfig().getString("placeholder2", "value doesnt exist");
        }
 
        // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%) 
        // was provided
        return null;
    }
}
```

As you can see is this method pretty similar to the one without any external plugins, since we can get an instance of our plugin much easier and also have a 100% guarantee that the plugin is installed and running.

Our final step now is to register the class and its placeholders. The plugin doesn't do this on its own.  
To achieve this, add the following to your `onEnable()` section (Use your expansion name of course):  
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
