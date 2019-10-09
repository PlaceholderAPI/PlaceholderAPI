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
            FilenameFilter filenameFilter = (dir, name) -> {
                if (fileName != null) {
                    return name.endsWith(".jar") && name.replace(".jar", "").equalsIgnoreCase(fileName.replace(".jar", ""));
                }

                return name.endsWith(".jar");
            };
            File[] jars = f.listFiles(filenameFilter);
            if (jars == null) {
                return list;
            }
            for (File file : jars) {
                list = gather(file.toURI().toURL(), list, type);
            }
            return list;
        } catch (Throwable ignored) {
        }

        return null;
    }

    private static List<Class<?>> gather(URL jar, List<Class<?>> list, Class<?> clazz) {
        if (list == null) {
            list = new ArrayList<>();
        }
        try (URLClassLoader cl = new URLClassLoader(new URL[]{jar}, clazz.getClassLoader()); JarInputStream jis = new JarInputStream(jar.openStream())) {
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
                    name = name.replace('/', '.');
                    String cname = name.substring(0, name.lastIndexOf(".class"));
                    Class<?> c = cl.loadClass(cname);
                    if (clazz.isAssignableFrom(c)) {
                        list.add(c);
                    }
                }
            }
        } catch (Throwable ignored) {
        }

        return list;
    }
}
