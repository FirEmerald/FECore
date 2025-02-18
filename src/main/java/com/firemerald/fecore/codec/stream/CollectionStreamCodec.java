package com.firemerald.fecore.codec.stream;

import java.util.Collection;
import java.util.function.IntFunction;

import net.minecraft.network.FriendlyByteBuf;

public record CollectionStreamCodec<T, U extends Collection<T>>(StreamCodec<T> codec, IntFunction<? extends U> newCollection) implements StreamCodec<U> {
	@Override
	public U decode(FriendlyByteBuf buf) {
		return buf.readCollection(newCollection, codec.reader());
	}

	@Override
	public void encode(FriendlyByteBuf buf, U object) {
		buf.writeCollection(object, codec.writer());
	}
}
