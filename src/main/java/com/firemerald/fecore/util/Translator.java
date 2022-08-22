package com.firemerald.fecore.util;

import net.minecraft.locale.Language;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Translator
{
	public static String translate(String key)
	{
		return Language.getInstance().getOrDefault(key);
	}

	public static String format(String key, Object... format)
	{
		return String.format(Language.getInstance().getOrDefault(key), format);
	}
}
