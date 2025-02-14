package com.firemerald.fecore;

import org.slf4j.Logger;

import com.firemerald.fecore.config.ClientConfig;
import com.firemerald.fecore.init.FECoreBoundingShapes;
import com.firemerald.fecore.init.FECoreDataComponents;
import com.firemerald.fecore.init.FECoreObjects;
import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(FECoreMod.MOD_ID)
public class FECoreMod
{
    public static final String MOD_ID = "fecore";
    public static final Logger LOGGER = LogUtils.getLogger();

    public FECoreMod(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
		FECoreObjects.init(modEventBus);
		FECoreBoundingShapes.init(modEventBus);
		FECoreDataComponents.init(modEventBus);
    }

    public static ResourceLocation id(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}
}
