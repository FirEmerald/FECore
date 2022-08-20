package com.firemerald.fecore.selectionshapes;

import java.util.function.Consumer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IShapeGui
{
	public void updateGuiButtonsList();

	public void openShape(BoundingShape shape, Consumer<BoundingShape> onAccepted);
}