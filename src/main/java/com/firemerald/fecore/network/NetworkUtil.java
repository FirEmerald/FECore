package com.firemerald.fecore.network;

import java.util.function.Function;

import com.firemerald.fecore.network.clientbound.ClientboundPacket;
import com.firemerald.fecore.network.serverbound.ServerboundPacket;

import io.netty.buffer.Unpooled;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.connection.ConnectionType;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkUtil {
	public static <T extends ServerboundPacket<RegistryFriendlyByteBuf>> void playToServer(PayloadRegistrar registrar, Type<T> type, Function<RegistryFriendlyByteBuf, T> constructor) {
		registrar.playToServer(type, new SimpleStreamCodec<>(constructor), SimplePacket::handle);
	}

	public static <T extends ClientboundPacket<RegistryFriendlyByteBuf>> void playToClient(PayloadRegistrar registrar, Type<T> type, Function<RegistryFriendlyByteBuf, T> constructor) {
		registrar.playToClient(type, new SimpleStreamCodec<>(constructor), SimplePacket::handle);
	}

	public static <T extends ServerboundPacket<FriendlyByteBuf>> void configurationToServer(PayloadRegistrar registrar, Type<T> type, Function<FriendlyByteBuf, T> constructor) {
		registrar.configurationToServer(type, new SimpleStreamCodec<>(constructor), SimplePacket::handle);
	}

	public static <T extends ClientboundPacket<FriendlyByteBuf>> void configurationToClient(PayloadRegistrar registrar, Type<T> type, Function<FriendlyByteBuf, T> constructor) {
		registrar.configurationToClient(type, new SimpleStreamCodec<>(constructor), SimplePacket::handle);
	}

    public static void sendToServer(CustomPacketPayload packet) {
    	PacketDistributor.sendToServer(packet);
    }

    public static void sendToClient(CustomPacketPayload packet, ServerPlayer player) {
    	PacketDistributor.sendToPlayer(player, packet);
    }

    public static void sendToAllClients(CustomPacketPayload packet) {
    	PacketDistributor.sendToAllPlayers(packet);
    }

    public static RegistryFriendlyByteBuf newBuffer(RegistryAccess registryAccess) {
    	return new RegistryFriendlyByteBuf(Unpooled.buffer(), registryAccess, ConnectionType.NEOFORGE);
    }

    public static RegistryFriendlyByteBuf readBuffer(RegistryFriendlyByteBuf buf) {
    	int length = buf.readVarInt();
    	int index = buf.readerIndex();
    	RegistryFriendlyByteBuf ret = new RegistryFriendlyByteBuf(buf.copy(index, length), buf.registryAccess(), buf.getConnectionType());
    	buf.readerIndex(index + length);
    	return ret;
    }

    public static FriendlyByteBuf readBuffer(FriendlyByteBuf buf) {
    	int length = buf.readVarInt();
    	int index = buf.readerIndex();
    	FriendlyByteBuf ret = new FriendlyByteBuf(buf.copy(index, length));
    	buf.readerIndex(index + length);
    	return ret;
    }

    public static void writeBuffer(FriendlyByteBuf buf, FriendlyByteBuf val) {
    	int index = val.readerIndex();
    	int length = val.readableBytes();
    	buf.writeVarInt(length);
    	buf.writeBytes(val, length);
    	val.readerIndex(index);
    }
}
