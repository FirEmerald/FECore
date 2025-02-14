package com.firemerald.fecore.util;

import java.util.Optional;

import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.conditions.ICondition.IContext;

public record LookupContext(Provider provider, RegistryAccess registryAccess) implements IContext {
	public static final RegistryAccess REGISTRY_ACCESS = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
	public static final LookupContext SIMPLE = new LookupContext(REGISTRY_ACCESS);

	public LookupContext(Provider provider) {
		this(provider, provider instanceof RegistryAccess access ? access : REGISTRY_ACCESS);
	}

	public LookupContext(RegistryAccess registryAccess) {
		this(registryAccess, registryAccess);
	}

	@Override
    public <T> boolean isTagLoaded(TagKey<T> key) {
		@SuppressWarnings("unchecked")
		Optional<Reference<Registry<T>>> registry = provider.get((ResourceKey<Registry<T>>) key.registry());
		return registry.isPresent() && registry.get().value().get(key).isPresent();
	}
}
