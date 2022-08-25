package com.firemerald.fecore.client.gui.screen;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public abstract class BlockEntityGUIScreen extends BetterScreen
{
	public BlockEntityGUIScreen(Component title)
	{
		super(title);
	}

	public abstract BlockPos getTilePos();

	public abstract void read(FriendlyByteBuf buf);

	public abstract void write(FriendlyByteBuf buf);
}