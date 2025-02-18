package com.firemerald.fecore.config;

import com.firemerald.fecore.FECoreMod;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@EventBusSubscriber(modid = FECoreMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ClientConfig {
	public static final DoubleValue SCROLL_SENSITIVITY;
	public static final ForgeConfigSpec SPEC;

	static {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Client only settings").push("client");
        SCROLL_SENSITIVITY = builder
        		.comment("Modifies the sensitivity of scrolling for BetterGUI scrollables. Default: 10.0")
        		.translation("fecore.config.scrollsensitivity")
        		.defineInRange("scrollSensitivity", 10.0, 0, Double.POSITIVE_INFINITY);
        SPEC = builder.build();
	}

	//public static double scrollSensitivity;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
    	//scrollSensitivity = SCROLL_SENSITIVITY.getAsDouble();
    }
}
