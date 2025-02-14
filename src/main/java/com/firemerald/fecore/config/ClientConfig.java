package com.firemerald.fecore.config;

import com.firemerald.fecore.FECoreMod;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;

@EventBusSubscriber(modid = FECoreMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ClientConfig {
	public static final DoubleValue SCROLL_SENSITIVITY;
	public static final ModConfigSpec SPEC;

	static {
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
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
