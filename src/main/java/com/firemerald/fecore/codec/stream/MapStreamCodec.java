package com.firemerald.fecore.codec.stream;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

import net.minecraft.network.FriendlyByteBuf;

public record MapStreamCodec<T, U, V extends Map<T, U>>(IntFunction<? extends V> newMap, StreamCodec<T> keyCodec, StreamCodec<U> valCodec) implements StreamCodec<V> {
	@SuppressWarnings("unchecked")
	public MapStreamCodec(StreamCodec<T> keyCodec, StreamCodec<U> valCodec) {
		this(size -> (V) new HashMap<>(size), keyCodec, valCodec);
	}

	@Override
	public V decode(FriendlyByteBuf buf) {
		return buf.readMap(newMap, keyCodec.reader(), valCodec.reader());
	}

	@Override
	public void encode(FriendlyByteBuf buf, V object) {
		buf.writeMap(object, keyCodec.writer(), valCodec.writer());
	}

}
