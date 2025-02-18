package com.firemerald.fecore.network.server;

import com.firemerald.fecore.client.gui.screen.NetworkedGUIEntityScreen;
import com.firemerald.fecore.network.SimpleNetwork;
import com.firemerald.fecore.util.INetworkedGUIEntity;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class EntityGUIClosedPacket<T extends Entity & INetworkedGUIEntity<T>> extends ServerPacket
{
	private final int entityId;
	private final FriendlyByteBuf data;

	@OnlyIn(Dist.CLIENT)
	public EntityGUIClosedPacket(NetworkedGUIEntityScreen<T> gui)
	{
		this.entityId = gui.entity.getId();
		this.data = new FriendlyByteBuf(Unpooled.buffer(0));
		gui.write(data);
	}

	public EntityGUIClosedPacket(FriendlyByteBuf buf)
	{
		entityId = buf.readVarInt();
		data = SimpleNetwork.readBuffer(buf);
	}

	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeVarInt(entityId);
		SimpleNetwork.writeBuffer(buf, data);
	}

	@Override
	public void handleServer(NetworkEvent.Context ctx)
	{
		final Player player = ctx.getSender();
		ctx.enqueueWork(() -> {
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
}
