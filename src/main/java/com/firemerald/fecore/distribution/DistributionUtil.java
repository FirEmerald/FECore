package com.firemerald.fecore.distribution;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import com.firemerald.fecore.util.SimpleCollector;
import com.firemerald.fecore.util.function.ToFloatFunction;

public class DistributionUtil {
	@SafeVarargs
	public static <T> IDistribution<T> get(T... values) {
		if (values.length == 0) return EmptyDistribution.get();
		else if (values.length == 1) return new SingletonUnweightedDistribution<>(values[0]);
		else return new UnweightedDistribution<>(Arrays.asList(values));
	}

	public static <T> IDistribution<T> get(Collection<T> values) {
		if (values.isEmpty()) return EmptyDistribution.get();
		else if (values.size() == 1) return new SingletonUnweightedDistribution<>(values.iterator().next());
		else return new UnweightedDistribution<>(values);
	}

	public static <T> IDistribution<T> get(Map<T, Float> weights) {
		return getWithWeightFromEntry(weights, Entry::getKey, Entry::getValue);
	}

	public static <T, U, V> IDistribution<V> getWithWeightFromKey(Map<T, U> map, Function<Entry<T, U>, V> toKey, ToFloatFunction<T> toWeight) {
		return getWithWeightFromEntry(map, toKey, entry -> toWeight.getFloat(entry.getKey()));
	}

	public static <T, U, V> IDistribution<V> getWithWeightFromValue(Map<T, U> map, Function<Entry<T, U>, V> toKey, ToFloatFunction<U> toWeight) {
		return getWithWeightFromEntry(map, toKey, entry -> toWeight.getFloat(entry.getValue()));
	}

	public static <T, U, V> IDistribution<V> getWithWeightFromEntry(Map<T, U> map, Function<Entry<T, U>, V> toKey, ToFloatFunction<Entry<T, U>> toWeight) {
		if (map.isEmpty()) return EmptyDistribution.get();
		else if (map.size() == 1) {
			Entry<T, U> entry = map.entrySet().iterator().next();
			V key = toKey.apply(entry);
			float weight = toWeight.getFloat(entry);
			if (weight != 1f) return new SingletonWeightedDistribution<>(key, weight);
			else return new SingletonUnweightedDistribution<>(key);
		} else {
			if (map.entrySet().stream().anyMatch(entry -> toWeight.getFloat(entry) != 1f)) return new WeightedDistribution<>(map.entrySet().stream().collect(SimpleCollector.toFloatMap(toKey, toWeight)));
			else return new UnweightedDistribution<>(map.entrySet().stream().map(toKey).toList());
		}
	}

	public static <T> IDistribution<T> merge(Collection<IDistribution<T>> distributions) {
		return merge(distributions, new HashMap<>());
	}

	public static <T> IDistribution<T> sortedMerge(Collection<IDistribution<T>> distributions) {
		return merge(distributions, new TreeMap<>());
	}

	private static <T> IDistribution<T> merge(Collection<IDistribution<T>> distributions, Map<T, Float> values) {
		return merge(distributions.stream(), values);
	}

	public static <T> IDistribution<T> merge(Stream<IDistribution<T>> distributions) {
		return merge(distributions, new HashMap<>());
	}

	public static <T> IDistribution<T> sortedMerge(Stream<IDistribution<T>> distributions) {
		return merge(distributions, new TreeMap<>());
	}

	private static <T> IDistribution<T> merge(Stream<IDistribution<T>> distributions, Map<T, Float> values) {
		distributions.flatMap(distribution -> {
			if (distribution.hasWeights()) return distribution.getWeightedValues().entrySet().stream();
			else return distribution.getUnweightedValues().stream().map(val -> Pair.of(val, 1f));
		}).forEach(entry -> values.compute(entry.getKey(), (key, cur) -> entry.getValue() + (cur == null ? 0 : cur)));
		return get(values);
	}
}
