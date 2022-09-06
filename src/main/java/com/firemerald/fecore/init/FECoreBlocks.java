package com.firemerald.fecore.init;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.block.FESlabBlock;
import com.firemerald.fecore.block.FEStairBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(FECoreMod.MOD_ID)
public class FECoreBlocks
{
	@ObjectHolder(RegistryNames.TEST_BLOCK_SLAB)
	public static final FESlabBlock TEST_SLAB = null;
	@ObjectHolder(RegistryNames.TEST_BLOCK_STAIRS)
	public static final FEStairBlock TEST_STAIRS = null;

	public static void registerBlocks(IEventBus eventBus)
	{
		DeferredRegister<Block> blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, FECoreMod.MOD_ID);
		if (FECoreMod.TEST_MODE)
		{
			blocks.register(RegistryNames.TEST_BLOCK_SLAB, () -> new FESlabBlock(Blocks.DIRT.defaultBlockState()));
			blocks.register(RegistryNames.TEST_BLOCK_STAIRS, () -> new FEStairBlock(Blocks.DIRT.defaultBlockState()));
		}
		blocks.register(eventBus);
	}
}
