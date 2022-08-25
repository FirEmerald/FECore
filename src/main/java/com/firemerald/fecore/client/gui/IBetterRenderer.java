package com.firemerald.fecore.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.Widget;

public interface IBetterRenderer extends Widget
{
	public abstract void render(PoseStack pose, int mx, int my, float partialTick, boolean canHover);
	
	@Override
	public default void render(PoseStack pose, int mx, int my, float partialTick)
	{
		render(pose, mx, my, partialTick, true);
	}
}
