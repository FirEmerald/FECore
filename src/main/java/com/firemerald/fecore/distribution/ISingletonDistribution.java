package com.firemerald.fecore.distribution;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.RandomSource;

public interface ISingletonDistribution<T> extends IDistribution<T> {
	@Override
	public default float weightedSize() {
		return getFirstWeight();
	}

	@Override
	public default int collectionSize() {
		return 1;
	}

	@Override
	public default T get(float value) {
		return value < 0 || value >= getFirstWeight() ? null : getFirstValue();
	}

	@Override
	public default Collection<T> getValues() {
		return Collections.singleton(getFirstValue());
	}

	@Override
	public default List<T> getUnweightedValues() {
		return Collections.singletonList(getFirstValue());
	}

	@Override
	public default Map<T, Float> getWeightedValues() {
		return Collections.singletonMap(getFirstValue(), getFirstWeight());
	}

	@Override
	public default boolean isEmpty() {
		return false;
	}

	@Override
	public default T getRandom(@Nonnull RandomSource randomSource) {
		return getFirstValue();
	}

	@Override
	public default T getRandom() {
		return getFirstValue();
	}

	@Override
	public default boolean contains(@Nonnull T value) {
		return value.equals(getFirstValue());
	}

	@Override
	public default T getOffset(T current, int offset) {
		return current;
	}

	@Override
	public default T pickOne(RandomSource randomSource, @Nullable T currentValue, int offsetIfFound) {
		return getFirstValue();
	}

	@Override
	public default Stream<T> stream() {
		return Stream.of(getFirstValue());
	}

	@Override
	public default Stream<T> parallelStream() {
		return Stream.of(getFirstValue());
	}

	@Override
	public default void forEach(Consumer<T> action) {
		action.accept(getFirstValue());
	}

	public default boolean matches(Predicate<T> predicate) {
		return predicate.test(getFirstValue());
	}

	@Override
	public default boolean anyMatch(Predicate<T> predicate) {
		return matches(predicate);
	}

	@Override
	public default boolean allMatch(Predicate<T> predicate) {
		return matches(predicate);
	}

	@Override
	public default boolean noneMatch(Predicate<T> predicate) {
		return !matches(predicate);
	}
}
