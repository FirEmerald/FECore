package com.firemerald.fecore.block;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.blockentity.BlockEntityGUI;
import com.firemerald.fecore.networking.client.BlockEntityGUIPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class BlockEntityGUIBlock extends BaseEntityBlock
{
	protected BlockEntityGUIBlock(Properties properties)
	{
		super(properties);
	}

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
    	if (!player.isCreative()) return InteractionResult.PASS;
    	else
    	{
    		BlockEntity blockEntity = level.getBlockEntity(blockPos);
    		if (blockEntity instanceof BlockEntityGUI)
    		{
    			if (level.isClientSide)
    	    	{
    	    		return InteractionResult.SUCCESS;
    	    	}
    	    	else
    	    	{
    	    		if (player instanceof ServerPlayer)
    	    			FECoreMod.NETWORK.sendTo(new BlockEntityGUIPacket((BlockEntityGUI) blockEntity), (ServerPlayer) player);
    	    		return InteractionResult.CONSUME;
    	    	}
    		}
    		else return InteractionResult.FAIL;
    	}
    }
}
