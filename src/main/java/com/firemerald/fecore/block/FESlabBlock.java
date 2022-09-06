package com.firemerald.fecore.block;

import com.firemerald.fecore.client.IBlockHighlight;
import com.firemerald.fecore.util.VoxelShapes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;

public class FESlabBlock extends FEPartialBlock implements ICustomBlockHighlight
{
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final DirectionProperty PLACING = BlockStateProperties.FACING;
	
	public FESlabBlock(BlockState model)
	{
		super(model);
		this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(PLACING, Direction.DOWN));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(WATERLOGGED, PLACING);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		BlockState blockState = super.getStateForPlacement(context);
		double hitX = context.getClickLocation().x - context.getClickedPos().getX() - .5;
		double hitY = context.getClickLocation().y - context.getClickedPos().getY() - .5;
		double hitZ = context.getClickLocation().z - context.getClickedPos().getZ() - .5;
        switch (context.getClickedFace())
        {
		case DOWN:
			if (Math.abs(hitX) <= .25f && Math.abs(hitZ) <= .25f) blockState = blockState.setValue(PLACING, Direction.UP);
			else if (hitX > hitZ)
			{
				if (hitX > -hitZ) blockState = blockState.setValue(PLACING, Direction.EAST);
				else blockState = blockState.setValue(PLACING, Direction.NORTH);
			}
			else
			{
				if (hitZ > -hitX) blockState = blockState.setValue(PLACING, Direction.SOUTH);
				else blockState = blockState.setValue(PLACING, Direction.WEST);
			}
			break;
		case UP:
			if (Math.abs(hitX) <= .25f && Math.abs(hitZ) <= .25f) blockState = blockState.setValue(PLACING, Direction.DOWN);
			else if (hitX > hitZ)
			{
				if (hitX > -hitZ) blockState = blockState.setValue(PLACING, Direction.EAST);
				else blockState = blockState.setValue(PLACING, Direction.NORTH);
			}
			else
			{
				if (hitZ > -hitX) blockState = blockState.setValue(PLACING, Direction.SOUTH);
				else blockState = blockState.setValue(PLACING, Direction.WEST);
			}
			break;
		case NORTH:
			if (Math.abs(hitX) <= .25f && Math.abs(hitY) <= .25f) blockState = blockState.setValue(PLACING, Direction.SOUTH);
			else if (hitX > hitY)
			{
				if (hitX > -hitY) blockState = blockState.setValue(PLACING, Direction.EAST);
				else blockState = blockState.setValue(PLACING, Direction.DOWN);
			}
			else
			{
				if (hitY > -hitX) blockState = blockState.setValue(PLACING, Direction.UP);
				else blockState = blockState.setValue(PLACING, Direction.WEST);
			}
			break;
		case SOUTH:
			if (Math.abs(hitX) <= .25f && Math.abs(hitY) <= .25f) blockState = blockState.setValue(PLACING, Direction.NORTH);
			else if (hitX > hitY)
			{
				if (hitX > -hitY) blockState = blockState.setValue(PLACING, Direction.EAST);
				else blockState = blockState.setValue(PLACING, Direction.DOWN);
			}
			else
			{
				if (hitY > -hitX) blockState = blockState.setValue(PLACING, Direction.UP);
				else blockState = blockState.setValue(PLACING, Direction.WEST);
			}
			break;
		case WEST:
			if (Math.abs(hitY) <= .25f && Math.abs(hitZ) <= .25f) blockState = blockState.setValue(PLACING, Direction.EAST);
			else if (hitZ > hitY)
			{
				if (hitZ > -hitY) blockState = blockState.setValue(PLACING, Direction.SOUTH);
				else blockState = blockState.setValue(PLACING, Direction.DOWN);
			}
			else
			{
				if (hitY > -hitZ) blockState = blockState.setValue(PLACING, Direction.UP);
				else blockState = blockState.setValue(PLACING, Direction.NORTH);
			}
			break;
		case EAST:
			if (Math.abs(hitY) <= .25f && Math.abs(hitZ) <= .25f) blockState = blockState.setValue(PLACING, Direction.WEST);
			else if (hitZ > hitY)
			{
				if (hitZ > -hitY) blockState = blockState.setValue(PLACING, Direction.SOUTH);
				else blockState = blockState.setValue(PLACING, Direction.DOWN);
			}
			else
			{
				if (hitY > -hitZ) blockState = blockState.setValue(PLACING, Direction.UP);
				else blockState = blockState.setValue(PLACING, Direction.NORTH);
			}
			break;
        }
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return blockState.setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
	}
	
	@Override
	@Deprecated
	public BlockState updateShape(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos)
	{
		if (state.getValue(WATERLOGGED)) level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		return super.updateShape(state, direction, otherState, level, pos, otherPos);
	}

	@Override
	@Deprecated
	public FluidState getFluidState(BlockState blockState)
	{
		return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
	}
	
	@Override
	public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable)
	{
		return state.getValue(PLACING) == facing && model.getBlock().canSustainPlant(model, world, pos, facing, plantable);
	}
	
	@Override
	public IBlockHighlight getBlockHighlight(Player player, BlockHitResult trace)
	{
		return IBlockHighlight.SLAB;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		switch (state.getValue(PLACING))
		{
		case WEST: return VoxelShapes.SLAB_WEST;
		case EAST: return VoxelShapes.SLAB_EAST;
		case DOWN: return VoxelShapes.SLAB_DOWN;
		case UP: return VoxelShapes.SLAB_UP;
		case NORTH: return VoxelShapes.SLAB_NORTH;
		case SOUTH: return VoxelShapes.SLAB_SOUTH;
		default: return super.getShape(state, level, pos, context);
		}
	}
}
