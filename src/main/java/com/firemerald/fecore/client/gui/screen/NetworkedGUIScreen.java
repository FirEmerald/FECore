package com.firemerald.fecore.client.gui.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public abstract class NetworkedGUIScreen extends BetterScreen
{
	public NetworkedGUIScreen(Component title)
	{
		super(title);
	}

	public abstract void read(FriendlyByteBuf buf);

	public abstract void write(FriendlyByteBuf buf);
}