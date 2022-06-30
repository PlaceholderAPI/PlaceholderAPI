## About
PlaceholderAPI uses an expansion-cloud (A website that has all kinds of expansions stored), to download jar-files, that contain the placeholders for it to use.

The expansion-cloud can be seen under https://api.extendedclip.com/home

## How it works
PlaceholderAPI connects to the ecloud on startup of your server, to check if the cloud is available and how many expansions are available on it.  
If you run `/papi ecloud download <expansion>` PlaceholderAPI will connect to the site to first check if the specified expansion exists and then downloads it if it does.  
Note that not all listed expansions are on the ecloud. Some are in the corresponding plugin itself and are registered on the startup of the server.

You can disable the connection to the cloud by setting `cloud_enabled` in the config.yml to false.

## Adding your own expansion
You can add your own expansion to the expansion-cloud for others to use.  
In order to do that, you have to follow those steps:
1. Make sure you have created a seperate jar-file like explained on the page [[PlaceholderExpansion]].
2. Create an account on the site, or log in, if you already have one.
3. Click on `Expansions` and then on [`Upload New`](https://api.extendedclip.com/manage/add/).
4. Fill out the required information. `Source URL` and `Dependency URL` are optional.
5. Click on the button that says `Choose an file...` and select the jar of your expansion.
    * **Important**! Make sure, that the name of the jar-file contains the same version like you set in the version-field!
6. Click on `Submit Expansion`

Your expansion is now uploaded and will be reviewed by a moderator.  
If everything is ok will your expansion be approved and will be available on the ecloud for PlaceholderAPI*.

> *You can only download verified Expansions through PlaceholderAPIs command, unless you enable the option `cloud_allow_unverified_expansions` in the config.yml  
> Unverified expansions can be downloaded manually by going to the site and download it yourself.

## Updating your expansion
Before you update, please note the following:  
If you aren't a verified dev and you upload an update, your expansion will become **unverified** until a moderator reviews the update and approves it!  
It is recommended to only update the expansion, if it contains huge changes or bug fixes.

To update your expansion, you first have to go to the list of [your expansions](https://api.extendedclip.com/manage/).  
For that click on `Expansions` and select `Your Expansions`.  
After that, follow those steps:
1. Click the name of the expansion, that you want to update.
2. Click on the button that says `Version`
3. Click on `Add Version`
4. Fill out the fields and upload the new jar.
    * **Important**! Make sure, that the name of the jar-file contains the same version like you set in the version-field!
5. Click on `Save Changes`

If you're a verified dev, your version will be approved and is available directly.  
If you aren't a verified dev, you have to wait until a moderator approves the update.

## Downloading a specific expansion version
In some cases, you may want to use a specific, older version of expansion. Such a case could be for example, when you run an old server version and the newest version of an expansion uses methods that aren't available on that particular server version.  
For that case is there a way, to download a specific version of expansion. You can download the expansion either manually, or through PlaceholderAPI itself.  
Here is how you can do it for each.

### Download with PlaceholderAPI
This is the easiest of both methods since it requires the least amount of effort.  
Run the following command in-game or in your console to download a specific version:  
`/papi ecloud download <expansion> [version]`

To find out, what versions are available for the expansion, run `/papi ecloud info <expansion>`  
You can then run `/papi ecloud versioninfo <expansion> <version>` to receive more infor about a specific version.

After you downloaded the specific version, run `/papi reload` to refresh the installed expansions.

### Download manually
To download an expansion manually, you first have to connect to the website and go to the expansion of your choice.  
There, you click on the button that says `Version` and click on the download-icon of the version you want to download.

Finally, stop your server, upload the jar to the folder in `/plugins/PlaceholderAPI/expansions` (Make sure to delete the old jar, if there's already one) and start the server again.