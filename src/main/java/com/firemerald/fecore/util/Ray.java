package com.firemerald.fecore.util;

import net.minecraft.world.phys.Vec3;

public class Ray
{
	public static final Ray NONE = new Ray(Vec3.ZERO, Vec3.ZERO);

	public final Vec3 from, direction;

	public Ray(double x, double y, double z, double dX, double dY, double dZ)
	{
		this(new Vec3(x, y, z), new Vec3(dX, dY, dZ));
	}

	public Ray(Vec3 from, Vec3 direction)
	{
		this.from = from;
		this.direction = direction;
	}
}