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

package me.clip.placeholderapi.expansion.manager;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.events.ExpansionRegisterEvent;
import me.clip.placeholderapi.events.ExpansionUnregisterEvent;
import me.clip.placeholderapi.events.ExpansionsLoadedEvent;
import me.clip.placeholderapi.expansion.*;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.FileUtil;
import me.clip.placeholderapi.util.Futures;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class LocalExpansionManager implements Listener {

  @NotNull
  private static final String EXPANSIONS_FOLDER_NAME = "expansions";

  @NotNull
  private static final Set<MethodSignature> ABSTRACT_EXPANSION_METHODS = Arrays.stream(PlaceholderExpansion.class.getDeclaredMethods())
          .filter(method -> Modifier.isAbstract(method.getModifiers()))
          .map(method -> new MethodSignature(method.getName(), method.getParameterTypes()))
          .collect(Collectors.toSet());

  @NotNull
  private final File folder;
  @NotNull
  private final PlaceholderAPIPlugin plugin;

  @NotNull
  private final Map<String, PlaceholderExpansion> expansions = new ConcurrentHashMap<>();
  private final ReentrantLock expansionsLock = new ReentrantLock();


  public LocalExpansionManager(@NotNull final PlaceholderAPIPlugin plugin) {
    this.plugin = plugin;
    this.folder = new File(plugin.getDataFolder(), EXPANSIONS_FOLDER_NAME);

    if (!this.folder.exists() && !folder.mkdirs()) {
      plugin.getLogger().log(Level.WARNING, "failed to create expansions folder!");
    }
  }

  public void load(@NotNull final CommandSender sender) {
    registerAll(sender);
  }

  public void kill() {
    unregisterAll();
  }


  @NotNull
  public File getExpansionsFolder() {
    return folder;
  }

  @NotNull
  @Unmodifiable
  public Collection<String> getIdentifiers() {
    expansionsLock.lock();
    try {
      return ImmutableSet.copyOf(expansions.keySet());
    } finally {
      expansionsLock.unlock();
    }
  }

  @NotNull
  @Unmodifiable
  public Collection<PlaceholderExpansion> getExpansions() {
    expansionsLock.lock();
    try {
      return ImmutableSet.copyOf(expansions.values());
    } finally {
      expansionsLock.unlock();
    }
  }

  @Nullable
  public PlaceholderExpansion getExpansion(@NotNull final String identifier) {
    expansionsLock.lock();
    try {
      return expansions.get(identifier.toLowerCase());
    } finally {
      expansionsLock.unlock();
    }
  }

  @NotNull
  public Optional<PlaceholderExpansion> findExpansionByName(@NotNull final String name) {
    expansionsLock.lock();
    try {
      PlaceholderExpansion bestMatch = null;
      for (Map.Entry<String, PlaceholderExpansion> entry : expansions.entrySet()) {
        PlaceholderExpansion expansion = entry.getValue();
        if (expansion.getName().equalsIgnoreCase(name)) {
          bestMatch = expansion;
          break;
        }
      }
      return Optional.ofNullable(bestMatch);
    } finally {
      expansionsLock.unlock();
    }
  }

  @NotNull
  public Optional<PlaceholderExpansion> findExpansionByIdentifier(
      @NotNull final String identifier) {
    return Optional.ofNullable(getExpansion(identifier));
  }


  public Optional<PlaceholderExpansion> register(
      @NotNull final Class<? extends PlaceholderExpansion> clazz) {
    try {
      final PlaceholderExpansion expansion = createExpansionInstance(clazz);
      
      if(expansion == null){
        return Optional.empty();
      }
      
      Objects.requireNonNull(expansion.getAuthor(), "The expansion author is null!");
      Objects.requireNonNull(expansion.getIdentifier(), "The expansion identifier is null!");
      Objects.requireNonNull(expansion.getVersion(), "The expansion version is null!");

      if (!expansion.register()) {
        return Optional.empty();
      }

      return Optional.of(expansion);
    } catch (LinkageError | NullPointerException ex) {
      final String reason;

      if (ex instanceof LinkageError) {
        reason = " (Is a dependency missing?)";
      } else {
        reason = " - One of its properties is null which is not allowed!";
      }

      plugin.getLogger().severe("Failed to load expansion class " + clazz.getSimpleName() +
              reason);
      plugin.getLogger().log(Level.SEVERE, "", ex);
    }

    return Optional.empty();
  }

  @ApiStatus.Internal
  public boolean register(@NotNull final PlaceholderExpansion expansion) {
    final String identifier = expansion.getIdentifier().toLowerCase();

    if (!expansion.canRegister()) {
      return false;
    }

    if (expansion instanceof Configurable) {
      Map<String, Object> defaults = ((Configurable) expansion).getDefaults();
      String pre = "expansions." + identifier + ".";
      FileConfiguration cfg = plugin.getConfig();
      boolean save = false;

      if (defaults != null) {
        for (Map.Entry<String, Object> entries : defaults.entrySet()) {
          if (entries.getKey() == null || entries.getKey().isEmpty()) {
            continue;
          }

          if (entries.getValue() == null) {
            if (cfg.contains(pre + entries.getKey())) {
              save = true;
              cfg.set(pre + entries.getKey(), null);
            }
          } else {
            if (!cfg.contains(pre + entries.getKey())) {
              save = true;
              cfg.set(pre + entries.getKey(), entries.getValue());
            }
          }
        }
      }

      if (save) {
        plugin.saveConfig();
        plugin.reloadConfig();
      }
    }

    if (expansion instanceof VersionSpecific) {
      VersionSpecific nms = (VersionSpecific) expansion;
      if (!nms.isCompatibleWith(PlaceholderAPIPlugin.getServerVersion())) {
        plugin.getLogger().warning("Your server version is not compatible with expansion " +
            expansion.getIdentifier() + " " + expansion.getVersion());
        return false;
      }
    }

    final PlaceholderExpansion removed = getExpansion(identifier);
    if (removed != null && !removed.unregister()) {
      return false;
    }

    final ExpansionRegisterEvent event = new ExpansionRegisterEvent(expansion);
    Bukkit.getPluginManager().callEvent(event);

    if (event.isCancelled()) {
      return false;
    }

    expansionsLock.lock();
    try {
      expansions.put(identifier, expansion);
    } finally {
      expansionsLock.unlock();
    }

    if (expansion instanceof Listener) {
      Bukkit.getPluginManager().registerEvents(((Listener) expansion), plugin);
    }

    plugin.getLogger().info("Successfully registered expansion: " + expansion.getIdentifier() + 
        " [" + expansion.getVersion() + "]");

    if (expansion instanceof Taskable) {
      ((Taskable) expansion).start();
    }

    if (plugin.getPlaceholderAPIConfig().isCloudEnabled()) {
      final Optional<CloudExpansion> cloudExpansionOptional =
          plugin.getCloudExpansionManager().findCloudExpansionByName(identifier);
      if (cloudExpansionOptional.isPresent()) {
        CloudExpansion cloudExpansion = cloudExpansionOptional.get();
        cloudExpansion.setHasExpansion(true);
        cloudExpansion.setShouldUpdate(
            !cloudExpansion.getLatestVersion().equals(expansion.getVersion()));
      }
    }

    return true;
  }

  @ApiStatus.Internal
  public boolean unregister(@NotNull final PlaceholderExpansion expansion) {
    if (expansions.remove(expansion.getIdentifier()) == null) {
      return false;
    }

    Bukkit.getPluginManager().callEvent(new ExpansionUnregisterEvent(expansion));

    if (expansion instanceof Listener) {
      HandlerList.unregisterAll((Listener) expansion);
    }

    if (expansion instanceof Taskable) {
      ((Taskable) expansion).stop();
    }

    if (expansion instanceof Cacheable) {
      ((Cacheable) expansion).clear();
    }

    if (plugin.getPlaceholderAPIConfig().isCloudEnabled()) {
      plugin.getCloudExpansionManager().findCloudExpansionByName(expansion.getName())
          .ifPresent(cloud -> {
            cloud.setHasExpansion(false);
            cloud.setShouldUpdate(false);
          });
    }

    return true;
  }


  private void registerAll(@NotNull final CommandSender sender) {
    plugin.getLogger().info("Placeholder expansion registration initializing...");

    Futures.onMainThread(plugin, findExpansionsOnDisk(), (classes, exception) -> {
      if (exception != null) {
        plugin.getLogger().log(Level.SEVERE, "failed to load class files of expansions", exception);
        return;
      }
      
      final List<PlaceholderExpansion> registered = classes.stream()
          .filter(Objects::nonNull)
          .map(this::register)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .collect(Collectors.toList());

      final long needsUpdate = registered.stream()
          .map(expansion -> plugin.getCloudExpansionManager().findCloudExpansionByName(expansion.getName()).orElse(null))
          .filter(Objects::nonNull)
          .filter(CloudExpansion::shouldUpdate)
          .count();

      StringBuilder message = new StringBuilder(registered.size() == 0 ? "&6" : "&a")
          .append(registered.size())
          .append(' ')
          .append("placeholder hook(s) registered!");
      
      if (needsUpdate > 0) {
        message.append(' ')
            .append("&6")
            .append(needsUpdate)
            .append(' ')
            .append("placeholder hook(s) have an update available.");
      }
      
      
      Msg.msg(sender, message.toString());

      Bukkit.getPluginManager().callEvent(new ExpansionsLoadedEvent(registered));
    });
  }

  private void unregisterAll() {
    for (final PlaceholderExpansion expansion : Sets.newHashSet(expansions.values())) {
      if (expansion.persist()) {
        continue;
      }

      expansion.unregister();
    }
  }

  @NotNull
  public CompletableFuture<@NotNull List<@Nullable Class<? extends PlaceholderExpansion>>> findExpansionsOnDisk() {
    File[] files = folder.listFiles((dir, name) -> name.endsWith(".jar"));
    if(files == null){
      return CompletableFuture.completedFuture(Collections.emptyList());
    }
    
    return Arrays.stream(files)
        .map(this::findExpansionInFile)
        .collect(Futures.collector());
  }

  @NotNull
  public CompletableFuture<@Nullable Class<? extends PlaceholderExpansion>> findExpansionInFile(
      @NotNull final File file) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        final Class<? extends PlaceholderExpansion> expansionClass = FileUtil.findClass(file, PlaceholderExpansion.class);

        if (expansionClass == null) {
          plugin.getLogger().severe("Failed to load Expansion: " + file.getName() + ", as it does not have" +
                  " a class which extends PlaceholderExpansion.");
          return null;
        }

        Set<MethodSignature> expansionMethods = Arrays.stream(expansionClass.getDeclaredMethods())
                .map(method -> new MethodSignature(method.getName(), method.getParameterTypes()))
                .collect(Collectors.toSet());
        if (!expansionMethods.containsAll(ABSTRACT_EXPANSION_METHODS)) {
          plugin.getLogger().severe("Failed to load Expansion: " + file.getName() + ", as it does not have the" +
                  " required methods declared for a PlaceholderExpansion.");
          return null;
        }

        return expansionClass;
      } catch (final VerifyError ex) {
        plugin.getLogger().severe("Failed to load Expansion class " + file.getName() +
            " (Is a dependency missing?)");
        plugin.getLogger().severe("Cause: " + ex.getClass().getSimpleName() + " " + ex.getMessage());
        return null;
      } catch (final Exception ex) {
        throw new CompletionException(ex);
      }
    });
  }


  @Nullable
  public PlaceholderExpansion createExpansionInstance(
      @NotNull final Class<? extends PlaceholderExpansion> clazz) throws LinkageError {
    try {
      return clazz.getDeclaredConstructor().newInstance();
    } catch (final Exception ex) {
      if (ex.getCause() instanceof LinkageError) {
        throw ((LinkageError) ex.getCause());
      }

      plugin.getLogger().warning("There was an issue with loading an expansion.");
      
      return null;
    }
  }


  @EventHandler
  public void onQuit(@NotNull final PlayerQuitEvent event) {
    for (final PlaceholderExpansion expansion : getExpansions()) {
      if (!(expansion instanceof Cleanable)) {
        continue;
      }

      ((Cleanable) expansion).cleanup(event.getPlayer());
    }
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onPluginDisable(@NotNull final PluginDisableEvent event) {
    final String name = event.getPlugin().getName();
    if (name.equals(plugin.getName())) {
      return;
    }

    for (final PlaceholderExpansion expansion : getExpansions()) {
      if (!name.equalsIgnoreCase(expansion.getRequiredPlugin())) {
        continue;
      }

      expansion.unregister();
      plugin.getLogger().info("Unregistered placeholder expansion: " + expansion.getName());
    }
  }

}
