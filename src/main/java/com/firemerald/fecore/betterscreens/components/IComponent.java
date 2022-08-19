package com.firemerald.fecore.betterscreens.components;

import com.firemerald.fecore.betterscreens.IBetterRenderer;
import com.firemerald.fecore.betterscreens.IComponentHolder;
import com.firemerald.fecore.betterscreens.PopupScreen;
import com.firemerald.fecore.betterscreens.ScissorUtil;

import net.minecraft.client.gui.narration.NarratableEntry;

public interface IComponent extends NarratableEntry, IBetterRenderer
{
	public abstract void setHolder(IComponentHolder holder);

	public abstract IComponentHolder getHolder();

	public default int getHolderOffsetX()
	{
		return getHolder() == null ? 0 : getHolder().getComponentOffsetX();
	}

	public default int getHolderOffsetY()
	{
		return getHolder() == null ? 0 : getHolder().getComponentOffsetY();
	}
	
	public abstract int getX1();

	public abstract int getY1();

	public abstract int getX2();

	public abstract int getY2();

	public default int getTrueX1()
	{
		return getHolderOffsetX() + getX1();
	}

	public default int getTrueY1()
	{
		return getHolderOffsetY() + getY1();
	}

	public default int getTrueX2()
	{
		return getHolderOffsetX() + getX2();
	}

	public default int getTrueY2()
	{
		return getHolderOffsetY() + getY2();
	}

	public default int getSelectorX1(PopupScreen selector)
	{
		return getTrueX1();
	}

	public default int getSelectorY1(PopupScreen selector)
	{
		return getTrueY1();
	}

	public default int getSelectorX2(PopupScreen selector)
	{
		return getTrueX2();
	}

	public default int getSelectorY2(PopupScreen selector)
	{
		return getTrueY2();
	}

	public default void setScissor(int offX1, int offY1, int w, int h)
	{
		int x1 = this.getTrueX1() + offX1;
		int y1 = this.getTrueY1() + offY1;
		ScissorUtil.pushScissor(x1, y1, x1 + w, y1 + h);
	}
	
	public default boolean isMouseOver(double x, double y)
	{
		return (x >= getX1() && y >= getY1() && x < getX2() && y < getY2());
	}
	
	public default void tick() {}
}