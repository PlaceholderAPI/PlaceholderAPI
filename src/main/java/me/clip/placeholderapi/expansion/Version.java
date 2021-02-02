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

  private final String version;
  private final Type type;
  private final boolean isSpigot;
  
  @Deprecated
  public Version(String version, boolean isSpigot) {
    this(version, isSpigot ? Type.SPIGOT : Type.UNKNOWN);
  }

  public Version(String version, Type type){
    this.version = version;
    this.type = type;
    this.isSpigot = type.isSpigot();
  }

  public String getVersion() {
    return version == null ? "unknown" : version;
  }
  
  public String getName(){
    return type.getName();
  }
  
  public boolean isSpigot() {
    return isSpigot;
  }
  
  public boolean isFork(){
    return type.isFork();
  }
  
  public boolean compareTo(String version) {
    return getVersion().equalsIgnoreCase(version);
  }
  
  public enum Type{
    SPIGOT("Spigot", true, false),
    PAPERMC("PaperMC", true, true),
    TUINITY("Tuinity", true, true),
    PURPUR("Purpur", true, true),
    
    UNKNOWN("Unknown", false, false);
    
    private final String name;
    private final boolean spigot;
    private final boolean fork;
    
    Type(String name, boolean spigot, boolean fork){
      this.name = name;
      this.spigot = spigot;
      this.fork = fork;
    }
  
    public String getName(){
      return name;
    }
  
    public boolean isSpigot(){
      return spigot;
    }
  
    public boolean isFork(){
      return fork;
    }
  }

}
