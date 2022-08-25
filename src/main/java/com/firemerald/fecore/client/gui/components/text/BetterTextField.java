package com.firemerald.fecore.client.gui.components.text;

import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public class BetterTextField extends TextField
{
	private boolean valid = true;
	public Predicate<String> onChanged;

	public BetterTextField(Font fontrendererObj, int x, int y, int w, int h, Component message, Consumer<String> onChanged)
	{
		this(fontrendererObj, x, y, w, h, message, v -> {
			onChanged.accept(v);
			return true;
		});
	}

	public BetterTextField(Font fontrendererObj, int x, int y, int w, int h, Component message, Predicate<String> onChanged)
	{
		super(fontrendererObj, x, y, w, h, message);
		this.onChanged = onChanged;
	}

	public BetterTextField(Font fontrendererObj, int x, int y, int w, int h, String val, Component message, Consumer<String> onChanged)
	{
		this(fontrendererObj, x, y, w, h, message, onChanged);
		setString(val);
	}

	public BetterTextField(Font fontrendererObj, int x, int y, int w, int h, String val, Component message, Predicate<String> onChanged)
	{
		this(fontrendererObj, x, y, w, h, message, onChanged);
		setString(val);
	}

	@Override
    public void setValue(String textIn)
    {
    	super.setValue(textIn);
    	onChanged();
    }

	@Override
	public void deleteChars(int p_94181_)
	{
		super.deleteChars(p_94181_);
		onChanged();
	}

	@Override
	public void insertText(String p_94165_)
	{
		super.insertText(p_94165_);
		onChanged();
	}

	@Override
	public void setMaxLength(int p_94200_)
	{
		super.setMaxLength(p_94200_);
		onChanged();
	}

    public void onChanged()
    {
    	boolean prevValid = valid;
    	try
    	{
    		valid = onChanged.test(getValue());
    	}
    	catch (NumberFormatException e)
    	{
    		valid = false;
    	}
    	if (prevValid ^ valid) this.setTextColor(valid ? 0xE0E0E0 : 0xE00000);
    }

    public void setString(String v)
    {
		valid = true;
    	super.setValue(v); //bypass setting val again
    }

    public boolean isValid()
    {
    	return valid;
    }
}
