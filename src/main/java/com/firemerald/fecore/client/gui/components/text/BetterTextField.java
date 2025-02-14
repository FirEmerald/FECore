package com.firemerald.fecore.client.gui.components.text;

import java.util.function.Consumer;
import java.util.function.Predicate;

import com.firemerald.fecore.util.function.CharUnaryOperator;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public class BetterTextField extends TextField
{
	private boolean valid = true;
	public Predicate<String> onChanged;
	public CharUnaryOperator validateChars = CharUnaryOperator.IDENTITY;

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

	public String operate(String textIn) {
		if (validateChars == CharUnaryOperator.IDENTITY) return textIn;
		int startIndex = 0;
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < textIn.length(); ++i) {
			char cur = textIn.charAt(i);
			char operated = validateChars.apply(cur);
			if (operated != cur) {
				if (i != startIndex) {
					builder.append(textIn.subSequence(startIndex, i));
				}
				startIndex = i + 1;
				if (operated != 0) builder.append(i);
			}
		}
		if (startIndex < textIn.length()) builder.append(textIn.subSequence(startIndex, textIn.length()));
		return builder.toString();
	}

	@Override
    public void setValue(String textIn)
    {
    	super.setValue(operate(textIn));
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
		super.insertText(operate(p_94165_));
		onChanged();
	}

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
    	char operated = validateChars.apply(codePoint);
    	return operated != 0 && super.charTyped(operated, modifiers);
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
