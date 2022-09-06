package com.firemerald.fecore.block;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public abstract class FEPartialBlock extends Block
{
	protected final BlockState model;
	
	public FEPartialBlock(BlockState model)
	{
		super(BlockBehaviour.Properties.copy(model.getBlock()));
		this.model = model;
	}
	
	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random random)
	{
		model.getBlock().animateTick(model, level, pos, random);
	}
	
	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float damage)
	{
		model.getBlock().fallOn(level, model, pos, entity, damage);
	}

	@Override
	public void updateEntityAfterFallOn(BlockGetter level, Entity entity)
	{
		model.getBlock().updateEntityAfterFallOn(level, entity);
	}
	
	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity)
	{
		model.getBlock().stepOn(level, pos, state, entity);
	}

	@Override
	public void handlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation)
	{
		model.getBlock().handlePrecipitation(model, level, pos, precipitation);
	}
}
