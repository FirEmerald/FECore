package com.firemerald.fecore.util;

import com.firemerald.fecore.FECoreMod;

import net.minecraft.resources.ResourceLocation;

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
}