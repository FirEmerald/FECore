package com.firemerald.fecore.distribution;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.firemerald.fecore.util.SimpleCollector;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public interface IUnweightedDistribution<T> extends IDistribution<T> {
	@Override
	public default boolean hasWeights() {
		return false;
	}

	@Override
	public default float getFirstWeight() {
		return 1f;
	}

	@Override
	public default int collectionSize() {
		return getUnweightedValues().size();
	}

	@Override
	public default float weightedSize() {
		return collectionSize();
	}

	@Override
	public default T get(float value) {
		return value < 0 || value >= collectionSize() ? null : getUnweightedValues().get(Mth.floor(value));
	}

	@Override
	public default Collection<T> getValues() {
		return getUnweightedValues();
	}

	@Override
	public default Map<T, Float> getWeightedValues() {
		return getUnweightedValues().stream().collect(SimpleCollector.toFloatMap(Function.identity(), key -> 1f));
	}

	@Override
	public default boolean isEmpty() {
		return getUnweightedValues().isEmpty();
	}

	@Override
	public default T getRandom(@Nonnull RandomSource randomSource) {
		return getUnweightedValues().get(randomSource.nextInt(collectionSize()));
	}

	@Override
	public default T getRandom() {
		return getUnweightedValues().get(Mth.floor(Math.random() * collectionSize()));
	}

	@Override
	public default boolean contains(@Nonnull T value) {
		return getUnweightedValues().contains(value);
	}
}
