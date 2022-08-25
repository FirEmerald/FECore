package com.firemerald.fecore.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.firemerald.fecore.client.gui.IScrollValuesHolder;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;

@Mixin(MouseHandler.class)
public class MixinMouseHandler implements IScrollValuesHolder
{
	double scrollX, scrollY;

	@Inject(method = "onScroll", at = @At("HEAD"))
	public void onScrollPre(long window, double scrollX, double scrollY, CallbackInfo ci)
	{
		if (window == Minecraft.getInstance().getWindow().getWindow())
		{
			this.scrollX = scrollX;
			this.scrollY = scrollY;
		}
	}

	@Override
	public double getScrollX()
	{
		return scrollX;
	}
	
	@Override
	public double getScrollY()
	{
		return scrollY;
	}
}