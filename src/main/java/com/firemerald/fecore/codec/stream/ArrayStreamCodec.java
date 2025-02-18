package com.firemerald.fecore.codec.stream;

import java.util.function.IntFunction;

import net.minecraft.network.FriendlyByteBuf;

public record ArrayStreamCodec<T>(StreamCodec<T> codec, IntFunction<T[]> newArray) implements StreamCodec<T[]> {
	@Override
	public T[] decode(FriendlyByteBuf buf) {
		int size = buf.readVarInt();
		T[] array = newArray.apply(size);
		for (int i = 0; i < size; ++i) array[i] = codec.decode(buf);
		return array;
	}

	@Override
	public void encode(FriendlyByteBuf buf, T[] object) {
		buf.writeVarInt(object.length);
		for (T obj : object) codec.encode(buf, obj);
	}
}
