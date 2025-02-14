package com.firemerald.fecore.client;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.boundingshapes.BoundingShape;
import com.firemerald.fecore.boundingshapes.IRenderableBoundingShape;
import com.firemerald.fecore.init.FECoreDataComponents;
import com.firemerald.fecore.init.FECoreObjects;
import com.firemerald.fecore.item.ICustomBlockHighlight;
import com.firemerald.fecore.network.serverbound.ShapeToolClickedPacket;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = FECoreMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ClientEventHandler {
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onClick(InputEvent.InteractionKeyMappingTriggered event)
	{
		if (event.isAttack()) {
			Player player = Minecraft.getInstance().player;
			InteractionHand hand = InteractionHand.MAIN_HAND;
			ItemStack stack = player.getMainHandItem();
			if (stack.isEmpty()) {
				stack = player.getOffhandItem();
				hand = InteractionHand.OFF_HAND;
			}
			if (FECoreObjects.SHAPE_TOOL.isThisItem(stack) ) {
				event.setSwingHand(false);
				event.setCanceled(true);
				new ShapeToolClickedPacket(hand).sendToServer();
			}
		}
	}

	@SubscribeEvent
	public static void onRenderLevelLast(RenderLevelStageEvent event)
	{
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS)
		{
			@SuppressWarnings("resource")
			Player player = Minecraft.getInstance().player;
			ItemStack stack = player.getMainHandItem();
			if (!FECoreObjects.SHAPE_TOOL.isThisItem(stack)) stack = player.getOffhandItem();
			if (FECoreObjects.SHAPE_TOOL.isThisItem(stack)) {
				BoundingShape shape = stack.get(FECoreDataComponents.HELD_SHAPE);
				if (shape instanceof IRenderableBoundingShape renderable) {
					Vec3 pos = event.getCamera().getPosition();
					PoseStack pose = event.getPoseStack();
					pose.pushPose();
					pose.translate(-pos.x, -pos.y, -pos.z);
					renderable.renderIntoWorld(event.getPoseStack(), player.getPosition(event.getPartialTick().getGameTimeDeltaPartialTick(true)), event.getPartialTick());
					pose.popPose();
				}
			}
		}
	}

	@SubscribeEvent
	public static void onHighlightBlock(RenderHighlightEvent.Block event)
	{
		@SuppressWarnings("resource")
		Player player = Minecraft.getInstance().player;
		ItemStack stack = player.getMainHandItem();
		if (stack.isEmpty()) stack = player.getOffhandItem();
		if (stack.getItem() instanceof ICustomBlockHighlight customHighlight)
		{
			IBlockHighlight highlight = customHighlight.getBlockHighlight(player, event);
			if (highlight != null) highlight.render(player, event);
		}
		else if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ICustomBlockHighlight customHighlight)
		{
			IBlockHighlight highlight = customHighlight.getBlockHighlight(player, event);
			if (highlight != null) highlight.render(player, event);
		}
	}
}
