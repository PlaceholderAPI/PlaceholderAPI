package me.clip.placeholderapi.configuration;

import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public enum ExpansionSort implements Comparator<CloudExpansion>
{

	NAME(Comparator.comparing(CloudExpansion::getName)),
	AUTHOR(Comparator.comparing(CloudExpansion::getAuthor)),
	LATEST(Comparator.comparing(CloudExpansion::getLastUpdate).reversed());


	@NotNull
	private final Comparator<CloudExpansion> comparator;

	ExpansionSort(@NotNull final Comparator<CloudExpansion> comparator)
	{
		this.comparator = comparator;
	}


	@Override
	public final int compare(final CloudExpansion expansion1, final CloudExpansion expansion2)
	{
		return comparator.compare(expansion1, expansion2);
	}

}
