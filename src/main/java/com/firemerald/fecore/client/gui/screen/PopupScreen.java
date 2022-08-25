package com.firemerald.fecore.client.gui.screen;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

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
	public void render(PoseStack pose, int mx, int my, float partialTicks, boolean canHover)
	{
		if (under != null)
		{
			if (under instanceof BetterScreen) ((BetterScreen) under).render(pose, mx, my, partialTicks, false);
			else under.render(pose, -1000, -1000, partialTicks);
		}
		pose.pushPose();
		pose.translate(0, 0, 300);
		renderBackground(pose, mx, my, partialTicks, canHover);
		super.render(pose, mx, my, partialTicks, canHover);
		pose.popPose();
	}

	public void renderBackground(PoseStack pose, int mx, int my, float partialTicks, boolean canHover) {}
}