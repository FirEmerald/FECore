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

public class BoundingShapeIntersection extends BoundingShapeCompound
{
	public static final MapCodec<BoundingShapeIntersection> CODEC = makeCodec(BoundingShapeIntersection::new);
	public static final StreamCodec<RegistryFriendlyByteBuf, BoundingShapeIntersection> STREAM_CODEC = makeStreamCodec(BoundingShapeIntersection::new);

	public BoundingShapeIntersection(Collection<BoundingShape> shapes) {
		super(shapes);
	}

	public BoundingShapeIntersection() {
		super();
	}

	@Override
	public String getUnlocalizedName()
	{
		return "fecore.shape.intersection";
	}

	@Override
	public boolean isWithin(@Nullable Entity entity, double posX, double posY, double posZ, double testerX, double testerY, double testerZ)
	{
		return !shapes.isEmpty() && shapes.stream().allMatch(shape -> shape.isWithin(entity, posX, posY, posZ, testerX, testerY, testerZ));
	}

	@Override
	public AABB getBounds(double testerX, double testerY, double testerZ)
	{
		AABB box = null;
		for (BoundingShape shape : shapes) if (shape instanceof IBounded bounded)
		{
			AABB other = bounded.getBounds(testerX, testerY, testerZ);
			if (box == null) box = other;
			else box = box.intersect(other);
		}
		return box == null ? ALL : box;
	}

	@Override
	public BoundingShapeIntersection fromNewShapes(Stream<BoundingShape> shapes) {
		return new BoundingShapeIntersection(shapes.toList());
	}

	@Override
	public BoundingShapeDefinition<BoundingShapeIntersection> definition() {
		return FECoreBoundingShapes.INTERSECTION.get();
	}
}