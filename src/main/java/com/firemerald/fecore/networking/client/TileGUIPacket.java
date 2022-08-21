package com.firemerald.fecore.networking.client;

import com.firemerald.fecore.betterscreens.BlockEntityGUI;
import com.firemerald.fecore.betterscreens.BlockEntityGUIScreen;
import com.firemerald.fecore.networking.FECoreNetwork;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class TileGUIPacket extends ClientPacket
{
	private final BlockPos position;
	private final FriendlyByteBuf data;

	public TileGUIPacket(BlockEntityGUI tile)
	{
		this.position = tile.getBlockPos();
		this.data = new FriendlyByteBuf(Unpooled.buffer(0));
		tile.write(data);
	}

	public TileGUIPacket(FriendlyByteBuf buf)
	{
		position = buf.readBlockPos();
		data = FECoreNetwork.readBuffer(buf);
	}

	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeBlockPos(position);
		FECoreNetwork.writeBuffer(buf, data);
	}

	@SuppressWarnings("resource")
	@Override
	@OnlyIn(Dist.CLIENT)
	public void handleClient(NetworkEvent.Context ctx)
	{
		ctx.enqueueWork(() -> {
			if (Minecraft.getInstance().level != null)
			{
				BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(position);
				if (blockEntity instanceof BlockEntityGUI)
				{
					BlockEntityGUIScreen gui = ((BlockEntityGUI) blockEntity).getScreen();
					int index = data.readerIndex();
					gui.read(data);
					data.readerIndex(index);
					Minecraft.getInstance().setScreen(gui);
				}
			}
		});
	}
}
