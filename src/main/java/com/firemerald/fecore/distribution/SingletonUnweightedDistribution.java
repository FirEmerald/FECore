package com.firemerald.fecore.distribution;

import java.util.Objects;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class SingletonUnweightedDistribution<T> implements ISingletonDistribution<T> {
	public static <T> Codec<SingletonUnweightedDistribution<T>> getCodec(Codec<T> keyCodec) {
		return keyCodec.xmap(SingletonUnweightedDistribution::new, SingletonUnweightedDistribution::getFirstValue);
	}

	public static <T> StreamCodec<ByteBuf, SingletonUnweightedDistribution<T>> getStreamCodec(StreamCodec<ByteBuf, T> keyCodec) {
		return keyCodec.map(SingletonUnweightedDistribution::new, SingletonUnweightedDistribution::getFirstValue);
	}

	@Nonnull
	private final T object;

	public SingletonUnweightedDistribution(@Nonnull T object) {
		this.object = object;
	}

	@Override
	public boolean hasWeights() {
		return false;
	}

	@Override
	public T getFirstValue() {
		return object;
	}

	@Override
	public float getFirstWeight() {
		return 1;
	}

	@Override
	public <S> SingletonUnweightedDistribution<S> map(Function<T, S> map) {
		return new SingletonUnweightedDistribution<>(map.apply(object));
	}

	@Override
	public int hashCode() {
		return object.hashCode();
	}

	@Override
	public String toString() {
		return "SingletonUnweighted<" + object.toString() + ">";
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		else if (o == this) return true;
		else if (o.getClass() != this.getClass()) return false;
		else return Objects.equals(((SingletonUnweightedDistribution<?>) o).object, object);
	}
}