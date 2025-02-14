package com.firemerald.fecore.init.registry;

import java.util.Objects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

public class BlockObject<B extends Block, I extends Item> extends ItemObject<I>
{
	public final DeferredBlock<B> block;

	public BlockObject(ResourceLocation id, DeferredBlock<B> block, DeferredItem<I> item)
	{
		super(id, item);
		this.block = block;
	}

	public B getBlock()
	{
		return Objects.requireNonNull(block.get(), "BlockObject missing block");
	}

	public boolean isThisBlock(Block block)
	{
		return block != Blocks.AIR && block == getBlock();
	}
}