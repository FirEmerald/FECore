package com.firemerald.fecore.codec;

import java.util.function.Function;
import java.util.function.Predicate;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;

import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;

public record CodedCodec<U, V>(Function<U, V> encode, Function<V, U> decode, DynamicOps<V> format, Predicate<DynamicOps<?>> isThisFormat) implements Codec<U> {
	public static <U> CodedCodec<U, JsonElement> ofJson(Function<U, JsonElement> encode, Function<JsonElement, U> decode) {
		return new CodedCodec<>(encode, decode, JsonOps.INSTANCE, ops -> ops instanceof JsonOps);
	}

	public static <U> CodedCodec<U, Tag> ofNbt(Function<U, Tag> encode, Function<Tag, U> decode) {
		return new CodedCodec<>(encode, decode, NbtOps.INSTANCE, ops -> ops instanceof NbtOps);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> DataResult<T> encode(U input, DynamicOps<T> ops, T prefix) {
		try {
			V encoded = encode.apply(input);
			return DataResult.success(isThisFormat.test(ops) ? (T) encoded : format.convertTo(ops, encoded));
		} catch (Throwable t) {
			return DataResult.error(t::getLocalizedMessage);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> DataResult<Pair<U, T>> decode(DynamicOps<T> ops, T input) {
		try {
			V data = isThisFormat.test(ops) ? (V) input : ops.convertTo(format, input);
			return DataResult.success(Pair.of(decode.apply(data), input));
		} catch (Throwable t) {
			return DataResult.error(t::getLocalizedMessage);
		}
	}
}
