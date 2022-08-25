package com.firemerald.fecore.client.gui.components;

import com.firemerald.fecore.client.gui.IComponentHolder;

public interface IComponentHolderComponent extends IComponentHolder, IComponent
{
	@Override
	public default int getComponentOffsetX()
	{
		return this.getHolder() != null ? this.getHolder().getComponentOffsetX() : 0;
	}

	public default int getComponentOffsetY()
	{
		return this.getHolder() != null ? this.getHolder().getComponentOffsetY() : 0;
	}
}