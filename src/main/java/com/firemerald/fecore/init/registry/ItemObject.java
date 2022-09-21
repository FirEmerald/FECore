package com.firemerald.fecore.init.registry;

import java.util.Objects;
import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class ItemObject<I extends Item> implements ItemLike
{
	public final ResourceLocation id;
	public final Supplier<I> item;

	public ItemObject(ResourceLocation id, Supplier<I> item)
	{
		this.id = id;
		this.item = item;
	}

	public I getItem()
	{
		return Objects.requireNonNull(item.get(), "ItemObject missing item");
	}

	public boolean isThisItem(Item item)
	{
		return item != Items.AIR && item == getItem();
	}

	public boolean isThisItem(ItemStack stack)
	{
		return !stack.isEmpty() && stack.getItem() == getItem();
	}

	@Override
	public I asItem()
	{
		return getItem();
	}
}