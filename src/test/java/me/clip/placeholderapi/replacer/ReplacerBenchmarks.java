package me.clip.placeholderapi.replacer;

import me.clip.placeholderapi.Values;
import org.openjdk.jmh.annotations.Benchmark;

public class ReplacerBenchmarks
{

	@Benchmark
	public void measureCharsReplacerSmallText()
	{
		Values.CHARS_REPLACER.apply(Values.SMALL_TEXT, null, Values.PLACEHOLDERS::get);
	}

	@Benchmark
	public void measureRegexReplacerSmallText()
	{
		Values.REGEX_REPLACER.apply(Values.SMALL_TEXT, null, Values.PLACEHOLDERS::get);
	}

	@Benchmark
	public void measureCharsReplacerLargeText()
	{
		Values.CHARS_REPLACER.apply(Values.LARGE_TEXT, null, Values.PLACEHOLDERS::get);
	}

	@Benchmark
	public void measureRegexReplacerLargeText()
	{
		Values.REGEX_REPLACER.apply(Values.LARGE_TEXT, null, Values.PLACEHOLDERS::get);
	}

	@Benchmark
	public void measureTestsReplacerSmallText()
	{
		Values.TESTS_REPLACER.apply(Values.SMALL_TEXT, null, Values.PLACEHOLDERS::get);
	}

	@Benchmark
	public void measureTestsReplacerLargeText()
	{
		Values.TESTS_REPLACER.apply(Values.LARGE_TEXT, null, Values.PLACEHOLDERS::get);
	}

}