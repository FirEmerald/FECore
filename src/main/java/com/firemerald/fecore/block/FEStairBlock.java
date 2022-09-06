package com.firemerald.fecore.block;

import com.firemerald.fecore.client.IBlockHighlight;
import com.firemerald.fecore.util.VoxelShapes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;

public class FEStairBlock extends FEPartialBlock implements ICustomBlockHighlight
{
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final EnumProperty<EnumPlacing> PLACING = EnumProperty.create("placing", EnumPlacing.class);
	public static final EnumProperty<EnumShape> SHAPE = EnumProperty.create("shape", EnumShape.class);
	public static final VoxelShape[][] SHAPE_CACHE = new VoxelShape[12][9];
	
	static
	{
		for (EnumPlacing placing : EnumPlacing.values())
		{
			VoxelShape[] shapes = SHAPE_CACHE[placing.ordinal()];
			shapes[EnumShape.STRAIGHT.ordinal()] = VoxelShapes.getStraightStairs(placing.top, placing.front);
			shapes[EnumShape.INSIDE_RIGHT.ordinal()] = VoxelShapes.getInnerStairs(placing.top, placing.front, placing.left);
			shapes[EnumShape.INSIDE_LEFT.ordinal()] = VoxelShapes.getInnerStairs(placing.top, placing.front, placing.right);
			shapes[EnumShape.OUTSIDE_RIGHT.ordinal()] = VoxelShapes.getOuterStairs(placing.top, placing.front, placing.right);
			shapes[EnumShape.OUTSIDE_LEFT.ordinal()] = VoxelShapes.getOuterStairs(placing.top, placing.front, placing.left);
			shapes[EnumShape.OUTSIDE_HORIZONTAL_RIGHT.ordinal()] = VoxelShapes.getOuterFlatStairs(placing.top, placing.front, placing.right);
			shapes[EnumShape.OUTSIDE_HORIZONTAL_LEFT.ordinal()] = VoxelShapes.getOuterFlatStairs(placing.top, placing.front, placing.left);
			shapes[EnumShape.OUTSIDE_VERTICAL_RIGHT.ordinal()] = VoxelShapes.getOuterFlatStairs(placing.front, placing.top, placing.right);
			shapes[EnumShape.OUTSIDE_VERTICAL_LEFT.ordinal()] = VoxelShapes.getOuterFlatStairs(placing.front, placing.top, placing.left);
		}
	}
	
	public FEStairBlock(BlockState model)
	{
		super(model);
		this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(PLACING, EnumPlacing.UP_SOUTH).setValue(SHAPE, EnumShape.STRAIGHT));
	}
	
	public static EnumPlacing getPlacing(BlockState state)
	{
		if (state.getBlock() instanceof FEStairBlock) return state.getValue(PLACING);
		else if (state.getBlock() instanceof StairBlock)
		{
			if (state.getValue(StairBlock.HALF) == Half.BOTTOM) switch (state.getValue(StairBlock.FACING))
			{
			case NORTH: return EnumPlacing.UP_SOUTH;
			case SOUTH: return EnumPlacing.UP_NORTH;
			case WEST: return EnumPlacing.UP_EAST;
			case EAST: return EnumPlacing.UP_WEST;
			default: return null;
			}
			else switch (state.getValue(StairBlock.FACING))
			{
			case NORTH: return EnumPlacing.DOWN_SOUTH;
			case SOUTH: return EnumPlacing.DOWN_NORTH;
			case WEST: return EnumPlacing.DOWN_EAST;
			case EAST: return EnumPlacing.DOWN_WEST;
			default: return null;
			}
		}
		else return null;
	}
	
	public static EnumShape getShape(BlockState state)
	{
		if (state.getBlock() instanceof FEStairBlock) return state.getValue(SHAPE);
		else if (state.getBlock() instanceof StairBlock)
		{
			if (state.getValue(StairBlock.HALF) == Half.BOTTOM) switch (state.getValue(StairBlock.SHAPE))
			{
			case STRAIGHT: return EnumShape.STRAIGHT;
			case INNER_LEFT: return EnumShape.INSIDE_LEFT;
			case INNER_RIGHT: return EnumShape.INSIDE_RIGHT;
			case OUTER_LEFT: return EnumShape.OUTSIDE_HORIZONTAL_LEFT;
			case OUTER_RIGHT: return EnumShape.OUTSIDE_HORIZONTAL_RIGHT;
			default: return null;
			}
			else switch (state.getValue(StairBlock.SHAPE))
			{
			case STRAIGHT: return EnumShape.STRAIGHT;
			case INNER_LEFT: return EnumShape.INSIDE_RIGHT;
			case INNER_RIGHT: return EnumShape.INSIDE_LEFT;
			case OUTER_LEFT: return EnumShape.OUTSIDE_HORIZONTAL_RIGHT;
			case OUTER_RIGHT: return EnumShape.OUTSIDE_HORIZONTAL_LEFT;
			default: return null;
			}
		}
		else return null;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(WATERLOGGED, PLACING, SHAPE);
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
			if (hitX < -.25f)
			{
				if (hitZ < -.25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.SOUTH_EAST);
					break;
				}
				else if (hitZ > .25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.NORTH_EAST);
					break;
				}
			}
			else if (hitX > .25f)
			{
				if (hitZ < -.25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.SOUTH_WEST);
					break;
				}
				else if (hitZ > .25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.NORTH_WEST);
					break;
				}
			}
			if (hitX > hitZ)
			{
				if (hitX > -hitZ)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.DOWN_WEST);
				}
				else
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.DOWN_SOUTH);
				}
			}
			else
			{
				if (hitZ > -hitX)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.DOWN_NORTH);
				}
				else
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.DOWN_EAST);
				}
			}
			break;
		case UP:
			if (hitX < -.25f)
			{
				if (hitZ < -.25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.SOUTH_EAST);
					break;
				}
				else if (hitZ > .25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.NORTH_EAST);
					break;
				}
			}
			else if (hitX > .25f)
			{
				if (hitZ < -.25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.SOUTH_WEST);
					break;
				}
				else if (hitZ > .25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.NORTH_WEST);
					break;
				}
			}
			if (hitX > hitZ)
			{
				if (hitX > -hitZ)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.UP_WEST);
				}
				else
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.UP_SOUTH);
				}
			}
			else
			{
				if (hitZ > -hitX)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.UP_NORTH);
				}
				else
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.UP_EAST);
				}
			}
			break;
		case NORTH:
			if (hitX < -.25f)
			{
				if (hitY < -.25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.UP_EAST);
					break;
				}
				else if (hitY > .25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.DOWN_EAST);
					break;
				}
			}
			else if (hitX > .25f)
			{
				if (hitY < -.25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.UP_WEST);
					break;
				}
				else if (hitY > .25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.DOWN_WEST);
					break;
				}
			}
			if (hitX > hitY)
			{
				if (hitX > -hitY)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.NORTH_WEST);
				}
				else
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.UP_NORTH);
				}
			}
			else
			{
				if (hitY > -hitX)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.DOWN_NORTH);
				}
				else
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.NORTH_EAST);
				}
			}
			break;
		case SOUTH:
			if (hitX < -.25f)
			{
				if (hitY < -.25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.UP_EAST);
					break;
				}
				else if (hitY > .25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.DOWN_EAST);
					break;
				}
			}
			else if (hitX > .25f)
			{
				if (hitY < -.25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.UP_WEST);
					break;
				}
				else if (hitY > .25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.DOWN_WEST);
					break;
				}
			}
			if (hitX > hitY)
			{
				if (hitX > -hitY)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.SOUTH_WEST);
				}
				else
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.UP_SOUTH);
				}
			}
			else
			{
				if (hitY > -hitX)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.DOWN_SOUTH);
				}
				else
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.SOUTH_EAST);
				}
			}
			break;
		case WEST:
			if (hitY < -.25f)
			{
				if (hitZ < -.25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.UP_SOUTH);
					break;
				}
				else if (hitZ > .25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.UP_NORTH);
					break;
				}
			}
			else if (hitY > .25f)
			{
				if (hitZ < -.25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.DOWN_SOUTH);
					break;
				}
				else if (hitZ > .25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.DOWN_NORTH);
					break;
				}
			}
			if (hitZ > hitY)
			{
				if (hitZ > -hitY)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.NORTH_WEST);
				}
				else
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.UP_WEST);
				}
			}
			else
			{
				if (hitY > -hitZ)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.DOWN_WEST);
				}
				else
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.SOUTH_WEST);
				}
			}
			break;
		case EAST:
			if (hitY < -.25f)
			{
				if (hitZ < -.25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.UP_SOUTH);
					break;
				}
				else if (hitZ > .25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.UP_NORTH);
					break;
				}
			}
			else if (hitY > .25f)
			{
				if (hitZ < -.25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.DOWN_SOUTH);
					break;
				}
				else if (hitZ > .25f)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.DOWN_NORTH);
					break;
				}
			}
			if (hitZ > hitY)
			{
				if (hitZ > -hitY)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.NORTH_EAST);
				}
				else
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.UP_EAST);
				}
			}
			else
			{
				if (hitY > -hitZ)
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.DOWN_EAST);
				}
				else
				{
					blockState = blockState.setValue(PLACING, EnumPlacing.SOUTH_EAST);
				}
			}
			break;
        }
        return blockState.setValue(SHAPE, getShape(blockState, context.getLevel(), context.getClickedPos()));
	}
	
	@Override
	public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable)
	{
		EnumPlacing placing = state.getValue(PLACING);
		return model.getBlock().canSustainPlant(state, world, pos, facing, plantable) &&
				switch (state.getValue(SHAPE)) {
				case STRAIGHT -> placing.bottom == facing || placing.back == facing;
				case INSIDE_RIGHT -> placing.bottom == facing || placing.back == facing || placing.left == facing;
				case INSIDE_LEFT -> placing.bottom == facing || placing.back == facing || placing.right == facing;
				case OUTSIDE_HORIZONTAL_RIGHT, OUTSIDE_HORIZONTAL_LEFT -> placing.bottom == facing;
				case OUTSIDE_VERTICAL_RIGHT, OUTSIDE_VERTICAL_LEFT -> placing.back == facing;
				default -> false;
				};
	}
	
	@Override
	public IBlockHighlight getBlockHighlight(Player player, BlockHitResult trace)
	{
		return IBlockHighlight.STAIRS;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE_CACHE[state.getValue(PLACING).ordinal()][state.getValue(SHAPE).ordinal()];
	}

	@Override
	@Deprecated
	public FluidState getFluidState(BlockState blockState)
	{
		return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
	}
	
	@Override
	@Deprecated
	public BlockState updateShape(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos)
	{
		if (state.getValue(WATERLOGGED)) level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		state = state.setValue(SHAPE, getShape(state, level, pos));
		return super.updateShape(state, direction, otherState, level, pos, otherPos);
	}
	
	public static EnumShape getShape(BlockState state, BlockGetter level, BlockPos pos)
	{
		EnumPlacing placing = state.getValue(PLACING);
		//prioritize back, bottom, front and left, right
		BlockState behind = level.getBlockState(pos.relative(placing.back));
		EnumPlacing behindPlace = getPlacing(behind);
		if (behindPlace != null)
		{
			if (behindPlace.top == placing.top)
			{
				if (behindPlace.right == placing.front) //outside left
				{
					BlockState below = level.getBlockState(pos.relative(placing.bottom));
					EnumPlacing belowPlace = getPlacing(below);
					if (belowPlace != null)
					{
						if ((belowPlace.right == placing.top && belowPlace.front == placing.front) || (belowPlace.left == placing.top && belowPlace.top == placing.front)) return EnumShape.OUTSIDE_LEFT;
					}
					return EnumShape.OUTSIDE_HORIZONTAL_LEFT;
				}
				else if (behindPlace.left == placing.front) //outside right
				{
					BlockState below = level.getBlockState(pos.relative(placing.bottom));
					EnumPlacing belowPlace = getPlacing(below);
					if (belowPlace != null)
					{
						if ((belowPlace.left == placing.top && belowPlace.front == placing.front) || (belowPlace.right == placing.top && belowPlace.top == placing.front)) return EnumShape.OUTSIDE_RIGHT;
					}
					return EnumShape.OUTSIDE_HORIZONTAL_RIGHT;
				}
			}
			else if (behindPlace.front == placing.top)
			{
				if (behindPlace.left == placing.front) //outside left
				{
					BlockState below = level.getBlockState(pos.relative(placing.bottom));
					EnumPlacing belowPlace = getPlacing(below);
					if (belowPlace != null)
					{
						if ((belowPlace.right == placing.top && belowPlace.front == placing.front) || (belowPlace.left == placing.top && belowPlace.top == placing.front)) return EnumShape.OUTSIDE_LEFT;
					}
					return EnumShape.OUTSIDE_HORIZONTAL_LEFT;
				}
				else if (behindPlace.right == placing.front) //outside right
				{
					BlockState below = level.getBlockState(pos.relative(placing.bottom));
					EnumPlacing belowPlace = getPlacing(below);
					if (belowPlace != null)
					{
						if ((belowPlace.left == placing.top && belowPlace.front == placing.front) || (belowPlace.right == placing.top && belowPlace.top == placing.front)) return EnumShape.OUTSIDE_RIGHT;
					}
					return EnumShape.OUTSIDE_HORIZONTAL_RIGHT;
				}
			}
		}
		BlockState below = level.getBlockState(pos.relative(placing.bottom));
		EnumPlacing belowPlace = getPlacing(below);
		if (belowPlace != null)
		{
			if (belowPlace.left == placing.top)
			{
				if (belowPlace.front == placing.front) return EnumShape.OUTSIDE_VERTICAL_LEFT;
				else if (belowPlace.top == placing.front) return EnumShape.OUTSIDE_VERTICAL_RIGHT;
			}
			else if (belowPlace.right == placing.top)
			{
				if (belowPlace.top == placing.front) return EnumShape.OUTSIDE_VERTICAL_LEFT;
				else if (belowPlace.front == placing.front) return EnumShape.OUTSIDE_VERTICAL_RIGHT;
			}
		}
		BlockState front = level.getBlockState(pos.relative(placing.front));
		EnumPlacing frontPlace = getPlacing(front);
		if (frontPlace != null)
		{
			if (frontPlace.top == placing.top)
			{
				if (frontPlace.left == placing.front) return EnumShape.INSIDE_LEFT;
				else if (frontPlace.right == placing.front) return EnumShape.INSIDE_RIGHT;
			}
			else if (frontPlace.front == placing.top)
			{
				if (frontPlace.right == placing.front) return EnumShape.INSIDE_LEFT;
				else if (frontPlace.left == placing.front) return EnumShape.INSIDE_RIGHT;
			}
		}
		BlockState above = level.getBlockState(pos.relative(placing.top));
		EnumPlacing abovePlace = getPlacing(above);
		if (abovePlace != null)
		{
			if (abovePlace.left == placing.top)
			{
				if (abovePlace.front == placing.front) return EnumShape.INSIDE_LEFT;
				else if (abovePlace.top == placing.front) return EnumShape.INSIDE_RIGHT;
			}
			else if (abovePlace.right == placing.top)
			{
				if (abovePlace.top == placing.front) return EnumShape.INSIDE_LEFT;
				else if (abovePlace.front == placing.front) return EnumShape.INSIDE_RIGHT;
			}
		}
		return EnumShape.STRAIGHT;
	}
	
	//TODO mixin StairBlock.getStairsShape

    public static enum EnumPlacing implements StringRepresentable
    {
        UP_WEST("up_west", Direction.UP, Direction.WEST),
        UP_EAST("up_east", Direction.UP, Direction.EAST),
        UP_NORTH("up_north", Direction.UP, Direction.NORTH),
        UP_SOUTH("up_south", Direction.UP, Direction.SOUTH),
        DOWN_WEST("down_west", Direction.DOWN, Direction.WEST),
        DOWN_EAST("down_east", Direction.DOWN, Direction.EAST),
        DOWN_NORTH("down_north", Direction.DOWN, Direction.NORTH),
        DOWN_SOUTH("down_south", Direction.DOWN, Direction.SOUTH),
        NORTH_WEST("north_west", Direction.NORTH, Direction.WEST),
        NORTH_EAST("north_east", Direction.NORTH, Direction.EAST),
        SOUTH_WEST("south_west", Direction.SOUTH, Direction.WEST),
        SOUTH_EAST("south_east", Direction.SOUTH, Direction.EAST);

        private final String name;
        public final Direction top, bottom, front, back, left, right;

        private EnumPlacing(String name, Direction top, Direction front)
        {
            this.name = name;
            this.top = top;
            this.bottom = top.getOpposite();
            this.front = front;
            this.back = front.getOpposite();
    		Vec3i rightV = front.getNormal().cross(top.getNormal());
    		this.right = Direction.fromNormal(rightV.getX(),rightV.getY(), rightV.getZ());
    		this.left = right.getOpposite();
        }

        @Override
		public String toString()
        {
            return this.name;
        }

        @Override
		public String getSerializedName()
        {
            return this.name;
        }
    }

    public static enum EnumShape implements StringRepresentable
    {
        STRAIGHT("straight"), //normal shape (flat on 2 planes)
        INSIDE_RIGHT("inside_right"), //flat on all planes
        OUTSIDE_RIGHT("outside_right"), //no flat
        OUTSIDE_HORIZONTAL_RIGHT("outside_horizontal_right"), //flat on XZ plane or flat on right plane
        OUTSIDE_VERTICAL_RIGHT("outside_vertical_right"), //other flat
        INSIDE_LEFT("inside_left"), //flat on all planes
        OUTSIDE_LEFT("outside_left"), //no flat
        OUTSIDE_HORIZONTAL_LEFT("outside_horizontal_left"), //flat on XZ plane or flat on right plane
        OUTSIDE_VERTICAL_LEFT("outside_vertical_left"); //other flat

        private final String name;

        private EnumShape(String name)
        {
            this.name = name;
        }

        @Override
		public String toString()
        {
            return this.name;
        }

        @Override
		public String getSerializedName()
        {
            return this.name;
        }
    }
}
