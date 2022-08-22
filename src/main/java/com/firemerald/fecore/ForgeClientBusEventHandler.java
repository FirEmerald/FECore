package com.firemerald.fecore;

import com.firemerald.fecore.item.FECoreItems;
import com.firemerald.fecore.item.ItemShapeTool;
import com.firemerald.fecore.networking.FECoreNetwork;
import com.firemerald.fecore.networking.server.ShapeToolClickedPacket;
import com.firemerald.fecore.selectionshapes.BoundingShape;
import com.firemerald.fecore.selectionshapes.IRenderableBoundingShape;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.ClickInputEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
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