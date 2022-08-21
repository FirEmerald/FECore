package com.firemerald.fecore;

import com.firemerald.fecore.capabilities.IShapeHolder;
import com.firemerald.fecore.item.FECoreItems;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEventHandler
{
	@SubscribeEvent
	public static void onRegisterItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().register(FECoreItems.SHAPE_TOOL);
	}
	
	@SubscribeEvent
	public void registerCaps(RegisterCapabilitiesEvent event)
	{
		event.register(IShapeHolder.class);
	}
}