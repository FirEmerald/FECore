package com.firemerald.fecore.client;

import com.firemerald.fecore.config.ConfigBase;

import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

public class ConfigClient extends ConfigBase
{
	private static ConfigClient instance;
	
	public static ConfigClient instance()
	{
		return instance;
	}
	
	public ConfigClient(ModLoadingContext context)
	{
		super(context, Type.CLIENT);
		instance = this;
	}

	public DoubleValue scrollSensitivity;

	@Override
	public void build()
	{
		this.beginSection("client", "Client only settings");
		scrollSensitivity = this.define("scrollSensitivity", 10.0, 0.0, Double.POSITIVE_INFINITY, "Modifies the sensitivity of scrolling for BetterGUI scrollables. Default: 10.0");
		this.endSection();
	}
}
