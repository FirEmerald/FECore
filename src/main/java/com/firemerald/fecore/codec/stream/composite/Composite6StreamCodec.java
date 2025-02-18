package com.firemerald.fecore.codec.stream.composite;

import java.util.function.Function;

import com.firemerald.fecore.codec.stream.StreamCodec;
import com.firemerald.fecore.util.function.SexaFunction;

import net.minecraft.network.FriendlyByteBuf;

public record Composite6StreamCodec<A, B, C, D, E, F, T>(
		StreamCodec<A> codecA, Function<T, A> getA,
		StreamCodec<B> codecB, Function<T, B> getB,
		StreamCodec<C> codecC, Function<T, C> getC,
		StreamCodec<D> codecD, Function<T, D> getD,
		StreamCodec<E> codecE, Function<T, E> getE,
		StreamCodec<F> codecF, Function<T, F> getF,
		SexaFunction<A, B, C, D, E, F, T> construct) implements StreamCodec<T> {
	@Override
	public T decode(FriendlyByteBuf buf) {
		A a = codecA.decode(buf);
		B b = codecB.decode(buf);
		C c = codecC.decode(buf);
		D d = codecD.decode(buf);
		E e = codecE.decode(buf);
		F f = codecF.decode(buf);
		return construct.apply(a, b, c, d, e, f);
	}

	@Override
	public void encode(FriendlyByteBuf buf, T object) {
		codecA.encode(buf, getA.apply(object));
		codecB.encode(buf, getB.apply(object));
		codecC.encode(buf, getC.apply(object));
		codecD.encode(buf, getD.apply(object));
		codecE.encode(buf, getE.apply(object));
		codecF.encode(buf, getF.apply(object));
	}
}
