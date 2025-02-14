package com.firemerald.fecore.network;

import com.firemerald.fecore.FECoreMod;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class SimplePacket<T extends FriendlyByteBuf> implements CustomPacketPayload {
	public abstract void write(T buf);

	public abstract PacketFlow getDirection();

	public abstract void handleInternal(IPayloadContext context);

	public void handle(IPayloadContext context) {
		if (context.flow() == getDirection()) handleInternal(context);
		else FECoreMod.LOGGER.error("Tried to handle " + getClass() + " with invalid direction " + context.flow());
	}

    public void reply(IPayloadContext context) {
    	context.reply(this);
    }
}
