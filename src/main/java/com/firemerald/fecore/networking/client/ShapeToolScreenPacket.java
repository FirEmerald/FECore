package com.firemerald.fecore.networking.client;

import com.firemerald.fecore.networking.FECoreNetwork;
import com.firemerald.fecore.networking.server.ShapeToolSetPacket;
import com.firemerald.fecore.selectionshapes.BoundingShape;
import com.firemerald.fecore.selectionshapes.GuiShapes;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class ShapeToolScreenPacket extends ClientPacket
{
	private final Vec3 pos;
	private final InteractionHand hand;
	private final BoundingShape shape;

	public ShapeToolScreenPacket(Vec3 pos, InteractionHand hand, BoundingShape shape)
	{
		this.pos = pos;
		this.hand = hand;
		this.shape = shape;
	}

	public ShapeToolScreenPacket(FriendlyByteBuf buf)
	{
		pos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
		hand = buf.readBoolean() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
		shape = BoundingShape.constructFromBuffer(buf);
	}

	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeDouble(pos.x);
		buf.writeDouble(pos.y);
		buf.writeDouble(pos.z);
		buf.writeBoolean(hand == InteractionHand.OFF_HAND);
		shape.saveToBuffer(buf);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handleClient(NetworkEvent.Context ctx)
	{
		if (shape != null) ctx.enqueueWork(() -> Minecraft.getInstance().setScreen(new GuiShapes(pos, shape, s -> FECoreNetwork.INSTANCE.sendToServer(new ShapeToolSetPacket(hand, s)))));
	}
}
