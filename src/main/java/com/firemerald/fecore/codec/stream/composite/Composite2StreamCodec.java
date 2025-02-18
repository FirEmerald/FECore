package com.firemerald.fecore.codec.stream.composite;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.firemerald.fecore.codec.stream.StreamCodec;

import net.minecraft.network.FriendlyByteBuf;

public record Composite2StreamCodec<A, B, T>(
		StreamCodec<A> codecA, Function<T, A> getA,
		StreamCodec<B> codecB, Function<T, B> getB,
		BiFunction<A, B, T> construct) implements StreamCodec<T> {
	@Override
	public T decode(FriendlyByteBuf buf) {
		A a = codecA.decode(buf);
		B b = codecB.decode(buf);
		return construct.apply(a, b);
	}

	@Override
	public void encode(FriendlyByteBuf buf, T object) {
		codecA.encode(buf, getA.apply(object));
		codecB.encode(buf, getB.apply(object));
	}
}
