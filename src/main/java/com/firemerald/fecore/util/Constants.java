package com.firemerald.fecore.util;

import com.firemerald.fecore.FECoreMod;
import com.mojang.math.Vector3f;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

public class Constants
{
	public static final ResourceLocation SHAPE_HOLDER_CAP_NAME = new ResourceLocation(FECoreMod.MOD_ID, "shape_holder");
	public static final ResourceLocation SHAPE_TOOL_CAP_NAME = new ResourceLocation(FECoreMod.MOD_ID, "shape_tool");
	public static final double
	PI = Math.PI,
	TAU = 2 * PI,
	HALF_PI = .5 * PI,
	DEG_TO_RAD = PI / 180,
	RAD_TO_DEG = 180 / PI;
	public static final float
	PI_F = (float) Math.PI,
	TAU_F = (float) TAU,
	HALF_PI_F = (float) HALF_PI,
	DEG_TO_RAD_F = (float) DEG_TO_RAD,
	RAD_TO_DEG_F = (float) RAD_TO_DEG;

	public static final int CIRCLE_SEGMENTS = 32;
	public static final int HALF_CIRCLE_SEGMENTS = CIRCLE_SEGMENTS >> 1;

	public static final Vec2[] CIRCLE_MESH_CACHE = new Vec2[CIRCLE_SEGMENTS + 1];
	public static final Vector3f[][] SPHERE_MESH_CACHE = new Vector3f[HALF_CIRCLE_SEGMENTS - 1][CIRCLE_SEGMENTS + 1];
	
	static
	{
		for (int i = 0; i <= CIRCLE_SEGMENTS; ++i)
		{
			float a = i * Constants.TAU_F / CIRCLE_SEGMENTS;
			CIRCLE_MESH_CACHE[i] = new Vec2(Mth.cos(a), Mth.sin(a));
		}
		for (int j = 1; j < HALF_CIRCLE_SEGMENTS; ++j) //we don't need top and bottom as they are single points
		{
			float a2 = j * Constants.PI_F / HALF_CIRCLE_SEGMENTS;
			float y = -Mth.cos(a2);
			float r = Mth.sin(a2);
			Vector3f[] cache = SPHERE_MESH_CACHE[j - 1];
			for (int i = 0; i <= CIRCLE_SEGMENTS; ++i)
			{
				float a = i * Constants.TAU_F / CIRCLE_SEGMENTS;
				cache[i] = new Vector3f(r * Mth.cos(a), y, r * Mth.sin(a));
			}
		}
	}
}