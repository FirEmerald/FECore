package com.firemerald.fecore.client;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.client.gui.screen.ModConfigScreen;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = FECoreMod.MOD_ID, dist = Dist.CLIENT)
public class FECoreClient {
    public FECoreClient(IEventBus modEventBus, ModContainer modContainer) {
    	modContainer.registerExtensionPoint(IConfigScreenFactory.class, (mc, prev) -> new ModConfigScreen(prev));
    }
}
