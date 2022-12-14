package com.firemerald.fecore.client.gui.components.text;

import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public class DoubleField extends BetterTextField
{
	public DoubleField(Font fontrendererObj, int x, int y, int w, int h, Component message, DoubleConsumer onChanged)
	{
		super(fontrendererObj, x, y, w, h, message, v -> {
			try
			{
				onChanged.accept(Double.parseDouble(v));
				return true;
			}
			catch (NumberFormatException e)
			{
				return false;
			}
		});
	}

	public DoubleField(Font fontrendererObj, int x, int y, int w, int h, double min, double max, Component message, DoubleConsumer onChanged)
	{
		super(fontrendererObj, x, y, w, h, message, v -> {
			try
			{
				double v2 = Double.parseDouble(v);
				if (v2 >= min && v2 <= max)
				{
					onChanged.accept(v2);
					return true;
				}
				else return false;
			}
			catch (NumberFormatException e)
			{
				return false;
			}
		});
	}

	public DoubleField(Font fontrendererObj, int x, int y, int w, int h, Component message, DoublePredicate onChanged)
	{
		super(fontrendererObj, x, y, w, h, message, v -> {
			try
			{
				return onChanged.test(Double.parseDouble(v));
			}
			catch (NumberFormatException e)
			{
				return false;
			}
		});
	}

	public DoubleField(Font fontrendererObj, int x, int y, int w, int h, double val, Component message, DoubleConsumer onChanged)
	{
		super(fontrendererObj, x, y, w, h, message, v -> {
			try
			{
				onChanged.accept(Double.parseDouble(v));
				return true;
			}
			catch (NumberFormatException e)
			{
				return false;
			}
		});
		this.setDouble(val);
	}

	public DoubleField(Font fontrendererObj, int x, int y, int w, int h, double val, double min, double max, Component message, DoubleConsumer onChanged)
	{
		super(fontrendererObj, x, y, w, h, message, v -> {
			try
			{
				double v2 = Double.parseDouble(v);
				if (v2 >= min && v2 <= max)
				{
					onChanged.accept(v2);
					return true;
				}
				else return false;
			}
			catch (NumberFormatException e)
			{
				return false;
			}
		});
		this.setDouble(val);
	}

	public DoubleField(Font fontrendererObj, int x, int y, int w, int h, double val, Component message, DoublePredicate onChanged)
	{
		super(fontrendererObj, x, y, w, h, message, v -> {
			try
			{
				return onChanged.test(Double.parseDouble(v));
			}
			catch (NumberFormatException e)
			{
				return false;
			}
		});
		this.setDouble(val);
	}

    public void setDouble(double v)
    {
    	setString(Double.toString(v)); //bypass setting val again
    }
}
