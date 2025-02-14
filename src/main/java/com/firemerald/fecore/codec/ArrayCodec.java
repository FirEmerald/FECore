package com.firemerald.fecore.codec;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

public class ArrayCodec<A> extends AbstractArrayCodec<A> {
	public final Codec<List<A>> plural;
	public final IntFunction<A[]> newArray;

	public ArrayCodec(Codec<List<A>> plural, IntFunction<A[]> newArray) {
		this.plural = plural;
		this.newArray = newArray;
	}

	@Override
	public <T> DataResult<T> encode(A[] input, DynamicOps<T> ops, T prefix) {
		return plural.encode(Arrays.asList(input), ops, prefix);
	}

	@Override
	public <T> DataResult<Pair<A[], T>> decode(DynamicOps<T> ops, T input) {
		return plural.decode(ops, input).map(res -> Pair.of(res.getFirst().toArray(newArray), res.getSecond()));
	}
}
