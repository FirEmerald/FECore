package com.firemerald.fecore.client;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.datagen.FECoreItemModelProvider;
import com.firemerald.fecore.init.FECoreObjects;

import net.minecraft.data.DataProvider;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT, modid = FECoreMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ClientModEventHandler {
	@SubscribeEvent
	public static void buildContents(BuildCreativeModeTabContentsEvent event) {
	    if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
	        event.accept(FECoreObjects.SHAPE_TOOL);
	    }
	}

	@SubscribeEvent
	public static void gatherClientData(GatherDataEvent event)
	{
		event.getGenerator().addProvider(true, (DataProvider.Factory<FECoreItemModelProvider>) output -> new FECoreItemModelProvider(output, event.getExistingFileHelper()));
	}
}
