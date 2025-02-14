package com.firemerald.fecore.distribution;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.firemerald.fecore.util.CollectionUtils;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class UnweightedDistribution<T> implements IUnweightedDistribution<T> {
	public static <T> Codec<UnweightedDistribution<T>> getCodec(Codec<T> keyCodec) {
		return getCodecFromList(keyCodec.listOf());
	}

	public static <T> Codec<UnweightedDistribution<T>> getCodecFromList(Codec<List<T>> keyCodec) {
		return keyCodec.xmap(UnweightedDistribution::new, UnweightedDistribution::getUnweightedValues);
	}

	public static <T> StreamCodec<ByteBuf, UnweightedDistribution<T>> getStreamCodec(StreamCodec<ByteBuf, T> keyCodec) {
		return getStreamCodecFromList(keyCodec.apply(ByteBufCodecs.list()));
	}

	public static <T> StreamCodec<ByteBuf, UnweightedDistribution<T>> getStreamCodecFromList(StreamCodec<ByteBuf, List<T>> keyCodec) {
		return keyCodec.map(UnweightedDistribution::new, UnweightedDistribution::getUnweightedValues);
	}

	private final List<T> objects;

	public UnweightedDistribution(@Nonnull Collection<T> objects) {
		this.objects = ImmutableList.copyOf(objects);
	}

	@SafeVarargs
	public UnweightedDistribution(T... objects) {
		this.objects = ImmutableList.copyOf(objects);
	}

	@Override
	public List<T> getUnweightedValues() {
		return objects;
	}

	@Override
	public T getFirstValue() {
		return objects.get(0);
	}

	@Override
	public <S> UnweightedDistribution<S> map(Function<T, S> map) {
		return new UnweightedDistribution<>(objects.stream().map(map).toList());
	}

	@Override
	public Stream<T> stream() {
		return objects.stream();
	}

	@Override
	public Stream<T> parallelStream() {
		return objects.parallelStream();
	}

	@Override
	public void forEach(Consumer<T> action) {
		objects.forEach(action);
	}

	@Override
	public int hashCode() {
		return objects.hashCode();
	}

	@Override
	public String toString() {
		return "Unweighted<" + objects.toString() + ">";
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		else if (o == this) return true;
		else if (o.getClass() != this.getClass()) return false;
		else {
			UnweightedDistribution<?> other = (UnweightedDistribution<?>) o;
			return CollectionUtils.equalUnordered(other.objects, objects);
		}
	}
}