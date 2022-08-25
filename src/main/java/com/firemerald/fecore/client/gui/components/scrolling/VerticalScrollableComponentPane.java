package com.firemerald.fecore.client.gui.components.scrolling;

import com.firemerald.fecore.client.gui.components.ComponentPane;
import com.firemerald.fecore.client.gui.components.IInteractableComponent;
import com.mojang.blaze3d.vertex.PoseStack;

public class VerticalScrollableComponentPane extends ComponentPane implements IVerticalScrollable
{
	public double height = 0;
	protected double scrollY = 0;
	protected double scrollSizeY = 0;
	public double h = 0;
	public VerticalScrollBar verticalScrollBar = null;

	public VerticalScrollableComponentPane(int x1, int y1, int x2, int y2)
	{
		this(x1, y1, x2, y2, 0);
	}

	public VerticalScrollableComponentPane(int x1, int y1, int x2, int y2, int border)
	{
		super(x1, y1, x2, y2, border);
		setSize(x1, y1, x2, y2);
	}

	public void setScrollBar(VerticalScrollBar scrollBar)
	{
		this.verticalScrollBar = scrollBar;
		scrollBar.setMaxScroll();
	}

	@Override
	public void setSize(int x1, int y1, int x2, int y2)
	{
		super.setSize(x1, y1, x2, y2);
		h = y2 - y1;
		updateScrollSize();
	}

	@Override
	public void updateComponentSize()
	{
		int h = this.componentsList.stream().mapToInt(c -> c.getY2()).max().orElse(0);
		if (h < 0) h = 0;
		height = h + margin * 2;
	}

	public void updateScrollSize()
	{
		scrollSizeY = height - h;
		if (scrollSizeY < 0) scrollY = scrollSizeY = 0;
		else if (scrollY > scrollSizeY) scrollY = scrollSizeY;
		if (verticalScrollBar != null) verticalScrollBar.setMaxScroll();
	}

	@Override
	public void preRender(PoseStack pose)
	{
		this.setScissor(margin, margin, (x2 - x1) - (margin << 1), (y2 - y1) - (margin << 1));
		pose.pushPose();
		pose.translate(ex1, ey1 - scrollY, 0);
	}

	@Override
	public double adjY(double y)
	{
		return y - ey1 + scrollY;
	}

	@Override
	public double getHeight()
	{
		return h;
	}

	@Override
	public double getFullHeight()
	{
		return height;
	}

	@Override
	public double getMaxVerticalScroll()
	{
		return scrollSizeY;
	}

	@Override
	public double getVerticalScroll()
	{
		return scrollY;
	}

	@Override
	public void setVerticalScroll(double scroll)
	{
		this.scrollY = scroll;
	}

	@Override
	public int getComponentOffsetY()
	{
		return super.getComponentOffsetY() - (int) Math.floor(scrollY);
	}

	@Override
	public double mouseScrolledY(double mx, double my, double scroll)
	{
		return this.scrollVertical(super.mouseScrolledY(mx, my, scroll));
	}

	@Override
	public void ensureInView(IInteractableComponent component)
	{
		//double prevScrollX = scrollX, prevScrollY = scrollY;
		if (component.getY2() > h + scrollY) scrollY = component.getY2() - h;
		if (component.getY1() < scrollY) scrollY = component.getY1();
		//if (prevScrollX != scrollX || prevScrollY != scrollY)
	}
}