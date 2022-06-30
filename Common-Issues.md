This page lists common issues you may encounter with PlaceholderAPI and how you can solve them.

If you have more questions, feel free to join the [Discord Server](https://discord.gg/helpchat).

## `java.lang.NoClassDefFoundError: com/google/gson/Gson`

If you encounter an issue such as

```
org.bukkit.plugin.InvalidPluginException: java.lang.NoClassDefFoundError: com/google/gson/Gson
```

does it mean that the Server you're using PlaceholderAPI on does not have Gson included.  
This is often the case for servers running 1.8 or older. To fix this, make sure to use at least 1.8.8 as that version does include the required dependency.

## Expansions won't work

If one or multiple expansions don't work, make sure you checked the following:

- You executed `/papi reload` after downloading the expansion(s).
- Any required plugin is installed and enabled.
- The expansion is valid (See next sections).

## `Failed to load expansion class <expansion> ...`

### `- One of its properties is null which is not allowed`

When this error appears does it mean that either `getAuthor()`, `getIdentifier()` or `getVersion()` in the expansion return `null` which is not allowed.  
In such a case, contact the developer of the expansion and inform them about this issue and that it should be fixed.

### `(Is a dependency missing?)`

This error is given whenever the expansion cannot be loaded, which often happens due to a missing dependency (required plugin) or because creating an expansion instance failed.

The only thing you can do is to provide the full error so that we can check if the issue is caused by PlaceholderAPI (More unlikely) or by the expansion.