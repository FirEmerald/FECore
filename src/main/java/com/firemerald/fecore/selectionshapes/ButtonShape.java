package com.firemerald.fecore.selectionshapes;

import java.util.List;
import java.util.function.Consumer;

import com.firemerald.fecore.betterscreens.GuiPopupSelector;
import com.firemerald.fecore.betterscreens.components.Button;

import net.minecraft.network.chat.TextComponent;

public class ButtonShape extends Button
{
	public BoundingShape shape;

	public ButtonShape(int x, int y, BoundingShape shape, Consumer<BoundingShape> setShape)
	{
		super(x, y, new TextComponent(shape.getLocalizedName()), null);
		this.shape = shape;
		this.setAction(() -> {
			List<BoundingShape> allShapes = BoundingShape.getShapeList(this.shape);
			BoundingShape[] values = allShapes.toArray(new BoundingShape[allShapes.size()]);
			new GuiPopupSelector(this, values, (index, value) -> {
				setShape(shape);
				setShape.accept(this.shape = shape);
			}, (value, size, action) -> new Button(size.x, size.y, size.width, size.height, new TextComponent(value.getLocalizedName()), action)).activate();
		});
	}

	public ButtonShape(int x, int y, int w, int h, BoundingShape shape, Consumer<BoundingShape> setShape)
	{
		super(x, y, w, h, new TextComponent(shape.getLocalizedName()), null);
		this.shape = shape;
		this.setAction(() -> {
			List<BoundingShape> allShapes = BoundingShape.getShapeList(this.shape);
			BoundingShape[] values = allShapes.toArray(new BoundingShape[allShapes.size()]);
			new GuiPopupSelector(this, values, (index, value) -> {
				setShape(value);
				setShape.accept(value);
			}, (value, size, action) -> new Button(size.x, size.y, size.width, size.height, new TextComponent(value.getLocalizedName()), action)).activate();
		});
	}

	public void setShape(BoundingShape shape)
	{
		this.shape = shape;
		this.displayString = new TextComponent(shape.getLocalizedName());
	}
}