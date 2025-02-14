package com.firemerald.fecore.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;

public interface IBetterRenderer extends Renderable
{
	public abstract void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, boolean canHover);

	@Override
    public default void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
	{
		render(guiGraphics, mouseX, mouseY, partialTick, true);
	}
}
