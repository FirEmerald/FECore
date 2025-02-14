package com.firemerald.fecore.util.psuedomap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Object2DoubleFunction;

public abstract class DoubleIndexedCollection<T> {
	public static <T, U> DoubleIndexedCollection<T> fromWeights(Collection<U> weightedValues, Function<U, T> getValue, ToDoubleFunction<U> getWeight) {
		int[] indices = new int[weightedValues.size()];
		List<T> values = new ArrayList<>(indices.length);
		int index = 0;
		int arrayIndex = 0;
		for (U entry : weightedValues) {
			index += getWeight.applyAsDouble(entry);
			indices[arrayIndex] = index;
			values.add(getValue.apply(entry));
			arrayIndex++;
		}
		return build(i -> indices[i], (listIndex, i) -> values.get(listIndex), arrayIndex);
	}

	public static <T> DoubleIndexedCollection<T> fromWeights(Map<T, Double> weightedValues) {
		return fromWeights(weightedValues.entrySet(), Map.Entry::getKey, Map.Entry::getValue);
	}

	public static <T, U> DoubleIndexedCollection<T> fromValues(SortedMap<Double, T> values) {
		Integer[] sortedKeys = values.keySet().toArray(Integer[]::new);
		return build(i -> sortedKeys[i], (listIndex, i) -> values.get(i), sortedKeys.length);
	}

	public static <T, U> DoubleIndexedCollection<T> fromValues(Map<Double, T> values) {
		return fromValues(values instanceof SortedMap<Double, T> sorted ? sorted : new TreeMap<>(values));
	}

	public static <T, U> DoubleIndexedCollection<T> fromValues(Collection<U> weightedValues, Function<U, T> getValue, Object2DoubleFunction<U> getWeight) {
		return fromValues(weightedValues.stream().collect(Collectors.toMap(getWeight, getValue, (a, b) -> a, TreeMap::new)));
	}

	public static <T, U, V> DoubleIndexedCollection<T> fromValues(Map<U, V> map, Function<Map.Entry<U, V>, T> getValue, Object2DoubleFunction<Map.Entry<U, V>> getWeight) {
		return fromValues(map.entrySet().stream().collect(Collectors.toMap(getWeight, getValue, (a, b) -> a, TreeMap::new)));
	}

	public static <T, U> DoubleIndexedCollection<T> build(Int2DoubleFunction getIndex, ValueGetter<T> getValue, int numElements) {
		return build(getIndex, getValue, 0, numElements);
	}

	public static <T> DoubleIndexedCollection<T> build(Int2DoubleFunction getIndex, ValueGetter<T> getValue, int startAt, int endBefore) {
		return build(getIndex, getValue, startAt, endBefore, true);
	}

	public static <T> DoubleIndexedCollection<T> build(Int2DoubleFunction getIndex, ValueGetter<T> getValue, int startAt, int endBefore, boolean lower) {
		if (startAt >= endBefore) return Empty.instance();
		int listIndex = startAt + (endBefore - startAt + (lower ? 0 : -1)) / 2;
		int higherStart = listIndex + 1;
		double index = getIndex.get(listIndex);
		T value = getValue.get(listIndex, index);
		double lesserHighest = 0, greaterLowest = 0;
		DoubleIndexedCollection<T> lesser, greater;
		if (startAt >= listIndex) lesser = null;
		else {
			lesserHighest = getIndex.get(listIndex - 1);
			lesser = build(getIndex, getValue, startAt, listIndex, true);
		}
		if (higherStart >= endBefore) greater = null;
		else {
			greaterLowest = getIndex.get(higherStart);
			greater = build(getIndex, getValue, higherStart, endBefore, false);
		}
		if (lesser == null) {
			if (greater == null) return new Leaf<>(index, value);
			else return new LowestNode<>(index, value, greaterLowest, greater);
		} else {
			if (greater == null) return new HighestNode<>(index, value, lesserHighest, lesser);
			else return new MiddleNode<>(index, value, lesserHighest, lesser, greaterLowest, greater);
		}
	}

	final double index;
	final T value;

	DoubleIndexedCollection(double index, T value) {
		this.index = index;
		this.value = value;
	}

	public abstract T getCeil(double index);

	public abstract T getFloor(double index);

	public abstract T get(double index);

	public abstract double lowestKey();

	public abstract T getLowest();

	public abstract double highestKey();

	public abstract T getHighest();

	public abstract void forEach(EntryOperator<T> operation);

	public abstract void forEach(Consumer<T> operation);

	public abstract void debug(String indent, String currentIndent, Consumer<String> addEntry);

	public final void debug(String indent, Consumer<String> addEntry) {
		debug(indent, "", addEntry);
	}

	public final void debug(Consumer<String> addEntry) {
		debug("    ", addEntry);
	}

	public final String debug(String indent) {
		StringJoiner joiner = new StringJoiner("\n");
		debug(indent, joiner::add);
		return joiner.toString();
	}

	public final String debug() {
		StringJoiner joiner = new StringJoiner("\n");
		debug(joiner::add);
		return joiner.toString();
	}

	public static interface ValueGetter<T> {
		public T get(int listIndex, double index);
	}

	public static interface EntryOperator<T> {
		public void accept(double index, T value);
	}

	public static class Empty<T> extends DoubleIndexedCollection<T> {
		public static final Empty<?> INSTANCE = new Empty<>();

		@SuppressWarnings("unchecked")
		public static <T> Empty<T> instance() {
			return (Empty<T>) INSTANCE;
		}

		private Empty() {
			super(0, null);
		}

		@Override
		public T getCeil(double index) {
			return null;
		}

		@Override
		public T getFloor(double index) {
			return null;
		}

		@Override
		public T get(double index) {
			return null;
		}

		@Override
		public double lowestKey() {
			return 0;
		}

		@Override
		public T getLowest() {
			return null;
		}

		@Override
		public double highestKey() {
			return 0;
		}

		@Override
		public T getHighest() {
			return null;
		}

		@Override
		public void forEach(EntryOperator<T> operation) {}

		@Override
		public void forEach(Consumer<T> operation) {}

		@Override
		public void debug(String indent, String currentIndent, Consumer<String> addEntry) {
			addEntry.accept(currentIndent + "EMPTY");
		}
	}

	public static class Leaf<T> extends DoubleIndexedCollection<T> {
		private Leaf(double index, T value) {
			super(index, value);
		}

		@Override
		public T getCeil(double index) {
			return index <= this.index ? value : null;
		}

		@Override
		public T getFloor(double index) {
			return index >= this.index ? value : null;
		}

		@Override
		public T get(double index) {
			return index == this.index ? value : null;
		}

		@Override
		public double lowestKey() {
			return index;
		}

		@Override
		public T getLowest() {
			return value;
		}

		@Override
		public double highestKey() {
			return index;
		}

		@Override
		public T getHighest() {
			return value;
		}

		@Override
		public void forEach(EntryOperator<T> operation) {
			operation.accept(index, value);
		}

		@Override
		public void forEach(Consumer<T> operation) {
			operation.accept(value);
		}

		@Override
		public void debug(String indent, String currentIndent, Consumer<String> addEntry) {
			addEntry.accept(currentIndent + index + "=" + value.toString());
		}
	}

	public static class LowestNode<T> extends DoubleIndexedCollection<T> {
		final double higherLowestIndex;
		final DoubleIndexedCollection<T> higher;

		LowestNode(double index, T value, double higherLowestIndex, DoubleIndexedCollection<T> higher) {
			super(index, value);
			this.higherLowestIndex = higherLowestIndex;
			this.higher = higher;
		}

		@Override
		public T getCeil(double indouble) {
			return index <= this.index ? value : higher.get(index);
		}

		@Override
		public T getFloor(double index) {
			if (index < this.index) return null;
			else if (index < higherLowestIndex) return value;
			else return higher.getFloor(index);
		}

		@Override
		public T get(double index) {
			if (index == this.index) return value;
			else if (index < this.higherLowestIndex) return null;
			else return higher.get(index);
		}

		@Override
		public double lowestKey() {
			return index;
		}

		@Override
		public T getLowest() {
			return value;
		}

		@Override
		public double highestKey() {
			return higher.highestKey();
		}

		@Override
		public T getHighest() {
			return higher.getHighest();
		}

		@Override
		public void forEach(EntryOperator<T> operation) {
			operation.accept(index, value);
			higher.forEach(operation);
		}

		@Override
		public void forEach(Consumer<T> operation) {
			operation.accept(value);
			higher.forEach(operation);
		}

		@Override
		public void debug(String indent, String currentIndent, Consumer<String> addEntry) {
			addEntry.accept(currentIndent + index + "=" + value.toString());
			higher.debug(indent, currentIndent + indent, addEntry);
		}
	}

	public static class HighestNode<T> extends DoubleIndexedCollection<T> {
		final double lowerHighestIndex;
		final DoubleIndexedCollection<T> lower;

		HighestNode(double index, T value, double lowerHighestIndex, DoubleIndexedCollection<T> lower) {
			super(index, value);
			this.lowerHighestIndex = lowerHighestIndex;
			this.lower = lower;
		}

		@Override
		public T getCeil(double index) {
			if (index > this.index) return null;
			else if (index > lowerHighestIndex) return value;
			else return lower.getCeil(index);
		}

		@Override
		public T getFloor(double index) {
			return index >= this.index ? value : lower.get(index);
		}

		@Override
		public T get(double index) {
			if (index == this.index) return value;
			else if (index > lowerHighestIndex) return null;
			else return lower.get(index);
		}

		@Override
		public double lowestKey() {
			return lower.lowestKey();
		}

		@Override
		public T getLowest() {
			return lower.getLowest();
		}

		@Override
		public double highestKey() {
			return index;
		}

		@Override
		public T getHighest() {
			return value;
		}

		@Override
		public void forEach(EntryOperator<T> operation) {
			lower.forEach(operation);
			operation.accept(index, value);
		}

		@Override
		public void forEach(Consumer<T> operation) {
			lower.forEach(operation);
			operation.accept(value);
		}

		@Override
		public void debug(String indent, String currentIndent, Consumer<String> addEntry) {
			lower.debug(indent, currentIndent + indent, addEntry);
			addEntry.accept(currentIndent + index + "=" + value.toString());
		}
	}

	public static class MiddleNode<T> extends DoubleIndexedCollection<T> {
		final double lowerHighestIndex;
		final DoubleIndexedCollection<T> lower;
		final double higherLowestIndex;
		final DoubleIndexedCollection<T> higher;

		MiddleNode(double index, T value, double lowerHighestIndex, DoubleIndexedCollection<T> lower, double higherLowestIndex, DoubleIndexedCollection<T> higher) {
			super(index, value);
			this.lowerHighestIndex = lowerHighestIndex;
			this.lower = lower;
			this.higherLowestIndex = higherLowestIndex;
			this.higher = higher;
		}

		@Override
		public T getCeil(double index) {
			if (index <= lowerHighestIndex) return lower.getCeil(index);
			else if (index <= this.index) return value;
			else return higher.getCeil(index);
		}

		@Override
		public T getFloor(double index) {
			if (index >= higherLowestIndex) return higher.getCeil(index);
			else if (index >= this.index) return value;
			else return lower.getCeil(index);
		}

		@Override
		public T get(double index) {
			if (index == this.index) return value;
			else if (index <= lowerHighestIndex) return lower.get(index);
			else if (index >= higherLowestIndex) return higher.get(index);
			else return null;
		}

		@Override
		public double lowestKey() {
			return lower.lowestKey();
		}

		@Override
		public T getLowest() {
			return lower.getLowest();
		}

		@Override
		public double highestKey() {
			return higher.highestKey();
		}

		@Override
		public T getHighest() {
			return higher.getHighest();
		}

		@Override
		public void forEach(EntryOperator<T> operation) {
			lower.forEach(operation);
			operation.accept(index, value);
			higher.forEach(operation);
		}

		@Override
		public void forEach(Consumer<T> operation) {
			lower.forEach(operation);
			operation.accept(value);
			higher.forEach(operation);
		}

		@Override
		public void debug(String indent, String currentIndent, Consumer<String> addEntry) {
			lower.debug(indent, currentIndent + indent, addEntry);
			addEntry.accept(currentIndent + index + "=" + value.toString());
			higher.debug(indent, currentIndent + indent, addEntry);
		}
	}
}
