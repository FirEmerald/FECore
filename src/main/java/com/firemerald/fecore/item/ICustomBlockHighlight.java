package com.firemerald.fecore.item;

import com.firemerald.fecore.client.IBlockHighlight;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHighlightEvent;

public interface ICustomBlockHighlight
{
	@OnlyIn(Dist.CLIENT)
	public IBlockHighlight getBlockHighlight(Player player, RenderHighlightEvent.Block event);
}