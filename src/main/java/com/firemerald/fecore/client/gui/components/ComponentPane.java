package com.firemerald.fecore.client.gui.components;

import com.firemerald.fecore.client.gui.IComponentHolder;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ComponentPane extends ComponentHolder
{
	protected int margin;
	protected int ex1, ey1, ex2, ey2;

	public ComponentPane(int x1, int y1, int x2, int y2, int margin)
	{
		super(x1, y1, x2, y2);
		this.setMargin(margin);
	}

	public ComponentPane(IComponentHolder parent, int x1, int y1, int x2, int y2, int margin)
	{
		super(parent, x1, y1, x2, y2);
		this.setMargin(margin);
	}

	@Override
	public int getComponentOffsetX()
	{
		return super.getComponentOffsetX() + margin;
	}

	@Override
	public int getComponentOffsetY()
	{
		return super.getComponentOffsetY() + margin;
	}

	@Override
	public void setSize(int x1, int y1, int x2, int y2)
	{
		super.setSize(x1, y1, x2, y2);
		ex1 = x1 + margin;
		ey1 = y1 + margin;
		ex2 = x2 - margin;
		ey2 = y2 - margin;
	}

	public void setMargin(int margin)
	{
		this.margin = margin;
		ex1 = x1 + margin;
		ey1 = y1 + margin;
		ex2 = x2 - margin;
		ey2 = y2 - margin;
	}

	@Override
	public boolean canHoverComponents(int mx, int my)
	{
		return (mx >= ex1 && my >= ey1 && mx < ex2 && my < ey2);
	}

	@Override
	public void preRender(PoseStack pose)
	{
		//this.setScissor(margin, margin, (x2 - x1) - (margin << 1), (y2 - y1) - (margin << 1));
		pose.pushPose();
		pose.translate(ex1, ey1, 0);
	}

	@Override
	public double adjX(double x)
	{
		return x - ex1;
	}

	@Override
	public double adjY(double y)
	{
		return y - ey1;
	}
}