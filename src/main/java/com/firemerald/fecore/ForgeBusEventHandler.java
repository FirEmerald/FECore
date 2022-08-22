package com.firemerald.fecore;

import com.firemerald.fecore.betterscreens.IBetterScreen;
import com.firemerald.fecore.betterscreens.IScrollValuesHolder;
import com.firemerald.fecore.capabilities.IShapeTool;
import com.firemerald.fecore.item.FECoreItems;
import com.firemerald.fecore.util.Constants;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeBusEventHandler
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onMouseScrollEventPre(ScreenEvent.MouseScrollEvent.Pre event)
	{
    	if (!Minecraft.ON_OSX && event.getScreen() instanceof IBetterScreen) //bad forge fix for MC-121772 that doesn't take into account whether shift is actually held means we can't do this on macOS ever
    	{
    		Minecraft minecraft = Minecraft.getInstance();
    		double scrollX = ((IScrollValuesHolder) minecraft.mouseHandler).getScrollX();
    		if (scrollX != 0) ((IBetterScreen) event.getScreen()).mouseScrolledX(event.getMouseX(), event.getMouseY(), (minecraft.options.discreteMouseScroll ? Math.signum(scrollX) : scrollX) * minecraft.options.mouseWheelSensitivity);
    	}
	}

	@SubscribeEvent
	public static void onAttachCapabilitiesItemStack(AttachCapabilitiesEvent<ItemStack> event)
	{
		if (event.getObject().getItem() == FECoreItems.SHAPE_TOOL) event.addCapability(Constants.SHAPE_TOOL_CAP_NAME, new IShapeTool.Impl());
	}
}