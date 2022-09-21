package com.firemerald.fecore.init;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.init.registry.ItemObject;
import com.firemerald.fecore.item.ShapeToolItem;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item.Properties;

public class FECoreItems
{
	public static final ItemObject<ShapeToolItem> SHAPE_TOOL = FECoreMod.REGISTRY.registerItem(RegistryNames.ITEM_SHAPE_TOOL, () -> new ShapeToolItem(new Properties().stacksTo(1).tab(CreativeModeTab.TAB_TOOLS)));

	public static void init() {}
}
