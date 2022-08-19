package com.firemerald.fecore;

import com.firemerald.fecore.betterscreens.IBetterScreen;
import com.firemerald.fecore.betterscreens.IScrollValuesHolder;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onMouseScrollEventPre(ScreenEvent.MouseScrollEvent.Pre event)
	{
    	if (!Minecraft.ON_OSX && event.getScreen() instanceof IBetterScreen) //bad forge fix for MC-121772 that doesn't take into account whether shift is actually held means we can't do this on macOS
    	{
    		Minecraft minecraft = Minecraft.getInstance();
    		double scrollX = ((IScrollValuesHolder) minecraft.mouseHandler).getScrollX();
    		if (scrollX != 0) ((IBetterScreen) event.getScreen()).mouseScrolledX(event.getMouseX(), event.getMouseY(), (minecraft.options.discreteMouseScroll ? Math.signum(scrollX) : scrollX) * minecraft.options.mouseWheelSensitivity);
    	}
	}
}