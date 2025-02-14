package com.firemerald.fecore.client.gui.screen;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.Music;

public abstract class PopupScreen extends BetterScreen
{
	@Nullable
	public Screen under;
	public boolean active = false;

	public PopupScreen(Component title)
	{
		super(title);
	}

	@SuppressWarnings("resource")
	public void activate()
	{
		active = true;
		under = Minecraft.getInstance().screen;
		Minecraft.getInstance().setScreen(this);
	}

	@Override
	public void resize(Minecraft mc, int width, int height)
    {
		if (under != null) under.resize(mc, width, height);
		super.resize(mc, width, height);
    }

	public void deactivate()
	{
		active = false;
		this.minecraft.setScreen(under);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mx, int my, float partialTicks, boolean canHover)
	{
		if (under != null)
		{
			guiGraphics.pose().pushPose();
			guiGraphics.pose().translate(0, 0, -1);
			if (under instanceof BetterScreen betterUnder) betterUnder.render(guiGraphics, mx, my, partialTicks, false);
			else under.render(guiGraphics, Integer.MIN_VALUE, Integer.MIN_VALUE, partialTicks);
			guiGraphics.pose().popPose();
		}
		renderBackground(guiGraphics, mx, my, partialTicks, canHover);
		super.render(guiGraphics, mx, my, partialTicks, canHover);
	}

	public void renderBackground(GuiGraphics guiGraphics, int mx, int my, float partialTicks, boolean canHover) {
		guiGraphics.fill(0, 0, width, height, 0x7F000000);
	}

	@Override
    public Music getBackgroundMusic() {
        return under != null ? under.getBackgroundMusic() : null;
    }
}