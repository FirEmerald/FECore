package com.firemerald.fecore.client.gui.components.scrolling;

import com.firemerald.fecore.client.gui.components.ComponentPane;

import net.minecraft.client.gui.GuiGraphics;

public class HorizontalScrollableComponentPane extends ComponentPane implements IHorizontalScrollable
{
	public int width = 0;
	protected double scrollX = 0;
	protected int scrollSizeX = 0;
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
		scrollSizeX = width - getWidth();
		if (scrollSizeX < 0) scrollX = scrollSizeX = 0;
		else if (scrollX > scrollSizeX) scrollX = scrollSizeX;
		if (horizontalScrollBar != null) horizontalScrollBar.setMaxScroll();
	}

	@Override
	public void preRender(GuiGraphics guiGraphics)
	{
		guiGraphics.enableScissor(ex1, ey1, ex2, ey2);
		guiGraphics.pose().pushPose();
		guiGraphics.pose().translate(ex1 - scrollX, ey1, 0);
	}

	@Override
	public double adjX(double x)
	{
		return x - ex1 + scrollX;
	}

	@Override
	public int getWidth()
	{
		return super.getWidth();
	}

	@Override
	public int getFullWidth()
	{
		return width;
	}

	@Override
	public int getMaxHorizontalScroll()
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
	public int getHolderOffsetX()
	{
		return super.getHolderOffsetX() - (int) Math.floor(scrollX);
	}

	@Override
	public double mouseScrolledX(double mx, double my, double scroll)
	{
		return this.scrollHorizontal(super.mouseScrolledX(mx, my, scroll));
	}

	@Override
	public void ensureInView(int minX, int minY, int maxX, int maxY)
	{
		if (minX < scrollX) scrollX = minX;
		else if (maxX > getWidth() + scrollX) scrollX = maxX - getWidth();
	}
}