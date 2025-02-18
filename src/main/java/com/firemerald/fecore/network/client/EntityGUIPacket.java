package com.firemerald.fecore.network.client;

import com.firemerald.fecore.client.gui.screen.NetworkedGUIScreen;
import com.firemerald.fecore.network.SimpleNetwork;
import com.firemerald.fecore.util.INetworkedGUIEntity;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class EntityGUIPacket<T extends Entity & INetworkedGUIEntity<T>> extends ClientPacket
{
	private final int entityId;
	private final FriendlyByteBuf data;

	public EntityGUIPacket(T entity)
	{
		this.entityId = entity.getId();
		this.data = new FriendlyByteBuf(Unpooled.buffer(0));
		entity.write(data);
	}

	public EntityGUIPacket(FriendlyByteBuf buf)
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

	@SuppressWarnings("resource")
	@Override
	@OnlyIn(Dist.CLIENT)
	public void handleClient(NetworkEvent.Context ctx)
	{
		ctx.enqueueWork(() -> {
			if (Minecraft.getInstance().level != null)
			{
				Entity entity = Minecraft.getInstance().level.getEntity(entityId);
				if (entity instanceof INetworkedGUIEntity)
				{
					@SuppressWarnings("unchecked")
					NetworkedGUIScreen gui = ((T) entity).getScreen();
					int index = data.readerIndex();
					gui.read(data);
					data.readerIndex(index);
					Minecraft.getInstance().setScreen(gui);
				}
			}
		});
	}
}
