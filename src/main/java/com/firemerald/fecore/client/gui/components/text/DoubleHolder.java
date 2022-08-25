package com.firemerald.fecore.client.gui.components.text;

import java.util.function.Predicate;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public class DoubleHolder extends BetterTextField
{
	private double val;

	public DoubleHolder(Font fontrendererObj, int x, int y, int w, int h, Component message)
	{
		super(fontrendererObj, x, y, w, h, message, (Predicate<String>) null);
		this.onChanged = (str) -> {
			try
			{
				this.val = Double.parseDouble(str);
				return true;
			}
			catch (NumberFormatException e)
			{
				return false;
			}
		};
	}

	public DoubleHolder(Font fontrendererObj, int x, int y, int w, int h, double min, double max, Component message)
	{
		super(fontrendererObj, x, y, w, h, message, (Predicate<String>) null);
		this.onChanged = (str) -> {
			try
			{
				double v2 = Double.parseDouble(str);
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

	public DoubleHolder(Font fontrendererObj, int x, int y, int w, int h, double val, Component message)
	{
		super(fontrendererObj, x, y, w, h, message, (Predicate<String>) null);
		this.onChanged = (str) -> {
			try
			{
				this.val = Double.parseDouble(str);
				return true;
			}
			catch (NumberFormatException e)
			{
				return false;
			}
		};
		this.setDouble(val);
	}

	public DoubleHolder(Font fontrendererObj, int x, int y, int w, int h, double val, double min, double max, Component message)
	{
		super(fontrendererObj, x, y, w, h, message, (Predicate<String>) null);
		this.onChanged = (str) -> {
			try
			{
				double v2 = Double.parseDouble(str);
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
		this.setDouble(val);
	}

    public void setDouble(double v)
    {
    	setString(Double.toString(this.val = v)); //bypass setting val again
    }

    public double getDouble()
    {
    	return val;
    }
}
