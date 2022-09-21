package com.firemerald.fecore.init.registry;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FluidObject<S extends Fluid, F extends Fluid, B extends Block, I extends Item> extends BlockObject<B, I>
{
	public final TagKey<Fluid> localTag;
	public final TagKey<Fluid> forgeTag;
	public final Supplier<S> stillFluid;
	public final Supplier<F> flowingFluid;

	public FluidObject(ResourceLocation id, String forgeName, Supplier<S> stillFluid, Supplier<F> flowingFluid, Supplier<B> block, Supplier<I> item)
	{
		super(id, block, item);
		this.localTag = FluidTags.create(id);
		this.forgeTag = FluidTags.create(new ResourceLocation("forge", forgeName));
		this.stillFluid = stillFluid;
		this.flowingFluid = flowingFluid;
	}

	public S getStillFluid()
	{
		return Objects.requireNonNull(stillFluid.get(), "FluidObject missing still fluid");
	}

	public boolean isStillFluid(Fluid fluid)
	{
		return fluid != Fluids.EMPTY && fluid == getStillFluid();
	}

	public F getFlowingFluid()
	{
		return Objects.requireNonNull(flowingFluid.get(), "FluidObject missing flowing fluid");
	}

	public boolean isFlowingFluid(Fluid fluid)
	{
		return fluid != Fluids.EMPTY && fluid == getFlowingFluid();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void setRenderLayer(RenderType layer)
	{
		ItemBlockRenderTypes.setRenderLayer(getStillFluid(), layer);
		ItemBlockRenderTypes.setRenderLayer(getFlowingFluid(), layer);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void setRenderLayer(Predicate<RenderType> layer)
	{
		ItemBlockRenderTypes.setRenderLayer(getStillFluid(), layer);
		ItemBlockRenderTypes.setRenderLayer(getFlowingFluid(), layer);
	}
}