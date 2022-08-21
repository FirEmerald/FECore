package com.firemerald.fecore.networking.client;

import java.util.function.Supplier;

import com.firemerald.fecore.networking.PacketBase;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public abstract class ClientPacket extends PacketBase
{
	@Override
	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getOriginationSide().isServer())
		{
			handleClient(context);
			context.setPacketHandled(true);
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public abstract void handleClient(NetworkEvent.Context ctx);
}