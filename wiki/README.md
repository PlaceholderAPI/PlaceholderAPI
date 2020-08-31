[Wiki]: https://github.com/PlaceholderAPI/PlaceholderAPI/wiki
[Placeholders]: https://github.com/PlaceholderAPI/PlaceholderAPI/wiki/Placeholders
[Plugins using PlaceholderAPI]: https://github.com/PlaceholderAPI/PlaceholderAPI/wiki/Placeholders

# Wiki
This is the wiki folder. It contains all pages that you can find in our [Wiki].  
It allows you to suggest changes through Pull request while keeping a simple way for us to maintain the wiki, without granting everyone push rights to it.

If you want to contribute towards the wiki would we highly recommend to follow the below contributing Guidelines, to keep the overal style of the wiki intact.

## Target branch
The wiki is handled on a separate branch called `docs/wiki`.  
When making a Pull request, make sure to target this specific branch. Any PR not targeting this branch may either be closed or the target changed.

## Adding your resource(s)
If you have one or multiple resources that support PlaceholderAPI (being it by just supporting placeholders from other plugins, or providing your own) can you add them to the wiki in the following ways.

### [Plugins using PlaceholderAPI]
You should always add your resource to this page, no matter if it only supports placeholders or also provides its own.

The format of a plugin is always as follows:
```md
- **[:name](:url)**
  - [?] Supports placeholders.
  - [?] Provides own placeholders. [:link]
```

A quick summary over the different parts:

- `:name` is the name of your resource. The resources are ordered by alphabet and if yours has the same name as another one listed, add it __below__ the other resource.
- `:url` should be replaced with a URL to your resource page (Spigot, dev.bukkit, etc.). If you use Spigot, make sure to remove any text after the `/resources/` and only leave the ID. For example will https://www.spigotmc.org/resources/placeholderapi.6245/ become https://www.spigotmc.org/resources/6245/.  
If you don't have your resource on any resource page can you either ommit the URL (Just provide the name) or provide a link to its source, if available.
- `?` should be replaced with either an `x` or a space depending on wether your plugin supports the option or not. So the `[?]` becomes either `[x]` or `[ ]`
- `:link` depends on wether your plugin provides own placeholders or not. If it doesn't, then you can just give `Link`. If it does provide placeholders, make sure to provide `[[Link|Placeholders#:name]]` where `:name` would be the name of your resource in the [Placeholders] page.

### [Placeholders]
If your plugin provides its own placeholders through an extension is it recommendet to add this extension to the [Placeholders] page.  
This page is split up into two sections: PAPI Placeholders and Plugin Placeholders.

PAPI Placeholders are extensions that don't require an external plugin or other dependency to function normally. Common examples are the Player or Server extensions.  
The Plugin Placeholders are extensions that require a plugin or other dependency to function. They are often used to provide values from one pluging (e.g. Vault) to another plugin through the help of PlaceholderAPI.

The overall structure of an entry is always the same:  
````md
- ### **[:name](:url)**
> :command

```
:placeholders
```
----
````

- `:name` is the name of your resource. The resources are ordered by alphabet and if yours has the same name as another one listed, add it __below__ the other resource.
- `:url` should be replaced with a URL to your resource page (Spigot, dev.bukkit, etc.). If you use Spigot, make sure to remove any text after the `/resources/` and only leave the ID. For example will https://www.spigotmc.org/resources/placeholderapi.6245/ become https://www.spigotmc.org/resources/6245/.  
If you don't have your resource on any resource page can you either ommit the URL (Just provide the name) or provide a link to its source, if available.
- `:command` depends on if your extension is available on the eCloud or is build into your resource. If you have it on the eCloud should you provide `/papi ecloud download :name` where `:name` is the name your expansion has on the eCloud.  
If your extension is build into your resource can you just set `NO DOWNLOAD COMMAND` instead.
- `:placeholders` would be a list of all placeholders that your extension offers (Sorted by alphabet). If your placeholders support multiple variables like item names, should you use either `<text>` or `[text]` depending on if it is required or optional.

Always keep an empty line in between the `----` and the next entry below it.  
If your extension is at the very bottom of the page can you ommit the `----`.

You will also need to add your extension's name to the list at the very top in the format `- **[:name](#:name)**` where `:name` is the extension name.

## Other Wiki pages
Please follow these general guidelines when editing any other pages.

### Linking
Linking should always be done through either the reference option or through the Wiki link option.  
When the link leads to a page on the wiki should you use either `[[:page]]` or `[[:name|:page]]` where `:page` would be the name of the Wiki page (Case sensitive) and `:name` the text that would be displayed instead.  
When linking to a section within a Wiki page should you link to it using a hashtag (`#`). For example would linking to the player extension result in `[[Placeholders#player]]` (Always have the section lowercase.  
However, when you link to a section in the same wiki page should you do it in the format `[:name](#:section)` where `:name` is the text to display and `:section` is the name of the section.


When you link to an external page that isn't part of the wiki should you do it as a reference link (Exception is the above mentioned cases for resources and extensions).  
You do this by adding `[:name]: :url` at the top of the page where `:name` is the reference name to use and `:url` is the url to link to.

You can then just use either `[:name]` or `[:display_text][:name]` to link to the url you set (`:display_text` could be any text (including spaces) to display).  
Reference links are case-insensitive.

This system allows us to easly maintain the links without the need to update a URL on several places within the page.

### Lists
Lists should always be started with a hyphen (`-`) to better distinguish it from other formatting characters like the asterisk (`*`) used for bold or italic text.

