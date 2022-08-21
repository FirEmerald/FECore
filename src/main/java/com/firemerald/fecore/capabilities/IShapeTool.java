package com.firemerald.fecore.capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.firemerald.fecore.selectionshapes.BoundingShape;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public interface IShapeTool extends IShapeHolder
{
	public abstract int getConfigurationIndex();

	public abstract void setConfigurationIndex(int index);
	
	public static class Impl extends IShapeHolder.Impl implements IShapeTool
	{
	    private final LazyOptional<IShapeTool> holder = LazyOptional.of(() -> this);
		private int configurationIndex = 0;

		@Override
		public CompoundTag serializeNBT()
		{
			CompoundTag tag = super.serializeNBT();
			tag.putInt("configurationIndex", configurationIndex);
			return tag;
		}

		@Override
		public void deserializeNBT(CompoundTag nbt)
		{
			super.deserializeNBT(nbt);
			configurationIndex = nbt.getInt("configurationIndex");
		}

		@Override
		public int getConfigurationIndex()
		{
			return configurationIndex;
		}

		@Override
		public void setConfigurationIndex(int index)
		{
			configurationIndex = index;
		}

		@Override
		public void setShape(BoundingShape shape)
		{
			super.setShape(shape);
			this.configurationIndex = 0;
		}
		
	    @Override
	    @Nonnull
	    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	    {
	    	return (capability == FECoreCapabilities.SHAPE_TOOL || capability == FECoreCapabilities.SHAPE_HOLDER) ? holder.cast() : LazyOptional.empty();
	    }
	}
}
