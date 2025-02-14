package com.firemerald.fecore.network.serverbound;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.boundingshapes.BoundingShape;
import com.firemerald.fecore.init.FECoreDataComponents;
import com.firemerald.fecore.util.INetworkedGUIEntity;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ShapeToolSetPacket<T extends Entity & INetworkedGUIEntity<T>> extends ServerboundPacket<RegistryFriendlyByteBuf> {
	public static final Type<ShapeToolSetPacket<?>> TYPE = new Type<>(FECoreMod.id("shape_tool_set"));

	private final InteractionHand hand;
	private final BoundingShape shape;

	@OnlyIn(Dist.CLIENT)
	public ShapeToolSetPacket(InteractionHand hand, BoundingShape shape)
	{
		this.hand = hand;
		this.shape = shape;
	}

	public ShapeToolSetPacket(RegistryFriendlyByteBuf buf) {
		hand = buf.readBoolean() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
		shape = BoundingShape.STREAM_CODEC.decode(buf);
	}

	@Override
	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeBoolean(hand == InteractionHand.OFF_HAND);
		BoundingShape.STREAM_CODEC.encode(buf, shape);
	}

	@Override
	public void handleServer(IPayloadContext context) {
		if (shape != null) {
			final Player player = context.player();
			context.enqueueWork(() -> {
	    		ItemStack held = player.getItemInHand(hand);
	    		if (!held.isEmpty() && held.has(FECoreDataComponents.HELD_SHAPE)) {
	    			held.set(FECoreDataComponents.HELD_SHAPE, shape);
	    		}
			});
		}
	}

	@Override
	public Type<ShapeToolSetPacket<?>> type() {
		return TYPE;
	}

}
