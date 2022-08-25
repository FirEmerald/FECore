package com.firemerald.fecore.client.gui.components.scrolling;

public interface IVerticalScrollable extends IScrollableBase
{
	public double getHeight();

	public double getFullHeight();

	public double getMaxVerticalScroll();

	public double getVerticalScroll();

	public void setVerticalScroll(double scroll);

	public default double scrollVertical(double scrollAmount)
	{
		return scroll(scrollAmount, getVerticalScroll(), getMaxVerticalScroll(), this::setVerticalScroll);
	}
}