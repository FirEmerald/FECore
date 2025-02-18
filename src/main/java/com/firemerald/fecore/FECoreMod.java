package com.firemerald.fecore;

import org.slf4j.Logger;

import com.firemerald.fecore.capabilities.IShapeHolder;
import com.firemerald.fecore.capabilities.IShapeTool;
import com.firemerald.fecore.client.gui.screen.ModConfigScreen;
import com.firemerald.fecore.config.ClientConfig;
import com.firemerald.fecore.init.FECoreBoundingShapes;
import com.firemerald.fecore.init.FECoreObjects;
import com.firemerald.fecore.network.SimpleNetwork;
import com.firemerald.fecore.network.client.BlockEntityGUIPacket;
import com.firemerald.fecore.network.client.EntityGUIPacket;
import com.firemerald.fecore.network.client.ShapeToolScreenPacket;
import com.firemerald.fecore.network.server.BlockEntityGUIClosedPacket;
import com.firemerald.fecore.network.server.EntityGUIClosedPacket;
import com.firemerald.fecore.network.server.ShapeToolClickedPacket;
import com.firemerald.fecore.network.server.ShapeToolSetPacket;
import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(FECoreMod.MOD_ID)
public class FECoreMod {
    public static final String MOD_ID = "fecore";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final SimpleNetwork NETWORK = new SimpleNetwork(id("main"), "3");

    public static ResourceLocation id(String name) {
		return new ResourceLocation(MOD_ID, name);
	}

    public FECoreMod(FMLJavaModLoadingContext loadingContext) {
    	IEventBus modEventBus = loadingContext.getModEventBus();
    	loadingContext.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    	modEventBus.addListener(this::setup);
    	modEventBus.addListener(this::registerCaps);
		FECoreObjects.init(modEventBus);
		FECoreBoundingShapes.init(modEventBus);
        if (FMLEnvironment.dist.isClient()) doClientStuff(loadingContext);
    }

    @OnlyIn(Dist.CLIENT)
    public void doClientStuff(FMLJavaModLoadingContext loadingContext) {
    	loadingContext.registerExtensionPoint(ConfigScreenFactory.class, () -> new ConfigScreenFactory((mc, prev) -> new ModConfigScreen(prev)));
    }

    private void setup(final FMLCommonSetupEvent event) {
    	NETWORK.registerServerPacket(BlockEntityGUIClosedPacket.class, BlockEntityGUIClosedPacket::new);
    	NETWORK.registerServerPacket(EntityGUIClosedPacket.class, EntityGUIClosedPacket::new);
    	NETWORK.registerClientPacket(ShapeToolScreenPacket.class, ShapeToolScreenPacket::new);
    	NETWORK.registerClientPacket(BlockEntityGUIPacket.class, BlockEntityGUIPacket::new);
    	NETWORK.registerClientPacket(EntityGUIPacket.class, EntityGUIPacket::new);
    	NETWORK.registerServerPacket(ShapeToolSetPacket.class, ShapeToolSetPacket::new);
    	NETWORK.registerServerPacket(ShapeToolClickedPacket.class, ShapeToolClickedPacket::new);
    }

	public void registerCaps(RegisterCapabilitiesEvent event) {
		event.register(IShapeHolder.class);
		event.register(IShapeTool.class);
	}
}
