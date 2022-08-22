package com.firemerald.fecore;

import com.firemerald.fecore.capabilities.IShapeHolder;

import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEventHandler
{
	@SubscribeEvent
	public void registerCaps(RegisterCapabilitiesEvent event)
	{
		event.register(IShapeHolder.class);
	}
}