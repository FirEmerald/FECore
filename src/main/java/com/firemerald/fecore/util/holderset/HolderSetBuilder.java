package com.firemerald.fecore.util.holderset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public abstract class HolderSetBuilder<T> {
	protected final RegistryLookup<T> lookup;
	private final List<HolderSet<T>> holderSets = new ArrayList<>();
	private final List<Holder<T>> holders = new ArrayList<>();
	private final List<TagKey<T>> tags = new ArrayList<>();

    public HolderSetBuilder(RegistryLookup<T> lookup) {
    	this.lookup = lookup;
    }

    public HolderSetBuilder(HolderLookup.Provider provider, ResourceKey<Registry<T>> registryKey) {
    	this(provider.lookupOrThrow(registryKey));
    }

	public abstract HolderSet<T> combinedSets(List<HolderSet<T>> holderSets);

	public abstract HolderSet<T> combinedHolders(List<Holder<T>> holders);

	public HolderSet<T> build() {
		List<HolderSet<T>> sets = new ArrayList<>(this.holderSets);
		if (!holders.isEmpty()) sets.add(combinedHolders(holders));
		tags.stream().map(lookup::getOrThrow).forEach(sets::add);
		if (sets.isEmpty()) return HolderSet.empty();
		else if (sets.size() == 1) return sets.get(0);
		else return combinedSets(sets);
	}

	public HolderSetBuilder<T> addSet(HolderSet<T> holderSet) {
		holderSets.add(holderSet);
		return this;
	}

	public HolderSetBuilder<T> addSets(@SuppressWarnings("unchecked") HolderSet<T>... holderSets) {
		for (HolderSet<T> holderSet : holderSets) this.holderSets.add(holderSet);
		return this;
	}

	public HolderSetBuilder<T> addSets(Collection<HolderSet<T>> holderSets) {
		this.holderSets.addAll(holderSets);
		return this;
	}

	public HolderSetBuilder<T> addHolder(Holder<T> holder) {
		holders.add(holder);
		return this;
	}

	public HolderSetBuilder<T> addHolders(@SuppressWarnings("unchecked") Holder<T>... holders) {
		for (Holder<T> holder : holders) this.holders.add(holder);
		return this;
	}

	public HolderSetBuilder<T> addHolders(Collection<Holder<T>> holders) {
		this.holders.addAll(holders);
		return this;
	}
}
