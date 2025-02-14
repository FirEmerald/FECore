package com.firemerald.fecore.client.gui.components.text;

import com.firemerald.fecore.client.gui.IComponentHolder;
import com.firemerald.fecore.client.gui.components.IInteractableComponent;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class TextField extends EditBox implements IInteractableComponent
{
	public IComponentHolder holder = null;

    public TextField(Font fontrendererObj, int x, int y, int width, int height, Component message)
    {
    	super(fontrendererObj, x + 1, y + 1, width - 2, height - 2, message);
    }

	@Override
	public void render(GuiGraphics guiGraphics, int mx, int my, float partialTick, boolean canHover)
	{
		if (this.visible)
		{
			this.isHovered = canHover && this.isMouseOver(mx, my);
			this.renderWidget(guiGraphics, mx, my, partialTick);
		}
	}

	@Override
	public int getX1()
	{
		return getX() - 1;
	}

	@Override
	public int getY1()
	{
		return getY() - 1;
	}

	@Override
	public int getX2()
	{
		return getX() + width + 1;
	}

	@Override
	public int getY2()
	{
		return getY() + height + 1;
	}

	@Override
	public void setHolder(IComponentHolder holder)
	{
		this.holder = holder;
	}

	@Override
	public IComponentHolder getHolder()
	{
		return holder;
	}

	public void setSize(int x1, int y1, int x2, int y2)
	{
		setRectangle(x2 - x1, y2 - y1, x1, y1);
	}

	@Override
	public boolean isFocused()
	{
		return super.isFocused();
	}
}
