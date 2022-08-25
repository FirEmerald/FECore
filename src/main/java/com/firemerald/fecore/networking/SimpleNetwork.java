package com.firemerald.fecore.networking;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.firemerald.fecore.networking.client.ClientPacket;
import com.firemerald.fecore.networking.server.ServerPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.TargetPoint;
import net.minecraftforge.network.simple.SimpleChannel;

public class SimpleNetwork
{
	public final String protocolVersion;
	public final SimpleChannel channel;
	private int packetId = 0;

	public SimpleNetwork(ResourceLocation name, String protocolVersion)
	{
		this.protocolVersion = protocolVersion;
		this.channel = NetworkRegistry.newSimpleChannel(
				name,
				() -> SimpleNetwork.this.protocolVersion,
				SimpleNetwork.this.protocolVersion::equals,
				SimpleNetwork.this.protocolVersion::equals
				);
	}

	public <T> SimpleNetwork registerPacket(Class<T> clazz, BiConsumer<T, FriendlyByteBuf> write, Function<FriendlyByteBuf, T> read, BiConsumer<T, Supplier<Context>> handler)
	{
		channel.registerMessage(packetId++, clazz, write, read, handler);
		return this;
	}

	public <T> SimpleNetwork registerPacket(Class<T> clazz, BiConsumer<T, FriendlyByteBuf> write, Function<FriendlyByteBuf, T> read, BiConsumer<T, Supplier<Context>> handler, NetworkDirection direction)
	{
		channel.registerMessage(packetId++, clazz, write, read, handler, Optional.of(direction));
		return this;
	}

	public <T extends PacketBase> SimpleNetwork registerPacket(Class<T> clazz, Function<FriendlyByteBuf, T> constructor)
	{
		return registerPacket(clazz, T::write, constructor, T::handle);
	}

	public <T extends PacketBase> SimpleNetwork registerPacket(Class<T> clazz, Function<FriendlyByteBuf, T> constructor, NetworkDirection direction)
	{
		return registerPacket(clazz, T::write, constructor, T::handle, direction);
	}

	public <T extends ClientPacket> SimpleNetwork registerClientPacket(Class<T> clazz, Function<FriendlyByteBuf, T> constructor)
	{
		return registerPacket(clazz, constructor, NetworkDirection.PLAY_TO_CLIENT);
	}

	public <T extends ClientPacket> SimpleNetwork registerClientPacketLogin(Class<T> clazz, Function<FriendlyByteBuf, T> constructor)
	{
		return registerPacket(clazz, constructor, NetworkDirection.LOGIN_TO_CLIENT);
	}

	public <T extends ServerPacket> SimpleNetwork registerServerPacket(Class<T> clazz, Function<FriendlyByteBuf, T> constructor)
	{
		return registerPacket(clazz, constructor, NetworkDirection.PLAY_TO_SERVER);
	}

	public <T extends ServerPacket> SimpleNetwork registerServerPacketLogin(Class<T> clazz, Function<FriendlyByteBuf, T> constructor)
	{
		return registerPacket(clazz, constructor, NetworkDirection.LOGIN_TO_SERVER);
	}

    public void sendTo(Object message, ServerPlayer player)
    {
    	channel.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public void sendToAllIn(Object message, ResourceKey<Level> level)
    {
    	channel.send(PacketDistributor.DIMENSION.with(() -> level), message);
    }

    public void sendToAllNear(Object message, TargetPoint point)
    {
    	channel.send(PacketDistributor.NEAR.with(() -> point), message);
    }

    public void sendToAll(Object message)
    {
    	channel.send(PacketDistributor.ALL.noArg(), message);
    }

    public void sendToServer(Object message)
    {
    	channel.sendToServer(message);
    }

    public void sendToAllTracking(Object message, Entity entity)
    {
    	channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }

    public void sendToAllTrackingAndSelf(Object message, Entity entity)
    {
    	channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
    }

    public void sendToAllTracking(Object message, LevelChunk chunk)
    {
    	channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), message);
    }


    public static FriendlyByteBuf readBuffer(FriendlyByteBuf buf)
    {
    	int length = buf.readVarInt();
    	int index = buf.readerIndex();
    	FriendlyByteBuf ret = new FriendlyByteBuf(buf.copy(index, length));
    	buf.readerIndex(index + length);
    	return ret;
    }

    public static void writeBuffer(FriendlyByteBuf buf, FriendlyByteBuf val)
    {
    	int index = val.readerIndex();
    	int length = val.readableBytes();
    	buf.writeVarInt(length);
    	buf.writeBytes(val, length);
    	val.readerIndex(index);
    }
}
