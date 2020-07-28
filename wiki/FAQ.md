Here are frequently asked questions about stuff related to PlaceholderAPI.

## It only shows %placeholder% and not the variable
Make sure, that you've tried the following steps:
- PlaceholderAPI is installed and running (Shows green in `/pl` and responds to the `/papi` command)
- You downloaded the expansion with `/papi ecloud download [expansion]`  
A list of available expansions and their placeholders can be found [[here|Placeholders]]!  
Also note that not all placeholders are in a seperate expansion. Some are "hardcoded" into a plugin.
- You reloaded PlaceholderAPI with `/papi reload` after downloading an expansion.
- Any possible dependency for the expansion (Plugin) is installed and running.
- The placeholder doesn't contain any typos.
- The plugin that should use the placeholder actually supports PlaceholderAPI.
For a list of plugins supporting PlaceholderAPI go [[here|Plugins-using-PlaceholderAPI]].

## I can't download the expansion
Make sure, that the connection to the cloud (https://api.extendedclip.com) isn't blocked by a firewall or similar.  
Next step would be to check if the expansion actually exists on the cloud. Not all plugins provide their placeholders through a separate jar on the cloud. Some have them build in and register them on startup.

If both checks failed, go to the cloud-page and download the jar manually. Put it then in the `expansions` folder of PlaceholderAPI (`/plugins/PlaceholderAPI/expansions`)

## How can other plugins use my placeholders with PlaceholderAPI?
A tutorial can be found [[here|Hook into PlaceholderAPI]]!