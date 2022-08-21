package com.firemerald.fecore;

import com.firemerald.fecore.item.FECoreItems;
import com.firemerald.fecore.networking.FECoreNetwork;
import com.firemerald.fecore.networking.server.ShapeToolClickedPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.ClickInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeClientBusEventHandler
{	
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onClick(ClickInputEvent event)
	{
		if (event.isAttack())
		{
			Player player = Minecraft.getInstance().player;
			ItemStack stack = player.getMainHandItem();
			if (stack.isEmpty())
			{
				stack = player.getOffhandItem();
				if (stack.getItem() == FECoreItems.SHAPE_TOOL)
				{
					FECoreNetwork.INSTANCE.sendToServer(new ShapeToolClickedPacket(InteractionHand.OFF_HAND));
					event.setCanceled(true);
				}
			}
			else if (stack.getItem() == FECoreItems.SHAPE_TOOL)
			{
				FECoreNetwork.INSTANCE.sendToServer(new ShapeToolClickedPacket(InteractionHand.MAIN_HAND));
				event.setCanceled(true);
			}
		}
	}
}