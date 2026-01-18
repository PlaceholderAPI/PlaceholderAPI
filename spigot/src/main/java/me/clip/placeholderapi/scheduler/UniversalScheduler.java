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

package me.clip.placeholderapi.scheduler;

import me.clip.placeholderapi.scheduler.bukkit.BukkitScheduler;
import me.clip.placeholderapi.scheduler.folia.FoliaScheduler;
import me.clip.placeholderapi.scheduler.paper.PaperScheduler;
import me.clip.placeholderapi.scheduler.scheduling.schedulers.TaskScheduler;
import me.clip.placeholderapi.scheduler.utils.JavaUtil;
import org.bukkit.plugin.Plugin;

public class UniversalScheduler {
    private static final boolean IS_FOLIA = JavaUtil.classExists("io.papermc.paper.threadedregions.RegionizedServer");
    private static final boolean IS_CANVAS = JavaUtil.classExists("io.canvasmc.canvas.server.ThreadedServer");
    private static final boolean IS_EXPANDED_SCHEDULING_AVAILABLE = JavaUtil.classExists("io.papermc.paper.threadedregions.scheduler.ScheduledTask");

    public static TaskScheduler getScheduler(Plugin plugin) {
        return IS_FOLIA || IS_CANVAS ? new FoliaScheduler(plugin) : (IS_EXPANDED_SCHEDULING_AVAILABLE ? new PaperScheduler(plugin) : new BukkitScheduler(plugin));
    }

}
