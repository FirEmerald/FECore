package com.firemerald.fecore.client.gui;

import com.mojang.blaze3d.platform.GlStateManager.LogicOp;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.util.ARGB;

public enum ButtonState
{
	NONE(1, 1, 1, false),
	INVERT(1, 1, 1, true),
	HOVER(.75f, .75f, 1, false),
	PUSH(.75f, .75f, .75f, true),
	DISABLED(.5f, .5f, .5f, false);

	public final float r, g, b;
	public final boolean invert;

	ButtonState(float r, float g, float b, boolean invert)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.invert = invert;
	}

	public void applyButtonEffects()
	{
		if (invert)
		{
			RenderSystem.enableColorLogicOp();
			RenderSystem.logicOp(LogicOp.COPY_INVERTED);
		}
	}

	public void removeButtonEffects()
	{
		if (invert)
		{
			RenderSystem.logicOp(LogicOp.COPY);
			RenderSystem.disableColorLogicOp();
		}
	}

	public int getColor(int r, int g, int b)
	{
		return getColor(r, g, b, 255);
	}

	public int getColor(int r, int g, int b, int a)
	{
		return invert ?
				ARGB.color(a, (int) ((1 - this.r) * r), (int) ((1 - this.g) * g), (int) ((1 - this.b) * b)) :
					ARGB.color(a, (int) (this.r * r), (int) (this.g * g), (int) (this.b * b));
	}

	public int getColorFromFloat(float r, float g, float b)
	{
		return getColorFromFloat(r, g, b, 1);
	}

	public int getColorFromFloat(float r, float g, float b, float a)
	{
		return invert ?
				ARGB.colorFromFloat(a, (1 - this.r) * r, (1 - this.g) * g, (1 - this.b) * b) :
					ARGB.colorFromFloat(a, this.r * r, this.g * g, this.b * b);
	}
}