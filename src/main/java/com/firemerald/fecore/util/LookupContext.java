package com.firemerald.fecore.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition.IContext;

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
	public <T> Map<ResourceLocation, Collection<Holder<T>>> getAllTags(ResourceKey<? extends Registry<T>> registry) {
		return provider.lookup(registry).map(lookup -> lookup.listTags().collect(Collectors.toMap(tag -> tag.key().location(), tag -> (Collection<Holder<T>>) tag.stream().collect(Collectors.toList())))).orElse(Collections.emptyMap());
	}
}
