package com.firemerald.fecore.network.serverbound;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.init.FECoreDataComponents;
import com.firemerald.fecore.network.clientbound.ShapeToolScreenPacket;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ShapeToolClickedPacket extends ServerboundPacket<RegistryFriendlyByteBuf> {
	public static final Type<ShapeToolClickedPacket> TYPE = new Type<>(FECoreMod.id("shape_tool_clicked"));

	private final InteractionHand hand;

	@OnlyIn(Dist.CLIENT)
	public ShapeToolClickedPacket(InteractionHand hand)
	{
		this.hand = hand;
	}

	public ShapeToolClickedPacket(RegistryFriendlyByteBuf buf)
	{
		hand = buf.readBoolean() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
	}

	@Override
	public void write(RegistryFriendlyByteBuf buf)
	{
		buf.writeBoolean(hand == InteractionHand.OFF_HAND);
	}

	@Override
	public void handleServer(IPayloadContext context) {
		final Player player = context.player();
		ItemStack stack = player.getItemInHand(hand);
		if (stack.has(FECoreDataComponents.HELD_SHAPE)) new ShapeToolScreenPacket<>(player.position(), hand, stack.get(FECoreDataComponents.HELD_SHAPE)).reply(context);
	}

	@Override
	public Type<ShapeToolClickedPacket> type() {
		return TYPE;
	}
}
