package com.firemerald.fecore.util;

public record Vec3d(double x, double y, double z)
{
	@Override
	public String toString()
	{
		return "<" + x + ", " + y + ", " + z + ">";
	}
}
