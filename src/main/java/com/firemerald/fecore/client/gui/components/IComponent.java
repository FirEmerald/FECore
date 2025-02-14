package com.firemerald.fecore.client.gui.components;

import com.firemerald.fecore.client.gui.IBetterRenderer;
import com.firemerald.fecore.client.gui.IComponentHolder;
import com.firemerald.fecore.client.gui.screen.PopupScreen;

import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.navigation.ScreenRectangle;

public interface IComponent extends NarratableEntry, IBetterRenderer
{
	public abstract void setHolder(IComponentHolder holder);

	public abstract IComponentHolder getHolder();

	public default int getHolderOffsetX()
	{
		return getHolder() == null ? 0 : getHolder().getHolderOffsetX();
	}

	public default int getHolderOffsetY()
	{
		return getHolder() == null ? 0 : getHolder().getHolderOffsetY();
	}

	public abstract int getX1();

	public abstract int getY1();

	public abstract int getX2();

	public abstract int getY2();

	public default int getWidth() {
		return getX2() - getX1();
	}

	public default int getHeight() {
		return getY2() - getY1();
	}

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

	public default boolean isMouseOver(double x, double y)
	{
		return (x >= getX1() && y >= getY1() && x < getX2() && y < getY2());
	}

	public default void tick() {}

    default ScreenRectangle getRectangle() {
        return new ScreenRectangle(getTrueX1(), getTrueY1(), getWidth(), getHeight());
    }
}