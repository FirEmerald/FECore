package com.firemerald.fecore.boundingshapes;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface IConfigurableBoundingShape
{
	public abstract int addPosition(Player player, BlockPos blockPos, int num);

	public abstract int removePosition(Player player, int num);

	@OnlyIn(Dist.CLIENT)
    public abstract void addInformation(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag);
}
