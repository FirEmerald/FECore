package com.firemerald.fecore.client.gui.components;

import java.util.function.Consumer;

import com.firemerald.fecore.boundingshapes.BoundingShape;
import com.firemerald.fecore.client.gui.screen.SelectorPopupScreen;

public class ButtonShape extends Button
{
	public BoundingShape shape;

	public ButtonShape(int x, int y, BoundingShape shape, Consumer<BoundingShape> setShape)
	{
		this(x, y, 200, 20, shape, setShape);
	}

	public ButtonShape(int x, int y, int w, int h, BoundingShape shape, Consumer<BoundingShape> setShape)
	{
		super(x, y, w, h, getShapeName(shape), null);
		this.shape = shape;
		this.setAction(() -> {
			BoundingShape[] values = BoundingShape.getShapeDefinitions().map(def -> {
				if (def == this.shape.definition()) return this.shape;
				else return def.newShape();
			}).toArray(BoundingShape[]::new);
			new SelectorPopupScreen(this, values, (index, value) -> {
				if (value != this.shape) {
					value.getPropertiesFrom(this.shape);
					setShape(value);
					setShape.accept(value);
				}
			}, (value, size, action) -> new Button(size.x, size.y, size.width, size.height, getShapeName(value), action)).activate();
		});
	}

	public static net.minecraft.network.chat.Component getShapeName(BoundingShape shape) {
		return net.minecraft.network.chat.Component.literal(shape.getLocalizedName());
	}

	public void setShape(BoundingShape shape) {
		this.shape = shape;
		this.displayString = getShapeName(shape);
	}
}