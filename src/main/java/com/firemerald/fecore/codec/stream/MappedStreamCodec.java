package com.firemerald.fecore.codec.stream;

import java.util.function.Function;

import net.minecraft.network.FriendlyByteBuf;

public record MappedStreamCodec<T, U>(StreamCodec<T> codec, Function<T, U> from, Function<U, T> to) implements StreamCodec<U> {
	@Override
	public U decode(FriendlyByteBuf buf) {
		return from.apply(codec.decode(buf));
	}

	@Override
	public void encode(FriendlyByteBuf buf, U object) {
		codec.encode(buf, to.apply(object));
	}
}
