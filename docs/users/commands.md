---
description: Available commands within PlaceholderAPI.
---

# Commands

This page shows all commands, including with a detailed description of what every command does.

## Overview

- **[Parse Commands](#parse-commands)**
    - [`/papi bcparse <player|me|--null> <string>`](#papi-bcparse)
    - [`/papi cmdparse <player|me|--null> <string>`](#papi-cmdparse)
    - [`/papi parse <player|me|--null> <string>`](#papi-parse)
    - [`/papi parserel <player> <player> <string>`](#papi-parserel)

- **[eCloud Commands](#ecloud-commands)**
    - [`/papi ecloud clear`](#papi-ecloud-clear)
    - [`/papi ecloud disable`](#papi-ecloud-disable)
    - [`/papi ecloud download <expansion> [version]`](#papi-ecloud-download)
    - [`/papi ecloud enable`](#papi-ecloud-enable)
    - [`/papi ecloud info <expansion> [version]`](#papi-ecloud-info)
    - [`/papi ecloud list <all|<author>|installed>`](#papi-ecloud-list)
    - [`/papi ecloud placeholders <expansion>`](#papi-ecloud-placeholders)
    - [`/papi ecloud refresh`](#papi-ecloud-refresh)
    - [`/papi ecloud status`](#papi-ecloud-status)

- **[Expansion Commands](#expansion-commands)**
    - [`/papi info <expansion>`](#papi-info)
    - [`/papi list`](#papi-list)
    - [`/papi register <jar file>`](#papi-register)
    - [`/papi unregister <jar file>`](#papi-unregister)

- **[Other Commands](#other-commands)**
    - [`/papi dump`](#papi-dump)
    - [`/papi help`](#papi-help)
    - [`/papi reload`](#papi-reload)
    - [`/papi version`](#papi-version)

----

### Parse Commands

These commands are used to parse placeholders into their respective values. Useful for debugging.

#### `/papi bcparse`

/// info |
**Description**:  
Parses placeholders of a String and broadcasts the result to all players.  

**Arguments**:  

- `<player|me|--null>` - The Player to parse values of the placeholder (Use `me` for yourself and `--null` to force a null player (Useful for consoles)).
- `<Text with placeholders>` - The text to parse.

**Example**:  
```
/papi bcparse funnycube My name is %player_name%!
```
///

#### `/papi cmdparse`

/// info |
**Description**:  
Parses placeholders of a String and executes it as a command.

**Arguments**:
 
- `<player|me|--null>` - The Player to parse values of the placeholder (Use `me` for yourself and `--null` to force a null player (Useful for consoles)).
 - `<Command with placeholders>` - The Text to parse and execute as command. Please leave away the `/` of the command.

**Example**:  
```
/papi cmdparse funnycube say My name is %player_name%!
```
///

#### `/papi parse`

/// info |
**Description**:  
Parses the placeholders in a given text and shows the result.

**Arguments**:

- `<player|me|--null>` - The Player to parse values of the placeholder (Use `me` for yourself and `--null` to force a null player (Useful for consoles)).
- `<Text with placeholders>` - The text to parse.

**Example**:  
```
/papi parse funnycube My group is %vault_group%
```
///

#### `/papi parserel`

/// info |
**Description**:  
Parses a relational placeholder.

**Arguments**:

- `<player1>` - The first player.
- `<player2>` - the second player to compare with.
- `<Text with placeholders>` - The actual placeholder to parse.

**Example**:  
```
/papi parserel funnycube extended_clip %placeholder%
```
///

----

### eCloud Commands

These commands all start with `/papi ecloud` and are used for things related about the [Expansion Cloud](../developers/expansion-cloud.md).

#### `/papi ecloud clear`

/// info |
**Description**:  
Clears the cache for the eCloud.
///

#### `/papi ecloud disable`

/// info |
**Description**:  
Disables the connection to the eCloud.
///

#### `/papi ecloud download`

/// info |
**Description**:  
Allows you to download an expansion from the eCloud

**Arguments**:

- `<expansion>` - The expansion to download.
- `[version]` - The specific version of the expansion to download (Optional)

**Example**:  
```
/papi ecloud download Vault
/papi ecloud download Vault 1.5.2
```
///

#### `/papi ecloud enable`

/// info |
**Description**:  
Enables the connection to the eCloud
///
 
#### `/papi ecloud info`

/// info |
**Description**:  
Gives information about a specific Expansion.

**Arguments**:

- `<expansion>` - The Expansion to retrieve information from.
- `[version]` - The Expansion's version to get information from.

**Example**:  
```
/papi ecloud info Vault
```
///

#### `/papi ecloud list`

/// info |
**Description**:  
Lists either all Expansions on the eCloud, only those by a specific author or only those that you have [installed](#papi-ecloud-download).  
Installed Expansions show as green in the list and Expansions that are installed and have an update available show as gold.

**Arguments**:

- `<all|<author>|installed>` - List all Expansions, Expansions of a specific author or all Expnansions you have installed.

**Example**:  
```
/papi ecloud list all
/papi ecloud list clip
/papi ecloud list installed
```
///

#### `/papi ecloud placeholders`

/// info |
**Description**:  
List all placeholders of an Expansion.

**Arguments**:

- `<expansion>` - The Expansion to list placeholders of.

**Example**:  
```
/papi ecloud placeholders Vault
```
///

#### `/papi ecloud refresh`

/// info |
**Description**:  
Refresh the cached data from the eCloud.
///

#### `/papi ecloud status`

/// info |
**Description**:  
Displays the actual Status of the eCloud.
///

----

### Expansion Commands

These commands can be used to manage the expansions that you have currently installed.

#### `/papi info`

/// info |
**Description**:  
Gives you information about the specified Expansion.

**Argument(s)**:

- `<expansion>` - The Expansion to get info from (Needs to be registered and active).

**Example**:  
```
/papi info Vault
```
///

#### `/papi list`

/// info |
**Description**:  
Lists all active/registered expansions.  
This is different to [/papi ecloud list installed](#papi-ecloud-list) in the fact, that it also includes expansions that were installed through a plugin (That aren't a separate   jar-file) and it also > doesn't show which one have updates available.
///

#### `/papi register`

/// info |
**Description**:  
Registers an expansion from a specified filename.  
This is useful in cases, where you downloaded the expansion manually and don't want to restart the server.  
The file needs to be inside `/plugins/PlaceholderAPI/expansions`.

**Arguments**:

- `<filename>` - The file to register (including the file-extension).

**Example**:  
```
/papi register MyExpansion.jar
```
///

#### `/papi unregister`

/// info |
**Description**:  
Unregisters the specified expansion.

**Arguments**:

- `<filename>` - The expansion to unregister.

**Example**:  
```
/papi unregister MyExpansion.jar
```
///

----

### Other Commands

These are other commands of PlaceholderAPI that don't fit any of the above categories.

#### `/papi dump`

/// info |
**Description**:  
Pastes useful information from PlaceholderAPI such as plugin version, server version and installed expansions to https://paste.helpch.at for simple sharing and support.
///

#### `/papi help`

/// info |
**Description**:  
Displays all the commands PlaceholderAPI currently offers.
///

#### `/papi reload`

/// info |
**Description**:  
Reloads the config settings.  
You need to use this command after [downloading Expansions](#papi-ecloud-download) from the eCloud or they won't be properly registered.
///

#### `/papi version`

/// info |
**Description**:  
Shows the current version and authors of PlaceholderAPI.
///
