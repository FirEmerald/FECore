package com.firemerald.fecore.codec.stream.composite;

import java.util.function.Function;

import com.firemerald.fecore.codec.stream.StreamCodec;
import com.firemerald.fecore.util.function.TriFunction;

import net.minecraft.network.FriendlyByteBuf;

public record Composite3StreamCodec<A, B, C, T>(
		StreamCodec<A> codecA, Function<T, A> getA,
		StreamCodec<B> codecB, Function<T, B> getB,
		StreamCodec<C> codecC, Function<T, C> getC,
		TriFunction<A, B, C, T> construct) implements StreamCodec<T> {
	@Override
	public T decode(FriendlyByteBuf buf) {
		A a = codecA.decode(buf);
		B b = codecB.decode(buf);
		C c = codecC.decode(buf);
		return construct.apply(a, b, c);
	}

	@Override
	public void encode(FriendlyByteBuf buf, T object) {
		codecA.encode(buf, getA.apply(object));
		codecB.encode(buf, getB.apply(object));
		codecC.encode(buf, getC.apply(object));
	}
}
