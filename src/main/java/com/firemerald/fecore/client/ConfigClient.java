package com.firemerald.fecore.client;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

public class ConfigClient
{
	public final DoubleValue scrollSensitivity;

	public ConfigClient(ForgeConfigSpec.Builder builder)
	{
        builder.comment("Client only settings").push("client");
        scrollSensitivity = builder
        		.comment("Modifies the sensitivity of scrolling for BetterGUI scrollables. Default: 10.0")
        		.translation("fecore.configgui.scrollSensitivity")
        		.defineInRange("scrollSensitivity", 10.0, 0.0, Double.POSITIVE_INFINITY);
	}
}
