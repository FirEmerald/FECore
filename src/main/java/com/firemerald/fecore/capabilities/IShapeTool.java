package com.firemerald.fecore.capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.boundingshapes.BoundingShape;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public interface IShapeTool extends IShapeHolder
{
	public static final ResourceLocation NAME = new ResourceLocation(FECoreMod.MOD_ID, "shape_tool");
	public static final Capability<IShapeTool> CAP = CapabilityManager.get(new CapabilityToken<>(){});

	public static LazyOptional<IShapeTool> get(ICapabilityProvider obj)
	{
		return obj.getCapability(CAP);
	}

	public static LazyOptional<IShapeTool> get(ICapabilityProvider obj, @Nullable Direction side)
	{
		return obj.getCapability(CAP, side);
	}

	public static IShapeTool getOrNull(ICapabilityProvider obj)
	{
		return get(obj).resolve().orElse(null);
	}

	public static IShapeTool getOrNull(ICapabilityProvider obj, @Nullable Direction side)
	{
		return get(obj, side).resolve().orElse(null);
	}

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
	    	return (capability == IShapeTool.CAP || capability == IShapeHolder.CAP) ? holder.cast() : LazyOptional.empty();
	    }
	}
}
