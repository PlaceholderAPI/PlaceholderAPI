/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2026 PlaceholderAPI Team
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

package at.helpch.placeholderapi.expansion.manager;

import at.helpch.placeholderapi.PlaceholderAPIPlugin;

import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.stream.Collectors;

import at.helpch.placeholderapi.configuration.ConfigManager;
import at.helpch.placeholderapi.configuration.PlaceholderAPIConfig;
import at.helpch.placeholderapi.events.ExpansionRegisterEvent;
import at.helpch.placeholderapi.events.ExpansionUnregisterEvent;
import at.helpch.placeholderapi.events.ExpansionsLoadedEvent;
import at.helpch.placeholderapi.expansion.Cacheable;
import at.helpch.placeholderapi.expansion.Cleanable;
import at.helpch.placeholderapi.expansion.Configurable;
import at.helpch.placeholderapi.expansion.PlaceholderExpansion;
import at.helpch.placeholderapi.expansion.Taskable;
import at.helpch.placeholderapi.expansion.cloud.CloudExpansion;
import at.helpch.placeholderapi.util.FileUtil;
import at.helpch.placeholderapi.util.Futures;
import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.event.IEventDispatcher;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.plugin.PluginBase;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public final class LocalExpansionManager /*implements Listener*/ {

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
    private final HytaleLogger logger;
    private final ConfigManager configManager;

    @NotNull
    private final Map<String, PlaceholderExpansion> expansions = new ConcurrentHashMap<>();
    private final ReentrantLock expansionsLock = new ReentrantLock();


    public LocalExpansionManager(@NotNull final PlaceholderAPIPlugin plugin) {
        this.plugin = plugin;
        this.folder = new File(plugin.getDataDirectory().toString(), EXPANSIONS_FOLDER_NAME);
        this.logger = plugin.getLogger();
        this.configManager = plugin.configManager();

        if (!this.folder.exists() && !folder.mkdirs()) {
            logger.atWarning().log("Failed to create expansions folder!");
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
            return Set.copyOf(expansions.keySet());
        } finally {
            expansionsLock.unlock();
        }
    }

    @NotNull
    @Unmodifiable
    public Collection<PlaceholderExpansion> getExpansions() {
        expansionsLock.lock();
        try {
            return Set.copyOf(expansions.values());
        } finally {
            expansionsLock.unlock();
        }
    }

    @Nullable
    public PlaceholderExpansion getExpansion(@NotNull final String identifier) {
        expansionsLock.lock();
        try {
            return expansions.get(identifier.toLowerCase(Locale.ROOT));
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

            if (expansion == null) {
                return Optional.empty();
            }

            Objects.requireNonNull(expansion.getAuthor(), "The expansion author is null!");
            Objects.requireNonNull(expansion.getIdentifier(), "The expansion identifier is null!");
            Objects.requireNonNull(expansion.getVersion(), "The expansion version is null!");

            if (expansion.getRequiredPlugin() != null && !expansion.getRequiredPlugin().isEmpty()) {
                if (HytaleServer.get().getPluginManager().getPlugin(PluginIdentifier.fromString(expansion.getRequiredPlugin())) == null) {
                    logger.atWarning().log("Cannot load expansion %s due to a missing plugin: %s", expansion.getIdentifier(),
                            expansion.getRequiredPlugin());
                    return Optional.empty();
                }
            }

            expansion.setExpansionType(PlaceholderExpansion.Type.EXTERNAL);

            if (!expansion.register()) {
                logger.atWarning().log("Cannot load expansion %s due to an unknown issue.", expansion.getIdentifier());
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

            logger.atSevere().log("Failed to load expansion class %s%s", ex, clazz.getSimpleName(), reason);
        }

        return Optional.empty();
    }

    /**
     * Attempt to register a {@link PlaceholderExpansion}
     *
     * @param expansion the expansion to register
     * @return if the expansion was registered
     */
    @ApiStatus.Internal
    public boolean register(@NotNull final PlaceholderExpansion expansion) {
        final String identifier = expansion.getIdentifier().toLowerCase(Locale.ROOT);
        
        if (expansion instanceof Configurable<?> configurable) {
            final PlaceholderAPIConfig config = configManager.config();

            if (config.expansions() == null) {
                config.expansions(new ConcurrentHashMap<>());
            }

            if (!config.expansions().containsKey(expansion.getIdentifier())) {
                config.expansions().put(expansion.getIdentifier(), configurable.provideDefault());
                configManager.save();
            } else {
                final Object expansionConfig = configManager.convertExpansion((Map<String, Object>) config.expansions().get(expansion.getIdentifier()), configurable.provideConfigType());
                config.expansions().put(expansion.getIdentifier(), expansionConfig);
            }
        }

        if (!expansion.canRegister()) {
            return false;
        }

        // Avoid loading two external expansions with the same identifier
        if (expansion.getExpansionType() == PlaceholderExpansion.Type.EXTERNAL && expansions.containsKey(identifier)) {
            logger.atWarning().log("Failed to load external expansion %s. Identifier is already in use.", expansion.getIdentifier());
            return false;
        }

//        if (expansion instanceof VersionSpecific) {
//            VersionSpecific nms = (VersionSpecific) expansion;
//            if (!nms.isCompatibleWith(PlaceholderAPIPlugin.getServerVersion())) {
//                Msg.warn("Your server version is incompatible with expansion %s %s",
//                        expansion.getIdentifier(), expansion.getVersion());
//                return false;
//            }
//        }

        final PlaceholderExpansion removed = getExpansion(identifier);
        if (removed != null && !removed.unregister()) {
            return false;
        }

        final ExpansionRegisterEvent event = new ExpansionRegisterEvent(expansion);
        final IEventDispatcher<ExpansionRegisterEvent, ExpansionRegisterEvent> eventDispatcher = HytaleServer.get().getEventBus().dispatchFor(ExpansionRegisterEvent.class);
        if (eventDispatcher.hasListener()) {
            eventDispatcher.dispatch(event);
        }

//        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }

        expansionsLock.lock();
        try {
            expansions.put(identifier, expansion);
        } finally {
            expansionsLock.unlock();
        }

//        if (expansion instanceof Listener) {
//            Bukkit.getPluginManager().registerEvents(((Listener) expansion), plugin);
//        }

        logger.at(Level.INFO).log(
                "Successfully registered %s expansion: %s [%s]",
                expansion.getExpansionType().name().toLowerCase(),
                expansion.getIdentifier(),
                expansion.getVersion()
        );

        if (expansion instanceof Taskable) {
            ((Taskable) expansion).start();
        }

        // Check eCloud for updates only if the expansion is external
        if (configManager.config().cloudEnabled() && expansion.getExpansionType() == PlaceholderExpansion.Type.EXTERNAL) {
            final Optional<CloudExpansion> cloudExpansionOptional = plugin.cloudExpansionManager().findCloudExpansionByName(identifier);
            if (cloudExpansionOptional.isPresent()) {
                CloudExpansion cloudExpansion = cloudExpansionOptional.get();
                cloudExpansion.setHasExpansion(true);
                cloudExpansion.setShouldUpdate(!cloudExpansion.getLatestVersion().equals(expansion.getVersion()));
            }
        }

        return true;
    }

    @ApiStatus.Internal
    public boolean unregister(@NotNull final PlaceholderExpansion expansion) {
        if (expansions.remove(expansion.getIdentifier().toLowerCase(Locale.ROOT)) == null) {
            return false;
        }

        final IEventDispatcher<ExpansionUnregisterEvent, ExpansionUnregisterEvent> eventDispatcher = HytaleServer.get().getEventBus().dispatchFor(ExpansionUnregisterEvent.class);
        if (eventDispatcher.hasListener()) {
            eventDispatcher.dispatch(new ExpansionUnregisterEvent(expansion));
        }

//        if (expansion instanceof Listener) {
//            HandlerList.unregisterAll((Listener) expansion);
//        }

        if (expansion instanceof Taskable) {
            ((Taskable) expansion).stop();
        }

        if (expansion instanceof Cacheable) {
            ((Cacheable) expansion).clear();
        }

        if (configManager.config().cloudEnabled()) {
            plugin.cloudExpansionManager().findCloudExpansionByName(expansion.getName())
                    .ifPresent(cloud -> {
                        cloud.setHasExpansion(false);
                        cloud.setShouldUpdate(false);
                    });
        }

        return true;
    }

    private void registerAll(@NotNull final CommandSender sender) {
        logger.at(Level.INFO).log("Placeholder expansion registration initializing...");

        Futures.onMainThread(plugin, findExpansionsOnDisk(), (classes, exception) -> {
            if (exception != null) {
                logger.atSevere().log("Failed to load class files of expansion.", exception);
                return;
            }

            final List<PlaceholderExpansion> registered = classes.stream()
                    .filter(Objects::nonNull)
                    .map(this::register)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            final long needsUpdate = registered.stream()
                    .map(expansion -> plugin.cloudExpansionManager().findCloudExpansionByName(expansion.getName()).orElse(null))
                    .filter(Objects::nonNull)
                    .filter(CloudExpansion::shouldUpdate)
                    .count();
            Message message = Message.raw(registered.size() + "").color(registered.isEmpty() ? Color.YELLOW : Color.GREEN)
                    .insert(" placeholder hook(s) registered!");

            if (needsUpdate > 0) {
                message = message.insert(" ")
                        .insert(Message.raw(needsUpdate + " placeholder hook(s) have an update available.").color(Color.YELLOW));
            }

//            logger.at(Level.INFO).log(message.toString());
            sender.sendMessage(message);

            final IEventDispatcher<ExpansionsLoadedEvent, ExpansionsLoadedEvent> eventDispatcher = HytaleServer.get().getEventBus().dispatchFor(ExpansionsLoadedEvent.class);
            if (eventDispatcher.hasListener()) {
                eventDispatcher.dispatch(new ExpansionsLoadedEvent(registered));
            }
        });
    }

    private void unregisterAll() {
        for (final PlaceholderExpansion expansion : new HashSet<>(expansions.values())) {
            if (expansion.persist()) {
                continue;
            }

            expansion.unregister();
        }
    }

    @NotNull
    public CompletableFuture<@NotNull List<@Nullable Class<? extends PlaceholderExpansion>>> findExpansionsOnDisk() {
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".jar"));
        if (files == null) {
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
                    logger.atSevere().log("Failed to load expansion %s, as it does not have a class which"
                            + " extends PlaceholderExpansion", file.getName());
                    return null;
                }

                Set<MethodSignature> expansionMethods = Arrays.stream(expansionClass.getDeclaredMethods())
                        .map(method -> new MethodSignature(method.getName(), method.getParameterTypes()))
                        .collect(Collectors.toSet());
                if (!expansionMethods.containsAll(ABSTRACT_EXPANSION_METHODS)) {
                    logger.atSevere().log("Failed to load expansion %s, as it does not have the required"
                            + " methods declared for a PlaceholderExpansion.", file.getName());
                    return null;
                }

                return expansionClass;
            } catch (VerifyError | NoClassDefFoundError e) {
                logger.atSevere().log("Failed to load expansion %s (is a dependency missing?)", e, file.getName());
                return null;
            } catch (Exception e) {
                logger.atSevere().log("Failed to load expansion file: " + file.getAbsolutePath(), e);
                return null;
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

            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.atWarning().log("There was an issue with loading an expansion: " + clazz + "\n%s", sw.toString());
            return null;
        }
    }

    public void onQuit(@NotNull final PlayerDisconnectEvent event) {
        for (final PlaceholderExpansion expansion : getExpansions()) {
            if (!(expansion instanceof Cleanable)) {
                continue;
            }

            ((Cleanable) expansion).cleanup(event.getPlayerRef());
        }
    }

//    @EventHandler(priority = EventPriority.HIGH)
    //todo: hytale has no plugin disable event as of yet :(
//    public void onPluginDisable() {
//        final String name = event.getPlugin().getName();
//        if (name.equals(plugin.getName())) {
//            return;
//        }
//
//        for (final PlaceholderExpansion expansion : getExpansions()) {
//            if (!name.equalsIgnoreCase(expansion.getRequiredPlugin())) {
//                continue;
//            }
//
//            expansion.unregister();
//            Msg.info("Unregistered placeholder expansion %s", expansion.getIdentifier());
//            Msg.info("Reason: required plugin %s was disabled.", name);
//        }
//    }

}
