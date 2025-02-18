package com.firemerald.fecore.client.gui;

import com.mojang.blaze3d.platform.GlStateManager.LogicOp;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.util.Mth;

public enum ButtonState
{
	NONE(new RGB(1, 1, 1), false),
	INVERT(new RGB(1, 1, 1), true),
	HOVER(new RGB(.75f, .75f, 1), false),
	PUSH(new RGB(.75f, .75f, .75f), true),
	DISABLED(new RGB(.5f, .5f, .5f), false);

	public final RGB color;
	public final boolean invert;

	ButtonState(RGB color, boolean invert)
	{
		this.color = color;
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

	public RGB getColor(RGB c)
	{
		return getColor(c.r(), c.g(), c.b());
	}

	public RGB getColor(float r, float g, float b)
	{
		return invert ?
				new RGB((1 - r) * color.r(), (1 - g) * color.g(), (1 - b) * color.b()) :
					new RGB(r * color.r(), g * color.g(), b * color.b());
	}

	public int getColorInt(RGB c)
	{
		return getColorInt(c.r(), c.g(), c.b());
	}

	public int getColorInt(float r, float g, float b)
	{
		return invert ?
				((int) Mth.clamp((1 - r) * color.r() * 255, 0, 255) << 16) | ((int) Mth.clamp((1 - g) * color.g() * 255, 0, 255) << 8) | ((int) Mth.clamp((1 - b) * color.b() * 255, 0, 255)) :
					((int) Mth.clamp(r * color.r() * 255, 0, 255) << 16) | ((int) Mth.clamp(g * color.g() * 255, 0, 255) << 8) | ((int) Mth.clamp(b * color.b() * 255, 0, 255));
	}

	public int getColorInt(RGB c, float a)
	{
		return getColorInt(c.r(), c.g(), c.b(), a);
	}

	public int getColorInt(float r, float g, float b, float a)
	{
		return ((int) Mth.clamp(a * 255, 0, 255) <<   24) | (invert ?
				((int) Mth.clamp((1 - r) * color.r() * 255, 0, 255) << 16) | ((int) Mth.clamp((1 - g) * color.g() * 255, 0, 255) << 8) | ((int) Mth.clamp((1 - b) * color.b() * 255, 0, 255)) :
					((int) Mth.clamp(r * color.r() * 255, 0, 255) << 16) | ((int) Mth.clamp(g * color.g() * 255, 0, 255) << 8) | ((int) Mth.clamp(b * color.b() * 255, 0, 255)));
	}
}