package com.firemerald.fecore.item;

import com.firemerald.fecore.FECoreMod;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(FECoreMod.MOD_ID)
public class FECoreItems
{
	@ObjectHolder("shape_tool")
	public static final ItemShapeTool SHAPE_TOOL = null;
	
	public static void registerItems(IEventBus eventBus)
	{
		DeferredRegister<Item> items = DeferredRegister.create(ForgeRegistries.ITEMS, FECoreMod.MOD_ID);
		items.register("shape_tool", ItemShapeTool::new);
		
		items.register(eventBus);
	}
}
