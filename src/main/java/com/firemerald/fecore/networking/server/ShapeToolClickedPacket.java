package com.firemerald.fecore.networking.server;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.boundingshapes.BoundingShape;
import com.firemerald.fecore.boundingshapes.BoundingShapeBoxPositions;
import com.firemerald.fecore.capabilities.IShapeHolder;
import com.firemerald.fecore.networking.client.ShapeToolScreenPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;

public class ShapeToolClickedPacket extends ServerPacket
{
	private final InteractionHand hand;

	@OnlyIn(Dist.CLIENT)
	public ShapeToolClickedPacket(InteractionHand hand)
	{
		this.hand = hand;
	}

	public ShapeToolClickedPacket(FriendlyByteBuf buf)
	{
		hand = buf.readBoolean() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
	}

	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeBoolean(hand == InteractionHand.OFF_HAND);
	}

	@Override
	public void handleServer(NetworkEvent.Context ctx)
	{
		final ServerPlayer player = ctx.getSender();
		ItemStack stack = player.getItemInHand(hand);
		LazyOptional<IShapeHolder> cap = IShapeHolder.get(stack);
		if (cap.isPresent())
		{
			BoundingShape shape = cap.resolve().get().getShape();
			if (shape == null)
			{
				shape = new BoundingShapeBoxPositions();
				((BoundingShapeBoxPositions) shape).isRelative = false;
			}
			FECoreMod.NETWORK.sendTo(new ShapeToolScreenPacket(player.position(), hand, shape), player);
		}
	}
}
