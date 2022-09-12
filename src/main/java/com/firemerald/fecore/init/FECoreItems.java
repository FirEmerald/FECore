package com.firemerald.fecore.init;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.item.ShapeToolItem;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(FECoreMod.MOD_ID)
public class FECoreItems
{
	@ObjectHolder(RegistryNames.ITEM_SHAPE_TOOL)
	public static final ShapeToolItem SHAPE_TOOL = null;

	public static void registerItems(IEventBus eventBus)
	{
		DeferredRegister<Item> items = DeferredRegister.create(ForgeRegistries.ITEMS, FECoreMod.MOD_ID);
		items.register(RegistryNames.ITEM_SHAPE_TOOL, ShapeToolItem::new);
		items.register(eventBus);
	}
}
