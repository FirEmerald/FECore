package com.firemerald.fecore.client;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.boundingshapes.BoundingShape;
import com.firemerald.fecore.boundingshapes.IRenderableBoundingShape;
import com.firemerald.fecore.client.gui.IBetterScreen;
import com.firemerald.fecore.client.gui.IScrollValuesHolder;
import com.firemerald.fecore.init.FECoreItems;
import com.firemerald.fecore.item.ItemShapeTool;
import com.firemerald.fecore.networking.server.ShapeToolClickedPacket;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.ClickInputEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEventHandler
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
					FECoreMod.NETWORK.sendToServer(new ShapeToolClickedPacket(InteractionHand.OFF_HAND));
					event.setCanceled(true);
				}
			}
			else if (stack.getItem() == FECoreItems.SHAPE_TOOL)
			{
				FECoreMod.NETWORK.sendToServer(new ShapeToolClickedPacket(InteractionHand.MAIN_HAND));
				event.setCanceled(true);
			}
		}
	}


	@SubscribeEvent
	public static void onRenderLevelLast(RenderLevelStageEvent event)
	{
		if (event.getStage() == Stage.AFTER_TRIPWIRE_BLOCKS)
		{
			@SuppressWarnings("resource")
			Player player = Minecraft.getInstance().player;
			ItemStack stack = player.getMainHandItem();
			if (stack.getItem() == FECoreItems.SHAPE_TOOL)
			{
				BoundingShape shape = ItemShapeTool.getShape(stack);
				if (shape instanceof IRenderableBoundingShape)
				{
					Vec3 pos = event.getCamera().getPosition();
					PoseStack pose = event.getPoseStack();
					pose.pushPose();
					pose.translate(-pos.x, -pos.y, -pos.z);
					((IRenderableBoundingShape) shape).renderIntoWorld(event.getPoseStack(), player.getPosition(event.getPartialTick()), event.getPartialTick());
					pose.popPose();
				}
			}
			else
			{
				stack = player.getOffhandItem();
				if (stack.getItem() == FECoreItems.SHAPE_TOOL)
				{
					BoundingShape shape = ItemShapeTool.getShape(stack);
					if (shape instanceof IRenderableBoundingShape)
					{
						Vec3 pos = event.getCamera().getPosition();
						PoseStack pose = event.getPoseStack();
						pose.pushPose();
						pose.translate(-pos.x, -pos.y, -pos.z);
						((IRenderableBoundingShape) shape).renderIntoWorld(event.getPoseStack(), player.getPosition(event.getPartialTick()), event.getPartialTick());
						pose.popPose();
					}
				}
			}
		}
	}
}