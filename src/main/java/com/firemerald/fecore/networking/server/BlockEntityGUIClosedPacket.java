package com.firemerald.fecore.networking.server;

import com.firemerald.fecore.blockentity.BlockEntityGUI;
import com.firemerald.fecore.client.gui.screen.BlockEntityGUIScreen;
import com.firemerald.fecore.networking.SimpleNetwork;

import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class BlockEntityGUIClosedPacket extends ServerPacket
{
	private final BlockPos position;
	private final FriendlyByteBuf data;

	@OnlyIn(Dist.CLIENT)
	public BlockEntityGUIClosedPacket(BlockEntityGUIScreen gui)
	{
		this.position = gui.getTilePos();
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
			BlockEntity tile = player.level.getBlockEntity(position);
			if (tile instanceof BlockEntityGUI)
			{
				BlockEntityGUI guiTile = (BlockEntityGUI) tile;
				int index = data.readerIndex();
				guiTile.readInternal(data);
				data.readerIndex(index);
				guiTile.setChanged();
			}
		});
	}
}
