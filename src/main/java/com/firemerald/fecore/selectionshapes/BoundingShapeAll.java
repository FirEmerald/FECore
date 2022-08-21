package com.firemerald.fecore.selectionshapes;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.firemerald.fecore.betterscreens.components.IComponent;

import net.minecraft.client.gui.Font;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class BoundingShapeAll extends BoundingShape
{
	@Override
	public String getUnlocalizedName()
	{
		return "fecore.shape.all";
	}

	@Override
	public boolean isWithin(@Nullable Entity entity, double posX, double posY, double posZ, double testerX, double testerY, double testerZ)
	{
		return true;
	}

	@Override
	public void addGuiElements(Vec3 pos, IShapeGui gui, Font font, Consumer<IComponent> addElement, int width) {}
}