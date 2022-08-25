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
	public void doRender(PoseStack pose, int mx, int my, float partialTicks, boolean canHover)
	{
		fill(pose, x1, y1, x2, y2, this.focused ? 0xFFD0D0D0 : 0xFF000000);
		if (enabled)
		{
			fill(pose, x1 + 1, y1 + 1, x2 - 1, y2 - 1, 0xFF404040);
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
			RGB outerCol = state.getColor(.5f, .5f, .5f);
			RGB innerCol = state.getColor(.9f, .9f, .9f);
			float scrollY = (float) (y1 + scrollHeight * scroll / max);
			float scrollBarSize = (float) this.scrollBarSize;
	        RenderSystem.disableTexture();
			Matrix4f mat = pose.last().pose();
	        Tesselator tessellator = Tesselator.getInstance();
	        BufferBuilder bufferbuilder = tessellator.getBuilder();
	        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
	        bufferbuilder.vertex(mat, x1, scrollY + scrollBarSize, 0).color(outerCol.r(), outerCol.g(), outerCol.b(), 1).endVertex();
	        bufferbuilder.vertex(mat, x2, scrollY + scrollBarSize, 0).color(outerCol.r(), outerCol.g(), outerCol.b(), 1).endVertex();
	        bufferbuilder.vertex(mat, x2, scrollY                , 0).color(outerCol.r(), outerCol.g(), outerCol.b(), 1).endVertex();
	        bufferbuilder.vertex(mat, x1, scrollY                , 0).color(outerCol.r(), outerCol.g(), outerCol.b(), 1).endVertex();
	        tessellator.end();
	        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
	        bufferbuilder.vertex(mat, x1 + 1, scrollY + scrollBarSize - 1, 0).color(innerCol.r(), innerCol.g(), innerCol.b(), 1).endVertex();
	        bufferbuilder.vertex(mat, x2 - 1, scrollY + scrollBarSize - 1, 0).color(innerCol.r(), innerCol.g(), innerCol.b(), 1).endVertex();
	        bufferbuilder.vertex(mat, x2 - 1, scrollY                 + 1, 0).color(innerCol.r(), innerCol.g(), innerCol.b(), 1).endVertex();
	        bufferbuilder.vertex(mat, x1 + 1, scrollY                 + 1, 0).color(innerCol.r(), innerCol.g(), innerCol.b(), 1).endVertex();
	        tessellator.end();
	        RenderSystem.enableTexture();
		}
		else fill(pose, x1 + 1, y1 + 1, x2 - 1, y2 - 1, 0xFF202020);
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
			if (mx >= x1 + 1 && mx < x2 - 1)
			{
				double scroll = scrollable.getVerticalScroll();
				double scrollPos = y1 + 1 + scrollHeight * scroll / scrollable.getMaxVerticalScroll();
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
    	output.add(NarratedElementType.TITLE, new TranslatableComponent("narration.scrollbar.vertical.title"));
    	if (this.enabled)
    	{
    		if (this.isFocused())
    		{
    			output.add(NarratedElementType.USAGE, new TranslatableComponent("narration.scrollbar.vertical.usage.focused"));
    		}
    		else
    		{
    			output.add(NarratedElementType.USAGE, new TranslatableComponent("narration.scrollbar.vertical.usage.hovered"));
    		}
    	}
	}

	@Override
	public boolean keyPressed(int key, int scancode, int mods)
	{
		if (key == InputConstants.KEY_UP) return this.scrollable.scrollVertical(1) != 0;
		else if (key == InputConstants.KEY_DOWN) return this.scrollable.scrollVertical(-1) != 0;
		else return false;
	}
}