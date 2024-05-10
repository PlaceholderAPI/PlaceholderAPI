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

package me.clip.placeholderapi.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class FileUtil {

  @Nullable
  public static <T> Class<? extends T> findClass(@NotNull final File file,
      @NotNull final Class<T> clazz) throws IOException, ClassNotFoundException {
    if (!file.exists()) {
      return null;
    }

    final URL jar = file.toURI().toURL();
    final URLClassLoader loader = new URLClassLoader(new URL[]{jar}, clazz.getClassLoader());
    final List<String> matches = new ArrayList<>();
    final List<Class<? extends T>> classes = new ArrayList<>();

    try (final JarInputStream stream = new JarInputStream(jar.openStream())) {
      JarEntry entry;
      while ((entry = stream.getNextJarEntry()) != null) {
        final String name = entry.getName();
        if (name.isEmpty() || !name.endsWith(".class")) {
          continue;
        }

        matches.add(name.substring(0, name.lastIndexOf('.')).replace('/', '.'));
      }

      for (final String match : matches) {
        try {
          final Class<?> loaded = loader.loadClass(match);
          if (clazz.isAssignableFrom(loaded)) {
            classes.add(loaded.asSubclass(clazz));
          }
        } catch (final NoClassDefFoundError ignored) {
        }
      }
    }
    if (classes.isEmpty()) {
        loader.close();
        return null;
    }
    return classes.get(0);
  }

}
