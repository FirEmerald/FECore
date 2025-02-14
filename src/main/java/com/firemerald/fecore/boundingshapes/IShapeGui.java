package com.firemerald.fecore.boundingshapes;

import java.util.function.Consumer;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IShapeGui
{
	public void updateGuiButtonsList();

	public void openShape(BoundingShape shape, Consumer<BoundingShape> onAccepted);
}