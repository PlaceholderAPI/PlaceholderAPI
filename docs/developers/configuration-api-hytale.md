---
description: How to create configurable expansions in Hytale.
---

# Hytale's Configurable<T> Interface

Unlike in spigot, in Hytale PAPI does not forward through Hytale's configuration API and instead implements its own. Effectively you'll be required to create a class representing your configuration section in PlaceholderAPI's main config.yml, and PAPI will handle the rest.

## Create Your Config Class

```java { .annotate title="CoolExpansionConfig.java" }
public final class CoolExpansionConfig {
	private String test;
	private String dateFormat;

	public CoolExpansionConfig(String test, String dateFormat) {
		this.test = test;
		this.dateFormat = dateFormat;
	}

	public String test() {
		return test;
	}

	public String dateFormat() {
		return dateFormat;
	}
}
```

Your config class needs to represent your config structure exactly. The only difference is in naming, in the above example, dateFormat will be converted to snake case (date_format) when reading the yaml. The above class will look like this in PAPI's config:

```yaml { .annotate title="HelpChat_PlaceholderAPI/config.yml" }
expansions:
    coolexpansion:
        test: wew
        date_format: "dd/mm/yyyy"
```

## Using Your Config Class

Once created, we need to tell PAPI about it. In your PlaceholderExpansion class, implement `Configurable<CoolExpansionConfig>`, you'll then need to also implement the 2 required methods.

```java { .annotate title="CoolExpansion.java" }
public final class CoolExpansion extends PlaceholderExpansion implements Configurable<CoolExpansionConfig> {
	@Override
	public Class<CoolExpansionConfig> provideConfigType() {
		return CoolExpansionConfig.class;
	}

	@Override
	public CoolExpansionConfig provideDefault() {
		return new CoolExpansionConfig("wew", "dd/mm/yyyy");
	}

	@Override
	public String onPlaceholderRequest(PlayerRef player, String params) {
		final CoolExpansionConfig config = getExpansionConfig(CoolExpansion.class);

		return switch(params) {
			case "test" -> config.test();
			case "date_format" -> config.dateFormat();
			default -> null;
		};
	}
}
```

That's pretty much it. PAPI will generate the default yaml from the values you provide in `provideDefault()` and write it to the PAPI config, then whenever your expansion starts it'll read the values from the config and put them into a CoolExpansionConfig object accessible via `PlaceholderExpansion<T>.getExpansionConfig(Class<T extends Configurable<T>>)`