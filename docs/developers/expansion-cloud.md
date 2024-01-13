---
description: Information about PlaceholderAPI's expansion cloud, including how to submit your own expansion or update it.
---

# eCloud

## About

PlaceholderAPI uses an expansion-cloud (A website that has all kinds of expansions stored), to download jar files, that contain the placeholders for it to use.

The expansion-cloud can be seen under https://api.extendedclip.com/home

## How it works

PlaceholderAPI connects to the ecloud on startup of your server, to check if the cloud is available and how many expansions are available on it.  
If you run [`/papi ecloud download <expansion>`](../commands.md#papi-ecloud-download), PlaceholderAPI will connect to the site to first check if the specified expansion exists and then downloads it if it does.

/// note
PlaceholderAPI can only download expansions that are verified on the eCloud. Any unverified expansion needs to be downloaded manually.
///

You can disable the connection to the cloud by setting `cloud_enabled` in the config.yml to false.

## Adding your own expansion

You can add your own expansion to the expansion-cloud for others to use.  
In order to do that, you have to follow those steps:

1. Make sure you have created a seperate jar file as described in the [Creating a PlaceholderExpansion](creating-a-placeholderexpansion.md) page.
2. Create an account on the site, or log in, if you already have one.
3. Click on `Expansions` and then on [`Upload New`](https://api.extendedclip.com/manage/add/).
4. Fill out the required information. `Source URL` and `Dependency URL` are optional and would link to the source code and any dependency (plugin) of your expansion respectively.
5. Click on the button that says `Choose an file...` and select the jar of your expansion.
    
    /// warning |
    **Important!**  
    Make sure, that the name of the jar file contains the same version like you set in the version field.
    ///

6. Click on `Submit Expansion`

Your expansion is now uploaded and will be reviewed by a moderator.  
If everything is ok will your expansion be approved and will be available on the ecloud for PlaceholderAPI*.

/// info | Note for Hosts
You can block specific expansions from being downloaded using the `PAPI_BLOCKED_EXPANSIONS` environment variable.  
Just define it with a value of comma-separated expansion names that should not be downloadable by PlaceholderAPI.

This feature exists since version 2.11.4 of PlaceholderAPI.
///

## Updating your expansion

Before you update, please note the following:  
Updating your expansion will automatically make it unverified, requiring a site moderator to verify it again. This was made to combat malware from being uploaded and distributed.

To update your expansion, you first have to go to the list of [your expansions](https://api.extendedclip.com/manage/).  
For that click on `Expansions` and select `Your Expansions`.  
After that, follow those steps:

1. Click the name of the expansion, that you want to update.
2. Click on the button that says `Version`
3. Click on `Add Version`
4. Fill out the fields and upload the new jar.
    
    /// warning |
    **Important!**  
    Make sure, that the name of the jar file contains the same version like you set in the version field.
    ///

5. Click on `Save Changes`

Your version should now be uploaded to the eCloud. You can now ask a responsible staff member on the [HelpChat Discord](https://discord.gg/helpchat) to review your expansion to get it re-verified. Please remain patient and polite when asking.

## Downloading a specific expansion version

In some cases, you may want to use a specific, older version of an expansion. Such a case could be for example, when you run an old server version and the newest version of an expansion uses methods that aren't available on that particular server version, causing compatability issues.  
For that case is there a way, to download a specific version of expansion. You can download the expansion either manually, or through PlaceholderAPI itself.  
Here is how you can do it for each.

### Download with PlaceholderAPI

This is the easiest of both methods since it requires the least amount of effort.  
Run the following command in-game or in your console to download a specific version:  
[`/papi ecloud download <expansion> [version]`](../commands.md#papi-ecloud-download)

To find out, what versions are available for the expansion, run [`/papi ecloud info <expansion>`](../commands.md#papi-ecloud-info).

After you downloaded the specific version, run [`/papi reload`](../commands.md#papi-reload) to refresh the installed expansions.

### Download manually

To download an expansion manually, you first have to connect to the website and go to the expansion of your choice.  
There, you click on the button that says `Version` and click on the download-icon of the version you want to download.

Finally, stop your server, upload the jar to the folder in `/plugins/PlaceholderAPI/expansions` (Make sure to delete the old jar, if there's already one) and start the server again.