package com.firemerald.fecore.client.gui.components.scrolling;

import com.firemerald.fecore.client.gui.components.ComponentPane;

import net.minecraft.client.gui.GuiGraphics;

public class VerticalScrollableComponentPane extends ComponentPane implements IVerticalScrollable
{
	public int height = 0;
	protected double scrollY = 0;
	protected int scrollSizeY = 0;
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
		guiGraphics.pose().translate(ex1, ey1 - scrollY, 0);
	}

	@Override
	public double adjY(double y)
	{
		return y - ey1 + scrollY;
	}

	@Override
	public int getHeight()
	{
		return super.getHeight();
	}

	@Override
	public int getFullHeight()
	{
		return height;
	}

	@Override
	public int getMaxVerticalScroll()
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
	public int getHolderOffsetY()
	{
		return super.getHolderOffsetY() - (int) Math.floor(scrollY);
	}

	@Override
	public double mouseScrolledY(double mx, double my, double scroll)
	{
		return this.scrollVertical(super.mouseScrolledY(mx, my, scroll));
	}

	@Override
	public void ensureInView(int minX, int minY, int maxX, int maxY)
	{
		if (minY < scrollY) scrollY = minY;
		else if (maxY > getHeight() + scrollY) scrollY = maxY - getHeight();
	}
}