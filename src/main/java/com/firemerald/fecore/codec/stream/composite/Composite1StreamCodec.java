package com.firemerald.fecore.codec.stream.composite;

import java.util.function.Function;

import com.firemerald.fecore.codec.stream.StreamCodec;

import net.minecraft.network.FriendlyByteBuf;

public record Composite1StreamCodec<A, T>(
		StreamCodec<A> codecA, Function<T, A> getA,
		Function<A, T> construct) implements StreamCodec<T> {
	@Override
	public T decode(FriendlyByteBuf buf) {
		return construct.apply(codecA.decode(buf));
	}

	@Override
	public void encode(FriendlyByteBuf buf, T object) {
		codecA.encode(buf, getA.apply(object));
	}
}
