package com.firemerald.fecore.boundingshapes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.lang3.mutable.MutableInt;

import com.firemerald.fecore.client.gui.components.Button;
import com.firemerald.fecore.client.gui.components.ButtonConfigureShape;
import com.firemerald.fecore.client.gui.components.IComponent;

import net.minecraft.client.gui.Font;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class BoundingShapeCompound extends BoundingShapeBounded implements IUnbounded
{
	public List<BoundingShape> shapes = new ArrayList<>();

	@Override
	public AABB getBounds(double testerX, double testerY, double testerZ)
	{
		AABB box = null;
		for (BoundingShape shape : shapes) if (shape instanceof IBounded)
		{
			AABB other = ((IBounded) shape).getBounds(testerX, testerY, testerZ);
			if (box == null) box = other;
			else box = box.minmax(other);
		}
		else return ALL;
		return box == null ? ALL : box;
	}

	@Override
	public Stream<Entity> getEntityList(LevelAccessor level, Entity entity, Predicate<? super Entity> filter, double testerX, double testerY, double testerZ)
	{
		AABB bounds = getBounds(testerX, testerY, testerZ);
		return bounds == ALL ? getEntityList(level, entity, filter) : level.getEntities(entity, bounds, filter).stream();
	}

	@Override
	public <T extends Entity> Stream<T> getEntityList(LevelAccessor level, EntityTypeTest<Entity, T> typeTest, Predicate<? super T> filter, double testerX, double testerY, double testerZ)
	{
		AABB bounds = getBounds(testerX, testerY, testerZ);
		return bounds == ALL ? getEntityList(level, typeTest, filter) : level.getEntities(typeTest, bounds, filter).stream();
	}

	@Override
	public void saveToNBT(CompoundTag tag)
	{
		super.saveToNBT(tag);
		ListTag list = new ListTag();
		shapes.stream().map(shape -> {
			CompoundTag tag2 = new CompoundTag();
			shape.saveToNBT(tag2);
			return tag2;
		}).forEach(list::add);
		tag.put("shapes", list);
	}

	@Override
	public void loadFromNBT(CompoundTag tag)
	{
		super.loadFromNBT(tag);
		shapes.clear();
		ListTag list = tag.getList("shapes", 10);
		if (list != null) for (int i = 0; i < list.size(); i++) shapes.add(BoundingShape.constructFromNBT(list.getCompound(i)));
	}

	@Override
	public void saveToBuffer(FriendlyByteBuf buf)
	{
		super.saveToBuffer(buf);
		buf.writeVarInt(shapes.size());
		shapes.forEach(shape -> shape.saveToBuffer(buf));
	}

	@Override
	public void loadFromBuffer(FriendlyByteBuf buf)
	{
		super.loadFromBuffer(buf);
		shapes.clear();
		int numShapes = buf.readVarInt();
		for (int i = 0; i < numShapes; i++) shapes.add(BoundingShape.constructFromBuffer(buf));
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
			addElement.accept(new Button(offX + 200, y.getValue(), 100, 20, new TranslatableComponent("fecore.shapesgui.compound.remove"), () -> {
				shapes.remove(j);
				gui.updateGuiButtonsList();
				}));
			y.add(20);
		}
		addElement.accept(new Button(offX + 50, y.getValue(), 200, 20, new TranslatableComponent("fecore.shapesgui.compound.add"), () -> {
			shapes.add(new BoundingShapeSphere());
			gui.updateGuiButtonsList();
		}));
	}
}