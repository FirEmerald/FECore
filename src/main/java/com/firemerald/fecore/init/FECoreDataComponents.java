package com.firemerald.fecore.init;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.boundingshapes.BoundingShape;
import com.mojang.serialization.Codec;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unused")
public class FECoreDataComponents {
	private static DeferredRegister<DataComponentType<?>> registry = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, FECoreMod.MOD_ID);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<BoundingShape>> HELD_SHAPE = register("shape", BoundingShape.CODEC, BoundingShape.STREAM_CODEC);
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> HELD_SHAPE_INDEX = register("shape_index", Codec.INT, ByteBufCodecs.INT);

	private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name) {
		return registry.register(name, () -> DataComponentType.<T>builder().build());
	}

	private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, Codec<T> codec) {
		return registry.register(name, () -> DataComponentType.<T>builder().persistent(codec).build());
	}

	private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
		return registry.register(name, () -> DataComponentType.<T>builder().networkSynchronized(streamCodec).build());
	}

	private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
		return registry.register(name, () -> DataComponentType.<T>builder().persistent(codec).networkSynchronized(streamCodec).build());
	}

	public static void init(IEventBus bus) {
		registry.register(bus);
		registry = null;
	}
}
