package com.firemerald.fecore.betterscreens.components.text;

import java.util.function.Consumer;
import java.util.function.Predicate;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public class LabeledBetterTextField extends BetterTextField
{
	public String label;
	public int disCol = 0x707070;
	public boolean bordered = true;
	public Font font;

	public LabeledBetterTextField(Font fontrendererObj, int x, int y, int w, int h, String label, Component message, Consumer<String> onChanged)
	{
		super(fontrendererObj, x, y, w, h, message, onChanged);
		this.font = fontrendererObj;
		this.label = label;
	}

	public LabeledBetterTextField(Font fontrendererObj, int x, int y, int w, int h, String label, Component message, Predicate<String> onChanged)
	{
		super(fontrendererObj, x, y, w, h, message, onChanged);
		this.font = fontrendererObj;
		this.label = label;
	}

	public LabeledBetterTextField(Font fontrendererObj, int x, int y, int w, int h, String val, String label, Component message, Consumer<String> onChanged)
	{
		super(fontrendererObj, x, y, w, h, val, message, onChanged);
		this.font = fontrendererObj;
		this.label = label;
	}

	public LabeledBetterTextField(Font fontrendererObj, int x, int y, int w, int h, String val, String label, Component message, Predicate<String> onChanged)
	{
		super(fontrendererObj, x, y, w, h, val, message, onChanged);
		this.font = fontrendererObj;
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
