package com.firemerald.fecore.item;

import java.util.List;

import javax.annotation.Nullable;

import com.firemerald.fecore.selectionshapes.BoundingShape;
import com.firemerald.fecore.selectionshapes.BoundingShapeBoxPositions;
import com.firemerald.fecore.selectionshapes.BoundingShapeConfigurable;
import com.firemerald.fecore.selectionshapes.IShapeTool;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ItemShapeTool extends Item implements IShapeTool
{
	public ItemShapeTool()
	{
		super(new Properties().stacksTo(1).tab(CreativeModeTab.TAB_COMBAT)); //TODO tab
	}

	/*
	@Override
    public boolean doesSneakBypassUse(ItemStack stack, net.minecraft.world.IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
		return true;
    }
    */

	//right-click on block = add pos
	//shift-right-click on block = default action
	@SuppressWarnings("resource")
	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		Player player = context.getPlayer();
		if (player.isCrouching()) return InteractionResult.PASS;
		else if (!context.getLevel().isClientSide)
		{
			ItemStack stack = context.getItemInHand();
			CompoundTag tag = stack.getTag();
			if (tag == null) stack.setTag(tag = new CompoundTag());
			int posIndex = tag.getInt("posIndex");
			CompoundTag shapeTag = tag.getCompound("shape");
			if (shapeTag == null) tag.put("shape", shapeTag = new CompoundTag());
			BoundingShape s = BoundingShape.constructFromNBT(shapeTag);
			BoundingShapeConfigurable shape;
			boolean isNew = false;
			if (s instanceof BoundingShapeConfigurable) shape = (BoundingShapeConfigurable) s;
			else
			{
				isNew = true;
				shape = new BoundingShapeBoxPositions();
				posIndex = 0;
				if (s != null) //was invalid shape
				{
					player.sendMessage(new TranslatableComponent("fecore.shapetool.invalid", new TranslatableComponent(s.getUnlocalizedName()), new TranslatableComponent(shape.getUnlocalizedName())), Util.NIL_UUID);
				}
				else //no shape selected
				{
					player.sendMessage(new TranslatableComponent("fecore.shapetool.new", new TranslatableComponent(shape.getUnlocalizedName())), Util.NIL_UUID);
				}
			}
			if (player.isCrouching())
			{
				if (!isNew)
				{
					List<BoundingShapeConfigurable> shapes = BoundingShape.getConfigurableShapeList(shape);
					int index = shapes.indexOf(shape);
					int newIndex = (index + 1) % shapes.size();
					if (index != newIndex)
					{
						shape = shapes.get(newIndex);
						posIndex = 0;
						player.sendMessage(new TranslatableComponent("fecore.shapetool.mode", new TranslatableComponent(shape.getUnlocalizedName())), Util.NIL_UUID);
					}
				}
			}
			else posIndex = shape.addPosition(player, context.getClickedPos(), posIndex);
			shapeTag = new CompoundTag();
			shape.saveToNBT(shapeTag);
			tag.put("shape", shapeTag);
			tag.putInt("posIndex", posIndex);
		}
		return InteractionResult.SUCCESS;
	}

	//right-click empty = remove pos
	//shift-right-click on empty = change mode
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide)
		{
			CompoundTag tag = stack.getTag();
			if (tag == null) stack.setTag(tag = new CompoundTag());
			int posIndex = tag.getInt("posIndex");
			CompoundTag shapeTag = tag.getCompound("shape");
			if (shapeTag == null) tag.put("shape", shapeTag = new CompoundTag());
			BoundingShape s = BoundingShape.constructFromNBT(shapeTag);
			BoundingShapeConfigurable shape;
			boolean isNew = false;
			if (s instanceof BoundingShapeConfigurable) shape = (BoundingShapeConfigurable) s;
			else
			{
				isNew = true;
				shape = new BoundingShapeBoxPositions();
				posIndex = 0;
				if (s != null) //was invalid shape
				{
					player.sendMessage(new TranslatableComponent("fecore.shapetool.invalid", new TranslatableComponent(s.getUnlocalizedName()), new TranslatableComponent(shape.getUnlocalizedName())), Util.NIL_UUID);
				}
				else //no shape selected
				{
					player.sendMessage(new TranslatableComponent("fecore.shapetool.new", new TranslatableComponent(shape.getUnlocalizedName())), Util.NIL_UUID);
				}
			}
			if (player.isCrouching())
			{
				if (!isNew)
				{
					List<BoundingShapeConfigurable> shapes = BoundingShape.getConfigurableShapeList(shape);
					int index = shapes.indexOf(shape);
					int newIndex = (index + 1) % shapes.size();
					if (index != newIndex)
					{
						shape = shapes.get(newIndex);
						posIndex = 0;
						player.sendMessage(new TranslatableComponent("fecore.shapetool.mode", new TranslatableComponent(shape.getUnlocalizedName())), Util.NIL_UUID);
					}
				}
			}
			else posIndex = shape.removePosition(player, posIndex);
			shapeTag = new CompoundTag();
			shape.saveToNBT(shapeTag);
			tag.put("shape", shapeTag);
			tag.putInt("posIndex", posIndex);
		}
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

    @Override
    public Component getName(ItemStack stack)
    {
    	BoundingShape shape = getShape(stack);
    	return new TranslatableComponent(this.getDescriptionId(stack), shape == null ? new TranslatableComponent("shape.none") : shape.getLocalizedName());
    }

	@Override
	public boolean canAcceptShape(ItemStack stack, BoundingShape shape)
	{
		return shape instanceof BoundingShapeConfigurable;
	}

	@Override
	public void setShape(ItemStack stack, BoundingShape shape)
	{
		IShapeTool.super.setShape(stack, shape);
		stack.getTag().putInt("posIndex", 0);
	}

	@Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
		tooltip.add(new TranslatableComponent("fecore.shapetool.tooltip.add_pos"));
		tooltip.add(new TranslatableComponent("fecore.shapetool.tooltip.remove_pos"));
		tooltip.add(new TranslatableComponent("fecore.shapetool.tooltip.change_mode"));
		tooltip.add(new TranslatableComponent("fecore.shapetool.tooltip.use_block"));
		BoundingShape shape = getShape(stack);
		if (shape instanceof BoundingShapeConfigurable) ((BoundingShapeConfigurable) shape).addInformation(stack, worldIn, tooltip, flagIn);
    }
}