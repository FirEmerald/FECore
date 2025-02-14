package com.firemerald.fecore.network.serverbound;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.client.gui.screen.NetworkedGUIEntityScreen;
import com.firemerald.fecore.network.NetworkUtil;
import com.firemerald.fecore.util.INetworkedGUIEntity;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class EntityGUIClosedPacket<T extends Entity & INetworkedGUIEntity<T>> extends ServerboundPacket<RegistryFriendlyByteBuf> {
	public static final Type<EntityGUIClosedPacket<?>> TYPE = new Type<>(FECoreMod.id("entity_gui_closed"));

	private final int entityId;
	private final RegistryFriendlyByteBuf data;

	public EntityGUIClosedPacket(NetworkedGUIEntityScreen<T> gui) {
		this.entityId = gui.entity.getId();
		this.data = NetworkUtil.newBuffer(gui.entity.level().registryAccess());
		gui.write(data);
	}

	public EntityGUIClosedPacket(RegistryFriendlyByteBuf buf) {
		entityId = buf.readVarInt();
		data = NetworkUtil.readBuffer(buf);
	}

	@Override
	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeVarInt(entityId);
		NetworkUtil.writeBuffer(buf, data);
	}

	@Override
	public void handleServer(IPayloadContext context) {
		final Player player = context.player();
		context.enqueueWork(() -> {
			Entity entity = player.level().getEntity(entityId);
			if (entity instanceof INetworkedGUIEntity)
			{
				@SuppressWarnings("unchecked")
				T guiTile = (T) entity;
				int index = data.readerIndex();
				guiTile.read(data);
				data.readerIndex(index);
			}
		});
	}

	@Override
	public Type<EntityGUIClosedPacket<?>> type() {
		return TYPE;
	}

}
