package com.firemerald.fecore.distribution;

import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.firemerald.fecore.codec.SingleEntryCodec;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class SingletonWeightedDistribution<T> implements ISingletonDistribution<T> {
	public static <T> Codec<SingletonWeightedDistribution<T>> getCodec(Codec<T> keyCodec) {
		return new SingleEntryCodec<>(keyCodec, Codec.FLOAT).xmap(SingletonWeightedDistribution::new, SingletonWeightedDistribution::asPair);
	}

	public static <T> StreamCodec<ByteBuf, SingletonWeightedDistribution<T>> getStreamCodec(StreamCodec<ByteBuf, T> keyCodec) {
		return StreamCodec.composite(
				keyCodec, SingletonWeightedDistribution::getFirstValue,
				ByteBufCodecs.FLOAT, SingletonWeightedDistribution::getFirstWeight,
				SingletonWeightedDistribution::new
				);
	}

	@Nonnull
	private final T object;
	private final float weight;

	public SingletonWeightedDistribution(@Nonnull T object, float weight) {
		this.object = object;
		this.weight = weight;
	}

	public SingletonWeightedDistribution(@Nonnull Pair<T, Float> pair) {
		this(pair.getFirst(), pair.getSecond());
	}

	public SingletonWeightedDistribution(@Nonnull Entry<T, Float> entry) {
		this(entry.getKey(), entry.getValue());
	}

	@Override
	public boolean hasWeights() {
		return true;
	}

	@Override
	public T getFirstValue() {
		return object;
	}

	@Override
	public float getFirstWeight() {
		return weight;
	}

	public Pair<T, Float> asPair() {
		return Pair.of(object, weight);
	}

	@Override
	public <S> SingletonWeightedDistribution<S> map(Function<T, S> map) {
		return new SingletonWeightedDistribution<>(map.apply(object), weight);
	}

	@Override
	public int hashCode() {
		return object.hashCode() ^ Float.floatToRawIntBits(weight);
	}

	@Override
	public String toString() {
		return "SingletonWeighted<" + object.toString() + ":" + weight + ">";
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		else if (o == this) return true;
		else if (o.getClass() != this.getClass()) return false;
		else {
			SingletonWeightedDistribution<?> other = (SingletonWeightedDistribution<?>) o;
			return other.weight == weight && Objects.equals(other.object, object);
		}
	}
}