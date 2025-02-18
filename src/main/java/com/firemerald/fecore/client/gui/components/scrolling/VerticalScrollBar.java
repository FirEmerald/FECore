package com.firemerald.fecore.client.gui.components.scrolling;

import com.firemerald.fecore.client.gui.ButtonState;
import com.firemerald.fecore.client.gui.components.InteractableComponent;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class VerticalScrollBar extends InteractableComponent
{
	private double scrollSize;
	protected double scrollHeight;
	protected double scrollBarSize;
	public boolean enabled = false;
	public final IVerticalScrollable scrollable;
	private double pY = 0;
	protected boolean pressedScroll = false;
	private double pressedScrollVal;

	public VerticalScrollBar(int x1, int y1, int x2, int y2, IVerticalScrollable scrollable)
	{
		super(x1, y1, x2, y2);
		this.scrollable = scrollable;
		setSize(x1, y1, x2, y2);
	}

	@Override
	public void setSize(int x1, int y1, int x2, int y2)
	{
		super.setSize(x1, y1, x2, y2);
		scrollSize = y2 - y1;
		setMaxScroll();
	}

	public void setMaxScroll()
	{
		double size = scrollable.getMaxVerticalScroll();
		if (size <= 0)
		{
			enabled = false;
		}
		else
		{
			enabled = true;
			scrollBarSize = scrollSize * scrollable.getHeight() / scrollable.getFullHeight();
			if (scrollBarSize < 10) scrollBarSize = 10;
			scrollHeight = scrollSize - scrollBarSize;
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
			double scroll = scrollable.getVerticalScroll();
			double max = scrollable.getMaxVerticalScroll();
			ButtonState state;
			if (pressedScroll) state = ButtonState.PUSH;
			else
			{
				if (canHover && mx >= x1 + 1 && mx < x2 - 1)
				{
					double scrollPos = y1 + 1 + scrollHeight * scroll / max;
					if (my >= scrollPos && my < scrollPos + scrollBarSize) state = ButtonState.HOVER;
					else state = ButtonState.NONE;
				}
				else state = ButtonState.NONE;
			}
			double scrollY = (y1 + scrollHeight * scroll / max);
			int scrollY1 = (int) scrollY;
			int scrollY2 = (int) (scrollY + scrollBarSize);
			guiGraphics.fill(x1, scrollY1, x2, scrollY2, 0xFF000000 | state.getColorInt(.5f, .5f, .5f));
			guiGraphics.fill(x1 + 1, scrollY1 + 1, x2 - 1, scrollY2 - 1, 0xFF000000 | state.getColorInt(.9f, .9f, .9f));
		}
		else guiGraphics.fill(x1 + 1, y1 + 1, x2 - 1, y2 - 1, 0xFF202020);
	}

	@Override
	public double mouseScrolledY(double mx, double my, double scroll)
	{
		return scrollable.scrollVertical(scroll);
	}

	@Override
	public boolean onMouseClicked(double mx, double my, int button)
	{
		if (button == 0)
		{
			pY = my;
			if (mx >= getX1() + 1 && mx < getX2() - 1)
			{
				double scroll = scrollable.getVerticalScroll();
				double scrollPos = getY1() + 1 + scrollHeight * scroll / scrollable.getMaxVerticalScroll();
				if (my >= scrollPos && my < scrollPos + scrollBarSize)
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
			double dY = my - pY;
			double max = scrollable.getMaxVerticalScroll();
			double scroll = pressedScrollVal + (dY * max / scrollHeight);
			if (scroll < 0) scroll = 0;
			else if (scroll > max) scroll = max;
			scrollable.setVerticalScroll(scroll);
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
    	output.add(NarratedElementType.TITLE, net.minecraft.network.chat.Component.translatable("narration.scrollbar.vertical.title"));
    	if (this.enabled)
    	{
    		if (this.isFocused())
    		{
    			output.add(NarratedElementType.USAGE, net.minecraft.network.chat.Component.translatable("narration.scrollbar.vertical.usage.focused"));
    		}
    		else
    		{
    			output.add(NarratedElementType.USAGE, net.minecraft.network.chat.Component.translatable("narration.scrollbar.vertical.usage.hovered"));
    		}
    	}
	}

	@Override
	public boolean keyPressed(int key, int scancode, int mods)
	{
		if (key == InputConstants.KEY_UP) return this.scrollable.scrollVertical(1) != 1;
		else if (key == InputConstants.KEY_DOWN) return this.scrollable.scrollVertical(-1) != -1;
		else return false;
	}

	@Override
	public Component getMessage() {
		return null;
	}
}