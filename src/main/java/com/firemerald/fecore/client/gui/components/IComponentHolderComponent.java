package com.firemerald.fecore.client.gui.components;

import com.firemerald.fecore.client.gui.IComponentHolder;

public interface IComponentHolderComponent extends IComponentHolder, IComponent
{
	@Override
	public default int getHolderOffsetX()
	{
		return this.getX1() + IComponent.super.getHolderOffsetX();
	}

	@Override
	public default int getHolderOffsetY()
	{
		return this.getY1() + IComponent.super.getHolderOffsetY();
	}

	@Override
	public default double adjX(double x)
	{
		return x - getX1();
	}

	@Override
	public default double adjY(double y)
	{
		return y - getY1();
	}
}