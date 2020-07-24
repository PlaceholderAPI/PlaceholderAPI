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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class FileUtil
{

	@NotNull
	public static <T> List<@NotNull Class<? extends T>> getClasses(@NotNull final File folder, @NotNull final Class<T> clazz) throws IOException, ClassNotFoundException
	{
		return getClasses(folder, clazz, null);
	}

	@NotNull
	public static <T> List<@NotNull Class<? extends T>> getClasses(@NotNull final File folder, @NotNull final Class<T> clazz, @Nullable final String target) throws IOException, ClassNotFoundException
	{
		if (!folder.exists())
		{
			return Collections.emptyList();
		}

		final File[] jars = folder.listFiles((dir, name) -> name.endsWith(".jar") && (target == null || name.replace(".jar", "").equalsIgnoreCase(target.replace(".jar", ""))));
		if (jars == null)
		{
			return Collections.emptyList();
		}

		final List<@NotNull Class<? extends T>> list = new ArrayList<>();

		for (final File file : jars)
		{
			gather(file.toURI().toURL(), clazz, list);
		}

		return list;
	}

	private static <T> void gather(@NotNull final URL jar, @NotNull final Class<T> clazz, @NotNull final List<@NotNull Class<? extends T>> list) throws IOException, ClassNotFoundException
	{
		try (final URLClassLoader loader = new URLClassLoader(new URL[]{jar}, clazz.getClassLoader()); final JarInputStream stream = new JarInputStream(jar.openStream()))
		{
			JarEntry entry;
			while ((entry = stream.getNextJarEntry()) != null)
			{
				final String name = entry.getName();
				if (name == null || name.isEmpty() || !name.endsWith(".class"))
				{
					continue;
				}

				try
				{
					final Class<?> loaded = loader.loadClass(name.substring(0, name.lastIndexOf('.')).replace('/', '.'));
					if (clazz.isAssignableFrom(loaded))
					{
						list.add(loaded.asSubclass(clazz));
					}
				}
				catch (final NoClassDefFoundError ignored)
				{
				}
			}
		}
	}

}
