package com.firemerald.fecore.client.gui.components.scrolling;

import com.firemerald.fecore.client.gui.components.ComponentPane;

import net.minecraft.client.gui.GuiGraphics;

public class FullyScrollableComponentPane extends ComponentPane implements IHorizontalScrollable, IVerticalScrollable
{
	public int width = 0, height = 0;
	protected double scrollX = 0, scrollY = 0;
	protected int scrollSizeX = 0, scrollSizeY = 0;
	public HorizontalScrollBar horizontalScrollBar = null;
	public VerticalScrollBar verticalScrollBar = null;

	public FullyScrollableComponentPane(int x1, int y1, int x2, int y2)
	{
		this(x1, y1, x2, y2, 0);
	}

	public FullyScrollableComponentPane(int x1, int y1, int x2, int y2, int border)
	{
		super(x1, y1, x2, y2, border);
		setSize(x1, y1, x2, y2);
	}

	public void setScrollBar(HorizontalScrollBar scrollBar)
	{
		this.horizontalScrollBar = scrollBar;
		scrollBar.setMaxScroll();
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
		updateScrollSize();
	}

	@Override
	public void updateComponentSize()
	{
		int w = this.componentsList.stream().mapToInt(c -> c.getX2()).max().orElse(0);
		if (w < 0) w = 0;
		width = w + margin * 2;
		int h = this.componentsList.stream().mapToInt(c -> c.getY2()).max().orElse(0);
		if (h < 0) h = 0;
		height = h + margin * 2;
	}

	public void updateScrollSize()
	{
		scrollSizeX = width - getWidth();
		if (scrollSizeX < 0) scrollX = scrollSizeX = 0;
		else if (scrollX > scrollSizeX) scrollX = scrollSizeX;
		if (horizontalScrollBar != null) horizontalScrollBar.setMaxScroll();
		scrollSizeY = height - getHeight();
		if (scrollSizeY < 0) scrollY = scrollSizeY = 0;
		else if (scrollY > scrollSizeY) scrollY = scrollSizeY;
		if (verticalScrollBar != null) verticalScrollBar.setMaxScroll();
	}

	@Override
	public void preRender(GuiGraphics guiGraphics)
	{
		guiGraphics.enableScissor(ex1, ey1, ex2, ey2);
		guiGraphics.pose().pushPose();
		guiGraphics.pose().translate(ex1 - scrollX, ey1 - scrollY, 0);
	}

	@Override
	public double adjX(double x)
	{
		return x - ex1 + scrollX;
	}

	@Override
	public double adjY(double y)
	{
		return y - ey1 + scrollY;
	}

	@Override
	public int getHeight() {
		return super.getHeight();
	}

	@Override
	public int getWidth() {
		return super.getWidth();
	}

	@Override
	public int getFullWidth()
	{
		return width;
	}

	@Override
	public int getFullHeight()
	{
		return height;
	}

	@Override
	public int getMaxHorizontalScroll()
	{
		return scrollSizeX;
	}

	@Override
	public int getMaxVerticalScroll()
	{
		return scrollSizeY;
	}

	@Override
	public double getHorizontalScroll()
	{
		return scrollX;
	}

	@Override
	public double getVerticalScroll()
	{
		return scrollY;
	}

	@Override
	public void setHorizontalScroll(double scroll)
	{
		this.scrollX = scroll;
	}

	@Override
	public void setVerticalScroll(double scroll)
	{
		this.scrollY = scroll;
	}

	@Override
	public int getHolderOffsetX()
	{
		return super.getHolderOffsetX() - (int) Math.floor(scrollX);
	}

	@Override
	public int getHolderOffsetY()
	{
		return super.getHolderOffsetY() - (int) Math.floor(scrollY);
	}

	@Override
	public double mouseScrolledX(double mx, double my, double scroll)
	{
		return this.scrollHorizontal(super.mouseScrolledX(mx, my, scroll));
	}

	@Override
	public double mouseScrolledY(double mx, double my, double scroll)
	{
		return this.scrollVertical(super.mouseScrolledY(mx, my, scroll));
	}

	@Override
	public void ensureInView(int minX, int minY, int maxX, int maxY)
	{
		if (minX < scrollX) scrollX = minX;
		else if (maxX > getWidth() + scrollX) scrollX = maxX - getWidth();
		if (minY < scrollY) scrollY = minY;
		else if (maxY > getHeight() + scrollY) scrollY = maxY - getHeight();
	}
}