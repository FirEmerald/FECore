package com.firemerald.fecore.client.gui.components.scrolling;

import com.firemerald.fecore.client.gui.components.ComponentPane;
import com.firemerald.fecore.client.gui.components.IInteractableComponent;
import com.mojang.blaze3d.vertex.PoseStack;

public class HorizontalScrollableComponentPane extends ComponentPane implements IHorizontalScrollable
{
	public double width = 0;
	protected double scrollX = 0;
	protected double scrollSizeX = 0;
	public double w = 0;
	public HorizontalScrollBar horizontalScrollBar = null;

	public HorizontalScrollableComponentPane(int x1, int y1, int x2, int y2)
	{
		this(x1, y1, x2, y2, 0);
	}

	public HorizontalScrollableComponentPane(int x1, int y1, int x2, int y2, int border)
	{
		super(x1, y1, x2, y2, border);
		setSize(x1, y1, x2, y2);
	}

	public void setScrollBar(HorizontalScrollBar scrollBar)
	{
		this.horizontalScrollBar = scrollBar;
		scrollBar.setMaxScroll();
	}

	@Override
	public void setSize(int x1, int y1, int x2, int y2)
	{
		super.setSize(x1, y1, x2, y2);
		w = x2 - x1;
		updateScrollSize();
	}

	@Override
	public void updateComponentSize()
	{
		int w = this.componentsList.stream().mapToInt(c -> c.getX2()).max().orElse(0);
		if (w < 0) w = 0;
		width = w + margin * 2;
	}

	public void updateScrollSize()
	{
		scrollSizeX = width - w;
		if (scrollSizeX < 0) scrollX = scrollSizeX = 0;
		else if (scrollX > scrollSizeX) scrollX = scrollSizeX;
		if (horizontalScrollBar != null) horizontalScrollBar.setMaxScroll();
	}

	@Override
	public void preRender(PoseStack pose)
	{
		this.setScissor(margin, margin, (x2 - x1) - (margin << 1), (y2 - y1) - (margin << 1));
		pose.pushPose();
		pose.translate(ex1 - scrollX, ey1, 0);
	}

	@Override
	public double adjX(double x)
	{
		return x - ex1 + scrollX;
	}

	@Override
	public double getWidth()
	{
		return w;
	}

	@Override
	public double getFullWidth()
	{
		return width;
	}

	@Override
	public double getMaxHorizontalScroll()
	{
		return scrollSizeX;
	}

	@Override
	public double getHorizontalScroll()
	{
		return scrollX;
	}

	@Override
	public void setHorizontalScroll(double scroll)
	{
		this.scrollX = scroll;
	}

	@Override
	public int getComponentOffsetX()
	{
		return super.getComponentOffsetX() - (int) Math.floor(scrollX);
	}

	@Override
	public double mouseScrolledX(double mx, double my, double scroll)
	{
		return this.scrollHorizontal(super.mouseScrolledX(mx, my, scroll));
	}

	@Override
	public void ensureInView(IInteractableComponent component)
	{
		//double prevScrollX = scrollX, prevScrollY = scrollY;
		if (component.getX2() > w + scrollX) scrollX = component.getX2() - w;
		if (component.getX1() < scrollX) scrollX = component.getX1();
		//if (prevScrollX != scrollX || prevScrollY != scrollY)
	}
}