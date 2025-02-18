package com.firemerald.fecore.network.server;

import com.firemerald.fecore.client.gui.screen.NetworkedGUIEntityScreen;
import com.firemerald.fecore.network.SimpleNetwork;
import com.firemerald.fecore.util.INetworkedGUIEntity;

import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class BlockEntityGUIClosedPacket<T extends BlockEntity & INetworkedGUIEntity<T>> extends ServerPacket
{
	private final BlockPos position;
	private final FriendlyByteBuf data;

	@OnlyIn(Dist.CLIENT)
	public BlockEntityGUIClosedPacket(NetworkedGUIEntityScreen<T> gui)
	{
		this.position = gui.entity.getBlockPos();
		this.data = new FriendlyByteBuf(Unpooled.buffer(0));
		gui.write(data);
	}

	public BlockEntityGUIClosedPacket(FriendlyByteBuf buf)
	{
		position = buf.readBlockPos();
		data = SimpleNetwork.readBuffer(buf);
	}

	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeBlockPos(position);
		SimpleNetwork.writeBuffer(buf, data);
	}

	@Override
	public void handleServer(NetworkEvent.Context ctx)
	{
		final Player player = ctx.getSender();
		ctx.enqueueWork(() -> {
			BlockEntity tile = player.level().getBlockEntity(position);
			if (tile instanceof INetworkedGUIEntity)
			{
				@SuppressWarnings("unchecked")
				T guiTile = (T) tile;
				int index = data.readerIndex();
				guiTile.read(data);
				data.readerIndex(index);
				guiTile.setChanged();
			}
		});
	}
}
