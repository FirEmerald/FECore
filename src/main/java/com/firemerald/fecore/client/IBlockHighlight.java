package com.firemerald.fecore.client;

import org.joml.Quaternionf;

import com.firemerald.fecore.util.Constants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderHighlightEvent;

@FunctionalInterface
public interface IBlockHighlight
{
	public static Quaternionf[] DIRECTION_TRANSFORMS = new Quaternionf[] {
			new Quaternionf(Constants.SQRT_2_INV, 0, 0, Constants.SQRT_2_INV), //DOWN
			new Quaternionf(-Constants.SQRT_2_INV, 0, 0, Constants.SQRT_2_INV), //UP
			new Quaternionf(0, 1, 0, 0), //NORTH
			new Quaternionf(0, 0, 0, 1), //SOUTH
			new Quaternionf(0, -Constants.SQRT_2_INV, 0, Constants.SQRT_2_INV), //WEST
			new Quaternionf(0, Constants.SQRT_2_INV, 0, Constants.SQRT_2_INV), //EAST
	};

	public static void transform(PoseStack pose, Direction sideHit)
	{
		pose.mulPose(DIRECTION_TRANSFORMS[sideHit.ordinal()]);
	}

	public default void render(Player player, RenderHighlightEvent.Block event) {
		render(event.getPoseStack(), event.getMultiBufferSource().getBuffer(RenderType.LINES), player, event.getTarget(), event.getCamera(), event.getPartialTick());
	}

	public default void render(PoseStack pose, VertexConsumer vertexConsumer, Player player, BlockHitResult result, Camera camera, float partialTick)
	{
		pose.pushPose();
		BlockPos hit = result.getBlockPos();
		double hitX = hit.getX();
		double hitY = hit.getY();
		double hitZ = hit.getZ();
		switch (result.getDirection()) {
		case WEST:
			hitX = result.getLocation().x - 1.005;
			break;
		case EAST:
			hitX = result.getLocation().x + .005;
			break;
		case DOWN:
			hitY = result.getLocation().y - 1.005;
			break;
		case UP:
			hitY = result.getLocation().y + .005;
			break;
		case NORTH:
			hitZ = result.getLocation().z - 1.005;
			break;
		case SOUTH:
			hitZ = result.getLocation().z + .005;
			break;
		default:
		}
		Vec3 pos = camera.getPosition();
		pose.translate(hitX - pos.x + .5, hitY - pos.y + .5, hitZ - pos.z + .5);
		transform(pose, result.getDirection());
		draw(pose, vertexConsumer, player, result, partialTick);
		pose.popPose();
	}

	public void draw(PoseStack pose, VertexConsumer vertexConsumer, Player player, BlockHitResult result, float partialTick);
}