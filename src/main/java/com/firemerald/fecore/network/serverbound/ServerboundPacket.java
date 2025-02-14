package com.firemerald.fecore.network.serverbound;

import com.firemerald.fecore.network.NetworkUtil;
import com.firemerald.fecore.network.SimplePacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class ServerboundPacket<T extends FriendlyByteBuf> extends SimplePacket<T> {
	@Override
	public PacketFlow getDirection() {
		return PacketFlow.SERVERBOUND;
	}

	public abstract void handleServer(IPayloadContext context);

	@Override
	public void handleInternal(IPayloadContext context) {
		handleServer(context);
	}

    public void sendToServer() {
    	NetworkUtil.sendToServer(this);
    }
}
