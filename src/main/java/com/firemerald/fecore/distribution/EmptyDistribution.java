package com.firemerald.fecore.distribution;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.serialization.Codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;

public class EmptyDistribution<T> implements IDistribution<T> {
	public static final EmptyDistribution<?> INSTANCE = new EmptyDistribution<>();
	public static final Codec<EmptyDistribution<?>> CODEC = Codec.unit(INSTANCE);
	public static final StreamCodec<ByteBuf, EmptyDistribution<?>> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	@SuppressWarnings("unchecked")
	public static <T> EmptyDistribution<T> get() {
		return (EmptyDistribution<T>) INSTANCE;
	}

	@SuppressWarnings("unchecked")
	public static <T> Codec<EmptyDistribution<T>> getCodec() {
		return (Codec<EmptyDistribution<T>>) (Object) CODEC;
	}

	private EmptyDistribution() {}

	@Override
	public float weightedSize() {
		return 0;
	}

	@Override
	public int collectionSize() {
		return 0;
	}

	@Override
	public T get(float value) {
		return null;
	}

	@Override
	public Collection<T> getValues() {
		return Collections.emptyList();
	}

	@Override
	public boolean hasWeights() {
		return false;
	}

	@Override
	public List<T> getUnweightedValues() {
		return Collections.emptyList();
	}

	@Override
	public Map<T, Float> getWeightedValues() {
		return Collections.emptyMap();
	}

	@Override
	public T getFirstValue() {
		return null;
	}

	@Override
	public float getFirstWeight() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	public T getRandom(@Nonnull Random rand) {
		return null;
	}

	@Override
	public T getRandom() {
		return null;
	}

	@Override
	public boolean contains(@Nonnull T value) {
		return false;
	}

	@Override
	public <S> EmptyDistribution<S> map(Function<T, S> map) {
		return get();
	}

	@Override
	public T getOffset(T current, int offset) {
		return null;
	}

	@Override
	public T pickOne(RandomSource randomSource, @Nullable T currentValue, int offsetIfFound) {
		return null;
	}

	@Override
	public Stream<T> stream() {
		return Stream.empty();
	}

	@Override
	public Stream<T> parallelStream() {
		return Stream.empty();
	}

	@Override
	public void forEach(Consumer<T> action) {}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public String toString() {
		return "EmptyDistribution";
	}

	@Override
	public boolean equals(Object o) {
		return o == this;
	}

	@Override
	public boolean anyMatch(Predicate<T> predicate) {
		return false;
	}

	@Override
	public boolean allMatch(Predicate<T> predicate) {
		return true;
	}

	@Override
	public boolean noneMatch(Predicate<T> predicate) {
		return true;
	}
}