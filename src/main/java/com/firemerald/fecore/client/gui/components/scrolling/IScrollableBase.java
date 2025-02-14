package com.firemerald.fecore.client.gui.components.scrolling;

import java.util.function.DoubleConsumer;

import com.firemerald.fecore.config.ClientConfig;

public interface IScrollableBase
{
	public default double scroll(double scrollAmount, double currentValue, double maxValue, DoubleConsumer setScroll)
	{
		if (scrollAmount != 0)
		{
			final double scrollSensitivity = ClientConfig.SCROLL_SENSITIVITY.getAsDouble();
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
