package com.firemerald.fecore.util.function;

import java.util.function.Function;

@FunctionalInterface
public interface ToIntFunction<T> extends Function<T, Integer> {
	public int getInt(T value);

	@Override
	public default Integer apply(T value) {
		return getInt(value);
	}
}
