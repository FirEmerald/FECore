package com.firemerald.fecore.client;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.common.CommonModEventHandler;
import com.firemerald.fecore.datagen.FECoreModelProvider;
import com.firemerald.fecore.init.FECoreObjects;

import net.minecraft.data.DataProvider;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = FECoreMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ClientModEventHandler {
	@SubscribeEvent
	public static void buildContents(BuildCreativeModeTabContentsEvent event) {
	    if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
	        event.accept(FECoreObjects.SHAPE_TOOL);
	    }
	}

	@SubscribeEvent
	public static void gatherClientData(GatherDataEvent.Client event)
	{
		event.getGenerator().addProvider(true, (DataProvider.Factory<FECoreModelProvider>) FECoreModelProvider::new);
		CommonModEventHandler.gatherData(event);
	}
}
