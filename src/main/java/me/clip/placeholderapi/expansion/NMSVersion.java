/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2024 PlaceholderAPI Team
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

package me.clip.placeholderapi.expansion;

public enum NMSVersion {

  UNKNOWN("unknown"),
  SPIGOT_1_7_R1("v1_7_R1"),
  SPIGOT_1_7_R2("v1_7_R2"),
  SPIGOT_1_7_R3("v1_7_R3"),
  SPIGOT_1_7_R4("v1_7_R4"),
  SPIGOT_1_8_R1("v1_8_R1"),
  SPIGOT_1_8_R2("v1_8_R2"),
  SPIGOT_1_8_R3("v1_8_R3"),
  SPIGOT_1_9_R1("v1_9_R1"),
  SPIGOT_1_9_R2("v1_9_R2"),
  SPIGOT_1_10_R1("v1_10_R1"),
  SPIGOT_1_11_R1("v1_11_R1"),
  SPIGOT_1_12_R1("v1_12_R1"),
  SPIGOT_1_13_R1("v1_13_R1"),
  SPIGOT_1_13_R2("v1_13_R2"),
  SPIGOT_1_14_R1("v1_14_R1"),
  SPIGOT_1_15_R1("v1_15_R1"),
  SPIGOT_1_16_R1("v1_16_R1"),
  SPIGOT_1_16_R2("v1_16_R2"),
  SPIGOT_1_16_R3("v1_16_R3"),
  SPIGOT_1_17_R1("v1_17_R1"),
  SPIGOT_1_18_R1("v1_18_R1"),
  SPIGOT_1_19_R1("v1_19_R1"),
  SPIGOT_1_19_R2("v1_19_R2"),
  SPIGOT_1_19_R3("v1_19_R3"),
  SPIGOT_1_20_R1("v1_20_R1"),
  SPIGOT_1_20_R2("v1_20_R2"),
  SPIGOT_1_20_R3("v1_20_R3"),
  SPIGOT_1_20_R4("v1_20_R4"),
  SPIGOT_1_21_R1("v1_21_R1");

  private final String version;

  NMSVersion(String version) {
    this.version = version;
  }

  public static NMSVersion getVersion(String version) {
    for (NMSVersion v : values()) {
      if (v.getVersion().equalsIgnoreCase(version)) {
        return v;
      }
    }

    return NMSVersion.UNKNOWN;
  }

  public String getVersion() {
    return version;
  }

}
