package com.firemerald.fecore.util;

import com.firemerald.fecore.client.gui.screen.NetworkedGUIEntityScreen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface INetworkedGUIEntity<T extends INetworkedGUIEntity<T>>
{
	public abstract void read(FriendlyByteBuf buf);

	public abstract void write(FriendlyByteBuf buf);

	@OnlyIn(Dist.CLIENT)
	public abstract NetworkedGUIEntityScreen<T> getScreen();
}