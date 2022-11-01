package com.firemerald.fecore.boundingshapes;

import java.util.function.Consumer;

import com.firemerald.fecore.client.gui.components.ButtonConfigureShape;
import com.firemerald.fecore.client.gui.components.IComponent;

import net.minecraft.client.gui.Font;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class BoundingShapeInversion extends BoundingShapeUnbounded
{
	public BoundingShape shape = new BoundingShapeCylinder();

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
	public void saveToNBT(CompoundTag tag)
	{
		super.saveToNBT(tag);
		CompoundTag tag2 = new CompoundTag();
		shape.saveToNBT(tag2);
		tag.put("shape", tag2);
	}

	@Override
	public void loadFromNBT(CompoundTag tag)
	{
		super.loadFromNBT(tag);
		shape = tag.contains("shape", 10) ? BoundingShape.constructFromNBT(tag.getCompound("shape")) : new BoundingShapeSphere();
	}

	@Override
	public void saveToBuffer(FriendlyByteBuf buf)
	{
		super.saveToBuffer(buf);
		shape.saveToBuffer(buf);
	}

	@Override
	public void loadFromBuffer(FriendlyByteBuf buf)
	{
		super.loadFromBuffer(buf);
		shape = BoundingShape.constructFromBuffer(buf);
	}

	@Override
	public void addGuiElements(Vec3 pos, IShapeGui gui, Font font, Consumer<IComponent> addElement, int width)
	{
		int offX = (width - 200) >> 1;
		addElement.accept(new ButtonConfigureShape(offX, 0, 200, 20, gui::openShape, () -> shape, shape -> this.shape = shape));
	}
}