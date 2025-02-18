package com.firemerald.fecore.client.gui.screen;

import com.firemerald.fecore.client.gui.EnumTextAlignment;
import com.firemerald.fecore.client.gui.components.Button;
import com.firemerald.fecore.client.gui.components.decoration.FloatingText;
import com.firemerald.fecore.client.gui.components.text.DoubleField;
import com.firemerald.fecore.config.ClientConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModConfigScreen extends BetterScreen
{
	final Screen previous;
	final FloatingText scrollSensitivityLabel;
	final DoubleField scrollSensitivity;
	final Button done;

	@SuppressWarnings("resource")
	public ModConfigScreen(Screen previous)
	{
		super(Component.translatable("fecore.configgui"));
		this.previous = previous;
		this.scrollSensitivityLabel = new FloatingText(0, 0, 100, 20, Minecraft.getInstance().font, I18n.get("fecore.configgui.scrollSensitivity"), EnumTextAlignment.RIGHT);
		this.scrollSensitivity = new DoubleField(Minecraft.getInstance().font, 100, 0, 100, 20, ClientConfig.SCROLL_SENSITIVITY.get(), 0, Double.POSITIVE_INFINITY, Component.literal("Scrolling sensitivity"), ClientConfig.SCROLL_SENSITIVITY::set);
		this.done = new Button(50, 20, 100, 20, Component.translatable("fecore.gui.done"), this::onClose);
	}

    @Override
    public void init()
    {
    	int x = (this.width - 200) >> 1;
    	int y = (this.height -40) >> 1;
    	scrollSensitivityLabel.setSize(x, y, x + 100, y + 20);
    	scrollSensitivity.setSize(x + 100, y, x + 200, y + 20);
    	done.setSize(x + 50, y + 20, x + 150, y + 40);
    	this.addRenderableWidget(scrollSensitivityLabel);
    	this.addRenderableWidget(scrollSensitivity);
    	this.addRenderableWidget(done);
    }

    @Override
    public void onClose()
    {
       this.minecraft.setScreen(previous);
    }

	@Override
	public void render(GuiGraphics guiGraphics, int mx, int my, float partialTick, boolean canHover)
	{
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mx, my, partialTick, canHover);
	}
}