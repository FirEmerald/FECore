package com.firemerald.fecore.util;

import com.firemerald.fecore.client.gui.screen.NetworkedGUIEntityScreen;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface INetworkedGUIEntity<T extends INetworkedGUIEntity<T>>
{
	public abstract void read(RegistryFriendlyByteBuf buf);

	public abstract void write(RegistryFriendlyByteBuf buf);

	@OnlyIn(Dist.CLIENT)
	public abstract NetworkedGUIEntityScreen<T> getScreen();
}