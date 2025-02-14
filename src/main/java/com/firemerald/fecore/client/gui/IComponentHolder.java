package com.firemerald.fecore.client.gui;

public interface IComponentHolder
{
	public int getHolderOffsetX();

	public int getHolderOffsetY();

	public default double adjX(double x)
	{
		return x;
	}

	public default double adjY(double y)
	{
		return y;
	}
}