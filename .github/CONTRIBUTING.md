[issue]: https://github.com/PlaceholderAPI/PlaceholderAPI/issues/new
[discord]: https://helpch.at/discord
[Code of Conduct]: https://github.com/PlaceholderAPI/PlaceholderAPI/blob/master/CODE_OF_CONDUCT.md

# Contributing Guidelines
We welcome everyone to contribute towards the PlaceholderAPI Project, but doing so will require you to follow specific rules to keep a consistent and welcoming way of contributing.

If you have any questions about this contributing file, feel free to contact us on our [Discord Server][discord].

## Issues
Like any other Project can you encounter bugs or a feature is missing for you in the plugin.  
For that can you open an [issue] to report a bug or suggest a new feature to be added.

When doing so, make sure you follow the below rules.

### Follow the Template
We have Issue templates to help us get the required information more easly. Please follow the provided template when either filing a bug report or feature request.  
We reserve the right to close your issue without warning for not following the templates.

### Using Latest Version
When it comes to bug reports should you always check first, if you're using the latest release of PlaceholderAPI.  
Often is a bug you encounter fixed in a newer release.

You can optionally also try out development builds to see, if your bug is fixed in those.

The same rules apply for when making a feature request.

### No duplicate issue
Make sure that there isn't any existing issue, which is still open, or is closed with a solution/explanation.  
Opening a separate issue for a bug report or feature request, that already exists on the issue tracker only slows down the process of fixing the bug or implementing the feature.

If an issue with the bug or feature you want to report/suggest exists, comment on it with your info (bug reports) or give it a :thumbsup: (Feature Request) to show that this is important for you.

### Issue isn't caused by external sourced
PlaceholderAPI provides a feature to have extensions (separate jar files) for placeholders. This gives it a possability that an issue you encounter is caused by said extensions or a separate plugin that uses this extension.  
In those cases should you report the issue to the issue tracker of the extension or plugin.

## Pull requests
As an open source project are we welcoming all contributions to improve PlaceholderAPI, being it changes to its code like bug fixes or new features, or contributions to its documentation such as the Wiki or the Javadoc.

### Code contributions
Any contributions to PlaceholderAPI's code should be done towards the `develop` branch. Targeting the `master` branch in your Pull request may get it closed without warning.  
Additionally should you follow the `Google Codestyle` when changing the code and Javadoc.

Some noteworthy points about Javadoc:

- `@param` tags are in a single line and any additional param tag after one don't have an empty line in between.  
Example:  
```java
/**
 * @param param1 description
 * @param param2 description
 */
```
- Tags follow the order `@param`, `@return`, `@since` and `@deprecated`
- The `@deprecated` tag should mention the version of when the annotated object will be removed. This usually the next minor version.

## Code of Conduct
We have a [Code of Conduct] to maintain a welcoming atmosphere in this project.  
If your contributions go against the above linked Code of Conduct will we reserve the right t deny or revert your contributions and to permanently remove your access from this project.
