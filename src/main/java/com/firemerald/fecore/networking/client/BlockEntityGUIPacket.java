package com.firemerald.fecore.networking.client;

import com.firemerald.fecore.blockentity.BlockEntityGUI;
import com.firemerald.fecore.client.gui.screen.BlockEntityGUIScreen;
import com.firemerald.fecore.networking.SimpleNetwork;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class BlockEntityGUIPacket extends ClientPacket
{
	private final BlockPos position;
	private final FriendlyByteBuf data;

	public BlockEntityGUIPacket(BlockEntityGUI tile)
	{
		this.position = tile.getBlockPos();
		this.data = new FriendlyByteBuf(Unpooled.buffer(0));
		tile.write(data);
	}

	public BlockEntityGUIPacket(FriendlyByteBuf buf)
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
