package com.firemerald.fecore.blockentity;

import com.firemerald.fecore.client.gui.screen.BlockEntityGUIScreen;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
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

	public void readInternal(FriendlyByteBuf buf)
	{
		this.read(buf);
		this.setChanged();
	}

	public abstract void read(FriendlyByteBuf buf);

	public abstract void write(FriendlyByteBuf buf);

	@OnlyIn(Dist.CLIENT)
	public abstract BlockEntityGUIScreen getScreen();
}