package com.firemerald.fecore.codec;

import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;

@SuppressWarnings("unchecked")
public record AdapterCodec<A>(Supplier<String> decodeError, Function<A, Encoder<A>> encoder, Decoder<A>... decoders) implements Codec<A> {
	@Override
	public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
		return encodeHelper(input, ops, prefix);
	}

	private <S, R extends A> DataResult<S> encodeHelper(A input, DynamicOps<S> ops, S prefix) {
		return ((Codec<R>) encoder.apply(input)).encode((R) input, ops, prefix);
	}

	@Override
	public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
		for (Decoder<A> decoder : decoders) {
			DataResult<Pair<A, T>> result = decoder.decode(ops, input);
			if (result.isSuccess()) return result;
		}
		return DataResult.error(decodeError);
	}

}
