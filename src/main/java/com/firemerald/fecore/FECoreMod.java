package com.firemerald.fecore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firemerald.fecore.capabilities.IShapeHolder;
import com.firemerald.fecore.capabilities.IShapeTool;
import com.firemerald.fecore.client.ConfigClient;
import com.firemerald.fecore.client.gui.screen.ModConfigScreen;
import com.firemerald.fecore.init.FECoreItems;
import com.firemerald.fecore.init.registry.DeferredObjectRegistry;
import com.firemerald.fecore.networking.SimpleNetwork;
import com.firemerald.fecore.networking.client.BlockEntityGUIPacket;
import com.firemerald.fecore.networking.client.ShapeToolScreenPacket;
import com.firemerald.fecore.networking.server.BlockEntityGUIClosedPacket;
import com.firemerald.fecore.networking.server.ShapeToolClickedPacket;
import com.firemerald.fecore.networking.server.ShapeToolSetPacket;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(FECoreMod.MOD_ID)
public class FECoreMod
{
	public static final String MOD_ID = "fecore";
    public static final Logger LOGGER = LoggerFactory.getLogger("FECore");
    public static final SimpleNetwork NETWORK = new SimpleNetwork(new ResourceLocation(MOD_ID, "main"), "1");
    
    public static final DeferredObjectRegistry REGISTRY = new DeferredObjectRegistry(MOD_ID);

    public FECoreMod()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerCaps);
        new ConfigClient(ModLoadingContext.get());
        FECoreItems.init();
        if (FMLEnvironment.dist.isClient()) doClientStuff();
        REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @OnlyIn(Dist.CLIENT)
    public void doClientStuff()
    {
    	ModLoadingContext.get().registerExtensionPoint(ConfigGuiFactory.class, () -> new ConfigGuiFactory((mc, prev) -> {
    		return new ModConfigScreen(prev);
    	}));
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    	NETWORK.registerServerPacket(BlockEntityGUIClosedPacket.class, BlockEntityGUIClosedPacket::new);
    	NETWORK.registerClientPacket(ShapeToolScreenPacket.class, ShapeToolScreenPacket::new);
    	NETWORK.registerClientPacket(BlockEntityGUIPacket.class, BlockEntityGUIPacket::new);
    	NETWORK.registerServerPacket(ShapeToolSetPacket.class, ShapeToolSetPacket::new);
    	NETWORK.registerServerPacket(ShapeToolClickedPacket.class, ShapeToolClickedPacket::new);
    }

	public void registerCaps(RegisterCapabilitiesEvent event)
	{
		event.register(IShapeHolder.class);
		event.register(IShapeTool.class);
	}
}
