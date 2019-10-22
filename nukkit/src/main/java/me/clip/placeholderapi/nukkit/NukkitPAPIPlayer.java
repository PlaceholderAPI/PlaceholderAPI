/*
 * PlaceholderAPI
 * Copyright (C) 2019 Ryan McCarthy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.clip.placeholderapi.nukkit;

import cn.nukkit.Player;
import cn.nukkit.network.SourceInterface;
import me.clip.placeholderapi.common.PAPIPlayer;
import me.clip.placeholderapi.common.util.PlatformUtil;

import java.util.UUID;

public class NukkitPAPIPlayer extends Player implements PAPIPlayer {
    private static NukkitPAPIPlayer instance;

    public NukkitPAPIPlayer(SourceInterface interfaz, Long clientID, String ip, int port) {
        super(interfaz, clientID, ip, port);
    }

    @Override
    public PlatformUtil.Platform getPlayerPlatform() {
        return PlatformUtil.Platform.NUKKIT;
    }

    @Override
    public PAPIPlayer getPAPIPlayer() {
        return this;
    }

    @Override
    public boolean isOnline() {
        return super.isOnline();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public UUID getUniqueId() {
        return super.getUniqueId();
    }

    @Override
    public void sendMessage(String message) {
        super.sendMessage(message);
    }

    public static NukkitPAPIPlayer getInstance() {
        return instance;
    }
}
