package com.firemerald.fecore.client.gui.components.text;

import java.util.function.Predicate;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public class IntHolder extends BetterTextField
{
	private int val;

	public IntHolder(Font fontrendererObj, int x, int y, int w, int h, Component message)
	{
		super(fontrendererObj, x, y, w, h, message, (Predicate<String>) null);
		this.onChanged = (str) -> {
			try
			{
				this.val = Integer.parseInt(str);
				return true;
			}
			catch (NumberFormatException e)
			{
				return false;
			}
		};
	}

	public IntHolder(Font fontrendererObj, int x, int y, int w, int h, int min, int max, Component message)
	{
		super(fontrendererObj, x, y, w, h, message, (Predicate<String>) null);
		this.onChanged = (str) -> {
			try
			{
				int v2 = Integer.parseInt(str);
				if (v2 >= min && v2 <= max)
				{
					this.val = v2;
					return true;
				}
				else return false;
			}
			catch (NumberFormatException e)
			{
				return false;
			}
		};
	}

	public IntHolder(Font fontrendererObj, int x, int y, int w, int h, int val, Component message)
	{
		super(fontrendererObj, x, y, w, h, message, (Predicate<String>) null);
		this.onChanged = (str) -> {
			try
			{
				this.val = Integer.parseInt(str);
				return true;
			}
			catch (NumberFormatException e)
			{
				return false;
			}
		};
		this.setInteger(val);
	}

	public IntHolder(Font fontrendererObj, int x, int y, int w, int h, int val, int min, int max, Component message)
	{
		super(fontrendererObj, x, y, w, h, message, (Predicate<String>) null);
		this.onChanged = (str) -> {
			try
			{
				int v2 = Integer.parseInt(str);
				if (v2 >= min && v2 <= max)
				{
					this.val = v2;
					return true;
				}
				else return false;
			}
			catch (NumberFormatException e)
			{
				return false;
			}
		};
		this.setInteger(val);
	}

    public void setInteger(int v)
    {
    	setString(Integer.toString(this.val = v)); //bypass setting val again
    }

    public int getInteger()
    {
    	return val;
    }
}
