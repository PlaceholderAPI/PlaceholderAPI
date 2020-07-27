package me.clip.placeholderapi.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Futures
{

	private Futures()
	{}


	public static <T> void onMainThread(@NotNull final Plugin plugin, @NotNull final CompletableFuture<T> future, @NotNull final BiConsumer<T, Throwable> consumer)
	{
		future.whenComplete((value, exception) -> {
			if (Bukkit.isPrimaryThread())
			{
				consumer.accept(value, exception);
			}
			else
			{
				Bukkit.getScheduler().runTask(plugin, () -> consumer.accept(value, exception));
			}
		});
	}


	@NotNull
	public static <T> Collector<CompletableFuture<T>, ?, CompletableFuture<List<T>>> collector()
	{
		return Collectors.collectingAndThen(Collectors.toList(), Futures::of);
	}


	@NotNull
	public static <T> CompletableFuture<List<T>> of(@NotNull final Stream<CompletableFuture<T>> futures)
	{
		return of(futures.collect(Collectors.toList()));
	}

	@NotNull
	public static <T> CompletableFuture<List<T>> of(@NotNull final Collection<CompletableFuture<T>> futures)
	{
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
								.thenApplyAsync($ -> awaitCompletion(futures));
	}

	@NotNull
	private static <T> List<T> awaitCompletion(@NotNull final Collection<CompletableFuture<T>> futures)
	{
		return futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
	}

}
