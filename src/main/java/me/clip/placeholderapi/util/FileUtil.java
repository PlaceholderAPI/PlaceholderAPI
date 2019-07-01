/*
 *
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
 *
 *
 */
package me.clip.placeholderapi.util;

import me.clip.placeholderapi.PlaceholderAPIPlugin;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class FileUtil {

  public static List<Class<?>> getClasses(String folder, Class<?> type) {
    return getClasses(folder, null, type);
  }

  public static List<Class<?>> getClasses(String folder, String fileName, Class<?> type) {
    List<Class<?>> list = new ArrayList<>();

    try {
      File f = new File(PlaceholderAPIPlugin.getInstance().getDataFolder(), folder);
      if (!f.exists()) {
        return list;
      }

      FilenameFilter fileNameFilter = (dir, name) -> {
        if (fileName != null) {
          return name.endsWith(".jar") && name.replace(".jar", "")
              .equalsIgnoreCase(fileName.replace(".jar", ""));
        }

        return name.endsWith(".jar");
      };

      File[] jars = f.listFiles(fileNameFilter);
      if (jars == null) {
        return list;
      }

      for (File file : jars) {
        list = gather(file.toURI().toURL(), list, type);
      }

      return list;
    } catch (Throwable t) {
    }

    return null;
  }

  private static List<Class<?>> gather(URL jar, List<Class<?>> list, Class<?> clazz) {
    if (list == null) {
      list = new ArrayList<>();
    }

    try (URLClassLoader cl = new URLClassLoader(new URL[]{jar}, clazz.getClassLoader());
         JarInputStream jis = new JarInputStream(jar.openStream())) {

      while (true) {
        JarEntry j = jis.getNextJarEntry();
        if (j == null) {
          break;
        }

        String name = j.getName();
        if (name == null || name.isEmpty()) {
          continue;
        }

        if (name.endsWith(".class")) {
          name = name.replace("/", ".");
          String cname = name.substring(0, name.lastIndexOf(".class"));

          Class<?> c = cl.loadClass(cname);
          if (clazz.isAssignableFrom(c)) {
            list.add(c);
          }
        }
      }
    } catch (Throwable t) {
    }

    return list;
  }
}
