package com.firemerald.fecore.client.gui.screen;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;

public abstract class NetworkedGUIScreen extends BetterScreen
{
	public NetworkedGUIScreen(Component title)
	{
		super(title);
	}

	public abstract void read(RegistryFriendlyByteBuf buf);

	public abstract void write(RegistryFriendlyByteBuf buf);
}