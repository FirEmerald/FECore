package com.firemerald.fecore.block;

import com.firemerald.fecore.network.clientbound.BlockEntityGUIPacket;
import com.firemerald.fecore.util.INetworkedGUIEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
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

	public abstract boolean canOpenGUI(BlockState state, Level level, BlockPos blockPos, Player player, BlockHitResult hitResult);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos blockPos, Player player, BlockHitResult hitResult)
    {
    	if (!canOpenGUI(state, level, blockPos, player, hitResult)) return InteractionResult.PASS;
    	else {
    		BlockEntity blockEntity = level.getBlockEntity(blockPos);
    		if (blockEntity instanceof INetworkedGUIEntity) {
    			if (level.isClientSide) return InteractionResult.SUCCESS;
    	    	else {
    	    		if (player instanceof ServerPlayer serverPlayer) new BlockEntityGUIPacket(blockEntity).sendToClient(serverPlayer);
    	    		return InteractionResult.CONSUME;
    	    	}
    		} else return InteractionResult.FAIL;
    	}
    }
}
