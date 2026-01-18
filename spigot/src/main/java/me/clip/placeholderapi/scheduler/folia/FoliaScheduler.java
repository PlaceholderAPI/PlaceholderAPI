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

package me.clip.placeholderapi.scheduler.folia;

import me.clip.placeholderapi.scheduler.scheduling.schedulers.TaskScheduler;
import me.clip.placeholderapi.scheduler.scheduling.tasks.MyScheduledTask;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class FoliaScheduler implements TaskScheduler {

    final Plugin plugin;

    public FoliaScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    private final RegionScheduler regionScheduler = Bukkit.getServer().getRegionScheduler();
    private final GlobalRegionScheduler globalRegionScheduler = Bukkit.getServer().getGlobalRegionScheduler();
    private final AsyncScheduler asyncScheduler = Bukkit.getServer().getAsyncScheduler();

    @Override
    public boolean isGlobalThread() {
        return Bukkit.getServer().isGlobalTickThread();
    }

    @Override
    public boolean isTickThread() {
        return Bukkit.getServer().isPrimaryThread(); // The Paper implementation checks whether this is a tick thread, this method exists to avoid confusion.
    }

    @Override
    public boolean isEntityThread(Entity entity) {
        return Bukkit.getServer().isOwnedByCurrentRegion(entity);
    }

    @Override
    public boolean isRegionThread(Location location) {
        return Bukkit.getServer().isOwnedByCurrentRegion(location);
    }

    @Override
    public MyScheduledTask runTask(Runnable runnable) {
        return new FoliaScheduledTask(globalRegionScheduler.run(plugin, task -> runnable.run()));
    }

    @Override
    public MyScheduledTask runTaskLater(Runnable runnable, long delay) {
        //Folia exception: Delay ticks may not be <= 0
        if (delay <= 0) {
            return runTask(runnable);
        }
        return new FoliaScheduledTask(globalRegionScheduler.runDelayed(plugin, task -> runnable.run(), delay));
    }

    @Override
    public MyScheduledTask runTaskTimer(Runnable runnable, long delay, long period) {
        //Folia exception: Delay ticks may not be <= 0
        delay = getOneIfNotPositive(delay);
        return new FoliaScheduledTask(globalRegionScheduler.runAtFixedRate(plugin, task -> runnable.run(), delay, period));
    }

    @Override
    public MyScheduledTask runTask(Plugin plugin, Runnable runnable) {
        return new FoliaScheduledTask(globalRegionScheduler.run(plugin, task -> runnable.run()));
    }

    @Override
    public MyScheduledTask runTaskLater(Plugin plugin, Runnable runnable, long delay) {
        //Folia exception: Delay ticks may not be <= 0
        if (delay <= 0) {
            return runTask(plugin, runnable);
        }
        return new FoliaScheduledTask(globalRegionScheduler.runDelayed(plugin, task -> runnable.run(), delay));
    }

    @Override
    public MyScheduledTask runTaskTimer(Plugin plugin, Runnable runnable, long delay, long period) {
        //Folia exception: Delay ticks may not be <= 0
        delay = getOneIfNotPositive(delay);
        return new FoliaScheduledTask(globalRegionScheduler.runAtFixedRate(plugin, task -> runnable.run(), delay, period));
    }

    @Override
    public MyScheduledTask runTask(Location location, Runnable runnable) {
        return new FoliaScheduledTask(regionScheduler.run(plugin, location, task -> runnable.run()));
    }

    @Override
    public MyScheduledTask runTaskLater(Location location, Runnable runnable, long delay) {
        //Folia exception: Delay ticks may not be <= 0
        if (delay <= 0) {
            return runTask(runnable);
        }
        return new FoliaScheduledTask(regionScheduler.runDelayed(plugin, location, task -> runnable.run(), delay));
    }

    @Override
    public MyScheduledTask runTaskTimer(Location location, Runnable runnable, long delay, long period) {
        //Folia exception: Delay ticks may not be <= 0
        delay = getOneIfNotPositive(delay);
        return new FoliaScheduledTask(regionScheduler.runAtFixedRate(plugin, location, task -> runnable.run(), delay, period));
    }

    @Override
    public MyScheduledTask runTask(Entity entity, Runnable runnable) {
        return new FoliaScheduledTask(entity.getScheduler().run(plugin, task -> runnable.run(), null));
    }

    @Override
    public MyScheduledTask runTaskLater(Entity entity, Runnable runnable, long delay) {
        //Folia exception: Delay ticks may not be <= 0
        if (delay <= 0) {
            return runTask(entity, runnable);
        }
        return new FoliaScheduledTask(entity.getScheduler().runDelayed(plugin, task -> runnable.run(), null, delay));
    }

    @Override
    public MyScheduledTask runTaskTimer(Entity entity, Runnable runnable, long delay, long period) {
        //Folia exception: Delay ticks may not be <= 0
        delay = getOneIfNotPositive(delay);
        return new FoliaScheduledTask(entity.getScheduler().runAtFixedRate(plugin, task -> runnable.run(), null, delay, period));
    }

    @Override
    public MyScheduledTask runTaskAsynchronously(Runnable runnable) {
        return new FoliaScheduledTask(asyncScheduler.runNow(plugin, task -> runnable.run()));
    }

    @Override
    public MyScheduledTask runTaskLaterAsynchronously(Runnable runnable, long delay) {
        //Folia exception: Delay ticks may not be <= 0
        delay = getOneIfNotPositive(delay);
        return new FoliaScheduledTask(asyncScheduler.runDelayed(plugin, task -> runnable.run(), delay * 50L, TimeUnit.MILLISECONDS));
    }

    @Override
    public MyScheduledTask runTaskTimerAsynchronously(Runnable runnable, long delay, long period) {
        return new FoliaScheduledTask(asyncScheduler.runAtFixedRate(plugin, task -> runnable.run(), delay * 50, period * 50, TimeUnit.MILLISECONDS));
    }

    @Override
    public MyScheduledTask runTaskAsynchronously(Plugin plugin, Runnable runnable) {
        return new FoliaScheduledTask(asyncScheduler.runNow(plugin, task -> runnable.run()));
    }

    @Override
    public MyScheduledTask runTaskLaterAsynchronously(Plugin plugin, Runnable runnable, long delay) {
        //Folia exception: Delay ticks may not be <= 0
        delay = getOneIfNotPositive(delay);
        return new FoliaScheduledTask(asyncScheduler.runDelayed(plugin, task -> runnable.run(), delay * 50L, TimeUnit.MILLISECONDS));
    }

    @Override
    public MyScheduledTask runTaskTimerAsynchronously(Plugin plugin, Runnable runnable, long delay, long period) {
        //Folia exception: Delay ticks may not be <= 0
        delay = getOneIfNotPositive(delay);
        return new FoliaScheduledTask(asyncScheduler.runAtFixedRate(plugin, task -> runnable.run(), delay * 50, period * 50, TimeUnit.MILLISECONDS));
    }

    @Override
    public void execute(Runnable runnable) {
        globalRegionScheduler.execute(plugin, runnable);
    }

    @Override
    public void execute(Location location, Runnable runnable) {
        regionScheduler.execute(plugin, location, runnable);
    }

    @Override
    public void execute(Entity entity, Runnable runnable) {
        entity.getScheduler().execute(plugin, runnable, null, 1L);
    }

    @Override
    public void cancelTasks() {
        globalRegionScheduler.cancelTasks(plugin);
        asyncScheduler.cancelTasks(plugin);
    }

    @Override
    public void cancelTasks(Plugin plugin) {
        globalRegionScheduler.cancelTasks(plugin);
        asyncScheduler.cancelTasks(plugin);
    }

    private long getOneIfNotPositive(long x) {
        return x <= 0 ? 1L : x;
    }
}
