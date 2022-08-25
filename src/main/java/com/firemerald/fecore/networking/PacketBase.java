package com.firemerald.fecore.networking;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public abstract class PacketBase
{
	public abstract void write(FriendlyByteBuf buf);

	public abstract void handle(Supplier<NetworkEvent.Context> ctx);
}