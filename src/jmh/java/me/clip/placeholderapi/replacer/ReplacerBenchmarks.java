/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2026 PlaceholderAPI Team
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

package me.clip.placeholderapi.replacer;

import me.clip.placeholderapi.Values;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.Nullable;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode({Mode.AverageTime, Mode.Throughput})
@Fork(value = 3, warmups = 1)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
public class ReplacerBenchmarks {

    private Function<String, @Nullable PlaceholderExpansion> expansionFunction;

    @Setup
    public void setup() {
        this.expansionFunction = Values.PLACEHOLDERS::get;
    }

    @Benchmark
    public void measureCharsReplacerSmallText(final Blackhole blackhole) {
        blackhole.consume(Values.CHARS_REPLACER.apply(Values.SMALL_TEXT, null, expansionFunction));
    }

    @Benchmark
    public void measureCharsReplacerLargeText(final Blackhole blackhole) {
        blackhole.consume(Values.CHARS_REPLACER.apply(Values.LARGE_TEXT, null, expansionFunction));
    }

    @Benchmark
    public void measureCharsReplacerSmallTextOld(final Blackhole blackhole) {
        blackhole.consume(Values.OLD_CHARS_REPLACER.apply(Values.SMALL_TEXT, null, expansionFunction));
    }

    @Benchmark
    public void measureCharsReplacerLargeTextOld(final Blackhole blackhole) {
        blackhole.consume(Values.OLD_CHARS_REPLACER.apply(Values.LARGE_TEXT, null, expansionFunction));
    }
}