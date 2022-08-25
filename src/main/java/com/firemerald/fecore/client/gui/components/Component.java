package com.firemerald.fecore.client.gui.components;

import com.firemerald.fecore.client.gui.IComponentHolder;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class Component extends GuiComponent implements IComponent
{
	public int x1, y1, x2, y2;
	private IComponentHolder holder;
	private boolean visible = true;
	private boolean hovered = false;

	public Component(int x1, int y1, int x2, int y2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public Component(IComponentHolder parent, int x1, int y1, int x2, int y2)
	{
		this.holder = parent;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public void setSize(int x1, int y1, int x2, int y2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	@Override
	public int getX1()
	{
		return x1;
	}

	@Override
	public int getY1()
	{
		return y1;
	}

	@Override
	public int getX2()
	{
		return x2;
	}

	@Override
	public int getY2()
	{
		return y2;
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
	
	public boolean isVisible()
	{
		return visible;
	}
	
	public void setVisible(boolean visible)
	{
		if (!(this.visible = visible)) hovered = false;
	}
	
	public void render(PoseStack pose, int mx, int my, float partialTicks, boolean canHover)
	{
		if (this.isVisible())
		{
			this.hovered = canHover && isMouseOver(mx, my);
			doRender(pose, mx, my, partialTicks, canHover);
		}
		else this.hovered = false;
	}
	
	public abstract void doRender(PoseStack pose, int mx, int my, float partialTicks, boolean canHover);
	
	public boolean isHovered()
	{
		return hovered;
	}
	
	public void setHovered(boolean hovered)
	{
		this.hovered = hovered;
	}
	
	@Override
	public NarratableEntry.NarrationPriority narrationPriority()
	{
		return this.isHovered() ? NarratableEntry.NarrationPriority.HOVERED : NarratableEntry.NarrationPriority.NONE;
	}
}