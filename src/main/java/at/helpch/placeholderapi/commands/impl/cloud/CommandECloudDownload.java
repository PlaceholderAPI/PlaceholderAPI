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

package at.helpch.placeholderapi.commands.impl.cloud;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import at.helpch.placeholderapi.PlaceholderAPIPlugin;
import at.helpch.placeholderapi.commands.PlaceholderCommand;
import at.helpch.placeholderapi.expansion.cloud.CloudExpansion;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandECloudDownload extends PlaceholderCommand {

    public CommandECloudDownload() {
        super("download");
    }

    private boolean isBlockedExpansion(String name) {
        String env = System.getenv("PAPI_BLOCKED_EXPANSIONS");
        if (env == null) {
            return false;
        }

        return Arrays.stream(env.split(","))
                .anyMatch(s -> s.equalsIgnoreCase(name));
    }

    @Override
    public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
                         @NotNull final CommandSender sender, @NotNull final String alias,
                         @NotNull @Unmodifiable final List<String> params) {
        final Message message = Message.raw("""
                                            Expansion downloads have been disabled to meet CurseForge's policy requirements.
                                            This limitation is imposed by the platform, not PlaceholderAPI.
        
                                            """).color(Color.RED)
                .insert(Message.raw("Please download expansions manually from ").color(Color.RED).insert(Message.raw("ecloud.placeholderapi.com").link("https://ecloud.placeholderapi.com").bold(true).italic(true).color(Color.WHITE)))
                .insert(Message.raw(" or install the full version of PlaceholderAPI from ").color(Color.RED))
                .insert(Message.raw("placeholderapi.com/downloads").link("https://placeholderapi.com/downloads").bold(true).italic(true).color(Color.WHITE));

        sender.sendMessage(message);
    }

}
