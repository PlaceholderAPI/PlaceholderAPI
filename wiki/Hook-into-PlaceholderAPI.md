[APIBadge]: https://img.shields.io/nexus/r/http/repo.extendedclip.com/me.clip/PlaceholderAPI.svg?label=API-Version

[SpigotBadge]: https://img.shields.io/spiget/version/6245?label=Spigot
[Spigot]: https://spigotmc.org/resources/6245

[GitHubBadge]: https://img.shields.io/github/v/release/PlaceholderAPI/PlaceholderAPI?label=GitHub%20Release
[GitHub]: /PlaceholderAPI/PlaceholderAPI/releases/latest

> [![SpigotBadge]][Spigot] [![GitHubBadge]][GitHub]
>
> ![APIBadge]  
>
> *The GitHub release may be different from the spigot release*

This page is about using PlaceholderAPI in your own plugin, to either let other plugins use your plugin, or just use placeholders from other plugins in your own.

Please note, that the examples in this page are only available for **PlaceholderAPI 2.10.0 or higher**!

## First steps
Before you can actually make use of PlaceholderAPI, you first have to import it into your project.

### Import with Maven
To import PlaceholderAPI, simply add the following code to your **pom.xml**  
Replace `{VERSION}` with the version listed at the top of this page.  
```xml
    <repositories>
        <repository>
            <id>placeholderapi</id>
            <url>https://repo.extendedclip.com/content/repositories/placeholderapi/</url>
        </repository>
    </repositories>
    <dependencies>
        <dependency>
         <groupId>me.clip</groupId>
          <artifactId>PlaceholderAPI</artifactId>
          <version>{VERSION}</version>
         <scope>provided</scope>
        </dependency>
    </dependencies>
```

### Import with Gradle
Here is how you can import PlaceholderAPI through gradle.  
Put this into your **Gradle.build**.  
Replace `{VERSION}` with the version listed at the top of this page.  
```gradle
repositories {
    maven {
        url = 'https://repo.extendedclip.com/content/repositories/placeholderapi/'
    }
}

dependencies {
    compileOnly 'me.clip:PlaceholderAPI:{VERSION}'
}
```

### Set PlaceholderAPI as (soft)depend
Next step is to go to your plugin.yml and add PlaceholderAPI as a depend or softdepend, depending (no pun intended) on if it is optional or not.

**Example Softdepend**:
```yaml
name: ExamplePlugin
version: 1.0
author: author
main: your.main.path.here

softdepend: [PlaceholderAPI] # This is used, if your plugin works without PlaceholderAPI.
```

**Example Depend**:
```yaml
name: ExamplePlugin
version: 1.0
author: author
main: your.main.path.here

depend: [PlaceholderAPI] # If your plugin requires PlaceholderAPI, to work, use this.
```

## Adding placeholders to PlaceholderAPI

A full guide on how to create expansions can be found on the [[PlaceholderExpansion]] page of this wiki.

## Setting placeholders in your plugin
PlaceholderAPI offers the ability, to automatically parse placeholders from other plugins within your own plugin, giving the ability for your plugin to support thousands of other placeholders without depending on each plugin individually.  
To use placeholders from other plugins in our own plugin, we simply have to [(soft)depend on PlaceholderAPI](#set-placeholderapi-as-softdepend) and use the `setPlaceholders` method.

It is also important to point out, that any required plugin/dependency for an expansion has to be on the server and enabled, or the `setPlaceholders` method will just return the placeholder itself (do nothing).

**Example**:  
Let's assume we want to send an own join message that shows the group a player has.  
To achieve that, we can do the following:  
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
            throw new RuntimeException("Could not find PlaceholderAPI!! Plugin can not work without it!");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        String joinText = "%player_name% &ajoined the server! He/she is rank &f%vault_rank%";

        /*
         * We parse the placeholders using "setPlaceholders"
         * This would turn %vault_rank% into the name of the Group, that the
         * joining player has.
         */
        joinText = PlaceholderAPI.setPlaceholders(event.getPlayer(), joinText);

        event.setJoinMessage(withPlaceholdersSet);
    }
}
```
