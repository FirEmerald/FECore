package com.firemerald.fecore.boundingshapes;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class BoundingShapeDefinition<T extends BoundingShape> {
	public static <T extends BoundingShape> BoundingShapeDefinition<T> of(MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec, Supplier<T> constructor) {
		return new BoundingShapeDefinition<>(codec, streamCodec, constructor, false);
	}

	public static <T extends BoundingShape & IConfigurableBoundingShape> BoundingShapeDefinition<T> ofConfigurable(MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec, Supplier<T> constructor) {
		return new BoundingShapeDefinition<>(codec, streamCodec, constructor, true);
	}

	public final MapCodec<T> codec;
	public final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
	public final Supplier<T> constructor;
	public final boolean configurable;

	private BoundingShapeDefinition(MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec, Supplier<T> constructor, boolean configurable) {
		this.codec = codec;
		this.streamCodec = streamCodec;
		this.constructor = constructor;
		this.configurable = configurable;
	}

	public T newShape() {
		return constructor.get();
	}
}
