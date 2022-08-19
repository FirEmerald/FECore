package com.firemerald.fecore.betterscreens.components.text;

import com.firemerald.fecore.betterscreens.IComponentHolder;
import com.firemerald.fecore.betterscreens.components.IInteractableComponent;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Font;
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
	public void render(PoseStack pose, int mx, int my, float partialTick, boolean canHover)
	{
		if (this.visible)
		{
			this.isHovered = canHover && mx >= this.x && my >= this.y && mx < this.x + this.width && my < this.y + this.height;
			this.renderButton(pose, mx, my, partialTick);
		}
	}

	@Override
	public int getX1()
	{
		return x - 1;
	}

	@Override
	public int getY1()
	{
		return y - 1;
	}

	@Override
	public int getX2()
	{
		return x + width + 1;
	}

	@Override
	public int getY2()
	{
		return y + height + 1;
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
		this.x = x1 + 1;
		this.y = y1 + 1;
		this.width = x2 - x1 - 2;
		this.height = y2 - y1 - 2;
	}
	
	@Override
	public boolean isFocused()
	{
		return super.isFocused();
	}
	
	public void setFocused(boolean focused)
	{
		super.setFocused(focused);
	}
}
