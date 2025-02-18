package com.firemerald.fecore.capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.boundingshapes.BoundingShape;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface IShapeHolder extends ICapabilitySerializable<CompoundTag>
{
	public static final ResourceLocation NAME = FECoreMod.id("shape_holder");
	public static final Capability<IShapeHolder> CAP = CapabilityManager.get(new CapabilityToken<>(){});

	public static LazyOptional<IShapeHolder> get(ICapabilityProvider obj)
	{
		return obj.getCapability(CAP);
	}

	public static LazyOptional<IShapeHolder> get(ICapabilityProvider obj, @Nullable Direction side)
	{
		return obj.getCapability(CAP, side);
	}

	public static IShapeHolder getOrNull(ICapabilityProvider obj)
	{
		return get(obj).resolve().orElse(null);
	}

	public static IShapeHolder getOrNull(ICapabilityProvider obj, @Nullable Direction side)
	{
		return get(obj, side).resolve().orElse(null);
	}

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
			CompoundTag tag;
			if (shape != null) tag = (CompoundTag) BoundingShape.CODEC.encode(shape, NbtOps.INSTANCE, EndTag.INSTANCE).result().orElseGet(CompoundTag::new);
			else tag = new CompoundTag();
			return tag;
		}

		@Override
		public void deserializeNBT(CompoundTag nbt)
		{
			if (nbt.isEmpty()) shape = null;
			else {
				DataResult<Pair<BoundingShape, Tag>> decoded = BoundingShape.CODEC.decode(NbtOps.INSTANCE, nbt);
				shape = decoded.get().map(p -> p.getFirst(), e -> null);
			}
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
	        return CAP.orEmpty(capability, holder);
	    }
	}
}
