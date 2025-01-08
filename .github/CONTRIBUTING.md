[issue]: https://github.com/PlaceholderAPI/PlaceholderAPI/issues/new
[discord]: https://helpch.at/discord
[code of conduct]: https://github.com/PlaceholderAPI/PlaceholderAPI/blob/master/CODE_OF_CONDUCT.md
[wiki]: https://github.com/PlaceholderAPI/PlaceholderAPI/blob/wiki
[master]: https://github.com/PlaceholderAPI/PlaceholderAPI/tree/master
[docs-wiki]: https://github.com/PlaceholderAPI/PlaceholderAPI/tree/docs/wiki
[style]: https://github.com/PlaceholderAPI/PlaceholderAPI/tree/master/config/style

# Contributing Guidelines
We welcome everyone to contribute towards the PlaceholderAPI Project, but doing so will require you to follow specific rules to keep a consistent and welcoming way of contributing.

If you have any questions about contributions towards the project, feel free to contact us on our [Discord Server][discord].

## Issues
Like any other project can you encounter bugs or a feature is missing for you in the plugin.  
For that, you can open an [issue] to report a bug, or suggest a new feature to be added.

When doing so, make sure you follow rules below:

### Follow the template
We have issue templates to help us get the required information more easily. Please follow the provided template when either filing a bug report or feature request.  
Your issue may be closed without warning for not following the template.

### Use the latest version
When it comes to bug reports should you always check first, that you're using the latest release of PlaceholderAPI.  
Often the bug you've encountered, is fixed in a newer version.

The same rules apply when making a feature request.

### No duplicate issue
Make sure that there aren't any existing issues relating to the problem, which are still open, or are closed with a solution/explanation.  
Opening a separate issue for a bug report or feature request, that already exists on the issue tracker only slows down the process of fixing the bug or implementing the feature.

If an issue with the bug or feature you want to report/suggest exists, comment on it with your info (bug reports) or give it a :thumbsup: (Feature Request) to show that this is important for you.

### Issue isn't caused by external source
PlaceholderAPI provides a feature to have expansions (separate jar files) for placeholders. This gives it a possability that an issue you encounter is caused by said expansions or a separate plugin that uses those expansions.  
In those cases should you report the issue to the issue tracker of the expansion or plugin.

## Pull requests
As an open source project are we welcoming all contributions to improve PlaceholderAPI, being it changes to its code, or contributions to its documentation such as the [Wiki] or the Javadocs.

> [!IMPORTANT]
> When contributing, make sure to both base of and target the mentioned branch. Pull requests targeting the wrong branch may get closed without a warning.

### Code contributions
> **Source and Target Branch:** [`master`][master]

When contributing towards the code of PlaceholderAPI, be it new features or just bug fixes, your changes should follow the general code styling used in the project.  
You can find the necessary files in the [`config/style`][style] directory of this repository.

### Javadocs contributions
> **Source and Target Branch:** [`master`][master]*

Javadocs changes should usually be combined with [code contributions](#code-contributions) when possible, but if not, make sure the changes are significant enough to warrant a new build on our CI server.

\*This branch may change in the future.

### Wiki contributions
> **Source and Target Branch:** [`wiki`][docs-wiki]

The Wiki of PlaceholderAPI is located on its own dedicated branch, hosting all the assets and files that get used to create it through the usage of GitHub Actions and GitHub Pages.  
We welcome contributions that update outdated information, add new expansions/plugins supporting PlaceholderAPI or even correct spelling mistakes and typos.

## Code of Conduct
We have a [Code of Conduct] to maintain a welcoming atmosphere in this project.  
If your contributions go against the Code of Conduct, linked above, we reserve the right to deny or revert your contributions.
