package com.firemerald.fecore.boundingshapes;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.firemerald.fecore.client.gui.components.IComponent;
import com.firemerald.fecore.codec.stream.StreamCodec;
import com.firemerald.fecore.init.FECoreBoundingShapes;
import com.mojang.serialization.MapCodec;

import net.minecraft.client.gui.Font;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class BoundingShapeAll extends BoundingShapeUnbounded
{
	public static final BoundingShapeAll INSTANCE = new BoundingShapeAll();
	public static final MapCodec<BoundingShapeAll> CODEC = MapCodec.unit(INSTANCE);
	public static final StreamCodec<BoundingShapeAll> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	private BoundingShapeAll() {}

	@Override
	public String getUnlocalizedName()
	{
		return "fecore.shape.all";
	}

	@Override
	public boolean isWithin(@Nullable Entity entity, double posX, double posY, double posZ, double testerX, double testerY, double testerZ)
	{
		return true;
	}

	@Override
	public void addGuiElements(Vec3 pos, IShapeGui gui, Font font, Consumer<IComponent> addElement, int width) {}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		return o == this;
	}

	@Override
	public BoundingShapeAll clone() {
		return this;
	}

	@Override
	public BoundingShapeAll asAbsolute(Vec3 testerPos) {
		return this;
	}

	@Override
	public boolean isAbsolute() {
		return true;
	}

	@Override
	public BoundingShapeDefinition<BoundingShapeAll> definition() {
		return FECoreBoundingShapes.ALL.get();
	}

	@Override
	public void getPropertiesFrom(BoundingShape other) {}
}