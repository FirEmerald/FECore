package com.firemerald.fecore.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class StackUtils {
	public static CompoundTag decodeBlockData(ItemStack stack) {
    	CompoundTag root = stack.getTag();
    	if (root != null && root.contains("BlockEntityTag", 10)) return root.getCompound("BlockEntityTag");
    	else return null;
	}

	public static CompoundTag decodeEntityData(ItemStack stack) {
    	CompoundTag root = stack.getTag();
    	if (root != null && root.contains("EntityTag", 10)) return root.getCompound("EntityTag");
    	else return null;
	}
}
