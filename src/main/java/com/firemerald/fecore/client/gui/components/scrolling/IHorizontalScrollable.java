package com.firemerald.fecore.client.gui.components.scrolling;

public interface IHorizontalScrollable extends IScrollableBase
{
	public int getWidth();

	public int getFullWidth();

	public int getMaxHorizontalScroll();

	public double getHorizontalScroll();

	public void setHorizontalScroll(double scroll);

	public default double scrollHorizontal(double scrollAmount) {
		return scroll(scrollAmount, getHorizontalScroll(), getMaxHorizontalScroll(), this::setHorizontalScroll);
	}

	public default boolean canScrollLeft() {
		return getHorizontalScroll() > 0;
	}

	public default boolean canScrollRight() {
		return getHorizontalScroll() < getMaxHorizontalScroll();
	}
}