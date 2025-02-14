package com.firemerald.fecore.init;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.init.registry.DeferredObjectRegistry;
import com.firemerald.fecore.init.registry.ItemObject;
import com.firemerald.fecore.item.ShapeToolItem;

import net.neoforged.bus.api.IEventBus;

public class FECoreObjects {
	private static DeferredObjectRegistry registry = new DeferredObjectRegistry(FECoreMod.MOD_ID);

	public static final ItemObject<ShapeToolItem> SHAPE_TOOL = registry.registerBasicItem("shape_tool", ShapeToolItem::new);

	public static void init(IEventBus bus) {
		registry.register(bus);
		registry = null;
	}
}
