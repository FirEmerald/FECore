package com.firemerald.fecore.networking.server;

import com.firemerald.fecore.capabilities.FECoreCapabilities;
import com.firemerald.fecore.capabilities.IShapeHolder;
import com.firemerald.fecore.networking.FECoreNetwork;
import com.firemerald.fecore.networking.client.ShapeToolScreenPacket;
import com.firemerald.fecore.selectionshapes.BoundingShape;
import com.firemerald.fecore.selectionshapes.BoundingShapeBoxPositions;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

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
		LazyOptional<IShapeHolder> cap = stack.getCapability(FECoreCapabilities.SHAPE_HOLDER);
		if (cap.isPresent())
		{
			BoundingShape shape = cap.resolve().get().getShape();
			if (shape == null) shape = new BoundingShapeBoxPositions();
			FECoreNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new ShapeToolScreenPacket(player.position(), hand, shape));
		}
	}
}
