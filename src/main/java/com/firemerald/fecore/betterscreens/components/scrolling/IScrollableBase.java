package com.firemerald.fecore.betterscreens.components.scrolling;

import java.util.function.DoubleConsumer;

import com.firemerald.fecore.FECoreMod;

public interface IScrollableBase
{
	public default double scroll(double scrollAmount, double currentValue, double maxValue, DoubleConsumer setScroll)
	{
		if (scrollAmount != 0)
		{
			final double scrollSensitivity = FECoreMod.CLIENT.scrollSensitivity.get();
			double toScroll;
			if (scrollAmount > 0)
			{
				toScroll = currentValue;
				if (toScroll > (scrollAmount * scrollSensitivity)) toScroll = scrollAmount * scrollSensitivity;
			}
			else
			{
				toScroll = currentValue - maxValue;
				if (toScroll < (scrollAmount * scrollSensitivity)) toScroll = scrollAmount * scrollSensitivity;
			}
			setScroll.accept(currentValue - toScroll);
			return scrollAmount - (toScroll / scrollSensitivity);
		}
		else return 0;
	}
}
