package com.firemerald.fecore.init;

import java.util.function.Supplier;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.boundingshapes.BoundingShape;
import com.firemerald.fecore.boundingshapes.BoundingShapeAddition;
import com.firemerald.fecore.boundingshapes.BoundingShapeAll;
import com.firemerald.fecore.boundingshapes.BoundingShapeBoxOffsets;
import com.firemerald.fecore.boundingshapes.BoundingShapeBoxPositions;
import com.firemerald.fecore.boundingshapes.BoundingShapeCylinder;
import com.firemerald.fecore.boundingshapes.BoundingShapeDefinition;
import com.firemerald.fecore.boundingshapes.BoundingShapeIntersection;
import com.firemerald.fecore.boundingshapes.BoundingShapeInversion;
import com.firemerald.fecore.boundingshapes.BoundingShapePolygon;
import com.firemerald.fecore.boundingshapes.BoundingShapeSphere;
import com.firemerald.fecore.boundingshapes.IConfigurableBoundingShape;
import com.firemerald.fecore.codec.stream.StreamCodec;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class FECoreBoundingShapes {
    public static final ResourceKey<Registry<BoundingShapeDefinition<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(FECoreMod.id("bounding_shape_definitions"));

	private static DeferredRegister<BoundingShapeDefinition<?>> registry = DeferredRegister.create(REGISTRY_KEY, FECoreMod.MOD_ID);

	public static final Supplier<IForgeRegistry<BoundingShapeDefinition<?>>> REGISTRY = registry.makeRegistry(() -> RegistryBuilder.of(REGISTRY_KEY.location()));
	public static final RegistryObject<BoundingShapeDefinition<BoundingShapeAll>> ALL = register("all",
			BoundingShapeAll.CODEC,
			BoundingShapeAll.STREAM_CODEC,
			() -> BoundingShapeAll.INSTANCE);
	public static final RegistryObject<BoundingShapeDefinition<BoundingShapeBoxOffsets>> BOX_OFFSETS = registerConfigurable("boxoffsets",
			BoundingShapeBoxOffsets.CODEC,
			BoundingShapeBoxOffsets.STREAM_CODEC,
			BoundingShapeBoxOffsets::new);
	public static final RegistryObject<BoundingShapeDefinition<BoundingShapeBoxPositions>> BOX_POSITIONS = registerConfigurable("boxpositions",
			BoundingShapeBoxPositions.CODEC,
			BoundingShapeBoxPositions.STREAM_CODEC,
			BoundingShapeBoxPositions::new);
	public static final RegistryObject<BoundingShapeDefinition<BoundingShapeCylinder>> CYLINDER = registerConfigurable("cylinder",
			BoundingShapeCylinder.CODEC,
			BoundingShapeCylinder.STREAM_CODEC,
			BoundingShapeCylinder::new);
	public static final RegistryObject<BoundingShapeDefinition<BoundingShapeSphere>> SPHERE = registerConfigurable("sphere",
			BoundingShapeSphere.CODEC,
			BoundingShapeSphere.STREAM_CODEC,
			BoundingShapeSphere::new);
	public static final RegistryObject<BoundingShapeDefinition<BoundingShapePolygon>> POLYGON = registerConfigurable("polygon",
			BoundingShapePolygon.CODEC,
			BoundingShapePolygon.STREAM_CODEC,
			BoundingShapePolygon::new);
	public static final RegistryObject<BoundingShapeDefinition<BoundingShapeAddition>> ADDITION = register("addition",
			BoundingShapeAddition.CODEC,
			BoundingShapeAddition.STREAM_CODEC,
			BoundingShapeAddition::new);
	public static final RegistryObject<BoundingShapeDefinition<BoundingShapeIntersection>> INTERSECTION = register("intersection",
			BoundingShapeIntersection.CODEC,
			BoundingShapeIntersection.STREAM_CODEC,
			BoundingShapeIntersection::new);
	public static final RegistryObject<BoundingShapeDefinition<BoundingShapeInversion>> INVERSION = register("inversion",
			BoundingShapeInversion.CODEC,
			BoundingShapeInversion.STREAM_CODEC,
			BoundingShapeInversion::new);

	private static <T extends BoundingShape> RegistryObject<BoundingShapeDefinition<T>> register(String name, MapCodec<T> codec, StreamCodec<T> streamCodec, Supplier<T> constructor) {
		return registry.register(name, () -> BoundingShapeDefinition.of(codec, streamCodec, constructor));
	}

	private static <T extends BoundingShape & IConfigurableBoundingShape> RegistryObject<BoundingShapeDefinition<T>> registerConfigurable(String name, MapCodec<T> codec, StreamCodec<T> streamCodec, Supplier<T> constructor) {
		return registry.register(name, () -> BoundingShapeDefinition.ofConfigurable(codec, streamCodec, constructor));
	}

	public static void init(IEventBus bus) {
		registry.register(bus);
		registry = null;
	}
}
