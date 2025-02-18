package com.firemerald.fecore.client.gui.components.scrolling;

import com.firemerald.fecore.client.gui.ButtonState;
import com.firemerald.fecore.client.gui.components.InteractableComponent;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class HorizontalScrollBar extends InteractableComponent
{
	private double scrollSize;
	protected double scrollWidth;
	protected double scrollBarSize;
	public boolean enabled = false;
	public final IHorizontalScrollable scrollable;
	private double pX = 0;
	protected boolean pressedScroll = false;
	private double pressedScrollVal;

	public HorizontalScrollBar(int x1, int y1, int x2, int y2, IHorizontalScrollable scrollable)
	{
		super(x1, y1, x2, y2);
		this.scrollable = scrollable;
		setSize(x1, y1, x2, y2);
	}

	@Override
	public void setSize(int x1, int y1, int x2, int y2)
	{
		super.setSize(x1, y1, x2, y2);
		scrollSize = x2 - x1;
		setMaxScroll();
	}

	public void setMaxScroll()
	{
		double size = scrollable.getMaxHorizontalScroll();
		if (size <= 0)
		{
			enabled = false;
		}
		else
		{
			enabled = true;
			scrollBarSize = scrollSize * scrollable.getWidth() / scrollable.getFullWidth();
			if (scrollBarSize < 10) scrollBarSize = 10;
			scrollWidth = scrollSize - scrollBarSize;
		}
	}

	@Override
	public void doRender(GuiGraphics guiGraphics, int mx, int my, float partialTicks, boolean canHover)
	{
		int x1 = getX1(), y1 = getY1(), x2 = getX2(), y2 = getY2();
		guiGraphics.fill(x1, y1, x2, y2, this.isFocused() ? 0xFFD0D0D0 : 0xFF000000);
		if (enabled)
		{
			guiGraphics.fill(x1 + 1, y1 + 1, x2 - 1, y2 - 1, 0xFF404040);
			double scroll = scrollable.getHorizontalScroll();
			double max = scrollable.getMaxHorizontalScroll();
			ButtonState state;
			if (pressedScroll) state = ButtonState.PUSH;
			else
			{
				if (canHover && my >= y1 + 1 && my < y2 - 1)
				{
					double scrollPos = x1 + 1 + scrollWidth * scroll / max;
					if (mx >= scrollPos && mx < scrollPos + scrollBarSize) state = ButtonState.HOVER;
					else state = ButtonState.NONE;
				}
				else state = ButtonState.NONE;
			}
			double scrollX = (x1 + scrollWidth * scroll / max);
			int scrollX1 = (int) scrollX;
			int scrollX2 = (int) (scrollX + scrollBarSize);
			guiGraphics.fill(scrollX1, y1, scrollX2, y2, 0xFF000000 | state.getColorInt(.5f, .5f, .5f));
			guiGraphics.fill(scrollX1 + 1, y1 + 1, scrollX2 - 1, y2 - 1, 0xFF000000 | state.getColorInt(.9f, .9f, .9f));
		}
		else guiGraphics.fill(x1 + 1, y1 + 1, x2 - 1, y2 - 1, 0xFF202020);
	}

	@Override
	public double mouseScrolledX(double mx, double my, double scroll)
	{
		return scrollable.scrollHorizontal(scroll);
	}

	@Override
	public boolean onMouseClicked(double mx, double my, int button)
	{
		if (button == 0)
		{
			pX = mx;
			if (my >= getY1() + 1 && my < getY2() - 1)
			{
				double scroll = scrollable.getHorizontalScroll();
				double scrollPos = getX1() + 1 + scrollWidth * scroll / scrollable.getMaxHorizontalScroll();
				if (mx >= scrollPos && mx < scrollPos + scrollBarSize)
				{
					pressedScroll = true;
					pressedScrollVal = scroll;
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean mouseDragged(double mx, double my, int button, double dx, double dy)
	{
		if (button == 0 && pressedScroll)
		{
			double dX = mx - pX;
			double max = scrollable.getMaxHorizontalScroll();
			double scroll = pressedScrollVal + (dX * max / scrollWidth);
			if (scroll < 0) scroll = 0;
			else if (scroll > max) scroll = max;
			scrollable.setHorizontalScroll(scroll);
			return true;
		}
		else return false;
	}

	@Override
	public boolean mouseReleased(double mx, double my, int button)
	{
		if (button == 0 && pressedScroll)
		{
			pressedScroll = false;
			return true;
		}
		else return false;
	}

	@Override
	public void setFocused(boolean focused) {
		super.setFocused(focused);
		if (!focused) pressedScroll = false;
	}

	@Override
	public void updateWidgetNarration(NarrationElementOutput output)
	{
    	output.add(NarratedElementType.TITLE, Component.translatable("narration.scrollbar.horizontal.title"));
    	if (this.enabled)
    	{
    		if (this.isFocused())
    		{
    			output.add(NarratedElementType.USAGE, Component.translatable("narration.scrollbar.horizontal.usage.focused"));
    		}
    		else
    		{
    			output.add(NarratedElementType.USAGE, Component.translatable("narration.scrollbar.horizontal.usage.hovered"));
    		}
    	}
	}

	@Override
	public boolean keyPressed(int key, int scancode, int mods)
	{
		if (key == InputConstants.KEY_LEFT) return this.scrollable.scrollHorizontal(1) != 1;
		else if (key == InputConstants.KEY_RIGHT) return this.scrollable.scrollHorizontal(-1) != -1;
		else return false;
	}

	@Override
	public Component getMessage() {
		return null;
	}
}