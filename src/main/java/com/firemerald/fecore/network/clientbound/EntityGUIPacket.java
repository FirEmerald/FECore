package com.firemerald.fecore.network.clientbound;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.client.gui.screen.NetworkedGUIEntityScreen;
import com.firemerald.fecore.network.NetworkUtil;
import com.firemerald.fecore.util.INetworkedGUIEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class EntityGUIPacket<T extends Entity & INetworkedGUIEntity<T>> extends ClientboundPacket<RegistryFriendlyByteBuf> {
	public static final Type<EntityGUIPacket<?>> TYPE = new Type<>(FECoreMod.id("entity_gui"));

	private final int entityId;
	private final RegistryFriendlyByteBuf data;

	public EntityGUIPacket(T entity) {
		this.entityId = entity.getId();
		this.data = NetworkUtil.newBuffer(entity.level().registryAccess());
		entity.write(data);
	}

	public EntityGUIPacket(RegistryFriendlyByteBuf buf) {
		entityId = buf.readVarInt();
		data = NetworkUtil.readBuffer(buf);
	}

	@Override
	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeVarInt(entityId);
		NetworkUtil.writeBuffer(buf, data);
	}

	@SuppressWarnings("resource")
	@Override
	public void handleClient(IPayloadContext context) {
		context.enqueueWork(() -> {
			if (Minecraft.getInstance().level != null)
			{
				Entity entity = Minecraft.getInstance().level.getEntity(entityId);
				if (entity instanceof INetworkedGUIEntity)
				{
					@SuppressWarnings("unchecked")
					NetworkedGUIEntityScreen<T> gui = ((T) entity).getScreen();
					int index = data.readerIndex();
					gui.read(data);
					data.readerIndex(index);
					Minecraft.getInstance().setScreen(gui);
				}
			}
		});
	}

	@Override
	public Type<EntityGUIPacket<?>> type() {
		return TYPE;
	}

}
