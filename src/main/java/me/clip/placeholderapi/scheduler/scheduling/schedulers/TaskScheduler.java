/*
 * MIT License
 *
 * Copyright (c) 2023 Sevastjan
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.clip.placeholderapi.scheduler.scheduling.schedulers;

import me.clip.placeholderapi.scheduler.scheduling.tasks.MyScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface TaskScheduler {

    /**
     * <b>Folia</b>: Returns whether the current thread is ticking the global region <br>
     * <b>Paper and Bukkit</b>: Returns {@link org.bukkit.Server#isPrimaryThread}
     */
    boolean isGlobalThread();

    /**
     * @return {@link org.bukkit.Server#isPrimaryThread}
     */
    default boolean isTickThread() {
        return Bukkit.getServer().isPrimaryThread();
    }

    /**
     * <b>Folia and Paper</b>: Returns whether the current thread is ticking a region and that the region
     * being ticked owns the specified entity. Note that this function is the only appropriate method of
     * checking for ownership of an entity, as retrieving the entity's location is undefined unless the
     * entity is owned by the current region
     * <p>
     * <b>Bukkit</b>: returns {@link org.bukkit.Server#isPrimaryThread}
     *
     * @param entity Specified entity
     */
    boolean isEntityThread(Entity entity);

    /**
     * <b>Folia and Paper</b>: Returns whether the current thread is ticking a region and that the region
     * being ticked owns the chunk at the specified world and block position as included in the specified location
     * <p>
     * <b>Bukkit</b>: returns {@link org.bukkit.Server#isPrimaryThread}
     *
     * @param location Specified location, must have a non-null world.
     */
    boolean isRegionThread(Location location);

    /**
     * Schedules a task to be executed on the next tick <br>
     * <b>Folia and Paper</b>: ...on the global region <br>
     * <b>Bukkit</b>: ...on the main thread
     *
     * @param runnable The task to execute
     */
    MyScheduledTask runTask(Runnable runnable);

    /**
     * Schedules a task to be executed after the specified delay in ticks <br>
     * <b>Folia and Paper</b>: ...on the global region <br>
     * <b>Bukkit</b>: ...on the main thread
     *
     * @param runnable The task to execute
     * @param delay    The delay, in ticks
     */
    MyScheduledTask runTaskLater(Runnable runnable, long delay);

    /**
     * Schedules a repeating task to be executed after the initial delay with the specified period <br>
     * <b>Folia and Paper</b>: ...on the global region <br>
     * <b>Bukkit</b>: ...on the main thread
     *
     * @param runnable The task to execute
     * @param delay    The initial delay, in ticks.
     * @param period   The period, in ticks.
     */
    MyScheduledTask runTaskTimer(Runnable runnable, long delay, long period);

    /**
     * Deprecated: use {@link #runTask(Runnable)}
     */
    @Deprecated
    default MyScheduledTask runTask(Plugin plugin, Runnable runnable) {
        return runTask(runnable);
    }

    /**
     * Deprecated: use {@link #runTaskLater(Runnable, long)}
     */
    @Deprecated
    default MyScheduledTask runTaskLater(Plugin plugin, Runnable runnable, long delay) {
        return runTaskLater(runnable, delay);
    }

    /**
     * Deprecated: use {@link #runTaskTimer(Runnable, long, long)}
     */
    @Deprecated
    default MyScheduledTask runTaskTimer(Plugin plugin, Runnable runnable, long delay, long period) {
        return runTaskTimer(runnable, delay, period);
    }

    /**
     * <b>Folia and Paper</b>: Schedules a task to be executed on the region which owns the location on the next tick
     * <p>
     * <b>Bukkit</b>: same as {@link #runTask(Runnable)}
     *
     * @param location The location which the region executing should own
     * @param runnable The task to execute
     */
    default MyScheduledTask runTask(Location location, Runnable runnable) {
        return runTask(runnable);
    }

    /**
     * <b>Folia and Paper</b>: Schedules a task to be executed on the region which owns the location after the
     * specified delay in ticks
     * <p>
     * <b>Bukkit</b>: same as {@link #runTaskLater(Runnable, long)}
     *
     * @param location The location which the region executing should own
     * @param runnable The task to execute
     * @param delay    The delay, in ticks.
     */
    default MyScheduledTask runTaskLater(Location location, Runnable runnable, long delay) {
        return runTaskLater(runnable, delay);
    }

    /**
     * <b>Folia and Paper</b>: Schedules a repeating task to be executed on the region which owns the location
     * after the initial delay with the specified period
     * <p>
     * <b>Bukkit</b>: same as {@link #runTaskTimer(Runnable, long, long)}
     *
     * @param location The location which the region executing should own
     * @param runnable The task to execute
     * @param delay    The initial delay, in ticks.
     * @param period   The period, in ticks.
     */
    default MyScheduledTask runTaskTimer(Location location, Runnable runnable, long delay, long period) {
        return runTaskTimer(runnable, delay, period);
    }

    /**
     * Deprecated: use {@link #runTaskLater(Runnable, long)}
     */
    @Deprecated
    default MyScheduledTask scheduleSyncDelayedTask(Runnable runnable, long delay) {
        return runTaskLater(runnable, delay);
    }

    /**
     * Deprecated: use {@link #execute(Runnable)} or {@link #runTask(Runnable)}
     */
    @Deprecated
    default MyScheduledTask scheduleSyncDelayedTask(Runnable runnable) {
        return runTask(runnable);
    }

    /**
     * Deprecated: use {@link #runTaskTimer(Runnable, long, long)}
     */
    @Deprecated
    default MyScheduledTask scheduleSyncRepeatingTask(Runnable runnable, long delay, long period) {
        return runTaskTimer(runnable, delay, period);
    }

    /**
     * <b>Folia and Paper</b>: Schedules a task to be executed on the region which owns the location
     * of given entity on the next tick
     * <p>
     * <b>Bukkit</b>: same as {@link #runTask(Runnable)}
     *
     * @param entity   The entity whose location the region executing should own
     * @param runnable The task to execute
     */
    default MyScheduledTask runTask(Entity entity, Runnable runnable) {
        return runTask(runnable);
    }

    /**
     * <b>Folia and Paper</b>: Schedules a task to be executed on the region which owns the location
     * of given entity after the specified delay in ticks
     * <p>
     * <b>Bukkit</b>: same as {@link #runTaskLater(Runnable, long)}
     *
     * @param entity   The entity whose location the region executing should own
     * @param runnable The task to execute
     * @param delay    The delay, in ticks.
     */
    default MyScheduledTask runTaskLater(Entity entity, Runnable runnable, long delay) {
        return runTaskLater(runnable, delay);
    }

    /**
     * <b>Folia and Paper</b>: Schedules a repeating task to be executed on the region which owns the
     * location of given entity after the initial delay with the specified period
     * <p>
     * <b>Bukkit</b>: same as {@link #runTaskTimer(Runnable, long, long)}
     *
     * @param entity   The entity whose location the region executing should own
     * @param runnable The task to execute
     * @param delay    The initial delay, in ticks.
     * @param period   The period, in ticks.
     */
    default MyScheduledTask runTaskTimer(Entity entity, Runnable runnable, long delay, long period) {
        return runTaskTimer(runnable, delay, period);
    }

    /**
     * Schedules the specified task to be executed asynchronously immediately
     *
     * @param runnable The task to execute
     * @return The {@link MyScheduledTask} that represents the scheduled task
     */
    MyScheduledTask runTaskAsynchronously(Runnable runnable);

    /**
     * Schedules the specified task to be executed asynchronously after the time delay has passed
     *
     * @param runnable The task to execute
     * @param delay    The time delay to pass before the task should be executed
     * @return The {@link MyScheduledTask} that represents the scheduled task
     */
    MyScheduledTask runTaskLaterAsynchronously(Runnable runnable, long delay);

    /**
     * Schedules the specified task to be executed asynchronously after the initial delay has passed,
     * and then periodically executed with the specified period
     *
     * @param runnable The task to execute
     * @param delay    The time delay to pass before the first execution of the task, in ticks
     * @param period   The time between task executions after the first execution of the task, in ticks
     * @return The {@link MyScheduledTask} that represents the scheduled task
     */
    MyScheduledTask runTaskTimerAsynchronously(Runnable runnable, long delay, long period);

    /**
     * Deprecated: use {@link #runTaskAsynchronously(Runnable)}
     */
    @Deprecated
    default MyScheduledTask runTaskAsynchronously(Plugin plugin, Runnable runnable) {
        return runTaskAsynchronously(runnable);
    }

    /**
     * Deprecated: use {@link #runTaskLaterAsynchronously(Runnable, long)}
     */
    @Deprecated
    default MyScheduledTask runTaskLaterAsynchronously(Plugin plugin, Runnable runnable, long delay) {
        return runTaskLaterAsynchronously(runnable, delay);
    }

    /**
     * Deprecated: use {@link #runTaskTimerAsynchronously(Runnable, long, long)}
     */
    @Deprecated
    default MyScheduledTask runTaskTimerAsynchronously(Plugin plugin, Runnable runnable, long delay, long period) {
        return runTaskTimerAsynchronously(runnable, delay, period);
    }

    /**
     * Calls a method on the main thread and returns a Future object. This task will be executed
     * by the main(Bukkit)/global(FoliaandPaper) server thread.
     * <p>
     * Note: The Future.get() methods must NOT be called from the main thread.
     * <p>
     * Note2: There is at least an average of 10ms latency until the isDone() method returns true.
     *
     * @param task Task to be executed
     */
    default <T> Future<T> callSyncMethod(final Callable<T> task) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        execute(() -> {
            try {
                completableFuture.complete(task.call());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return completableFuture;
    }

    /**
     * Schedules a task to be executed on the global region
     *
     * @param runnable The task to execute
     */
    void execute(Runnable runnable);

    /**
     * Schedules a task to be executed on the region which owns the location
     *
     * @param location The location which the region executing should own
     * @param runnable The task to execute
     */
    default void execute(Location location, Runnable runnable) {
        execute(runnable);
    }

    /**
     * Schedules a task to be executed on the region which owns the location of given entity
     *
     * @param entity   The entity which location the region executing should own
     * @param runnable The task to execute
     */
    default void execute(Entity entity, Runnable runnable) {
        execute(runnable);
    }

    /**
     * Attempts to cancel all tasks scheduled by this plugin
     */
    void cancelTasks();

    /**
     * Attempts to cancel all tasks scheduled by the specified plugin
     *
     * @param plugin specified plugin
     */
    void cancelTasks(Plugin plugin);
}
