package com.firemerald.fecore.init.registry;

import java.util.Objects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityObject<E extends BlockEntity, B extends Block, I extends Item> extends BlockObject<B, I>
{
	public final RegistryObject<BlockEntityType<E>> blockEntity;

	public BlockEntityObject(ResourceLocation id, RegistryObject<BlockEntityType<E>> blockEntity, RegistryObject<B> block, RegistryObject<I> item)
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