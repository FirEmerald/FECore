package com.firemerald.fecore.boundingshapes;

import javax.annotation.Nullable;

import net.minecraft.world.entity.Entity;

public class BoundingShapeAddition extends BoundingShapeCompound
{
	@Override
	public String getUnlocalizedName()
	{
		return "fecore.shape.addition";
	}

	@Override
	public boolean isWithin(@Nullable Entity entity, double posX, double posY, double posZ, double testerX, double testerY, double testerZ)
	{
		return shapes.stream().anyMatch(shape -> shape.isWithin(entity, posX, posY, posZ, testerX, testerY, testerZ));
	}
}