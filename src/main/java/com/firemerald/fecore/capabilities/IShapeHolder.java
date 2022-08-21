package com.firemerald.fecore.capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.firemerald.fecore.selectionshapes.BoundingShape;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface IShapeHolder extends ICapabilitySerializable<CompoundTag>
{
	public abstract BoundingShape getShape();

	public abstract boolean canAcceptShape(BoundingShape shape);

	public abstract void setShape(BoundingShape shape);
	
	public static class Impl implements IShapeHolder
	{
	    private final LazyOptional<IShapeHolder> holder = LazyOptional.of(() -> this);
		private BoundingShape shape = null;

		@Override
		public CompoundTag serializeNBT()
		{
			CompoundTag tag = new CompoundTag();
			if (shape != null) shape.saveToNBT(tag);
			return tag;
		}

		@Override
		public void deserializeNBT(CompoundTag nbt)
		{
			shape = BoundingShape.constructFromNBTOptional(nbt);
		}

		@Override
		public BoundingShape getShape()
		{
			return this.shape;
		}

		@Override
		public boolean canAcceptShape(BoundingShape shape)
		{
			return shape != null;
		}

		@Override
		public void setShape(BoundingShape shape)
		{
			this.shape = shape;
		}
		
	    @Override
	    @Nonnull
	    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	    {
	        return FECoreCapabilities.SHAPE_HOLDER.orEmpty(capability, holder);
	    }
	}
}
