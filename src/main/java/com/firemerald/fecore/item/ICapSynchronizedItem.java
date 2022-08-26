package com.firemerald.fecore.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.common.util.LazyOptional;

public interface ICapSynchronizedItem<T> extends IForgeItem
{
	@Nonnull
	public abstract LazyOptional<T> getCap(ItemStack stack);
	
	@Override
	@Nonnull
    public default CompoundTag getShareTag(ItemStack stack)
    {
		CompoundTag tag = IForgeItem.super.getShareTag(stack);
		if (tag == null) tag = new CompoundTag();
		final CompoundTag nbt = tag;
		getCap(stack).ifPresent(holder -> {
			CompoundTag capsTag;
			if (nbt.contains("ForgeCaps", 10)) capsTag = nbt.getCompound("ForgeCaps");
			else nbt.put("ForgeCaps", capsTag = new CompoundTag());
			capsTag.put("Parent", writeCap(holder, stack));
		});
		return nbt;
    }

	@Override
    public default void readShareTag(ItemStack stack, @Nullable CompoundTag nbt)
    {
		IForgeItem.super.readShareTag(stack, nbt);
		if (nbt != null) getCap(stack).ifPresent(holder -> {
			CompoundTag capsTag = nbt.getCompound("ForgeCaps");
			if (capsTag != null && capsTag.contains("Parent", 10)) readCap(holder, stack, capsTag.getCompound("Parent"));
		});
    }
	
	public CompoundTag writeCap(T cap, ItemStack stack);
	
	public void readCap(T cap, ItemStack stack, CompoundTag tag);
}