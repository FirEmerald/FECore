package com.firemerald.fecore.init.registry;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class MutableSupplier<T> implements Supplier<T>
{
	public static final Supplier<Item> DEFAULT_ITEM = () -> Items.AIR;
	public static final Supplier<Block> DEFAULT_BLOCK = () -> Blocks.AIR;
	public static final Supplier<?> DEFAULT_NULL = () -> null;

	public static MutableSupplier<Item> ofItem()
	{
		return new MutableSupplier<>(DEFAULT_ITEM);
	}

	public static MutableSupplier<Block> ofBlock()
	{
		return new MutableSupplier<>(DEFAULT_BLOCK);
	}

	@Nonnull
	private Supplier<? extends  T> supplier;

	public MutableSupplier(@Nonnull Supplier<? extends T> supplier)
	{
		this.supplier = supplier;
	}

	public MutableSupplier(T object)
	{
		this(() -> object);
	}

	@SuppressWarnings("unchecked")
	public MutableSupplier()
	{
		this((Supplier<? extends T>) DEFAULT_NULL);
	}

	@Override
	public T get()
	{
		return supplier.get();
	}

	public void set(@Nonnull Supplier<? extends T> supplier)
	{
		this.supplier = supplier;
	}
}