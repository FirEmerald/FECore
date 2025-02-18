package com.firemerald.fecore.boundingshapes;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.firemerald.fecore.client.gui.components.ButtonConfigureShape;
import com.firemerald.fecore.client.gui.components.IComponent;
import com.firemerald.fecore.codec.stream.StreamCodec;
import com.firemerald.fecore.init.FECoreBoundingShapes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.gui.Font;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class BoundingShapeInversion extends BoundingShapeUnbounded
{
	public static final MapCodec<BoundingShapeInversion> CODEC = RecordCodecBuilder.mapCodec(
			builder -> builder
			.group(BoundingShape.CODEC.fieldOf("shape").forGetter(inversion -> inversion.shape))
			.apply(builder, BoundingShapeInversion::new)
			);
	public static final StreamCodec<BoundingShapeInversion> STREAM_CODEC = StreamCodec.composite(
			BoundingShape.STREAM_CODEC, inversion -> inversion.shape,
			BoundingShapeInversion::new
			);

	public BoundingShape shape;

	public BoundingShapeInversion(@Nonnull BoundingShape shape) {
		this.shape = shape;
	}

	public BoundingShapeInversion() {
		this(new BoundingShapeCylinder());
	}

	@Override
	public String getUnlocalizedName()
	{
		return "fecore.shape.inversion";
	}

	@Override
	public boolean isWithin(Entity entity, double posX, double posY, double posZ, double testerX, double testerY, double testerZ)
	{
		return !shape.isWithin(entity, posX, posY, posZ, testerX, testerY, testerZ);
	}

	@Override
	public void addGuiElements(Vec3 pos, IShapeGui gui, Font font, Consumer<IComponent> addElement, int width)
	{
		int offX = (width - 200) >> 1;
		addElement.accept(new ButtonConfigureShape(offX, 0, 200, 20, gui::openShape, () -> shape, shape -> this.shape = shape));
	}

	@Override
	public int hashCode() {
		return ~shape.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		else if (o == this) return true;
		else if (o.getClass() != this.getClass()) return false;
		else {
			BoundingShapeInversion inversion = (BoundingShapeInversion) o;
			return inversion.shape.equals(shape);
		}
	}

	@Override
	public BoundingShapeInversion clone() {
		return new BoundingShapeInversion(shape.clone());
	}

	@Override
	public BoundingShapeInversion asAbsolute(Vec3 testerPos) {
		if (isAbsolute()) return this;
		else return new BoundingShapeInversion(shape.asAbsolute(testerPos));
	}

	@Override
	public boolean isAbsolute() {
		return shape.isAbsolute();
	}

	@Override
	public BoundingShapeDefinition<BoundingShapeInversion> definition() {
		return FECoreBoundingShapes.INVERSION.get();
	}

	@Override
	public void getPropertiesFrom(BoundingShape other) {
		if (other instanceof BoundingShapeInversion inversion) this.shape = inversion.shape.clone();
		else if (other instanceof BoundingShapeCompound compound && !compound.shapes.isEmpty()) this.shape = compound.shapes.get(0).clone();
	}
}