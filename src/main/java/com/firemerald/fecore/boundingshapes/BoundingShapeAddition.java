package com.firemerald.fecore.boundingshapes;

import java.util.Collection;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.firemerald.fecore.init.FECoreBoundingShapes;
import com.mojang.serialization.MapCodec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

public class BoundingShapeAddition extends BoundingShapeCompound
{
	public static final MapCodec<BoundingShapeAddition> CODEC = makeCodec(BoundingShapeAddition::new);
	public static final StreamCodec<RegistryFriendlyByteBuf, BoundingShapeAddition> STREAM_CODEC = makeStreamCodec(BoundingShapeAddition::new);

	public BoundingShapeAddition(Collection<BoundingShape> shapes) {
		super(shapes);
	}

	public BoundingShapeAddition() {
		super();
	}

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

	@Override
	public AABB getBounds(double testerX, double testerY, double testerZ)
	{
		AABB box = null;
		for (BoundingShape shape : shapes) if (shape instanceof IBounded bounded)
		{
			AABB other = bounded.getBounds(testerX, testerY, testerZ);
			if (box == null) box = other;
			else box = box.minmax(other);
		}
		else return ALL;
		return box == null ? ALL : box;
	}

	@Override
	public BoundingShapeAddition fromNewShapes(Stream<BoundingShape> shapes) {
		return new BoundingShapeAddition(shapes.toList());
	}

	@Override
	public BoundingShapeDefinition<BoundingShapeAddition> definition() {
		return FECoreBoundingShapes.ADDITION.get();
	}
}