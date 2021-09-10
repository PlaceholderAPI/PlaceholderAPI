[wiki]: https://github.com/PlaceholderAPI/PlaceholderAPI/wiki

[placeholders]: https://github.com/PlaceholderAPI/PlaceholderAPI/wiki/Placeholders
[using_papi]: https://github.com/PlaceholderAPI/PlaceholderAPI/wiki/Plugins-using-PlaceholderAPI

[discord]: https://discord.gg/HelpChat
[discussion]: https://github.com/PlaceholderAPI/PlaceholderAPI/discussions

[andre]: https://github.com/Andre601
[andrew]: https://github.com/Andrew-Chen-Wang
[action]: https://github.com/Andrew-Chen-Wang/github-wiki-action

<!-- Images -->
[branch-selector]: https://user-images.githubusercontent.com/11576465/132779328-8571458a-d63d-4c25-b920-96aa16ae0058.png
[upstream_up-to-date]: https://user-images.githubusercontent.com/11576465/132779917-96f19239-077d-44ed-98e7-6a76f305b33a.png
[upstream_needs_update]: https://user-images.githubusercontent.com/11576465/132779798-1fe3dc72-a6c2-41eb-a8bc-95793671f384.png
[create_branch]: https://user-images.githubusercontent.com/11576465/132780127-45599156-1400-40c9-b865-351574786873.png

# Wiki
Welcome to the Wiki folder!

This folder contains all the files of the [PlaceholderAPI Wiki][wiki].  
It allows you to contribute towards the wiki and us to have more control about what changes are commited to it.

## Contrbuting to the wiki
If you want to contribute towards the wiki, will you need to follow the below instructions to not get your Pull request closed without a warning.

### Fork the Repository (Not wiki!)
> Already having a fork? Skip to the [next Step](#select-target-branch)!

You need to make a fork of the PlaceholderAPI Repository to contribute towards the wiki.  
To do this, click the "Fork" button on the top-right corner of the site and select the account you want your fork to be created on. This will also show you any already existing forks of this Repository that you may have.

The forking process should only take a few seconds and you should be redirected to your fork afterwards.

### Select target branch
All main changes to the wiki are made on the dedicated `docs/wiki` branch.  
Before you try to make any changes should you make sure that you have the `docs/wiki` branch selected. To do that, check the button next to the `X branches` text. If it says anything other than `docs/wiki` will you need to click it and select the right branch.

![branch-selector]

### Fetch Changes from Upstream
This is only required when you already had a fork and didn't update it for some time.

While you're on the `docs/wiki` branch, click the `Fetch upstream` text located right below the green `Code` button.  
Depending on the status of your branch can the prompt show different outcomes:

- `This branch is not behind the upstream PlaceholderAPI:docs/wiki`  
  Your fork's `docs/wiki` branch is up-to-date with the latest changes from Upstream (This Repository). You don't have to update anything.  
  ![upstream_up-to-date]
- `Fetch and merge <number> upstream commits from PlaceholderAPI:docs/wiki`  
  This is shown when your fork's branch is outdated and upstream (This Repository) has changes. Click the `Fetch and merge` button to fetch the latest commits and update your fork's `docs/wiki` branch.  
  ![upstream_needs_update]

### Commit changes
To commit changes will you need to choose, if you want to directly commit to your fork's `docs/wiki` branch, or make a dedicated branch for it.

#### Make separate branch (Optional)
If you want to have a dedicated branch for it, will you need to click the button saying `docs/wiki`, type in the small text field the name of the branch you want to use and click the text saying `Create branch: <branch> from 'docs/wiki'`

![create_branch]

After that should you now have a separate branch that is based of the `docs/wiki` branch and you can finally commit changes to it.

#### Formatting
The wiki uses normal Markdown formatting with some special design-rules you need to keep in mind.  
These rules are as follows:

- Unordered lists need to start with a `-` and not `*`. This is to not get it confused with *italic text* which uses single `*` characters.
- New lines in lists need to keep their indends. This means you need to add two spaces at the start to keep the text in the same line.  
  Example:  
  ```markdown
  - Line 1  
    Line 2
  ```
- Use the `[[Page]]` and `[[Text|Page]]` format to link to other wiki pages. While those aren't rendered in the wiki folder will they render in the final wiki pages.
- External links should be set as Refernce Links, which means they are set as `[text]: link` at the top of the page and then used either through `[text]` or `[display text][text]` througout the page.

#### Adding new Expansion
When you add a new expansion to the wiki's [Placeholder page][placeholders] will you need to follow the following format:  
````markdown
- ### [:name](:link)
  > :command
  
  :text
  
  ```
  :placeholders
  ```
````

There are a few extra rules you need to also keep in mind:

- `:name` would be the name of your Expansion, not the plugin (Unless it is integrated into your plugin).
  - Only embed a link, if your Expansion requires a plugin to function.
  - When linking to a Spigot page, make sure to sanitze the link.  
    This means that f.e. https://www.spigotmc.org/resources/placeholderapi.6245/ becomes https://www.spigotmc.org/resources/6245/
- `:command` should be replaced with either the PlaceholderAPI command to download your expansion from the eCloud (`/papi ecloud download :name`) or with `NO DOWNLOAD COMMAND` if no separate download is available.
- `:text` is optional and should only be provided to link to additional documentation, your own placeholder list, or explain more complicated placeholders/features.
- `:placeholders` will be the list of placeholders your expansion provides.  
  Please avoid specific examples (i.e. `%placeholder_player_user123%`) and instead use `<>` and `[]` to mark required and optional options respecively (`%placeholder_player_<player>%`)

**Note:**  
When your Expansion's entry is after and/or before other entries will you need to add `----` before or after it to separate it from other entries.

Example:  
````markdown
- ### SomeExpansion
  > NO DOWNLOAD COMMAND
  
  ```
  %someexpansion_placeholder%
  ```

----

- ### YourExpansion
  > NO DOWNLOAD COMMAND
  
  ```
  %yourexpansion_placeholder%
  ```

----

- ### AnotherExpansion
  > NO DOWNLOAD COMMAND
  
  ```
  %anotherexpansion_placeholder%
  ```
````

After you added your expansion to this page will you also need to add an entry to the list at the top of the page.  
You do so by adding `- **[:text](#:name)**` to the list, where `:text` is the text to display (Usually the name you set) and `:name` is the name you just set. If your expansion shares the exact same name as another entry on the page will you need to make sure that your expansion is listed **after** the other one AND that the list-entry has a `-1` appended to `#:name` (So f.e. `#expansion` becomes `#expansion-1`).

Finally can you now commit your changes and move forward to the [Plugins using PlaceholderAPI][using_papi] page in the wiki folder.

#### Adding new plugin
This step is only required if you either add a new plugin to the list, or you added an Expansion that is included in your own plugin.

Similar to the [Placeholders page][placeholders] does this page follow a specific format which we will explain real quick.

```markdown
- [:name](:link)
  - [?] Supports placeholders.
  - [?] Provides own placeholders. [:page]
```

Here are the following rules:

- `:name` needs to be replaced with the Name of your plugin.
- `:link` needs to be the link of the plugin's resource page.
  - If no resource page is available can a GitHub repository be linked (if available) or the link omited altogether)
  - When linking to a Spigot page, make sure to sanitze the link.  
    This means that f.e. https://www.spigotmc.org/resources/placeholderapi.6245/ becomes https://www.spigotmc.org/resources/6245/
- `[?]` needs to be replaced with either `[ ]` or `[x]` depending on whether the mentioned option is supported or not.
- `:page` needs to replace with the right value, depending on the conditions.
  - If your plugin provides own Placeholders for other plugins to use can you set `**[[Link|Placeholders#:name]]**` where `:name` is the title you set in the placeholders page.
  - If your plugin does not provide own placeholders will you need to set `Link`.

### Questions?
If you have any questions, do not hesitate to ask in the [HelpChat Discord][discord] or [open a new discussion][discussion] in this repository. We will be happy to help you.

### Credits
- The Wiki is maintained by [Andre601][andre].
- We use the [GitHub Wiki Action][action] by [Andrew-Chen-Wang][andrew] to update the PlaceholderAPI wiki through GitHub Actions.
