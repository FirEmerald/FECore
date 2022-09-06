package com.firemerald.fecore.init;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.item.FEBlockItem;
import com.firemerald.fecore.item.ShapeToolItem;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(FECoreMod.MOD_ID)
public class FECoreItems
{
	@ObjectHolder(RegistryNames.ITEM_SHAPE_TOOL)
	public static final ShapeToolItem SHAPE_TOOL = null;
	@ObjectHolder(RegistryNames.TEST_BLOCK_SLAB)
	public static final FEBlockItem TEST_SLAB = null;
	@ObjectHolder(RegistryNames.TEST_BLOCK_STAIRS)
	public static final FEBlockItem TEST_STAIRS = null;

	public static void registerItems(IEventBus eventBus)
	{
		DeferredRegister<Item> items = DeferredRegister.create(ForgeRegistries.ITEMS, FECoreMod.MOD_ID);
		items.register(RegistryNames.ITEM_SHAPE_TOOL, ShapeToolItem::new);
		if (FECoreMod.TEST_MODE)
		{
			items.register(RegistryNames.TEST_BLOCK_SLAB, () -> new FEBlockItem(FECoreBlocks.TEST_SLAB, new Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
			items.register(RegistryNames.TEST_BLOCK_STAIRS, () -> new FEBlockItem(FECoreBlocks.TEST_STAIRS, new Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
		}
		items.register(eventBus);
	}
}
