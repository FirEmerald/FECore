package com.firemerald.fecore.codec;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

public class EnumCodec<E extends Enum<E>> implements Codec<E> {
	private final Map<String, E> toEnum;
	private final Map<E, String> toString;

	@SuppressWarnings("unchecked")
	@SafeVarargs
	public EnumCodec(E... values) {
		if (values.length == 0) {
			this.toEnum = Collections.emptyMap();
			this.toString = Collections.emptyMap();
		} else {
			this.toEnum = new HashMap<>();
			this.toString = new EnumMap<>((Class<E>) values[0].getClass());
			for (E val : values) {
				String name = val.name().toLowerCase(Locale.ROOT);
				this.toString.put(val, name);
				this.toEnum.put(name, val);
			}
		}
	}

	@Override
	public <T> DataResult<T> encode(E input, DynamicOps<T> ops, T prefix) {
		String name = toString.get(input);
		if (name == null) return DataResult.error(() -> "Enum value " + input + " not registered in codec");
		else return Codec.STRING.encode(name, ops, prefix);
	}

	@Override
	public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> ops, T input) {
		return Codec.STRING.decode(ops, input).flatMap(pair -> {
			String res = pair.getFirst().toLowerCase(Locale.ROOT);
			E val = toEnum.get(res);
			if (val == null) return DataResult.error(() -> "Invalid value " + res + " not found in enum");
			else return DataResult.success(Pair.of(val, pair.getSecond()));
		});
	}
}
