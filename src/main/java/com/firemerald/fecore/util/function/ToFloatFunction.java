package com.firemerald.fecore.util.function;

import java.util.function.Function;

@FunctionalInterface
public interface ToFloatFunction<T> extends Function<T, Float> {
	public float getFloat(T value);

	@Override
	public default Float apply(T value) {
		return getFloat(value);
	}
}
