package com.firemerald.fecore.client.gui.components;

import com.firemerald.fecore.client.gui.IComponentHolder;

import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetTooltipHolder;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class Component implements IComponent
{
    private int x1;
    private int y1;
    private int x2;
    private int y2;
	private IComponentHolder holder;
	private boolean visible = true;
	private boolean hovered = false;
    private final WidgetTooltipHolder tooltip = new WidgetTooltipHolder();

	public Component(int x1, int y1, int x2, int y2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public Component(IComponentHolder parent, int x1, int y1, int x2, int y2)
	{
		this(x1, y1, x2, y2);
		this.holder = parent;
	}

	public void setSize(int x1, int y1, int x2, int y2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public void setRectangle(int x, int y, int width, int height)
	{
		setSize(x, y, x + width, y + height);
	}

	@Override
	public int getX1() {
		return x1;
	}

	@Override
	public int getY1() {
		return y1;
	}

	@Override
	public int getX2() {
		return x2;
	}

	@Override
	public int getY2() {
		return y2;
	}

	@Override
	public void setHolder(IComponentHolder holder)
	{
		this.holder = holder;
	}

	@Override
	public IComponentHolder getHolder()
	{
		return holder;
	}

	public boolean isVisible()
	{
		return visible;
	}

	public void setVisible(boolean visible)
	{
		if (!(this.visible = visible)) hovered = false;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mx, int my, float partialTicks, boolean canHover)
	{
		if (this.isVisible())
		{
			this.hovered = canHover && isMouseOver(mx, my);
			doRender(guiGraphics, mx, my, partialTicks, canHover);
		}
		else this.hovered = false;
	}

	public abstract void doRender(GuiGraphics guiGraphics, int mx, int my, float partialTicks, boolean canHover);

	public boolean isHovered()
	{
		return hovered;
	}

	public void setHovered(boolean hovered)
	{
		this.hovered = hovered;
	}

	@Override
	public NarratableEntry.NarrationPriority narrationPriority()
	{
		return this.isHovered() ? NarratableEntry.NarrationPriority.HOVERED : NarratableEntry.NarrationPriority.NONE;
	}

    @Override
    public final void updateNarration(NarrationElementOutput narrationElementOutput) {
        this.updateWidgetNarration(narrationElementOutput);
        this.tooltip.updateNarration(narrationElementOutput);
    }

    protected abstract void updateWidgetNarration(NarrationElementOutput narrationElementOutput);

    public static void renderScrollingString(GuiGraphics guiGraphics, Font font, net.minecraft.network.chat.Component text, int minX, int minY, int maxX, int maxY, int color) {
        renderScrollingString(guiGraphics, font, text, (minX + maxX) / 2, minX, minY, maxX, maxY, color);
    }

    public static void renderScrollingString(GuiGraphics guiGraphics, Font font, net.minecraft.network.chat.Component text, int centerX, int minX, int minY, int maxX, int maxY, int color) {
        int textWidth = font.width(text);
        int textY = (minY + maxY - 9) / 2 + 1;
        int maxWidth = maxX - minX;
        if (textWidth > maxWidth) {
            int overflow = textWidth - maxWidth;
            double scrollTime = Util.getMillis() / 1000.0;
            double maxScrollAmount = Math.max(overflow * 0.5, 3.0);
            double scrollTimeAdjusted = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * scrollTime / maxScrollAmount)) / 2.0 + 0.5;
            double shiftLeft = Mth.lerp(scrollTimeAdjusted, 0.0, overflow);
            guiGraphics.enableScissor(minX, minY, maxX, maxY);
            guiGraphics.drawString(font, text, minX - (int)shiftLeft, textY, color);
            guiGraphics.disableScissor();
        } else {
            int textX = Mth.clamp(centerX, minX + textWidth / 2, maxX - textWidth / 2);
            guiGraphics.drawCenteredString(font, text, textX, textY, color);
        }
    }

    protected void renderScrollingString(GuiGraphics guiGraphics, Font font, int margin, int color) {
        renderScrollingString(guiGraphics, font, this.getMessage(), this.getX1() + margin, this.getY1(), this.getX2() - margin, this.getY2(), color);
    }

    public abstract net.minecraft.network.chat.Component getMessage();
}