package com.firemerald.fecore.betterscreens.components;

import net.minecraft.client.gui.components.events.GuiEventListener;

public interface IInteractableComponent extends GuiEventListener, IComponent
{
	@Override
	public default boolean mouseScrolled(double mx, double my, double scroll)
	{
		return mouseScrolledY(mx, my, scroll) != scroll;
	}
	
	/**
	 * @param mx Mouse X
	 * @param my Mouse Y
	 * @param scrollX scrollX amount
	 * @return the remaining amount of scrolling to be done
	 */
	public default double mouseScrolledX(double mx, double my, double scroll)
	{
		return scroll;
	}

	/**
	 * @param mx Mouse X
	 * @param my Mouse Y
	 * @param scrollX scrollX amount
	 * @return the remaining amount of scrolling to be done
	 */
	public default double mouseScrolledY(double mx, double my, double scroll)
	{
		return scroll;
	}
	
	@Override
	default boolean mouseClicked(double mx, double my, int button)
	{
		return this.isMouseOver(mx, my) && onMouseClicked(mx, my, button);
	}

	default boolean onMouseClicked(double mx, double my, int button)
	{
		return false;
	}
	
	public boolean isFocused();
	
	public void setFocused(boolean focused);

	@Override
	default boolean changeFocus(boolean initial)
	{
		boolean focused = !this.isFocused();
		this.setFocused(focused);
		return focused;
	}

	@Override
	default boolean isMouseOver(double x, double y)
	{
		return IComponent.super.isMouseOver(x, y);
	}
}