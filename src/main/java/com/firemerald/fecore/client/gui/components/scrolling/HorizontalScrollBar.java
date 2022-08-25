package com.firemerald.fecore.client.gui.components.scrolling;

import com.firemerald.fecore.client.gui.ButtonState;
import com.firemerald.fecore.client.gui.RGB;
import com.firemerald.fecore.client.gui.components.InteractableComponent;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;

import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TranslatableComponent;

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
	public void doRender(PoseStack pose, int mx, int my, float partialTicks, boolean canHover)
	{
		fill(pose, x1, y1, x2, y2, this.focused ? 0xFFD0D0D0 : 0xFF000000);
		if (enabled)
		{
			fill(pose, x1 + 1, y1 + 1, x2 - 1, y2 - 1, 0xFF404040);
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
			RGB outerCol = state.getColor(.5f, .5f, .5f);
			RGB innerCol = state.getColor(1, 1, 1);
			float scrollX = (float) (x1 + scrollWidth * scroll / max);
			float scrollBarSize = (float) this.scrollBarSize;
	        RenderSystem.disableTexture();
			Matrix4f mat = pose.last().pose();
	        Tesselator tessellator = Tesselator.getInstance();
	        BufferBuilder bufferbuilder = tessellator.getBuilder();
	        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
	        bufferbuilder.vertex(mat, scrollX                , y2, 0).color(outerCol.r(), outerCol.g(), outerCol.b(), 1).endVertex();
	        bufferbuilder.vertex(mat, scrollX + scrollBarSize, y2, 0).color(outerCol.r(), outerCol.g(), outerCol.b(), 1).endVertex();
	        bufferbuilder.vertex(mat, scrollX + scrollBarSize, y1, 0).color(outerCol.r(), outerCol.g(), outerCol.b(), 1).endVertex();
	        bufferbuilder.vertex(mat, scrollX                , y1, 0).color(outerCol.r(), outerCol.g(), outerCol.b(), 1).endVertex();
	        tessellator.end();
	        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
	        bufferbuilder.vertex(mat, scrollX                 + 1, y2 - 1, 0).color(innerCol.r(), innerCol.g(), innerCol.b(), 1).endVertex();
	        bufferbuilder.vertex(mat, scrollX + scrollBarSize - 1, y2 - 1, 0).color(innerCol.r(), innerCol.g(), innerCol.b(), 1).endVertex();
	        bufferbuilder.vertex(mat, scrollX + scrollBarSize - 1, y1 + 1, 0).color(innerCol.r(), innerCol.g(), innerCol.b(), 1).endVertex();
	        bufferbuilder.vertex(mat, scrollX                 + 1, y1 + 1, 0).color(innerCol.r(), innerCol.g(), innerCol.b(), 1).endVertex();
	        tessellator.end();
	        RenderSystem.enableTexture();
		}
		else fill(pose, x1 + 1, y1 + 1, x2 - 1, y2 - 1, 0xFF202020);
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
			if (my >= y1 + 1 && my < y2 - 1)
			{
				double scroll = scrollable.getHorizontalScroll();
				double scrollPos = x1 + 1 + scrollWidth * scroll / scrollable.getMaxHorizontalScroll();
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
	public boolean changeFocus(boolean focused)
	{
		if (!enabled) return false;
		else if (!super.changeFocus(focused))
		{
			pressedScroll = false;
			return false;
		}
		else return true;
	}

	@Override
	public void updateNarration(NarrationElementOutput output)
	{
    	output.add(NarratedElementType.TITLE, new TranslatableComponent("narration.scrollbar.horizontal.title"));
    	if (this.enabled)
    	{
    		if (this.isFocused())
    		{
    			output.add(NarratedElementType.USAGE, new TranslatableComponent("narration.scrollbar.horizontal.usage.focused"));
    		}
    		else
    		{
    			output.add(NarratedElementType.USAGE, new TranslatableComponent("narration.scrollbar.horizontal.usage.hovered"));
    		}
    	}
	}

	@Override
	public boolean keyPressed(int key, int scancode, int mods)
	{
		if (key == InputConstants.KEY_LEFT) return this.scrollable.scrollHorizontal(1) != 0;
		else if (key == InputConstants.KEY_RIGHT) return this.scrollable.scrollHorizontal(-1) != 0;
		else return false;
	}
}