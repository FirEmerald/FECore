package com.firemerald.fecore.codec;

import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

@SuppressWarnings("unchecked")
public record MultiCodec<A>(Supplier<String> encodeError, Supplier<String> decodeError, Codec<A>... codecs) implements Codec<A> {

	@Override
	public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
		for (Codec<A> codec : codecs) {
			DataResult<T> result = codec.encode(input, ops, prefix);
			if (result.isSuccess()) return result;
		}
		return DataResult.error(decodeError);
	}

	@Override
	public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
		for (Codec<A> codec : codecs) {
			DataResult<Pair<A, T>> result = codec.decode(ops, input);
			if (result.isSuccess()) return result;
		}
		return DataResult.error(decodeError);
	}
}
