package me.clip.placeholderapi.util;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

/**
 * For the record, I am not sorry.
 */
public final class Format
{

	private Format()
	{}


	public enum Align
	{
		LEFT, RIGHT
	}


	@NotNull
	public static Optional<List<String>> tablify(@NotNull final Align align, @NotNull final List<List<String>> rows)
	{
		return findSpacing(rows)
				.map(spacing -> buildFormat(align, spacing))
				.map(format -> rows.stream()
								   .map(row -> String.format(format, row.toArray()).substring(align == Align.RIGHT ? 2 : 0))
								   .collect(toList()));
	}


	@NotNull
	private static String buildFormat(@NotNull final Align align, @NotNull final int[] spacing)
	{
		return stream(spacing)
				.mapToObj(space -> "%" + (align == Align.LEFT ? "-" : "") + (space + 2) + "s")
				.collect(joining());
	}

	@NotNull
	private static Optional<int[]> findSpacing(@NotNull final List<List<String>> rows)
	{
		return rows.stream()
				   .map(row -> row.stream().mapToInt(String::length).toArray())
				   .reduce((l, r) -> range(0, min(l.length, r.length)).map(i -> max(l[i], r[i])).toArray());
	}

}
