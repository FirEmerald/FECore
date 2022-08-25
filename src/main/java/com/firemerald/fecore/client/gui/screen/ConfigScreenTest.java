package com.firemerald.fecore.client.gui.screen;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.client.Translator;
import com.firemerald.fecore.client.gui.EnumTextAlignment;
import com.firemerald.fecore.client.gui.components.Button;
import com.firemerald.fecore.client.gui.components.ToggleButton;
import com.firemerald.fecore.client.gui.components.decoration.FloatingText;
import com.firemerald.fecore.client.gui.components.scrolling.FullyScrollableComponentPane;
import com.firemerald.fecore.client.gui.components.scrolling.HorizontalScrollBar;
import com.firemerald.fecore.client.gui.components.scrolling.VerticalScrollBar;
import com.firemerald.fecore.client.gui.components.text.*;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ConfigScreenTest extends BetterScreen
{
	final Screen previous;
	final FullyScrollableComponentPane pane;
	final VerticalScrollBar vertScroll;
	final HorizontalScrollBar horScroll;
	final Button button;
	final ToggleButton toggleButton;
	final FloatingText text;
	final BetterTextField textField;
	final LabeledBetterTextField labeledTextField;
	final DoubleField doubleField;
	final LabeledDoubleField labeledDoubleField;
	final CompoundTagField compundTagField;
	final FloatingText loremIpsum1;
	final FloatingText loremIpsum2;
	final FloatingText loremIpsum3;
	final FloatingText loremIpsum4;
	final FloatingText loremIpsum5;
	final FloatingText loremIpsum6;
	final FloatingText loremIpsum7;
	final FloatingText loremIpsum8;
	final FloatingText scrollSensitivityLabel;
	final DoubleField scrollSensitivity;

	@SuppressWarnings("resource")
	public ConfigScreenTest(Screen previous)
	{
		super(new TextComponent("Config Test"));
		this.previous = previous;
		this.pane = new FullyScrollableComponentPane(0, 0, 100, 100);
		this.vertScroll = new VerticalScrollBar(100, 0, 110, 100, pane);
		this.horScroll = new HorizontalScrollBar(0, 100, 100, 110, pane);
		this.button = new Button(0, 0, 210, 20, new TextComponent("test button"), () -> FECoreMod.LOGGER.info("button test"));
		this.toggleButton = new ToggleButton(210, 0, 210, 20, new TextComponent("test toggle"), v -> FECoreMod.LOGGER.info("toggle test: " + v));
		this.text = new FloatingText(0, 20, 420, 40, Minecraft.getInstance().font, "test floating text");
		this.textField = new BetterTextField(Minecraft.getInstance().font, 0, 40, 420, 20, "test text field", new TextComponent("test text field"), v -> {
			FECoreMod.LOGGER.info("text field test: " + v);
			return v.length() < 20;
		});
		textField.setMaxLength(Integer.MAX_VALUE);
		this.labeledTextField = new LabeledBetterTextField(Minecraft.getInstance().font, 0, 60, 420, 20, "test labeled text field", new TextComponent("test labeled text field"), v -> {
			FECoreMod.LOGGER.info("labeled text field test: " + v);
			return v.length() < 20;
		});
		labeledTextField.setMaxLength(Integer.MAX_VALUE);
		this.doubleField = new DoubleField(Minecraft.getInstance().font, 0, 80, 420, 20, 0, new TextComponent("test double field"), v -> {
			FECoreMod.LOGGER.info("double field test: " + v);
			return v >= 0;
		});
		doubleField.setMaxLength(Integer.MAX_VALUE);
		this.labeledDoubleField = new LabeledDoubleField(Minecraft.getInstance().font, 0, 100, 420, 20, "test labeled double field", new TextComponent("test labeled double field"), v -> {
			FECoreMod.LOGGER.info("labeled double field test: " + v);
			return v >= 0;
		});
		labeledDoubleField.setMaxLength(Integer.MAX_VALUE);
		this.compundTagField = new CompoundTagField(Minecraft.getInstance().font, 0, 120, 420, 20, new TextComponent("test compound tag field"), v -> {
			FECoreMod.LOGGER.info("compound tag field test: " + v);
			return v.size() < 3;
		});
		this.loremIpsum1 = new FloatingText(0, 140, 420, 160, Minecraft.getInstance().font, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do");
		this.loremIpsum2 = new FloatingText(0, 160, 420, 180, Minecraft.getInstance().font, "eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
		this.loremIpsum3 = new FloatingText(0, 180, 420, 200, Minecraft.getInstance().font, "enim ad minim veniam, quis nostrud exercitation ullamco");
		this.loremIpsum4 = new FloatingText(0, 200, 420, 220, Minecraft.getInstance().font, "laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure");
		this.loremIpsum5 = new FloatingText(0, 220, 420, 240, Minecraft.getInstance().font, "dolor in reprehenderit in voluptate velit esse cillum dolore eu");
		this.loremIpsum6 = new FloatingText(0, 240, 420, 260, Minecraft.getInstance().font, "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non");
		this.loremIpsum7 = new FloatingText(0, 260, 420, 280, Minecraft.getInstance().font, "proident, sunt in culpa qui officia deserunt mollit anim id est");
		this.loremIpsum8 = new FloatingText(0, 280, 420, 300, Minecraft.getInstance().font, "laborum.");
		this.scrollSensitivityLabel = new FloatingText(0, 300, 200, 320, Minecraft.getInstance().font, Translator.translate("fecore.configgui.scrollSensitivity"), EnumTextAlignment.RIGHT);
		this.scrollSensitivity = new DoubleField(Minecraft.getInstance().font, 200, 300, 200, 20, FECoreMod.CLIENT.scrollSensitivity.get(), 0, Double.POSITIVE_INFINITY, new TextComponent("Scrolling sensitivity"), v -> {FECoreMod.CLIENT.scrollSensitivity.set(v);});
		compundTagField.setMaxLength(Integer.MAX_VALUE);
		pane.setScrollBar(vertScroll);
		pane.setScrollBar(horScroll);
		pane.addComponent(button);
		pane.addComponent(toggleButton);
		pane.addComponent(text);
		pane.addComponent(textField);
		pane.addComponent(labeledTextField);
		pane.addComponent(doubleField);
		pane.addComponent(labeledDoubleField);
		pane.addComponent(compundTagField);
		pane.addComponent(loremIpsum1);
		pane.addComponent(loremIpsum2);
		pane.addComponent(loremIpsum3);
		pane.addComponent(loremIpsum4);
		pane.addComponent(loremIpsum5);
		pane.addComponent(loremIpsum6);
		pane.addComponent(loremIpsum7);
		pane.addComponent(loremIpsum8);
		pane.addComponent(scrollSensitivityLabel);
		pane.addComponent(scrollSensitivity);
	}

    @Override
    public void init()
    {
    	pane.setSize(0, 0, width - 10, height - 10);
    	vertScroll.setSize(width - 10, 0, width, height - 10);
    	horScroll.setSize(0, height - 10, width - 10, height);
		this.addRenderableWidget(pane);
		this.addRenderableWidget(vertScroll);
		this.addRenderableWidget(horScroll);
    }

    @Override
    public void onClose()
    {
       this.minecraft.setScreen(previous);
    }

	@Override
	public void render(PoseStack pose, int mx, int my, float partialTick, boolean canHover)
	{
		this.renderBackground(pose);
		super.render(pose, mx, my, partialTick, canHover);
	}
}