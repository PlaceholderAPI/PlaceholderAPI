[issue]: https://github.com/PlaceholderAPI/PlaceholderAPI/issues/new?template=change_request_placeholderapi.md
[Discord]: https://helpch.at/discord

# Contributing

When contributing to this repository, please first discuss the change you wish to make via [issue] or through [Discord] with the owners of this repository before making a change. 

Please note we have a code of conduct, please follow it in all your interactions with the project.

## Pull Request Process

When creating a Pull request should you follow the below rules:

- When pushing towards the code of PlaceholderAPI make the Pull request target the `develop` branch of this repository.
- When pushing towards the wiki (Towards the dedicated Wiki folder) make the Pull request target the `docs/wiki` branch of this repository.
- Any Pull requests targeting the `master` branch for above reasons will be denied and closed.

### Code structure
When changing the code of PlaceholderAPI should you follow these basic guidelines for how to do stuff.

#### Codestyle
We follow the Google Codestyle for the project. Please make sure that you follow it when contributing code to this project.

#### Documentation
When you implement/change methods that are supposed to be usable by the end-user (developer) is it required to also add or update the corresponding Javadocs of the methods.  
Deprecating a method requires you to add a `@deprecated` tag to the comments and mention the next version this may get removed.
