package com.firemerald.fecore.distribution;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class DistributionBuilder<T> {
	private final Map<T, Float> weights;

	public DistributionBuilder() {
		weights = new HashMap<>();
	}

	public DistributionBuilder(int size) {
		weights = new HashMap<>(size);
	}

	public DistributionBuilder(@Nonnull Map<T, Float> weights) {
		this.weights = new HashMap<>(weights);
	}

	public DistributionBuilder(@Nonnull T value, float weight) {
		this(1);
		add(value, weight);
	}

	public DistributionBuilder(@Nonnull T value) {
		this(value, 1f);
	}

	public DistributionBuilder(@Nonnull Collection<T> values) {
		this(values.size());
		for (T value : values) add(value, 1f);
	}

	@SafeVarargs
	public DistributionBuilder(T... values) {
		this(values.length);
		for (T value : values) add(value, 1f);
	}

	public DistributionBuilder(IDistribution<T> derive) {
		this(derive.getWeightedValues());
	}

	public DistributionBuilder<T> add(@Nonnull T object, float weight) {
		weights.compute(object, (o, w) -> w == null ? weight : (weight + w));
		return this;
	}

	public DistributionBuilder<T> remove(@Nonnull T object, float weight) {
		add(object, -weight);
		return this;
	}

	public DistributionBuilder<T> remove(@Nonnull T object) {
		weights.remove(object);
		return this;
	}

	public IDistribution<T> build() {
		return DistributionUtil.get(weights);
	}
}
