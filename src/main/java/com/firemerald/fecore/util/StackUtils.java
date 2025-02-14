package com.firemerald.fecore.util;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapDecoder;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class StackUtils {
	public static <T> T decodeData(ItemStack stack, MapDecoder<T> decoder, DataComponentType<CustomData> componentType) {
		if (stack.has(componentType)) {
			CustomData blockData = stack.get(componentType);
			if (!blockData.isEmpty()) {
				DataResult<T> decoded = blockData.read(decoder);
				if (decoded.isSuccess()) return decoded.result().get();
			}
		}
		return null;
	}

	public static <T> T decodeBlockData(ItemStack stack, MapDecoder<T> decoder) {
		return decodeData(stack, decoder, DataComponents.BLOCK_ENTITY_DATA);
	}

	public static <T> T decodeEntityData(ItemStack stack, MapDecoder<T> decoder) {
		return decodeData(stack, decoder, DataComponents.ENTITY_DATA);
	}

	public static <T> T decodeBucketData(ItemStack stack, MapDecoder<T> decoder) {
		return decodeData(stack, decoder, DataComponents.BUCKET_ENTITY_DATA);
	}
}
