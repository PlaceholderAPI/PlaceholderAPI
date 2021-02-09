[Wiki]: https://github.com/PlaceholderAPI/PlaceholderAPI/wiki
[Placeholders]: https://github.com/PlaceholderAPI/PlaceholderAPI/wiki/Placeholders
[Plugins using PlaceholderAPI]: https://github.com/PlaceholderAPI/PlaceholderAPI/wiki/Plugins-using-PlaceholderAPI

# Wiki
This is the wiki folder. It contains all pages that you can find in our [Wiki].  
It allows you to suggest changes through Pull request while keeping a simple way for us to maintain the wiki, without granting everyone push rights to it.

If you want to contribute towards the wiki would we highly recommend to follow the below contributing Guidelines, to keep the overal style of the wiki intact.

## Target branch
The wiki is handled on a separate branch called `docs/wiki`.  
When making a Pull request, make sure to target this specific branch. Any PR not targeting this branch may either be closed or the target changed.

## Adding your resource(s)
When you either have a plugin, which adds and/or uses placeholders or an expansion and you want to add it to the wiki should you follow the below steps.

- [Plugins using PlaceholderAPI](#plugins-using-placeholderapi)
- [Placeholders](#placeholders)

### [Plugins using PlaceholderAPI]
This is only required for plugins.  
If your plugin supports placeholders of other plugins and/or provides own placeholders through PlaceholderAPI should you always add it the the `Plugins using PlaceholderAPI` page.

Each entry on this page follows a specific format that you need to follow:  
```markdown
- **[:name](:url)**
  - [?] Supports placeholders.
  - [?] Provides own placeholders. [:link]
```

| Key     | Description                                                                                                                                                                              |
| ------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `:name` | The name of the Plugin. Plugins are ordered by alphabet and if your plugin shares the same name as another one will you need to add it **below** the other resource.                     |
| `:url`  | The URL to your plugin. Spigot URLs should only contain the ID. E.g. https://www.spigotmc.org/resources/placeholderapi.6245/ becomes https://www.spigotmc.org/resources/6245/            |
| `?`     | Should be replaced with either an `x` or a space, depending on if the option is supported. So the result is either `[x]` or `[ ]`                                                        |
| `:link` | If your plugin also provides own placeholders should you add it to the [Placeholders] page. In such a case should you use `[**[[Link\|Placeholders#:name]]**]` otherwhise just `[Link]` |

### [Placeholders]
This step is required if you either have a plugin or an expansion that provides their own placeholders. You should add your resource to the Placeholders page of the wiki.  
This page is split up into two sections: PAPI Placeholders and Plugin Placeholders.

PAPI Placeholders are extensions that don't require an external plugin or other dependency to function normally. Common examples are the Player or Server extensions.  
The Plugin Placeholders are extensions that require a plugin or other dependency to function. They are often used to provide values from one pluging (e.g. Vault) to another plugin through the help of PlaceholderAPI.

The syntax used for each entry is the same:
````markdown
- ### **[:name](:url)**
> :command

```
:placeholders
```
----
````

| Key             | Description                                                                                                                                                                                        |
| --------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `:name`         | The name of the resource. Resources are ordered by alphabet and if your plugin shares the same name as another one will you need to add it **below** the other resource.                           |
| `:url`          | The URL to your plugin. Spigot URLs should only contain the ID. E.g. https://www.spigotmc.org/resources/placeholderapi.6245/ becomes https://www.spigotmc.org/resources/6245/                      |
| `:command`      | The download command. When your resource is an expansion on the ecloud would you need to add `/papi ecloud download :name`. If it isn't an expansion should you put `NO DOWNLOAD COMMAND` instead. |
| `:placeholders` | List of placeholders your plugin/expansion offers. The list should stay in alphabetical order. You can use `<>` and `[]` to indicate required and optional variables.                              |

#### Extra notes

- If your entry has another one below it will you need to add an empty line, followed by for hypthons (`----`) at the bottom of your entry to separate it.
- You are allowed to add a description between the `> :command` and the placeholder list. Keep in mind to keep an empty line after the command line to prevent wrong formatting. A description is only useful/required if your expansion/plugin offers specific placeholder values and/or features.
- Always add your entry's name to the list at the top of the page in the format `- [:name](#:name)`. Note that if your entry shares the same name as another one on the page and you added it below it, that you will need to append a `-1`, `-2`, ... to the name in the brackets, depending on how many entries with the same name there are.

----
## Other Wiki pages
Please follow these general guidelines when editing any other pages.

### Links
The wiki uses 3 types of links:

- [Reference Links](#reference-links)
- [Wiki Links](#wiki-links)
- [Header Links](#header-links)

#### Reference Links
Reference Links are in the format `[:text]: :url` where `:text` is the name to use as reference and `:url` is the url.  
These types of links are usually put at the top of the page and allow us easier updating of these links, by just altering the URL without the need to replace them in the entire file.

To use a reference link, either use `[:text]` or `[:displayed_text][:text]` to link with a differently shown text.

For example: With `[wiki]: https://github.com/PlaceholderAPI/PlaceholderAPI/wiki` will `[wiki]` become [wiki] and `[to the wiki][wiki]` becomes [to the wiki][wiki].

Always use reference links for URLs that point outside of the Wiki. Only exceptions are Links in the [Placeholders] and [Plugins using PlaceholderAPI] page (See above for details).

#### Wiki Links
Wiki links are used to link to other pages on the wiki.  
These types of links are in the format `[[:pagename]]` or `[[:text|:pagename]]` to display a different text. `:pagename` is case-sensitive.

If you want to link to a header in another wiki page can you use `[[:text|:pagename#:header]]`.

#### Header Links
Header links are used to link to a section within the same wiki page.  
The format is `[:text](#:header)`. The header name is case-insensitive but it's recommended to keep it lowercase.

----
### Lists
Lists should always be started with a hyphen (`-`) to better distinguish it from other formatting characters like the asterisk (`*`) used for bold or italic text.

