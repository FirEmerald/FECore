package com.firemerald.fecore.distribution;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.util.RandomSource;

public interface IWeightedDistribution<T> extends IDistribution<T> {
	@Override
	public default boolean hasWeights() {
		return true;
	}

	@Override
	public default int collectionSize() {
		return getWeightedValues().size();
	}

	@Override
	public default Collection<T> getValues() {
		return getWeightedValues().keySet();
	}

	@Override
	public default List<T> getUnweightedValues() {
		return getWeightedValues().keySet().stream().toList();
	}

	@Override
	public default boolean isEmpty() {
		return getWeightedValues().isEmpty();
	}

	@Override
	public default T getRandom(@Nonnull RandomSource randomSource) {
		return get(randomSource.nextFloat() * weightedSize());
	}

	@Override
	public default T getRandom() {
		return get((float) (Math.random() * weightedSize()));
	}

	@Override
	public default boolean contains(@Nonnull T value) {
		return getWeightedValues().containsKey(value);
	}
}
