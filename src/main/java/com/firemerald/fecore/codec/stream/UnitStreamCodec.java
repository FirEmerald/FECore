package com.firemerald.fecore.codec.stream;

import net.minecraft.network.FriendlyByteBuf;

public record UnitStreamCodec<T>(T constant) implements StreamCodec<T> {
	@Override
	public T decode(FriendlyByteBuf buf) {
		return constant;
	}

	@Override
	public void encode(FriendlyByteBuf buf, T object) {}
}
