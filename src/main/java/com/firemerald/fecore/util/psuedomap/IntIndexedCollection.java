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
import java.util.stream.Collectors;

import com.firemerald.fecore.util.function.ToIntFunction;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

public abstract class IntIndexedCollection<T> {
	public static <T, U> IntIndexedCollection<T> fromWeights(Collection<U> weightedValues, Function<U, T> getValue, ToIntFunction<U> getWeight) {
		int[] indices = new int[weightedValues.size()];
		List<T> values = new ArrayList<>(indices.length);
		int index = 0;
		int arrayIndex = 0;
		for (U entry : weightedValues) {
			index += getWeight.getInt(entry);
			indices[arrayIndex] = index;
			values.add(getValue.apply(entry));
			arrayIndex++;
		}
		return build(i -> indices[i], (listIndex, i) -> values.get(listIndex), arrayIndex);
	}

	public static <T> IntIndexedCollection<T> fromWeights(Map<T, Integer> weightedValues) {
		return fromWeights(weightedValues.entrySet(), Map.Entry::getKey, Map.Entry::getValue);
	}

	public static <T, U> IntIndexedCollection<T> fromValues(SortedMap<Integer, T> values) {
		Integer[] sortedKeys = values.keySet().toArray(Integer[]::new);
		return build(i -> sortedKeys[i], (listIndex, i) -> values.get(i), sortedKeys.length);
	}

	public static <T, U> IntIndexedCollection<T> fromValues(Map<Integer, T> values) {
		return fromValues(values instanceof SortedMap<Integer, T> sorted ? sorted : new TreeMap<>(values));
	}

	public static <T, U> IntIndexedCollection<T> fromValues(Collection<U> weightedValues, Function<U, T> getValue, ToIntFunction<U> getWeight) {
		return fromValues(weightedValues.stream().collect(Collectors.toMap(getWeight, getValue, (a, b) -> a, TreeMap::new)));
	}

	public static <T, U, V> IntIndexedCollection<T> fromValues(Map<U, V> map, Function<Map.Entry<U, V>, T> getValue, ToIntFunction<Map.Entry<U, V>> getWeight) {
		return fromValues(map.entrySet().stream().collect(Collectors.toMap(getWeight, getValue, (a, b) -> a, TreeMap::new)));
	}

	public static <T, U> IntIndexedCollection<T> build(Int2IntFunction getIndex, ValueGetter<T> getValue, int numElements) {
		return build(getIndex, getValue, 0, numElements);
	}

	public static <T> IntIndexedCollection<T> build(Int2IntFunction getIndex, ValueGetter<T> getValue, int startAt, int endBefore) {
		return build(getIndex, getValue, startAt, endBefore, true);
	}

	public static <T> IntIndexedCollection<T> build(Int2IntFunction getIndex, ValueGetter<T> getValue, int startAt, int endBefore, boolean lower) {
		if (startAt >= endBefore) return Empty.instance();
		int listIndex = startAt + (endBefore - startAt + (lower ? 0 : -1)) / 2;
		int higherStart = listIndex + 1;
		int index = getIndex.get(listIndex);
		T value = getValue.get(listIndex, index);
		int lesserHighest = 0, greaterLowest = 0;
		IntIndexedCollection<T> lesser, greater;
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

	final int index;
	final T value;

	IntIndexedCollection(int index, T value) {
		this.index = index;
		this.value = value;
	}

	public abstract T getCeil(int index);

	public abstract T getFloor(int index);

	public abstract T get(int index);

	public abstract int lowestKey();

	public abstract T getLowest();

	public abstract int highestKey();

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
		public T get(int listIndex, int index);
	}

	public static interface EntryOperator<T> {
		public void accept(int index, T value);
	}

	public static class Empty<T> extends IntIndexedCollection<T> {
		public static final Empty<?> INSTANCE = new Empty<>();

		@SuppressWarnings("unchecked")
		public static <T> Empty<T> instance() {
			return (Empty<T>) INSTANCE;
		}

		private Empty() {
			super(0, null);
		}

		@Override
		public T getCeil(int index) {
			return null;
		}

		@Override
		public T getFloor(int index) {
			return null;
		}

		@Override
		public T get(int index) {
			return null;
		}

		@Override
		public int lowestKey() {
			return 0;
		}

		@Override
		public T getLowest() {
			return null;
		}

		@Override
		public int highestKey() {
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

	public static class Leaf<T> extends IntIndexedCollection<T> {
		private Leaf(int index, T value) {
			super(index, value);
		}

		@Override
		public T getCeil(int index) {
			return index <= this.index ? value : null;
		}

		@Override
		public T getFloor(int index) {
			return index >= this.index ? value : null;
		}

		@Override
		public T get(int index) {
			return index == this.index ? value : null;
		}

		@Override
		public int lowestKey() {
			return index;
		}

		@Override
		public T getLowest() {
			return value;
		}

		@Override
		public int highestKey() {
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

	public static class LowestNode<T> extends IntIndexedCollection<T> {
		final int higherLowestIndex;
		final IntIndexedCollection<T> higher;

		LowestNode(int index, T value, int higherLowestIndex, IntIndexedCollection<T> higher) {
			super(index, value);
			this.higherLowestIndex = higherLowestIndex;
			this.higher = higher;
		}

		@Override
		public T getCeil(int index) {
			return index <= this.index ? value : higher.get(index);
		}

		@Override
		public T getFloor(int index) {
			if (index < this.index) return null;
			else if (index < higherLowestIndex) return value;
			else return higher.getFloor(index);
		}

		@Override
		public T get(int index) {
			if (index == this.index) return value;
			else if (index < this.higherLowestIndex) return null;
			else return higher.get(index);
		}

		@Override
		public int lowestKey() {
			return index;
		}

		@Override
		public T getLowest() {
			return value;
		}

		@Override
		public int highestKey() {
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

	public static class HighestNode<T> extends IntIndexedCollection<T> {
		final int lowerHighestIndex;
		final IntIndexedCollection<T> lower;

		HighestNode(int index, T value, int lowerHighestIndex, IntIndexedCollection<T> lower) {
			super(index, value);
			this.lowerHighestIndex = lowerHighestIndex;
			this.lower = lower;
		}

		@Override
		public T getCeil(int index) {
			if (index > this.index) return null;
			else if (index > lowerHighestIndex) return value;
			else return lower.getCeil(index);
		}

		@Override
		public T getFloor(int index) {
			return index >= this.index ? value : lower.get(index);
		}

		@Override
		public T get(int index) {
			if (index == this.index) return value;
			else if (index > lowerHighestIndex) return null;
			else return lower.get(index);
		}

		@Override
		public int lowestKey() {
			return lower.lowestKey();
		}

		@Override
		public T getLowest() {
			return lower.getLowest();
		}

		@Override
		public int highestKey() {
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

	public static class MiddleNode<T> extends IntIndexedCollection<T> {
		final int lowerHighestIndex;
		final IntIndexedCollection<T> lower;
		final int higherLowestIndex;
		final IntIndexedCollection<T> higher;

		MiddleNode(int index, T value, int lowerHighestIndex, IntIndexedCollection<T> lower, int higherLowestIndex, IntIndexedCollection<T> higher) {
			super(index, value);
			this.lowerHighestIndex = lowerHighestIndex;
			this.lower = lower;
			this.higherLowestIndex = higherLowestIndex;
			this.higher = higher;
		}

		@Override
		public T getCeil(int index) {
			if (index <= lowerHighestIndex) return lower.getCeil(index);
			else if (index <= this.index) return value;
			else return higher.getCeil(index);
		}

		@Override
		public T getFloor(int index) {
			if (index >= higherLowestIndex) return higher.getCeil(index);
			else if (index >= this.index) return value;
			else return lower.getCeil(index);
		}

		@Override
		public T get(int index) {
			if (index == this.index) return value;
			else if (index <= lowerHighestIndex) return lower.get(index);
			else if (index >= higherLowestIndex) return higher.get(index);
			else return null;
		}

		@Override
		public int lowestKey() {
			return lower.lowestKey();
		}

		@Override
		public T getLowest() {
			return lower.getLowest();
		}

		@Override
		public int highestKey() {
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
