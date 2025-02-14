package com.firemerald.fecore.codec;

import java.util.function.Function;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

public class TwoStageCodec<A, B> implements Codec<Pair<A, B>> {
	final Codec<A> firstStage;
	final Function<A, Codec<B>> secondStage;

	public TwoStageCodec(Codec<A> firstStage, Function<A, Codec<B>> secondStage) {
		this.firstStage = firstStage;
		this.secondStage = secondStage;
	}

	@Override
	public <T> DataResult<T> encode(Pair<A, B> input, DynamicOps<T> ops, T prefix) {
		return firstStage.encode(input.getFirst(), ops, prefix).flatMap(p -> secondStage.apply(input.getFirst()).encode(input.getSecond(), ops, p));
	}

	@Override
	public <T> DataResult<Pair<Pair<A, B>, T>> decode(DynamicOps<T> ops, T input) {
		return firstStage.decode(ops, input).flatMap(p -> secondStage.apply(p.getFirst()).decode(ops, input).map(d -> Pair.of(Pair.of(p.getFirst(), d.getFirst()), d.getSecond())));
	}
}
