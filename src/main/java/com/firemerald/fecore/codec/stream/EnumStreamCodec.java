package com.firemerald.fecore.codec.stream;

import net.minecraft.network.FriendlyByteBuf;

public class EnumStreamCodec<T extends Enum<T>> implements StreamCodec<T> {
	private final T[] values;

	public EnumStreamCodec(Class<T> clazz) {
		values = clazz.getEnumConstants();
	}

	@Override
	public T decode(FriendlyByteBuf buf) {
		return values[buf.readVarInt()];
	}

	@Override
	public void encode(FriendlyByteBuf buf, T object) {
		buf.writeVarInt(object.ordinal());
	}
}
