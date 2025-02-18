package com.firemerald.fecore.codec;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import net.minecraft.Util;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

public class StringRepresentableCodec<S extends StringRepresentable> implements Codec<S> {
    public static <T extends StringRepresentable> Codec<T> fromValues(Supplier<T[]> valuesSup) {
        T[] values = valuesSup.get();
        Function<String, T> nameLookup = createNameLookup(values, Function.identity());
        ToIntFunction<T> indexLookup = Util.createIndexLookup(Arrays.asList(values));
        return new StringRepresentableCodec<>(values, nameLookup, indexLookup);
    }

    public static <T extends StringRepresentable> Function<String, T> createNameLookup(T[] values, Function<String, String> keyFunction) {
        if (values.length > 16) {
			final Map<String, T> map = Arrays.stream(values).collect(Collectors.toMap(val -> keyFunction.apply(val.getSerializedName()), Function.identity()));
            return name -> name == null ? null : map.get(name);
        } else {
            return name -> {
                for (T val : values) if (keyFunction.apply(val.getSerializedName()).equals(name)) return val;
                return null;
            };
        }
    }

    private final Codec<S> codec;

    public StringRepresentableCodec(S[] values, Function<String, S> nameLookup, ToIntFunction<S> indexLookup) {
        this.codec = ExtraCodecs.orCompressed(
            Codecs.stringResolver(StringRepresentable::getSerializedName, nameLookup),
            ExtraCodecs.idResolverCodec(indexLookup, index -> index >= 0 && index < values.length ? values[index] : null, -1)
        );
    }

    @Override
    public <T> DataResult<Pair<S, T>> decode(DynamicOps<T> ops, T value) {
        return this.codec.decode(ops, value);
    }

    @Override
	public <T> DataResult<T> encode(S input, DynamicOps<T> ops, T prefix) {
        return this.codec.encode(input, ops, prefix);
    }
}
