/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2021 PlaceholderAPI Team
 *
 * PlaceholderAPI free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlaceholderAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.clip.placeholderapi.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class Futures {

  private Futures() {}


  public static <T> void onMainThread(@NotNull final Plugin plugin,
      @NotNull final CompletableFuture<T> future,
      @NotNull final BiConsumer<T, Throwable> consumer) {
    future.whenComplete((value, exception) -> {
      if (Bukkit.isPrimaryThread()) {
        consumer.accept(value, exception);
      } else {
        PlaceholderAPIPlugin.getScheduler().runTask(() -> consumer.accept(value, exception));
      }
    });
  }


  @NotNull
  public static <T> Collector<CompletableFuture<T>, ?, CompletableFuture<List<T>>> collector() {
    return Collectors.collectingAndThen(Collectors.toList(), Futures::of);
  }


  @NotNull
  public static <T> CompletableFuture<List<T>> of(
      @NotNull final Stream<CompletableFuture<T>> futures) {
    return of(futures.collect(Collectors.toList()));
  }

  @NotNull
  public static <T> CompletableFuture<List<T>> of(
      @NotNull final Collection<CompletableFuture<T>> futures) {
    return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
        .thenApplyAsync($ -> awaitCompletion(futures));
  }

  @NotNull
  private static <T> List<T> awaitCompletion(
      @NotNull final Collection<CompletableFuture<T>> futures) {
    return futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
  }

}
