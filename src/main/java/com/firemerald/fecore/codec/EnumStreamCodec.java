package com.firemerald.fecore.codec;

import java.util.function.Supplier;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class EnumStreamCodec<B extends ByteBuf, E extends Enum<E>> implements StreamCodec<B, E> {
	public final Supplier<E[]> allEnumValues;

	public EnumStreamCodec(Supplier<E[]> allEnumValues) {
		this.allEnumValues = allEnumValues;
	}

	@Override
	public E decode(B buffer) {
		return allEnumValues.get()[buffer.readUnsignedByte()];
	}

	@Override
	public void encode(B buffer, E value) {
		buffer.writeByte(value.ordinal());
	}
}
