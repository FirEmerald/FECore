package com.firemerald.fecore.compat;

import java.util.Optional;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;

public interface ICompatProvider
{
	public default void init()
	{
		Optional<? extends ModContainer> opt = ModList.get().getModContainerById(getModID());
		if (opt.isPresent() && isValid(opt.get())) setPresent();
	}
	
	public String getModID();

	public boolean isValid(ModContainer modContainer);

	public boolean isPresent();

	public void setPresent();
}