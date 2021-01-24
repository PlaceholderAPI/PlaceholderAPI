/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2020 PlaceholderAPI Team
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

public final class Version {

  private final boolean isSpigot;
  private final String version;
  private final Type type;

  public Version(String version, Type type){
    this.version = version;
    this.type = type;
    this.isSpigot = type.isSpigot();
  }
  
  @Deprecated
  public Version(String version, boolean isSpigot) {
    this.version = version;
    this.type = isSpigot ? Type.SPIGOT : Type.UNKNOWN;
    this.isSpigot = isSpigot;
  }

  public String getVersion() {
    return version == null ? "unknown" : version;
  }

  public boolean isSpigot() {
    return isSpigot;
  }
  
  public String getName(){
    return type.getName();
  }
  
  public Type getType(){
    return type;
  }
  
  public boolean compareTo(String version) {
    return getVersion().equalsIgnoreCase(version);
  }
  
  public enum Type{
    SPIGOT("Spigot", true),
    PAPERMC("PaperMC", true),
    TUINITY("Tuinity", true),
    PURPUR("Purpur", true),
    
    UNKNOWN("Unknown", false);
    
    private final String name;
    private final boolean spigot;
    
    Type(String name, boolean spigot){
      this.name = name;
      this.spigot = spigot;
    }
  
    public String getName(){
      return name;
    }
  
    public boolean isSpigot(){
      return spigot;
    }
  }

}
