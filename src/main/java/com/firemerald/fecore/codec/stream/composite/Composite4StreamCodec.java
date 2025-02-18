package com.firemerald.fecore.codec.stream.composite;

import java.util.function.Function;

import com.firemerald.fecore.codec.stream.StreamCodec;
import com.firemerald.fecore.util.function.QuadFunction;

import net.minecraft.network.FriendlyByteBuf;

public record Composite4StreamCodec<A, B, C, D, T>(
		StreamCodec<A> codecA, Function<T, A> getA,
		StreamCodec<B> codecB, Function<T, B> getB,
		StreamCodec<C> codecC, Function<T, C> getC,
		StreamCodec<D> codecD, Function<T, D> getD,
		QuadFunction<A, B, C, D, T> construct) implements StreamCodec<T> {
	@Override
	public T decode(FriendlyByteBuf buf) {
		A a = codecA.decode(buf);
		B b = codecB.decode(buf);
		C c = codecC.decode(buf);
		D d = codecD.decode(buf);
		return construct.apply(a, b, c, d);
	}

	@Override
	public void encode(FriendlyByteBuf buf, T object) {
		codecA.encode(buf, getA.apply(object));
		codecB.encode(buf, getB.apply(object));
		codecC.encode(buf, getC.apply(object));
		codecD.encode(buf, getD.apply(object));
	}
}
