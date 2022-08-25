package com.firemerald.fecore.boundingshapes;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class BoundingShapeConfigurable extends BoundingShape
{
	public abstract int addPosition(Player player, BlockPos blockPos, int num);

	public abstract int removePosition(Player player, int num);

	@OnlyIn(Dist.CLIENT)
    public abstract void addInformation(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn);
}