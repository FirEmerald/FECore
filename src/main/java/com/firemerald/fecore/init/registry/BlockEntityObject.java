package com.firemerald.fecore.init.registry;

import java.util.Objects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

public class BlockEntityObject<E extends BlockEntity, B extends Block, I extends Item> extends BlockObject<B, I>
{
	public final DeferredHolder<BlockEntityType<?>, BlockEntityType<E>> blockEntity;

	public BlockEntityObject(ResourceLocation id, DeferredHolder<BlockEntityType<?>, BlockEntityType<E>> blockEntity, DeferredBlock<B> block, DeferredItem<I> item)
	{
		super(id, block, item);
		this.blockEntity = blockEntity;
	}

	public BlockEntityType<E> getBlockEntityType()
	{
		return Objects.requireNonNull(blockEntity.get(), "BlockEntityObject missing block entity");
	}

	public boolean isThisBlockEntity(BlockEntity entity)
	{
		return entity != null && entity.getType() == getBlockEntityType();
	}

	public boolean isThisBlockEntity(BlockEntityType<?> type)
	{
		return type == getBlockEntityType();
	}
}