package com.firemerald.fecore.client.gui.components.scrolling;

public interface IHorizontalScrollable extends IScrollableBase
{
	public double getWidth();

	public double getFullWidth();
	
	public double getMaxHorizontalScroll();

	public double getHorizontalScroll();

	public void setHorizontalScroll(double scroll);

	public default double scrollHorizontal(double scrollAmount)
	{
		return scroll(scrollAmount, getHorizontalScroll(), getMaxHorizontalScroll(), this::setHorizontalScroll);
	}
}