package com.firemerald.fecore.init.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

public class DeferredDeferredRegister<T extends IForgeRegistryEntry<T>>
{
	private DeferredRegister<T> registry;
	private Supplier<DeferredRegister<T>> registrySupplier;

	public DeferredDeferredRegister(IForgeRegistry<T> type, String modId)
	{
		this(() -> DeferredRegister.create(type, modId));
	}

	public DeferredDeferredRegister(Supplier<DeferredRegister<T>> registrySupplier)
	{
		this.registrySupplier = registrySupplier;
	}

	public DeferredRegister<T> getRegistry()
	{
		if (registry == null)
		{
			registry = registrySupplier.get();
			registrySupplier = null;
		}
		return registry;
	}

	public <I extends T> RegistryObject<I> register(final String name, final Supplier<? extends I> sup)
	{
		return getRegistry().register(name, sup);
	}

	@Deprecated
	public <E extends IForgeRegistryEntry<E>> Supplier<IForgeRegistry<E>> makeRegistry(final String name, final Supplier<RegistryBuilder<E>> sup)
	{
		return getRegistry().makeRegistry(name, sup);
	}

	public <E extends IForgeRegistryEntry<E>> Supplier<IForgeRegistry<E>> makeRegistry(final Class<E> base, final Supplier<RegistryBuilder<E>> sup)
	{
		return getRegistry().makeRegistry(base, sup);
	}

    @NotNull
    public TagKey<T> createTagKey(@NotNull String path)
    {
    	return getRegistry().createTagKey(path);
    }

    @NotNull
    public TagKey<T> createTagKey(@NotNull ResourceLocation location)
    {
    	return getRegistry().createTagKey(location);
    }

    @NotNull
    public TagKey<T> createOptionalTagKey(@NotNull String path, @NotNull Set<? extends Supplier<T>> defaults)
    {
    	return getRegistry().createOptionalTagKey(path, defaults);
    }

    @NotNull
    public TagKey<T> createOptionalTagKey(@NotNull ResourceLocation location, @NotNull Set<? extends Supplier<T>> defaults)
    {
    	return getRegistry().createOptionalTagKey(location, defaults);
    }

    public void addOptionalTagDefaults(@NotNull TagKey<T> name, @NotNull Set<? extends Supplier<T>> defaults)
    {
    	getRegistry().addOptionalTagDefaults(name, defaults);
    }

    public void register(IEventBus bus)
    {
    	if (registry != null) registry.register(bus);
    }

    public Collection<RegistryObject<T>> getEntries()
    {
        return registry == null ? Collections.emptyList() : registry.getEntries();
    }

    @Nullable
    public ResourceLocation getRegistryName()
    {
    	return registry == null ? null : registry.getRegistryName();
    }
}