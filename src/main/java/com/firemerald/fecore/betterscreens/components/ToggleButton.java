package com.firemerald.fecore.betterscreens.components;

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

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    @Override
	protected int getHoverState(boolean mouseOver)
    {
    	return this.state ? super.getHoverState(mouseOver) : !mouseOver && this.focused ? 2 : 0;
    }
    
    @Override
    public int getTextColor(boolean isHovered)
    {
    	return (this.state || !this.focused) ? super.getTextColor(isHovered) : 
    		isHovered ? 0xFFFFA0 : 0xA0A0A0;
    }
}
