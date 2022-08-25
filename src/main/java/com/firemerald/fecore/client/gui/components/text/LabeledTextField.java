package com.firemerald.fecore.client.gui.components.text;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public class LabeledTextField extends TextField
{
	public String label;
	public int disCol = 7368816;
	public boolean bordered = true;
	public Font font;

	public LabeledTextField(Font font, int x, int y, int w, int h, String label, Component message)
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
	public void renderButton(PoseStack pose, int mx, int my, float partialTick)
	{
		super.renderButton(pose, mx, my, partialTick);
		if (this.isVisible() && !this.isFocused() && this.getValue().isEmpty())
		{
			int l = this.bordered ? this.x + 4 : this.x;
			int i1 = this.bordered ? this.y + (this.height - 8) / 2 : this.y;
			this.font.draw(pose, label, l, i1, disCol);
		}
	}
}
