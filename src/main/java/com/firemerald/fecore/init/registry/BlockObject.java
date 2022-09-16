package com.firemerald.fecore.init.registry;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockObject<B extends Block, I extends Item> extends ItemObject<I>
{
	private final Supplier<B> block;
	
	public BlockObject(ResourceLocation id, Supplier<B> block, Supplier<I> item)
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

	@OnlyIn(Dist.CLIENT)
	public void setRenderLayer(RenderType layer)
	{
		ItemBlockRenderTypes.setRenderLayer(getBlock(), layer);
	}

	@OnlyIn(Dist.CLIENT)
	public void setRenderLayer(Predicate<RenderType> layer)
	{
		ItemBlockRenderTypes.setRenderLayer(getBlock(), layer);
	}
}