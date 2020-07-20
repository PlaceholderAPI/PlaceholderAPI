package me.clip.placeholderapi.replacer;

import me.clip.placeholderapi.Values;
import org.junit.jupiter.api.Test;

import static me.clip.placeholderapi.Values.MockPlayerPlaceholderHook.PLAYER_NAME;
import static me.clip.placeholderapi.Values.MockPlayerPlaceholderHook.PLAYER_X;
import static me.clip.placeholderapi.Values.MockPlayerPlaceholderHook.PLAYER_Y;
import static me.clip.placeholderapi.Values.MockPlayerPlaceholderHook.PLAYER_Z;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ReplacerUnitTester
{

	@Test
	void testCharsReplacerProducesExpectedSingleValue()
	{
		assertEquals(PLAYER_NAME, Values.CHARS_REPLACER.apply("%player_name%", null, Values.PLACEHOLDERS::get));
	}

	@Test
	void testRegexReplacerProducesExpectedSingleValue()
	{
		assertEquals(PLAYER_NAME, Values.REGEX_REPLACER.apply("%player_name%", null, Values.PLACEHOLDERS::get));
	}

	@Test
	void testCharsReplacerProducesExpectedSentence()
	{
		assertEquals(String.format("My name is %s and my location is (%s, %s, %s), this placeholder is invalid %%server_name%%", PLAYER_NAME, PLAYER_X, PLAYER_Y, PLAYER_Z), Values.CHARS_REPLACER.apply(Values.LARGE_TEXT, null, Values.PLACEHOLDERS::get));
	}

	@Test
	void testRegexReplacerProducesExpectedSentence()
	{
		assertEquals(String.format("My name is %s and my location is (%s, %s, %s), this placeholder is invalid %%server_name%%", PLAYER_NAME, PLAYER_X, PLAYER_Y, PLAYER_Z), Values.REGEX_REPLACER.apply(Values.LARGE_TEXT, null, Values.PLACEHOLDERS::get));
	}

	@Test
	void testResultsAreTheSameAsReplacement()
	{
		final String resultChars = Values.CHARS_REPLACER.apply("%player_name%", null, Values.PLACEHOLDERS::get);
		final String resultRegex = Values.REGEX_REPLACER.apply("%player_name%", null, Values.PLACEHOLDERS::get);

		assertEquals(resultChars, resultRegex);

		assertEquals(PLAYER_NAME, resultChars);
	}

	@Test
	void testResultsAreTheSameNoReplacement()
	{
		final String resultChars = Values.CHARS_REPLACER.apply("%player_location%", null, Values.PLACEHOLDERS::get);
		final String resultRegex = Values.REGEX_REPLACER.apply("%player_location%", null, Values.PLACEHOLDERS::get);

		assertEquals(resultChars, resultRegex);
	}

	@Test
	void testCharsReplacerIgnoresMalformed()
	{
		final String text = "10% and %hello world 15%";

		assertEquals(text, Values.CHARS_REPLACER.apply(text, null, Values.PLACEHOLDERS::get));
	}

}
