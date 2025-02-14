package com.firemerald.fecore.util;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;

import com.firemerald.fecore.util.function.ToFloatFunction;

public record SimpleCollector<V, I, R>(
		Supplier<I> supplier,
		BiConsumer<I, V> accumulator,
		BinaryOperator<I> combiner,
		Function<I, R> finisher,
		Set<Characteristics> characteristics
		) implements Collector<V, I, R> {

	@SuppressWarnings("unchecked")
	SimpleCollector(Supplier<I> supplier,
			BiConsumer<I, V> accumulator,
			BinaryOperator<I> combiner,
			Set<Characteristics> characteristics) {
		this(supplier, accumulator, combiner, v -> (R) v, characteristics);
	}

	public static final Set<Collector.Characteristics> IDENTITY = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));

	public static <E, K, V, M extends Map<K, V>> SimpleCollector<E, M, M> toMergedMap(
			Supplier<M> newMap,
			Function<E, K> getKey,
			Function<E, V> getValue,
			BiFunction<V, E, V> addValue,
			BinaryOperator<V> mergeValue
			) {
		return new SimpleCollector<>(
				newMap,
				(map, value) -> map.compute(getKey.apply(value), (key, v) -> v == null ? getValue.apply(value) : addValue.apply(v, value)),
				(map1, map2) -> {
					map2.forEach((key, value) -> map1.compute(key, (k, v) -> v == null ? value : mergeValue.apply(v, value)));
					return map1;
				},
				IDENTITY
				);
	}

	public static <E, K, V> SimpleCollector<E, HashMap<K, V>, HashMap<K, V>> toMergedMap(
			Function<E, K> getKey,
			Function<E, V> getValue,
			BiFunction<V, E, V> addValue,
			BinaryOperator<V> mergeValue
			) {
		return toMergedMap(
				HashMap::new,
				getKey,
				getValue,
				addValue,
				mergeValue
				);
	}

	public static <E, K, V, M extends Map<K, V>> SimpleCollector<E, M, M> toMergedMap(
			Supplier<M> newMap,
			Function<E, K> getKey,
			Function<E, V> getValue,
			BinaryOperator<V> mergeValue
			) {
		return toMergedMap(
				newMap,
				getKey,
				getValue,
				(v, e) -> mergeValue.apply(v, getValue.apply(e)),
				mergeValue);
	}

	public static <E, K, V> SimpleCollector<E, HashMap<K, V>, HashMap<K, V>> toMergedMap(
			Function<E, K> getKey,
			Function<E, V> getValue,
			BinaryOperator<V> mergeValue
			) {
		return toMergedMap(
				HashMap::new,
				getKey,
				getValue,
				mergeValue
				);
	}

	public static <E, K, M extends Map<K, Double>> SimpleCollector<E, M, M> toDoubleMap(
			Supplier<M> newMap,
			Function<E, K> getKey,
			ToDoubleFunction<E> getValue
			) {
		return toMergedMap(
				newMap,
				getKey,
				v -> getValue.applyAsDouble(v),
				(v1, v2) -> v1 + v2
				);
	}

	public static <E, K> SimpleCollector<E, HashMap<K, Double>, HashMap<K, Double>> toDoubleMap(
			Function<E, K> getKey,
			ToDoubleFunction<E> getValue
			) {
		return toDoubleMap(
				HashMap::new,
				getKey,
				getValue
				);
	}

	public static <E, K, M extends Map<K, Float>> SimpleCollector<E, M, M> toFloatMap(
			Supplier<M> newMap,
			Function<E, K> getKey,
			ToFloatFunction<E> getValue
			) {
		return toMergedMap(
				newMap,
				getKey,
				getValue,
				(v1, v2) -> v1 + v2
				);
	}

	public static <E, K> SimpleCollector<E, HashMap<K, Float>, HashMap<K, Float>> toFloatMap(
			Function<E, K> getKey,
			ToFloatFunction<E> getValue
			) {
		return toFloatMap(
				HashMap::new,
				getKey,
				getValue
				);
	}

	public static <E, K, M extends Map<K, Integer>> SimpleCollector<E, M, M> toIntMap(
			Supplier<M> newMap,
			Function<E, K> getKey,
			ToIntFunction<E> getValue
			) {
		return toMergedMap(
				newMap,
				getKey,
				v -> getValue.applyAsInt(v),
				(v1, v2) -> v1 + v2
				);
	}

	public static <E, K> SimpleCollector<E, HashMap<K, Integer>, HashMap<K, Integer>> toIntMap(
			Function<E, K> getKey,
			ToIntFunction<E> getValue
			) {
		return toIntMap(
				HashMap::new,
				getKey,
				getValue
				);
	}

	public static <E, K, M extends Map<K, Long>> SimpleCollector<E, M, M> toLongMap(
			Supplier<M> newMap,
			Function<E, K> getKey,
			ToLongFunction<E> getValue
			) {
		return toMergedMap(
				newMap,
				getKey,
				v -> getValue.applyAsLong(v),
				(v1, v2) -> v1 + v2
				);
	}

	public static <E, K> SimpleCollector<E, HashMap<K, Long>, HashMap<K, Long>> toLongMap(
			Function<E, K> getKey,
			ToLongFunction<E> getValue
			) {
		return toLongMap(
				HashMap::new,
				getKey,
				getValue
				);
	}
}
