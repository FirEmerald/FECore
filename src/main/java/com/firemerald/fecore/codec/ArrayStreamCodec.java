package com.firemerald.fecore.codec;

import java.util.function.IntFunction;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class ArrayStreamCodec<B extends ByteBuf, A> implements StreamCodec<B, A[]> {
	public final StreamCodec<B, A> singular;
	public final IntFunction<A[]> newArray;

	public ArrayStreamCodec(StreamCodec<B, A> singular, IntFunction<A[]> newArray) {
		this.singular = singular;
		this.newArray = newArray;
	}

	@Override
	public A[] decode(B buffer) {
		int size = ByteBufCodecs.readCount(buffer, Integer.MAX_VALUE);
		A[] array = newArray.apply(size);
		for (int i = 0; i < size; ++i) array[i] = singular.decode(buffer);
		return array;
	}

	@Override
	public void encode(B buffer, A[] value) {
		ByteBufCodecs.writeCount(buffer, value.length, Integer.MAX_VALUE);
		for (A val : value) singular.encode(buffer, val);
	}
}
