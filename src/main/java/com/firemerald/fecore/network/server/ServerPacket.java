package com.firemerald.fecore.network.server;

import java.util.function.Supplier;

import com.firemerald.fecore.network.PacketBase;

import net.minecraftforge.network.NetworkEvent;

public abstract class ServerPacket extends PacketBase
{
	@Override
	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getOriginationSide().isClient())
		{
			handleServer(context);
			context.setPacketHandled(true);
		}
	}

	public abstract void handleServer(NetworkEvent.Context ctx);
}