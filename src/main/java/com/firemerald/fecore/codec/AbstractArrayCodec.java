package com.firemerald.fecore.codec;

import java.util.Arrays;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;

public abstract class AbstractArrayCodec<A> implements Codec<A[]> {
	@Override
    public MapCodec<A[]> optionalFieldOf(final String name, final A[] defaultValue) {
        return optionalFieldOf2(name, defaultValue, false);
    }

	@Override
    public MapCodec<A[]> optionalFieldOf(final String name, final Lifecycle fieldLifecycle, final A[] defaultValue, final Lifecycle lifecycleOfDefault) {
        // setting lifecycle to stable on the outside since it will be overriden by the passed parameters
        return optionalFieldOf2(name, fieldLifecycle, defaultValue, lifecycleOfDefault, false);
    }

	@Override
    public MapCodec<A[]> lenientOptionalFieldOf(final String name, final A[] defaultValue) {
        return optionalFieldOf2(name, defaultValue, true);
    }

	@Override
    public MapCodec<A[]> lenientOptionalFieldOf(final String name, final Lifecycle fieldLifecycle, final A[] defaultValue, final Lifecycle lifecycleOfDefault) {
        return optionalFieldOf2(name, fieldLifecycle, defaultValue, lifecycleOfDefault, true);
    }

    private MapCodec<A[]> optionalFieldOf2(final String name, final A[] defaultValue, final boolean lenient) {
        return Codec.optionalField(name, this, lenient).xmap(
            o -> o.orElse(defaultValue),
            a -> Arrays.deepEquals(a, defaultValue) ? Optional.empty() : Optional.of(a)
        );
    }

    private MapCodec<A[]> optionalFieldOf2(final String name, final Lifecycle fieldLifecycle, final A[] defaultValue, final Lifecycle lifecycleOfDefault, final boolean lenient) {
        // setting lifecycle to stable on the outside since it will be overriden by the passed parameters
        return Codec.optionalField(name, this, lenient).stable().flatXmap(
            o -> o.map(v -> DataResult.success(v, fieldLifecycle)).orElse(DataResult.success(defaultValue, lifecycleOfDefault)),
            a -> Arrays.deepEquals(a, defaultValue) ? DataResult.success(Optional.empty(), lifecycleOfDefault) : DataResult.success(Optional.of(a), fieldLifecycle)
        );
    }
}
