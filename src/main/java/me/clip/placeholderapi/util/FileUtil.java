/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2021 PlaceholderAPI Team
 *
 * PlaceholderAPI is free software: you can redistribute it and/or modify
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

package me.clip.placeholderapi.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class FileUtil {

  @Nullable
  public static <T> Class<? extends T> findClass(@NotNull final File file,
      @NotNull final Class<T> clazz) throws IOException, ClassNotFoundException {
    if (!file.exists()) {
      return null;
    }

    JarFile jar = new JarFile(file);
    Enumeration<? extends ZipEntry> entries = jar.entries();
    List<Class<? extends T>> classes = new ArrayList<>();
    try (URLClassLoader loader =
        new URLClassLoader(new URL[] {file.toURI().toURL()}, clazz.getClassLoader())) {
      while (entries.hasMoreElements()) {
        ZipEntry zip = entries.nextElement();
        JarEntry entry = jar.getJarEntry(zip.getName());
        if (entry == null) {
          continue;
        }
        String name = entry.getName();
        if (!name.endsWith(".class")) {
          continue;
        }
        name = name.substring(0, name.indexOf('.')).replace('/', '.');
        try {
          Class<?> loaded = loader.loadClass(name);
          if (clazz.isAssignableFrom(loaded)) {
            classes.add(loaded.asSubclass(clazz));
          }
        } catch (NoClassDefFoundError ignored) {
        }
      }
    }
    if (classes.isEmpty()) {
      return null;
    }
    return classes.get(0);
  }
}
