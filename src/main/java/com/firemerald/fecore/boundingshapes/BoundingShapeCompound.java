package com.firemerald.fecore.boundingshapes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.lang3.mutable.MutableInt;

import com.firemerald.fecore.client.gui.components.Button;
import com.firemerald.fecore.client.gui.components.ButtonConfigureShape;
import com.firemerald.fecore.client.gui.components.IComponent;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.gui.Font;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class BoundingShapeCompound extends BoundingShapeBounded implements IUnbounded
{
	public static <T extends BoundingShapeCompound> MapCodec<T> makeCodec(Function<List<BoundingShape>, T> transform) {
		return RecordCodecBuilder.mapCodec(
				builder -> builder
				.group(LIST_CODEC.fieldOf("shapes").forGetter(compound -> compound.shapes))
				.apply(builder, transform));
	}
	public static <T extends BoundingShapeCompound> StreamCodec<RegistryFriendlyByteBuf, T> makeStreamCodec(Function<List<BoundingShape>, T> transform) {
		return StreamCodec.composite(
				STREAM_LIST_CODEC, compound -> compound.shapes,
				transform
				);
	}

	public List<BoundingShape> shapes;

	public BoundingShapeCompound(Collection<BoundingShape> shapes) {
		this.shapes = new ArrayList<>(shapes);
	}

	public BoundingShapeCompound() {
		this.shapes = new ArrayList<>();
	}

	@Override
	public Stream<Entity> getEntityList(LevelAccessor level, Entity entity, Predicate<? super Entity> filter, AABB bounds)
	{
		return bounds == ALL ? IUnbounded.super.getEntityList(level, entity, filter) : super.getEntityList(level, entity, filter, bounds);
	}

	@Override
	public <T extends Entity> Stream<T> getEntityList(LevelAccessor level, EntityTypeTest<Entity, T> typeTest, Predicate<? super T> filter, AABB bounds)
	{
		return bounds == ALL ? IUnbounded.super.getEntityList(level, typeTest, filter) : super.getEntityList(level, typeTest, filter, bounds);
	}

	@Override
	public void addGuiElements(Vec3 pos, IShapeGui gui, Font font, Consumer<IComponent> addElement, int width)
	{
		int offX = (width - 300) >> 1;
		MutableInt y = new MutableInt(0);
		for (int i = 0; i < shapes.size(); i++)
		{
			final int j = i;
			addElement.accept(new ButtonConfigureShape(offX, y.getValue(), 200, 20, gui::openShape, () -> shapes.get(j), shape -> shapes.set(j, shape)));
			addElement.accept(new Button(offX + 200, y.getValue(), 100, 20, Component.translatable("fecore.shapesgui.compound.remove"), () -> {
				shapes.remove(j);
				gui.updateGuiButtonsList();
				}));
			y.add(20);
		}
		addElement.accept(new Button(offX + 50, y.getValue(), 200, 20, Component.translatable("fecore.shapesgui.compound.add"), () -> {
			shapes.add(new BoundingShapeSphere());
			gui.updateGuiButtonsList();
		}));
	}

	@Override
	public int hashCode() {
		return shapes.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		else if (o == this) return true;
		else if (o.getClass() != this.getClass()) return false;
		else {
			BoundingShapeCompound compound = (BoundingShapeCompound) o;
			return compound.shapes.equals(shapes);
		}
	}

	@Override
	public void getPropertiesFrom(BoundingShape other) {
		if (other instanceof BoundingShapeCompound compound) {
			this.shapes.clear();
			compound.shapes.stream().map(BoundingShape::clone).forEach(this.shapes::add);
		}
		else if (other instanceof BoundingShapeInversion inversion) {
			this.shapes.clear();
			this.shapes.add(inversion.shape);
		}
	}

	@Override
	public BoundingShapeCompound clone() {
		return fromNewShapes(shapes.stream().map(BoundingShape::clone));
	}

	@Override
	public BoundingShapeCompound asAbsolute(Vec3 pos) {
		if (isAbsolute()) return this;
		else return fromNewShapes(shapes.stream().map(shape -> shape.asAbsolute(pos)));
	}

	@Override
	public boolean isAbsolute() {
		return shapes.stream().allMatch(BoundingShape::isAbsolute);
	}

	public abstract BoundingShapeCompound fromNewShapes(Stream<BoundingShape> shapes);
}