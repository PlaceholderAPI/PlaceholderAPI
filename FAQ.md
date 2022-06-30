[readme]: https://github.com/PlaceholderAPI/PlaceholderAPI/blob/docs/wiki/wiki/README.md

Here are frequently asked questions about stuff related to PlaceholderAPI.

## It only shows `%placeholder%` and not the variable
When a plugin or `/papi parse me %placeholder%` only returns the placeholder itself and no value should you check for the following things:

### The expansion is actually installed.
In many cases is the cause that the expansion of the placeholder is missing.  
Just execute `/papi ecloud download <name of expansion>` followed by `/papi reload` to activate it. You can find a list of Expansions and their Placeholders [[on this page|Placeholders]].

**NOTE!**  
Not all placeholders come in their own expansion. Some plugins *hardcode* them in and load them on startup, when hooking into PlaceholderAPI.

### Plugin actualls supports PlaceholderAPI
It can happen that the plugin you use to display the placeholder in doesn't support PlaceholderAPI. In such a case check, if the parse command returns the actual value of a placeholder.  
If that is the case while the plugin is still displaying the placeholder, can this be an indicator of the plugin not supporting PlaceholderAPI.

You can find a list of plugins supporting PlaceholderAPI [[here|Plugins-using-PlaceholderAPI]].  
Just make sure that "Supports placeholders" has a check mark in front of it.

### No typo in the placeholder
Double-check that the placeholder you set doesn't contain a typo. You can use `/papi ecloud placeholders <expansion>` (replace `<expansion>` with the name of the expansion) to get a list of all the placeholders the expansion may have.  
Keep in mind that this only works for separate expansions on the eCloud and not for those that are loaded by plugins.

### Plugin is enabled
If an expansion depends on a plugin, make sure you have the plugin installed and that it is enabled (Shows green in `/pl`).

## I can't download the expansion
Make sure, that the connection to the cloud (https://api.extendedclip.com) isn't blocked by a firewall or similar.  
Next step would be to check if the expansion actually exists on the cloud. Not all plugins provide their placeholders through a separate jar on the cloud. Some have them build in and register them on startup.

If both checks failed, go to the cloud-page and download the jar manually. Put it then in the `expansions` folder of PlaceholderAPI (`/plugins/PlaceholderAPI/expansions`)

## How can other plugins use my placeholders with PlaceholderAPI?
A tutorial can be found [[here|Hook into PlaceholderAPI]]!

## Can I help on this wiki?
You sure can!  
We welcome contributions to our wiki by everyone. If you found a typo or want to improve this wiki in another way, head over to the [Wiki's readme file][readme] to find out about how you can contribute towards this wiki.

## PlaceholderAPI is posting an error about an outdated expansion?
```
[00:00:01 ERROR]: [PlaceholderAPI] Failed to load Expansion class <expansion> (Is a dependency missing?)
[00:00:01 ERROR]: [PlaceholderAPI] Cause: NoClassDefFoundError <path>
```

If you receive the above error, try to do the following steps:

- Make sure any required dependency of the mentioned expansion (e.g. a plugin) is installed.
- Make sure you use the latest version supported for the server version you use.
- If you downloaded the jar from the ecloud, make sure it isn't malformed/corrupted.

If the issue persists after you've done those checks, report it to the author of the expansion.  
In most cases is the issue that either a dependency is missing or that the expansion tries to use outdated methods from PlaceholderAPI.
