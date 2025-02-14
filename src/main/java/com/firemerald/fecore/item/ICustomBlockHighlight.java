package com.firemerald.fecore.item;

import com.firemerald.fecore.client.IBlockHighlight;

import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;

public interface ICustomBlockHighlight
{
	@OnlyIn(Dist.CLIENT)
	public IBlockHighlight getBlockHighlight(Player player, RenderHighlightEvent.Block event);
}