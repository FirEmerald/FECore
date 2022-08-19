package com.firemerald.fecore.betterscreens;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class BlockEntityGUI extends BlockEntity
{
	public BlockEntityGUI(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	public abstract void read(ByteBuf buf);

	public abstract void write(ByteBuf buf);

	@OnlyIn(Dist.CLIENT)
	public abstract GuiTileEntityGui getScreen();
}