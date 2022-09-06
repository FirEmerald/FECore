package com.firemerald.fecore.item;

import com.firemerald.fecore.block.ICustomBlockHighlight;
import com.firemerald.fecore.client.IBlockHighlight;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;

public class FEBlockItem extends BlockItem implements ICustomBlockHighlight
{
	public final Block block;
	
	public FEBlockItem(Block block, Properties properties)
	{
		super(block, properties);
		this.block = block;
	}

	@Override
	public IBlockHighlight getBlockHighlight(Player player, BlockHitResult trace)
	{
		return block instanceof ICustomBlockHighlight ? ((ICustomBlockHighlight) block).getBlockHighlight(player, trace) : null;
	}
}