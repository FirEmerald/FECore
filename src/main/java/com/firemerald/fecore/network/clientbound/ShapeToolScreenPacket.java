package com.firemerald.fecore.network.clientbound;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.boundingshapes.BoundingShape;
import com.firemerald.fecore.client.gui.screen.ShapesScreen;
import com.firemerald.fecore.network.serverbound.ShapeToolSetPacket;
import com.firemerald.fecore.util.INetworkedGUIEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ShapeToolScreenPacket<T extends Entity & INetworkedGUIEntity<T>> extends ClientboundPacket<RegistryFriendlyByteBuf> {
	public static final Type<ShapeToolScreenPacket<?>> TYPE = new Type<>(FECoreMod.id("shape_tool_screen"));

	private final Vec3 pos;
	private final InteractionHand hand;
	private final BoundingShape shape;

	public ShapeToolScreenPacket(Vec3 pos, InteractionHand hand, BoundingShape shape)
	{
		this.pos = pos;
		this.hand = hand;
		this.shape = shape;
	}

	public ShapeToolScreenPacket(RegistryFriendlyByteBuf buf) {
		pos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
		hand = buf.readBoolean() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
		shape = BoundingShape.STREAM_CODEC.decode(buf);
	}

	@Override
	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeDouble(pos.x);
		buf.writeDouble(pos.y);
		buf.writeDouble(pos.z);
		buf.writeBoolean(hand == InteractionHand.OFF_HAND);
		BoundingShape.STREAM_CODEC.encode(buf, shape);
	}

	@Override
	public void handleClient(IPayloadContext context) {
		if (shape != null) {
			context.enqueueWork(() -> Minecraft.getInstance().setScreen(new ShapesScreen(pos, shape, s -> new ShapeToolSetPacket<>(hand, s).sendToServer())));
		}
	}

	@Override
	public Type<ShapeToolScreenPacket<?>> type() {
		return TYPE;
	}

}
