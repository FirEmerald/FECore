package com.firemerald.fecore.betterscreens.components.text;

import it.unimi.dsi.fastutil.floats.FloatConsumer;
import it.unimi.dsi.fastutil.floats.FloatPredicate;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public class FloatField extends BetterTextField
{
	public FloatField(Font fontrendererObj, int x, int y, int w, int h, Component message, FloatConsumer onChanged)
	{
		super(fontrendererObj, x, y, w, h, message, v -> {
			try
			{
				onChanged.accept(Float.parseFloat(v));
				return true;
			}
			catch (NumberFormatException e)
			{
				return false;
			}
		});
	}

	public FloatField(Font fontrendererObj, int x, int y, int w, int h, float min, float max, Component message, FloatConsumer onChanged)
	{
		super(fontrendererObj, x, y, w, h, message, v -> {
			try
			{
				float v2 = Float.parseFloat(v);
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

	public FloatField(Font fontrendererObj, int x, int y, int w, int h, Component message, FloatPredicate onChanged)
	{
		super(fontrendererObj, x, y, w, h, message, v -> {
			try
			{
				return onChanged.test(Float.parseFloat(v));
			}
			catch (NumberFormatException e)
			{
				return false;
			}
		});
	}

	public FloatField(Font fontrendererObj, int x, int y, int w, int h, float val, Component message, FloatConsumer onChanged)
	{
		super(fontrendererObj, x, y, w, h, message, v -> {
			try
			{
				onChanged.accept(Float.parseFloat(v));
				return true;
			}
			catch (NumberFormatException e)
			{
				return false;
			}
		});
		this.setFloat(val);
	}

	public FloatField(Font fontrendererObj, int x, int y, int w, int h, float val, float min, float max, Component message, FloatConsumer onChanged)
	{
		super(fontrendererObj, x, y, w, h, message, v -> {
			try
			{
				float v2 = Float.parseFloat(v);
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
		this.setFloat(val);
	}

	public FloatField(Font fontrendererObj, int x, int y, int w, int h, float val, Component message, FloatPredicate onChanged)
	{
		super(fontrendererObj, x, y, w, h, message, v -> {
			try
			{
				return onChanged.test(Float.parseFloat(v));
			}
			catch (NumberFormatException e)
			{
				return false;
			}
		});
		this.setFloat(val);
	}

    public void setFloat(float v)
    {
    	setString(Float.toString(v)); //bypass setting val again
    }
}
