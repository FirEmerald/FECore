package com.firemerald.fecore.client.gui.screen;

import com.firemerald.fecore.util.INetworkedGUIEntity;

import net.minecraft.network.chat.Component;

public abstract class NetworkedGUIEntityScreen<T extends INetworkedGUIEntity<T>> extends NetworkedGUIScreen
{
	public final T entity;

	public NetworkedGUIEntityScreen(Component title, T entity)
	{
		super(title);
		this.entity = entity;
	}
}