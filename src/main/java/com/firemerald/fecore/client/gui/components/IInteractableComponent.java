package com.firemerald.fecore.client.gui.components;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.resources.ResourceLocation;

public interface IInteractableComponent extends GuiEventListener, IComponent
{
	@Override
	public default boolean mouseScrolled(double mx, double my, double scrollY) {
		double remainingY = mouseScrolledY(mx, my, scrollY);
		return remainingY != scrollY;
	}

	/**
	 * @param mx Mouse X
	 * @param my Mouse Y
	 * @param scrollX scrollX amount
	 * @return the remaining amount of scrolling to be done
	 */
	public default double mouseScrolledX(double mx, double my, double scroll) {
		return scroll;
	}

	/**
	 * @param mx Mouse X
	 * @param my Mouse Y
	 * @param scrollX scrollX amount
	 * @return the remaining amount of scrolling to be done
	 */
	public default double mouseScrolledY(double mx, double my, double scroll) {
		return scroll;
	}

	@Override
	default boolean mouseClicked(double mx, double my, int button) {
		return this.isMouseOver(mx, my) && onMouseClicked(mx, my, button);
	}

	default boolean onMouseClicked(double mx, double my, int button) {
		return false;
	}

	@Override
	default boolean isMouseOver(double x, double y) {
		return IComponent.super.isMouseOver(x, y);
	}

	@Override
	default ScreenRectangle getRectangle() {
		return IComponent.super.getRectangle();
	}

    public default ResourceLocation getTexture(WidgetSprites sprites, boolean hovered) {
        return sprites.get(this.renderAsActive(hovered), this.renderAsFocused(hovered));
    }

    public default boolean renderAsActive(boolean hovered) {
    	return this.isActive();
    }

    public default boolean renderAsFocused(boolean hovered) {
    	return this.isFocused() || hovered;
    }
}