package com.firemerald.fecore.network.clientbound;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.client.gui.screen.NetworkedGUIEntityScreen;
import com.firemerald.fecore.network.NetworkUtil;
import com.firemerald.fecore.util.INetworkedGUIEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class BlockEntityGUIPacket<T extends BlockEntity & INetworkedGUIEntity<T>> extends ClientboundPacket<RegistryFriendlyByteBuf> {
	public static final Type<BlockEntityGUIPacket<?>> TYPE = new Type<>(FECoreMod.id("blockentity_gui"));

	private final BlockPos position;
	private final RegistryFriendlyByteBuf data;

	public BlockEntityGUIPacket(T tile) {
		this.position = tile.getBlockPos();
		this.data = NetworkUtil.newBuffer(tile.getLevel().registryAccess());
		tile.write(data);
	}

	public BlockEntityGUIPacket(RegistryFriendlyByteBuf buf) {
		position = buf.readBlockPos();
		data = NetworkUtil.readBuffer(buf);
	}

	@Override
	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeBlockPos(position);
		NetworkUtil.writeBuffer(buf, data);
	}

	@SuppressWarnings("resource")
	@Override
	public void handleClient(IPayloadContext context) {
		context.enqueueWork(() -> {
			if (Minecraft.getInstance().level != null)
			{
				BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(position);
				if (blockEntity instanceof INetworkedGUIEntity)
				{
					@SuppressWarnings("unchecked")
					NetworkedGUIEntityScreen<T> gui = ((T) blockEntity).getScreen();
					int index = data.readerIndex();
					gui.read(data);
					data.readerIndex(index);
					Minecraft.getInstance().setScreen(gui);
				}
			}
		});
	}

	@Override
	public Type<BlockEntityGUIPacket<?>> type() {
		return TYPE;
	}

}
