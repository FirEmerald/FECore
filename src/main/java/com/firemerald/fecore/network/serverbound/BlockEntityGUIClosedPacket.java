package com.firemerald.fecore.network.serverbound;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.client.gui.screen.NetworkedGUIEntityScreen;
import com.firemerald.fecore.network.NetworkUtil;
import com.firemerald.fecore.util.INetworkedGUIEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class BlockEntityGUIClosedPacket<T extends BlockEntity & INetworkedGUIEntity<T>> extends ServerboundPacket<RegistryFriendlyByteBuf> {
	public static final Type<BlockEntityGUIClosedPacket<?>> TYPE = new Type<>(FECoreMod.id("blockentity_gui_closed"));

	private final BlockPos position;
	private final RegistryFriendlyByteBuf data;

	public BlockEntityGUIClosedPacket(NetworkedGUIEntityScreen<T> gui) {
		this.position = gui.entity.getBlockPos();
		this.data = NetworkUtil.newBuffer(gui.entity.getLevel().registryAccess());
		gui.write(data);
	}

	public BlockEntityGUIClosedPacket(RegistryFriendlyByteBuf buf) {
		position = buf.readBlockPos();
		data = NetworkUtil.readBuffer(buf);
	}

	@Override
	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeBlockPos(position);
		NetworkUtil.writeBuffer(buf, data);
	}

	@Override
	public void handleServer(IPayloadContext context) {
		final Player player = context.player();
		context.enqueueWork(() -> {
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

	@Override
	public Type<BlockEntityGUIClosedPacket<?>> type() {
		return TYPE;
	}

}
