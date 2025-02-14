package com.firemerald.fecore.client.gui.components.scrolling;

public interface IVerticalScrollable extends IScrollableBase
{
	public int getHeight();

	public int getFullHeight();

	public int getMaxVerticalScroll();

	public double getVerticalScroll();

	public void setVerticalScroll(double scroll);

	public default double scrollVertical(double scrollAmount)
	{
		return scroll(scrollAmount, getVerticalScroll(), getMaxVerticalScroll(), this::setVerticalScroll);
	}

	public default boolean canScrollUp() {
		return getVerticalScroll() > 0;
	}

	public default boolean canScrollDown() {
		return getVerticalScroll() < getMaxVerticalScroll();
	}
}