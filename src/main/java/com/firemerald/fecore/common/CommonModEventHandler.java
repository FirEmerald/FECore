package com.firemerald.fecore.common;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.init.FECoreRegistries;
import com.firemerald.fecore.network.NetworkUtil;
import com.firemerald.fecore.network.clientbound.BlockEntityGUIPacket;
import com.firemerald.fecore.network.clientbound.EntityGUIPacket;
import com.firemerald.fecore.network.clientbound.ShapeToolScreenPacket;
import com.firemerald.fecore.network.serverbound.BlockEntityGUIClosedPacket;
import com.firemerald.fecore.network.serverbound.EntityGUIClosedPacket;
import com.firemerald.fecore.network.serverbound.ShapeToolClickedPacket;
import com.firemerald.fecore.network.serverbound.ShapeToolSetPacket;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid = FECoreMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CommonModEventHandler {
	@SubscribeEvent
	public static void registerRegistries(NewRegistryEvent event) {
		FECoreRegistries.registerRegistries(event);
	}

	@SubscribeEvent
	public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar(FECoreMod.MOD_ID);
		NetworkUtil.playToClient(registrar, BlockEntityGUIPacket.TYPE, BlockEntityGUIPacket::new);
		NetworkUtil.playToClient(registrar, EntityGUIPacket.TYPE, EntityGUIPacket::new);
		NetworkUtil.playToClient(registrar, ShapeToolScreenPacket.TYPE, ShapeToolScreenPacket::new);
		NetworkUtil.playToServer(registrar, BlockEntityGUIClosedPacket.TYPE, BlockEntityGUIClosedPacket::new);
		NetworkUtil.playToServer(registrar, EntityGUIClosedPacket.TYPE, EntityGUIClosedPacket::new);
		NetworkUtil.playToServer(registrar, ShapeToolClickedPacket.TYPE, ShapeToolClickedPacket::new);
		NetworkUtil.playToServer(registrar, ShapeToolSetPacket.TYPE, ShapeToolSetPacket::new);
	}

	@SubscribeEvent
	public static void gatherClientData(GatherDataEvent.Client event) {
		gatherData(event);
	}

	public static void gatherData(GatherDataEvent event) {}
}
