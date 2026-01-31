---
description: List of frequently asked questions and their answers.
---

# FAQ

Here are frequently asked questions about stuff related to PlaceholderAPI.

## What is an Expansion?

An expansion (or PlaceholderExpansion) refers to either a jar file or part of a plugin that provides placeholders to use through PlaceholderAPI itself.  
Whether said expansion is a separate jar file or part of a plugin depends on the expansion itself and its main purpose.

Expansions that are separate jar files can be found on the eCloud and are downloadable through [`/papi ecloud download <expansion>`](users/commands.md#papi-ecloud-download) if the expansion is verified.

## It only shows `%placeholder%` and not the variable

When a plugin or [`/papi parse me %placeholder%`](users/commands.md#papi-parse) only returns the placeholder itself and no value should you check for the following things:

- ### The expansion is actually installed.
  
    Some expansions may not be integrated into a plugin or don't even have a plugin to depend on, meaning that they may be their own separate jar file that you have to download.  
    Such expansions can usually be found on the eCloud of PlaceholderAPI and be downloaded using the [`/papi ecloud download <expansion>`](users/commands.md#papi-ecloud-download) command.
    
    Whether an expansion is available on the eCloud or not can be found out in the [Placeholder List](users/placeholder-list/index.md) with any expansion displaying a papi command being downlodable.
  
- ### Plugin actually supports PlaceholderAPI

    It can happen that the plugin you use to display the placeholder in doesn't support PlaceholderAPI. In such a case check, if the parse command returns the actual value of a placeholder.  
    If that is the case while the plugin is still displaying the placeholder, can this be an indicator of the plugin not supporting PlaceholderAPI.
    
    You can find a list of plugins supporting PlaceholderAPI [here](users/plugins-using-placeholderapi.md).  
    Just make sure that "Supports placeholders" has a check mark in front of it.
  
- ### No typo in the placeholder
    
    Double-check that the placeholder you set doesn't contain a typo. You can use [`/papi ecloud placeholders <expansion>`](users/commands.md#papi-ecloud-placeholders) (replace `<expansion>` with the name of the expansion) to get a list of all the placeholders the expansion may have.  
    Keep in mind that this only works for separate expansions on the eCloud and not for those that are loaded by plugins.
    
    Additionally can the placeholder list from the eCloud be outdated. It is recommended to check the [Placeholder List](users/placeholder-list/index.md) or see if there is any documentation for the placeholders you want to use.
  
- ### Plugin is enabled
    
    If an expansion depends on a plugin, make sure you have the plugin installed and that it is enabled (Shows green in `/pl`).

## I can't download the expansion

Make the following checks:

1. The connection to the eCloud (Located at https://api.extendedclip.com) is not blocked through a firewall or your server host.
2. The expansion you want to use is actually on the eCloud. Some expansions are included in a plugin directly.
3. The expansion is verified. Only verified expansions can be downloaded through PlaceholderAPI's download command. This is a security measure to prevent the spread of malware.

If the above checks are all fine and you still can't get the expansion through the download command, consider downloading it manually.  
To do that, head to the expansion's page on the ecloud, download the jar file and put it into `/plugins/PlaceholderAPI/expansions/` before using [`/papi reload`](users/commands.md#papi-reload).

## How can other plugins use my placeholders with PlaceholderAPI?

See the [Using PlaceholderAPI](developers/using-placeholderapi.md) page.

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

[readme]: https://github.com/PlaceholderAPI/PlaceholderAPI/blob/wiki/README.md