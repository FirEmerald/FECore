package com.firemerald.fecore.client.gui.components;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.firemerald.fecore.boundingshapes.BoundingShape;

import net.minecraft.network.chat.TranslatableComponent;

public class ButtonConfigureShape extends Button
{
	public ButtonConfigureShape(int x, int y, int width, int height, BiConsumer<BoundingShape, Consumer<BoundingShape>> configure, Supplier<BoundingShape> getShape, Consumer<BoundingShape> setShape)
	{
		super(x, y, width, height, new TranslatableComponent("fecore.shapesgui.configure", getShape.get().getLocalizedName()), null);
		this.onClick = () -> configure.accept(BoundingShape.copy(getShape.get()), setShape.andThen(this::onShapeChanged));
	}

	public void onShapeChanged(BoundingShape shape)
	{
		this.displayString = new TranslatableComponent("fecore.shapesgui.configure", shape.getLocalizedName());
	}
}