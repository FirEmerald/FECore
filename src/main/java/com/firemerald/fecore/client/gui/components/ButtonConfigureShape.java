package com.firemerald.fecore.client.gui.components;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.firemerald.fecore.boundingshapes.BoundingShape;

public class ButtonConfigureShape extends Button
{
	public ButtonConfigureShape(int x, int y, int width, int height, BiConsumer<BoundingShape, Consumer<BoundingShape>> configure, Supplier<BoundingShape> getShape, Consumer<BoundingShape> setShape)
	{
		super(x, y, width, height, net.minecraft.network.chat.Component.translatable("fecore.shapesgui.configure", getShape.get().getLocalizedName()), null);
		this.onClick = () -> configure.accept(getShape.get().clone(), setShape.andThen(this::onShapeChanged));
	}

	public void onShapeChanged(BoundingShape shape)
	{
		this.displayString = net.minecraft.network.chat.Component.translatable("fecore.shapesgui.configure", shape.getLocalizedName());
	}
}