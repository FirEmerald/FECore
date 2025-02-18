package com.firemerald.fecore.init.registry;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RegistryUtil {
	public static <A> RegistryObject<A> register(DeferredRegister<A> register, ResourceLocation name, Function<String, A> construct) {
		return register.register(name.getPath(), () -> construct.apply(name.toString()));
	}

	@SuppressWarnings("unchecked")
	public static <T extends Entity> RegistryObject<EntityType<T>> registerEntityType(DeferredRegister<EntityType<?>> register, ResourceLocation name, Supplier<EntityType.Builder<T>> builder) {
		return (RegistryObject<EntityType<T>>) (Object) RegistryUtil.register(register, name, key -> builder.get().build(key));
	}

	public static RegistryObject<CreativeModeTab> registerTab(DeferredRegister<CreativeModeTab> register, String name, Supplier<ItemStack> icon, ItemLike... items) {
		return register.register(name, () -> CreativeModeTab.builder()
				.title(Component.translatable("itemGroup." + name))
				.icon(icon)
				.displayItems((params, output) -> {
					for (ItemLike item : items) output.accept(item);
				})
				.build()
				);
	}

	public static RegistryObject<CreativeModeTab> registerTab(DeferredRegister<CreativeModeTab> register, String name, ItemLike icon, ItemLike... items) {
		return registerTab(register, name, () -> new ItemStack(icon), items);
	}
}
