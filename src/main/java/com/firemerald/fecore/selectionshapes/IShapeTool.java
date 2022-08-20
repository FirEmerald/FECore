package com.firemerald.fecore.selectionshapes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface IShapeTool
{
	public default BoundingShape getShape(ItemStack stack)
	{
		CompoundTag tag = stack.getTag();
		if (tag != null)
		{
			CompoundTag shapeTag = tag.getCompound("shape");
			if (shapeTag != null)
			{
				return BoundingShape.constructFromNBT(shapeTag);
			}
		}
		return null;
	}

	public boolean canAcceptShape(ItemStack stack, BoundingShape shape);

	public default void setShape(ItemStack stack, BoundingShape shape)
	{
		CompoundTag tag = stack.getTag();
		if (tag == null) stack.setTag(tag = new CompoundTag());
		CompoundTag shapeTag = new CompoundTag();
		shape.saveToNBT(shapeTag);
		tag.put("shape", shapeTag);
	}
}