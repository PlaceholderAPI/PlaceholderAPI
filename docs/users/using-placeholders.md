# Using Placeholders

This page is intended for server owners or server staff who want to learn how to use placeholders in a plugin.

If you're a developer and would like to learn how to provide placeholders or support placeholders from other plugins in your own, check out [Using PlaceholderAPI](../developers/using-placeholderapi.md).

## Prerequisites

Before you can use placeholders should you check a few things first.

### Plugin supports PlaceholderAPI

The first and most important thing is, to find out if the plugin you want to use placeholders in is actually supporting PlaceholderAPI.  
Chat-plugins such as EssentialsXChat do not natively support PlaceholderAPI and instead require separate plugins to "inject" the parsed placeholders into the final chat message.

One way to check, if a Plugin is supporing PlaceholderAPI, is to check the [Plugins using PlaceholderAPI](plugins-using-placeholderapi.md) page.  
If the plugin is listed and if the `Supports placeholders` text has a check, does it mean that PlaceholderAPI support is available.

If the plugin isn't listed, can you usually check its plugin page, or any other source of information, such as a wiki, for clues on if PlaceholderAPI is supported.

### Proper Internet connection

PlaceholderAPI connects towards an eCloud located under https://api.extendedclip.com to retrieve information about placeholder expansions, but also to download said expansions from it.  
Make sure that your server is allowing external connections to the above URL. If it doesn't, and you're using a host, contact their support and ask them to whitelist this URL.

/// info | Info for hosts
PlaceholderAPI provides and checks for a specific Environment variable to block the download of specific expansions.  
Should you as a host want to block the download of specific expansions, add the `PAPI_BLOCKED_EXPANSIONS` Environment variable containing a comma-separated list of expansion names that PlaceholderAPI should not be able to download.

This feature exists since version 2.11.4 of PlaceholderAPI
///

## Download/Get Expansion

The way PlaceholderAPI's system works, allows a Placeholder Expansion and its corresponding placeholders to either be included within a plugin (If placeholder requires said plugin) or to be available as a separate jar file on the eCloud of PlaceholderAPI.  
Depending on what type you have, will you need to do some extra steps to use the placeholder from the Placeholder Expansion.

One way to find out, if an Expansion is included or separate, is to check the [Placeholder List](placeholder-list.md) page for any entry of it.  
If it exists on the page, can you check, if the line right after the title says `NO DOWNLOAD COMMAND` or `/papi ecloud download ...` (i.e. `/papi ecloud download Player`).

If the line says the former, does it mean, the expansion is part of the plugin and doesn't need any extra steps to be active (Unless the plugin author mentions otherwise).  
In the case of the later, will you need to download the expansion from the eCloud. Simply copy the command and execute it either in the console, or in-game. Afterwards, reload PlaceholderAPI using `/papi reload`.

You can check what expansions are loaded by running `/papi list`.

## Use Expansion

Using the placeholders of the Expansion is a straigh forward process.  
Simply put the right placeholder format (i.e. `%player_name%`) inside whatever configuration option supports it. Please refer to any manuals or wikis a plugin may offer about what options support placeholders.
