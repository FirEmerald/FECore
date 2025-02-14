package com.firemerald.fecore.network.clientbound;

import com.firemerald.fecore.network.NetworkUtil;
import com.firemerald.fecore.network.SimplePacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class ClientboundPacket<T extends FriendlyByteBuf> extends SimplePacket<T>
{
	@Override
	public PacketFlow getDirection() {
		return PacketFlow.CLIENTBOUND;
	}

	@OnlyIn(Dist.CLIENT)
	public abstract void handleClient(IPayloadContext context);

	@Override
	public void handleInternal(IPayloadContext context) {
		handleClient(context);
	}

    public void sendToClient(ServerPlayer player) {
    	NetworkUtil.sendToClient(this, player);
    }

    public void sendToAllClients() {
    	NetworkUtil.sendToAllClients(this);
    }
}
