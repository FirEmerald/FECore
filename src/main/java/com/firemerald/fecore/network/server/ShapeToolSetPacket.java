package com.firemerald.fecore.network.server;

import com.firemerald.fecore.boundingshapes.BoundingShape;
import com.firemerald.fecore.capabilities.IShapeHolder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class ShapeToolSetPacket extends ServerPacket
{
	private final InteractionHand hand;
	private final BoundingShape shape;

	@OnlyIn(Dist.CLIENT)
	public ShapeToolSetPacket(InteractionHand hand, BoundingShape shape)
	{
		this.hand = hand;
		this.shape = shape;
	}

	public ShapeToolSetPacket(FriendlyByteBuf buf)
	{
		hand = buf.readBoolean() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
		shape = BoundingShape.STREAM_CODEC.decode(buf);
	}

	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeBoolean(hand == InteractionHand.OFF_HAND);
		BoundingShape.STREAM_CODEC.encode(buf, shape);
	}

	@Override
	public void handleServer(NetworkEvent.Context ctx)
	{
		if (shape != null)
		{
			final Player player = ctx.getSender();
			ctx.enqueueWork(() -> {
	    		ItemStack held = player.getItemInHand(hand);
	    		if (!held.isEmpty())
	    		{
	    			IShapeHolder.get(held).ifPresent(holder -> {
	    				if (holder.canAcceptShape(shape)) holder.setShape(shape);
	    			});
	    		}
			});
		}
	}
}
