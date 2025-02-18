package com.firemerald.fecore.codec.stream.composite;

import java.util.function.Function;

import com.firemerald.fecore.codec.stream.StreamCodec;
import com.firemerald.fecore.util.function.OctaFunction;

import net.minecraft.network.FriendlyByteBuf;

public record Composite8StreamCodec<A, B, C, D, E, F, G, H, T>(
		StreamCodec<A> codecA, Function<T, A> getA,
		StreamCodec<B> codecB, Function<T, B> getB,
		StreamCodec<C> codecC, Function<T, C> getC,
		StreamCodec<D> codecD, Function<T, D> getD,
		StreamCodec<E> codecE, Function<T, E> getE,
		StreamCodec<F> codecF, Function<T, F> getF,
		StreamCodec<G> codecG, Function<T, G> getG,
		StreamCodec<H> codecH, Function<T, H> getH,
		OctaFunction<A, B, C, D, E, F, G, H, T> construct) implements StreamCodec<T> {
	@Override
	public T decode(FriendlyByteBuf buf) {
		A a = codecA.decode(buf);
		B b = codecB.decode(buf);
		C c = codecC.decode(buf);
		D d = codecD.decode(buf);
		E e = codecE.decode(buf);
		F f = codecF.decode(buf);
		G g = codecG.decode(buf);
		H h = codecH.decode(buf);
		return construct.apply(a, b, c, d, e, f, g, h);
	}

	@Override
	public void encode(FriendlyByteBuf buf, T object) {
		codecA.encode(buf, getA.apply(object));
		codecB.encode(buf, getB.apply(object));
		codecC.encode(buf, getC.apply(object));
		codecD.encode(buf, getD.apply(object));
		codecE.encode(buf, getE.apply(object));
		codecF.encode(buf, getF.apply(object));
		codecG.encode(buf, getG.apply(object));
		codecH.encode(buf, getH.apply(object));
	}
}
