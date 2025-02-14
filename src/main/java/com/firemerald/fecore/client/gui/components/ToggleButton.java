package com.firemerald.fecore.client.gui.components;

import java.util.function.Consumer;
import java.util.function.Function;

public class ToggleButton extends Button
{
	public boolean state = true;

    public ToggleButton(int x, int y, net.minecraft.network.chat.Component buttonText, Consumer<Boolean> onClick)
    {
    	super(x, y, buttonText, null);
    	setToggleAction(onClick);
    }

    public ToggleButton(int x, int y, int widthIn, int heightIn, net.minecraft.network.chat.Component buttonText, Consumer<Boolean> onClick)
    {
    	super(x, y, widthIn, heightIn, buttonText, null);
    	setToggleAction(onClick);
    }

    public ToggleButton(int x, int y, net.minecraft.network.chat.Component buttonText, boolean state, Consumer<Boolean> onClick)
    {
    	super(x, y, buttonText, null);
    	this.state = state;
    	setToggleAction(onClick);
    }

    public ToggleButton(int x, int y, int widthIn, int heightIn, net.minecraft.network.chat.Component buttonText, boolean state, Consumer<Boolean> onClick)
    {
    	super(x, y, widthIn, heightIn, buttonText, null);
    	this.state = state;
    	setToggleAction(onClick);
    }

    public ToggleButton setToggleAction(Consumer<Boolean> onClick)
    {
    	this.onClick = () -> onClick.accept(this.state = !this.state);
    	return this;
    }

    public ToggleButton setToggleAction(Function<? super ToggleButton, Consumer<Boolean>> onClickFunction)
    {
    	return setToggleAction(onClickFunction.apply(this));
    }

    @Override
    public boolean renderAsActive(boolean hovered) {
    	return super.renderAsActive(hovered) && this.state;
    }

    @Override
    public boolean renderTextAsActive(boolean hovered) {
    	return super.renderAsActive(hovered) && (this.state || this.renderTextAsFocused(hovered));
    }
}
