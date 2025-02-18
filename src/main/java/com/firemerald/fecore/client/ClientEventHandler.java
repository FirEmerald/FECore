package com.firemerald.fecore.client;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.boundingshapes.BoundingShape;
import com.firemerald.fecore.boundingshapes.IRenderableBoundingShape;
import com.firemerald.fecore.client.gui.IBetterScreen;
import com.firemerald.fecore.client.gui.IScrollValuesHolder;
import com.firemerald.fecore.init.FECoreObjects;
import com.firemerald.fecore.item.ICustomBlockHighlight;
import com.firemerald.fecore.item.ShapeToolItem;
import com.firemerald.fecore.network.server.ShapeToolClickedPacket;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT, modid = FECoreMod.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
public class ClientEventHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onMouseScrollEventPre(ScreenEvent.MouseScrolled.Pre event)
	{
    	if (!Minecraft.ON_OSX && event.getScreen() instanceof IBetterScreen screen) //bad forge fix for MC-121772 that doesn't take into account whether shift is actually held means we can't do this on macOS ever
    	{
    		Minecraft minecraft = Minecraft.getInstance();
    		double scrollX = ((IScrollValuesHolder) minecraft.mouseHandler).getScrollX();
    		if (scrollX != 0) screen.mouseScrolledX(event.getMouseX(), event.getMouseY(), (minecraft.options.discreteMouseScroll().get() ? Math.signum(scrollX) : scrollX) * minecraft.options.mouseWheelSensitivity().get());
    	}
	}

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
				FECoreMod.NETWORK.sendToServer(new ShapeToolClickedPacket(hand));
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
				BoundingShape shape = ShapeToolItem.getShape(stack);
				if (shape instanceof IRenderableBoundingShape renderable) {
					Vec3 pos = event.getCamera().getPosition();
					PoseStack pose = event.getPoseStack();
					pose.pushPose();
					pose.translate(-pos.x, -pos.y, -pos.z);
					renderable.renderIntoWorld(event.getPoseStack(), player.getPosition(event.getPartialTick()), event.getPartialTick());
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
