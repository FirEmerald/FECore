package com.firemerald.fecore.init.registry;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegistryUtil {
	public static <A, B extends A> DeferredHolder<A, B> register(DeferredRegister<A> register, String name, Function<ResourceKey<A>, B> construct) {
		return register.register(name, id -> construct.apply(ResourceKey.create(register.getRegistryKey(), id)));
	}

	public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerEntityType(DeferredRegister<EntityType<?>> register, String name, Supplier<EntityType.Builder<T>> builder) {
		return register(register, name, key -> builder.get().build(key));
	}

	public static <B extends Block> DeferredBlock<B> registerBlock(DeferredRegister.Blocks register, String name, Function<ResourceKey<Block>, B> construct) {
		return register.register(name, id -> construct.apply(ResourceKey.create(register.getRegistryKey(), id)));
	}

	public static <I extends Item> DeferredItem<I> registerItem(DeferredRegister.Items register, String name, Function<ResourceKey<Item>, I> construct) {
		return register.register(name, id -> construct.apply(ResourceKey.create(register.getRegistryKey(), id)));
	}

	public static DeferredHolder<CreativeModeTab, CreativeModeTab> registerTab(DeferredRegister<CreativeModeTab> register, String name, Supplier<ItemStack> icon, ItemLike... items) {
		return register.register(name, () -> CreativeModeTab.builder()
				.title(Component.translatable("itemGroup." + name))
				.icon(icon)
				.displayItems((params, output) -> {
					for (ItemLike item : items) output.accept(item);
				})
				.build()
				);
	}

	public static DeferredHolder<CreativeModeTab, CreativeModeTab> registerTab(DeferredRegister<CreativeModeTab> register, String name, ItemLike icon, ItemLike... items) {
		return registerTab(register, name, () -> new ItemStack(icon), items);
	}
}
