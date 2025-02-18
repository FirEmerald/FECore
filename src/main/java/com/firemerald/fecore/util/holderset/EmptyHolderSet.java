package com.firemerald.fecore.util.holderset;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.mojang.datafixers.util.Either;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;

public class EmptyHolderSet<T> implements HolderSet<T> {
	public static final EmptyHolderSet<Object> INSTANCE = new EmptyHolderSet<>();

	@SuppressWarnings("unchecked")
	public static <T> EmptyHolderSet<T> get() {
		return (EmptyHolderSet<T>) INSTANCE;
	}

	@Override
	public Iterator<Holder<T>> iterator() {
		return Collections.emptyIterator();
	}

	@Override
	public Stream<Holder<T>> stream() {
		return Stream.empty();
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public Either<TagKey<T>, List<Holder<T>>> unwrap() {
		return Either.right(Collections.emptyList());
	}

	@Override
	public Optional<Holder<T>> getRandomElement(RandomSource pRandom) {
		return Optional.empty();
	}

	@Override
	public Holder<T> get(int pIndex) {
		return null;
	}

	@Override
	public boolean contains(Holder<T> pHolder) {
		return false;
	}

	@Override
	public boolean canSerializeIn(HolderOwner<T> pOwner) {
		return true;
	}

	@Override
	public Optional<TagKey<T>> unwrapKey() {
		return Optional.empty();
	}

}
