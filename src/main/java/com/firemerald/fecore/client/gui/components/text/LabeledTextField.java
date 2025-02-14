package com.firemerald.fecore.client.gui.components.text;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class LabeledTextField extends TextField
{
	public net.minecraft.network.chat.Component label;
	public int disCol = 7368816;
	public boolean bordered = true;
	public Font font;

	public LabeledTextField(Font font, int x, int y, int w, int h, net.minecraft.network.chat.Component label, Component message)
	{
		super(font, x, y, w, h, message);
		this.font = font;
		this.label = label;
	}

	@Override
	public void setTextColorUneditable(int col)
	{
		super.setTextColorUneditable(this.disCol = col);
	}

	@Override
	public void setBordered(boolean bordered)
	{
		super.setBordered(this.bordered = bordered);
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mx, int my, float partialTick)
	{
		super.renderWidget(guiGraphics, mx, my, partialTick);
		if (this.isVisible() && !this.isFocused() && this.getValue().isEmpty())
			this.renderLabel(guiGraphics, font, disCol);
	}

    public void renderLabel(GuiGraphics guiGraphics, Font font, int color) {
        this.renderScrollingLabel(guiGraphics, font, this.bordered ? 4 : 0, color);
    }

    protected void renderScrollingLabel(GuiGraphics guiGraphics, Font font, int margin, int color) {
        renderScrollingString(guiGraphics, font, label, this.getX1() + margin, this.getY1(), this.getX2() - margin, this.getY2(), color);
    }
}
