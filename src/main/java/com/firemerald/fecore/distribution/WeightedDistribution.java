package com.firemerald.fecore.distribution;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.firemerald.fecore.util.SimpleCollector;
import com.firemerald.fecore.util.psuedomap.FloatIndexedCollection;
import com.mojang.serialization.Codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class WeightedDistribution<T> implements IWeightedDistribution<T> {
	public static <T> Codec<WeightedDistribution<T>> getCodec(Codec<T> keyCodec) {
		return Codec.unboundedMap(keyCodec, Codec.FLOAT).xmap(WeightedDistribution::new, WeightedDistribution::getWeightedValues);
	}

	public static <T> StreamCodec<ByteBuf, WeightedDistribution<T>> getStreamCodecFromList(StreamCodec<ByteBuf, T> keyCodec) {
		return ByteBufCodecs.map(size -> (Map<T, Float>) new HashMap<T, Float>(size), keyCodec, ByteBufCodecs.FLOAT).map(WeightedDistribution::new, WeightedDistribution::getWeightedValues);
	}

	private final Map<T, Float> weights;
	private final FloatIndexedCollection<T> objects;
	private final float size;

	public WeightedDistribution(@Nonnull Map<T, Float> objects) {
		weights = Collections.unmodifiableMap(objects);
		this.objects = FloatIndexedCollection.fromWeights(weights);
		this.size = this.objects.highestKey();
	}

	@Override
	public T get(float value) {
		return value < 0 || value >= size ? null : objects.getCeil(value);
	}

	@Override
	public float weightedSize() {
		return size;
	}

	@Override
	public Map<T, Float> getWeightedValues() {
		return weights;
	}

	@Override
	public T getFirstValue() {
		return objects.getLowest();
	}

	@Override
	public float getFirstWeight() {
		return objects.lowestKey();
	}

	@Override
	public <S> WeightedDistribution<S> map(Function<T, S> map) {
		return new WeightedDistribution<>(weights.entrySet().stream().collect(SimpleCollector.toFloatMap(entry -> map.apply(entry.getKey()), Map.Entry::getValue)));
	}

	@Override
	public Stream<T> stream() {
		return weights.keySet().stream();
	}

	@Override
	public Stream<T> parallelStream() {
		return weights.keySet().parallelStream();
	}

	@Override
	public void forEach(Consumer<T> action) {
		weights.keySet().forEach(action);
	}

	@Override
	public int hashCode() {
		return objects.hashCode();
	}

	@Override
	public String toString() {
		return "Weighted<" + objects.toString() + ">";
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		else if (o == this) return true;
		else if (o.getClass() != this.getClass()) return false;
		else {
			WeightedDistribution<?> other = (WeightedDistribution<?>) o;
			return Objects.equals(other.objects, objects);
		}
	}
}