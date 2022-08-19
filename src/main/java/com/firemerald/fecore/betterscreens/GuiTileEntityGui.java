package com.firemerald.fecore.betterscreens;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public abstract class GuiTileEntityGui extends BetterScreen
{
	public GuiTileEntityGui(Component title)
	{
		super(title);
	}

	public abstract BlockPos getTilePos();

	public abstract void read(ByteBuf buf);

	public abstract void write(ByteBuf buf);
}