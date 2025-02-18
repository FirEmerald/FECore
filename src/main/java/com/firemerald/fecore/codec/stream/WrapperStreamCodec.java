package com.firemerald.fecore.codec.stream;

import net.minecraft.network.FriendlyByteBuf;

public record WrapperStreamCodec<T>(FriendlyByteBuf.Reader<T> reader, FriendlyByteBuf.Writer<T> writer) implements StreamCodec<T> {
	@Override
	public T decode(FriendlyByteBuf buf) {
		return reader.apply(buf);
	}

	@Override
	public void encode(FriendlyByteBuf buf, T object) {
		writer.accept(buf, object);
	}
}
