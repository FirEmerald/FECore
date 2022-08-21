package com.firemerald.fecore.networking;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.networking.client.*;
import com.firemerald.fecore.networking.server.*;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class FECoreNetwork
{
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
      new ResourceLocation(FECoreMod.MOD_ID, "main"),
      () -> PROTOCOL_VERSION,
      PROTOCOL_VERSION::equals,
      PROTOCOL_VERSION::equals
    );
    
    public static void init()
    {
    	int id  = 0;
    	INSTANCE.registerMessage(id++, ShapeToolSetPacket.class, ShapeToolSetPacket::write, ShapeToolSetPacket::new, ShapeToolSetPacket::handle);
    	INSTANCE.registerMessage(id++, ShapeToolClickedPacket.class, ShapeToolClickedPacket::write, ShapeToolClickedPacket::new, ShapeToolClickedPacket::handle);
    	INSTANCE.registerMessage(id++, TileGUIClosedPacket.class, TileGUIClosedPacket::write, TileGUIClosedPacket::new, TileGUIClosedPacket::handle);
    	INSTANCE.registerMessage(id++, ShapeToolScreenPacket.class, ShapeToolScreenPacket::write, ShapeToolScreenPacket::new, ShapeToolScreenPacket::handle);
    	INSTANCE.registerMessage(id++, TileGUIPacket.class, TileGUIPacket::write, TileGUIPacket::new, TileGUIPacket::handle);
    }
    
    public static BlockPos readBlockpos(FriendlyByteBuf buf)
    {
    	return buf.readBlockPos();
    }
    
    public static FriendlyByteBuf readBuffer(FriendlyByteBuf buf)
    {
    	int index = buf.readerIndex();
    	int length = buf.readVarInt();
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
