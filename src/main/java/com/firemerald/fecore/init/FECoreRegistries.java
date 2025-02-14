package com.firemerald.fecore.init;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.boundingshapes.BoundingShapeDefinition;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class FECoreRegistries {
    public static final Registry<BoundingShapeDefinition<?>> BOUNDING_SHAPE_DEFINITIONS = new RegistryBuilder<>(Keys.BOUNDING_SHAPE_DEFINITIONS).sync(true).create();

	public static void registerRegistries(NewRegistryEvent event) {
		event.register(BOUNDING_SHAPE_DEFINITIONS);
	}

    public static class Keys {
        public static final ResourceKey<Registry<BoundingShapeDefinition<?>>> BOUNDING_SHAPE_DEFINITIONS = key("bounding_shape_definitions");

        private static <T> ResourceKey<Registry<T>> key(String name) {
            return ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(FECoreMod.MOD_ID, name));
        }
    }
}
