package com.firemerald.fecore.distribution;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.RandomSource;

public interface IDistribution<T> {
	public float weightedSize();

	public int collectionSize();

	public T get(float value);

	public Collection<T> getValues();

	public boolean hasWeights();

	public List<T> getUnweightedValues();

	public Map<T, Float> getWeightedValues();

	public T getFirstValue();

	public float getFirstWeight();

	public default boolean isEmpty() {
		return collectionSize() == 0;
	}

	public default T getRandom(@Nonnull RandomSource randomSource) {
		return get(randomSource.nextFloat() * weightedSize());
	}

	public default T getRandom() {
		return get((float) (Math.random() * weightedSize()));
	}

	public default boolean contains(@Nonnull T value) {
		return getValues().contains(value);
	}

	public default T pickOne(RandomSource randomSource)
	{
		if (this.isEmpty()) return null;
		else return this.getRandom(randomSource);
	}

	/**
	 * Helper method to pick a value from this distribution, or return the current value if it is already in the distribution.
	 *
	 * @param randomSource an instance of Random
	 * @param currentValue the current value.
	 * @return a randomly chosen value, or the current value if it was in the distribution
	 */
	public default T pickOne(RandomSource randomSource, @Nullable T currentValue)
	{
		return pickOne(randomSource, currentValue, 0);
	}

	public default T pickOne(RandomSource randomSource, @Nullable T currentValue, int offsetIfFound)
	{
		if (this.isEmpty()) return null;
		else if (currentValue != null && this.contains(currentValue)) {
			if (offsetIfFound == 0) return currentValue;
			else {
				List<T> values = getUnweightedValues();
				return values.get((values.indexOf(currentValue) + offsetIfFound) % values.size());
			}
		}
		return this.getRandom(randomSource);
	}

	public default T getOffset(T current, int offset) {
		if (isEmpty()) return null;
		else {
			List<T> values = getUnweightedValues();
			int index = values.indexOf(current);
			if (index >= 0) return values.get((index + offset) % values.size());
			else return null;
		}
	}

	public default T pickOne(RandomSource randomSource, @Nullable Predicate<T> currentValue)
	{
		return pickOne(randomSource, currentValue, 0);
	}

	public default T pickOne(RandomSource randomSource, @Nullable Predicate<T> currentValue, int offsetIfFound)
	{
		if (this.isEmpty()) return null;
		else if (currentValue != null) {
			List<T> values = getUnweightedValues();
			int index = getFirstIndex(values, currentValue);
			if (index >= 0) return values.get((index + offsetIfFound) % values.size());
		}
		return this.getRandom(randomSource);
	}

	public default T getOffset(Predicate<T> currentValue, int offset) {
		if (isEmpty()) return null;
		else {
			List<T> values = getUnweightedValues();
			int index = getFirstIndex(values, currentValue);
			if (index >= 0) return values.get((index + offset) % values.size());
			else return null;
		}
	}

	public static <T> int getFirstIndex(List<T> values, Predicate<T> predicate) {
		for (int i = 0; i < values.size(); ++i) if (predicate.test(values.get(i))) return i;
		return -1;
	}

	public <S> IDistribution<S> map(Function<T, S> map);

	public Stream<T> stream();

	public Stream<T> parallelStream();

	public void forEach(Consumer<T> action);

	public default boolean anyMatch(Predicate<T> predicate) {
		return stream().anyMatch(predicate);
	}

	public default boolean allMatch(Predicate<T> predicate) {
		return stream().allMatch(predicate);
	}

	public default boolean noneMatch(Predicate<T> predicate) {
		return stream().noneMatch(predicate);
	}
}