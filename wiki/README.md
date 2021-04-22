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

| Key     | Description                                                                                                                                                                 |
| ------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `:name` | The name of the Plugin. Plugins are ordered by alphabet and if your plugin shares the same name as another one will you need to add it **below** the other resource.        |
| `:url`  | This should be a URL to either the Spigot page of the plugin, to the GitHub Repository or similar. You can leave the URL away and only use `:name` if there isn't any link. |
| `?`     | Should be replaced with either an `x` or a space, depending on if the option is supported. So the result is either `[x]` or `[ ]`                                           |
| `:link` | Should be replaced with either `**[[Link\|Placeholders#:name]]**` or just `Link` depending on if your resource has own Placeholders listed in the [[Placeholders]] page.    |                                                 |

### [Placeholders]
If you either have an Expansion that provides placeholders, or your plugin provides its own to use in other plugins, should you add it to this page.

Each entry in the page follows the same general syntax:  
````markdown
- ### **[:name](:url)**
  > :command
  
  :description
  
  ```
  :placeholders
  ```
````

Please take note of the indent, which is 2 spaces.  
This aligns the content with the list's indent to make it look better.

| Key             | Description                                                                                                                                                                                |
| --------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `:name`         | The name to show. This should either be the plugin's name, or the name used on the eCloud.                                                                                                 |
| `:url`          | This should be a URL to either the Spigot page of the plugin, to the GitHub Repository or similar. You can leave the URL away and only use `:name` if there isn't any link.                |
| `:command`      | The command to use for downloading the expansion.<br>If your expansion is hardcoded into the plugin or the jar is downloaded on a different place can you put `NO DOWNLOAD COMMAND` there. |
| `:description`  | Optional. A brief description about the Expansion. You can also link to any page explaining the placeholders in more detail.                                                               |
| `:placeholders` | List of placeholders provided by the expansion. The placeholders should be in alphabetical order and you can use `<>` and `[]` to indicate required and optional parameters respectively.  |

### Special Notes
- When linking to a resource page of Spigot will you need to use the following URL format, where you replace `:id` with the resource ID:  
  https://spigotmc.org/resources/:id
- On the Placeholders page, if your Entry has other Entries before or after it will you need to add four hyphens (`----`) surrounded by an empty line before and after it, depending on whether there is another entry before/after yours.
- You need to add your Entry in the list at the top of the Placeholders page.  
  The format is `- **[:name](#:header)**` where `:name` is the same as in the entry and `:header` is the lowercase version of it.  
  When there are more than one Expansion with the same name will you need to add yours **below** the existing one and add `-1` to the header.

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

