package com.firemerald.fecore.client.gui.components.decoration;

import com.firemerald.fecore.client.gui.EnumTextAlignment;
import com.firemerald.fecore.client.gui.ScissorUtil;
import com.firemerald.fecore.client.gui.components.InteractableComponent;
import com.mojang.blaze3d.platform.GlStateManager.LogicOp;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;

public class FloatingText extends InteractableComponent
{
	protected String text = "";
	public int clickPos = 0;
	public int selStart = 0, selEnd = 0;
	public Font font;
	protected float selX1, selX2;
	public float clickTime = 0;
	public int clickNum = 0;
	private EnumTextAlignment alignment = EnumTextAlignment.LEFT;
	protected int offset = 0;
	public int color = 0xE0E0E0, focusedColor = 0xFFFFE0;

	public FloatingText(int x1, int y1, int x2, int y2, Font font, String text, EnumTextAlignment alignment)
	{
		this(x1, y1, x2, y2, font);
		this.alignment = alignment;
		setText(text);
	}

	public FloatingText(int x1, int y1, int x2, int y2, Font font, String text)
	{
		this(x1, y1, x2, y2, font, text, EnumTextAlignment.LEFT);
	}

	public FloatingText(int x1, int y1, int x2, int y2, Font font)
	{
		super(x1, y1, x2, y2);
		this.font = font;
	}

	@Override
	public void setSize(int x1, int y1, int x2, int y2)
	{
		super.setSize(x1, y1, x2, y2);
		updateOffset();
		updatePos();
	}

	@Override
	public void updateNarration(NarrationElementOutput output)
	{
		output.add(NarratedElementType.TITLE, text);
	}

	protected void updateOffset()
	{
		switch (alignment)
		{
		case CENTER:
			offset = ((x2 - x1) - font.width(text)) / 2;
			break;
		case RIGHT:
			offset = (x2 - x1) - font.width(text) - 2;
			break;
		case LEFT:
		default:
			offset = 2;
			break;
		}
	}

	protected void updatePos()
	{
		selX1 = x1 + font.width(text.substring(0, selStart));
		selX2 = x1 + font.width(text.substring(0, selEnd));
		clickNum = 0;
		clickTime = 0;
	}

	protected void updatePos2()
	{
		selX1 = x1 + font.width(text.substring(0, selStart));
		selX2 = x1 + font.width(text.substring(0, selEnd));
	}

	public void setText(String text)
	{
		this.text = text;
		selStart = selEnd = 0;
		updateOffset();
	}

	public String getText()
	{
		return text;
	}

	@Override
	public boolean onMouseClicked(double mx, double my, int button)
	{
		mx -= offset;
		if (button == 0)
		{
			int clicked = font.plainSubstrByWidth(text, (int) Math.floor(mx - x1)).length();
			if (clicked == clickPos && clickTime > 0)
			{
				if (clickNum == 0)
				{
					selStart = getBeforePreviousSymbol(clicked);
					selEnd = getNextSymbol(clicked);
					updatePos2();
					clickNum = 1;
				}
				else if (clickNum == 1)
				{
					selStart = 0;
					selEnd = text.length();
					updatePos2();
					clickNum = 2;
				}
				else
				{
					clickPos = selStart = selEnd = clicked;
					updatePos();
				}
			}
			else
			{
				clickPos = selStart = selEnd = clicked;
				updatePos();
			}
			clickTime = .5f;
			return true;
		}
		else return false;
	}

	public int getBeforePreviousSymbol(int pos)
	{
		if (pos <= 0) return 0;
		else if (pos >= text.length()) return text.length();
		else
		{
			for (int i = pos; i >= 0; i--) if (!Character.isLetterOrDigit(text.charAt(i))) return i + 1;
			return 0;
		}
	}

	public int getNextSymbol(int pos)
	{
		if (pos >= text.length()) return text.length();
		else
		{
			for (int i = pos; i < text.length(); i++) if (!Character.isLetterOrDigit(text.charAt(i))) return i;
			return text.length();
		}
	}

	@Override
	public boolean mouseDragged(double mx, double my, int button, double dx, double dy)
	{
		mx -= offset;
		if (button == 0)
		{
			int pos = font.plainSubstrByWidth(text, (int) Math.floor(mx - x1)).length();
			if (pos == clickPos) selStart = selEnd = pos;
			else if (pos < clickPos)
			{
				selStart = pos;
				selEnd = clickPos;
				clickTime = 0;
			}
			else if (pos > clickPos)
			{
				selStart = clickPos;
				selEnd = pos;
				clickTime = 0;
			}
			updatePos();
			return true;
		}
		else return false;
	}

	@Override
	public void tick()
	{
		if (clickTime > 0 && (clickTime -= .05f) < 0) clickTime = 0;
	}

	@Override
	public void doRender(PoseStack pose, int mx, int my, float partialTicks, boolean canHover)
	{
		this.setScissor(0, 0, x2 - x1, y2 - y1);
		font.draw(pose, text, x1 + offset, y1 + (y2 - y1 - 8) / 2, this.focused ? focusedColor : color);
		if (selStart != selEnd)
		{
			Matrix4f mat = pose.last().pose();
			Tesselator tessellator = Tesselator.getInstance();
	        BufferBuilder bufferbuilder = tessellator.getBuilder();
	        RenderSystem.disableTexture();
	        RenderSystem.enableColorLogicOp();
	        RenderSystem.logicOp(LogicOp.OR_REVERSE);
	        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
	        bufferbuilder.vertex(mat, selX1 + offset, y2 - 2, 0).color(0f, 0f, 1f, 1f).endVertex();
	        bufferbuilder.vertex(mat, selX2 + offset, y2 - 2, 0).color(0f, 0f, 1f, 1f).endVertex();
	        bufferbuilder.vertex(mat, selX2 + offset, y1 + 2, 0).color(0f, 0f, 1f, 1f).endVertex();
	        bufferbuilder.vertex(mat, selX1 + offset, y1 + 2, 0).color(0f, 0f, 1f, 1f).endVertex();
	        tessellator.end();
	        RenderSystem.disableColorLogicOp();
	        RenderSystem.enableTexture();
		}
		ScissorUtil.popScissor();
	}

	@Override
	public boolean keyPressed(int key, int scancode, int mods)
	{
		if (Screen.isCopy(key) || Screen.isCut(key))
		{
			onCopy();
			return true;
		}
		else return false;
	}

	@SuppressWarnings("resource")
	public void onCopy()
	{
		if (selStart != selEnd) Minecraft.getInstance().keyboardHandler.setClipboard(text.substring(selStart, selEnd));
	}

	@Override
	public boolean changeFocus(boolean focused)
	{
		if (super.changeFocus(focused))
		{
			selStart = 0;
			selEnd = this.text.length();
			updatePos();
			return true;
		}
		else return false;
	}

	@Override
	public void setIsFocused(boolean focused)
	{
		if (!focused)
		{
			super.setIsFocused(false);
			selStart = selEnd = 0;
		}
		else super.setIsFocused(true);
	}
}