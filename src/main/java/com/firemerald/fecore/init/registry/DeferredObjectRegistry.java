package com.firemerald.fecore.init.registry;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid.Flowing;
import net.minecraftforge.fluids.ForgeFlowingFluid.Properties;
import net.minecraftforge.fluids.ForgeFlowingFluid.Source;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class DeferredObjectRegistry {
	public final String modId;
	public final DeferredRegister<Fluid> fluidRegistry;
	public final DeferredRegister<BlockEntityType<?>> blockEntityRegistry;
	public final DeferredRegister<Block> blockRegistry;
	public final DeferredRegister<Item> itemRegistry;
	public final DeferredRegister<EntityType<?>> entityRegistry;

	public DeferredObjectRegistry(String modId) {
		this.modId = modId;
		fluidRegistry = DeferredRegister.create(Registries.FLUID, modId);
		blockEntityRegistry = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, modId);
		blockRegistry = DeferredRegister.create(Registries.BLOCK, modId);
		itemRegistry = DeferredRegister.create(Registries.ITEM, modId);
		entityRegistry = DeferredRegister.create(Registries.ENTITY_TYPE, modId);
	}

	public void register(IEventBus bus) {
		fluidRegistry.register(bus);
		blockEntityRegistry.register(bus);
		blockRegistry.register(bus);
		itemRegistry.register(bus);
		entityRegistry.register(bus);
	}

	public <S extends Source, F extends Flowing, B extends LiquidBlock, I extends Item> FluidObject<S, F, B, I> registerFluid(String name, String forgeName, BiFunction<Supplier<S>, Supplier<F>, Properties> fluidProperties, Function<Properties, S> still, Function<Properties, F> flowing, Function<Supplier<S>, B> block, Function<Supplier<S>, I> bucket) {
		MutableSupplier<S> stillMutable = new MutableSupplier<>();
		MutableSupplier<F> flowingMutable = new MutableSupplier<>();
		Properties properties = fluidProperties.apply(stillMutable, flowingMutable);
		RegistryObject<S> stillReg = fluidRegistry.register(name, () -> still.apply(properties));
		stillMutable.set(stillReg);
		RegistryObject<F> flowingReg = fluidRegistry.register(name + "_flowing", () -> flowing.apply(properties));
		flowingMutable.set(flowingReg);
		RegistryObject<B> blockSup;
		if (block != null) {
			blockSup = blockRegistry.register(name, () -> block.apply(stillReg));
			properties.block(blockSup);
		} else blockSup = null;
		RegistryObject<I> bucketSup;
		if (bucket != null) {
			bucketSup = itemRegistry.register(name, () -> bucket.apply(stillReg));
			properties.bucket(bucketSup);
		} else bucketSup = null;
		return new FluidObject<>(ResourceLocation.fromNamespaceAndPath(modId, name), forgeName, stillReg, flowingReg, blockSup, bucketSup);
	}

	public <S extends Source, F extends Flowing, B extends LiquidBlock, I extends Item> FluidObject<S, F, B, I> registerFluid(String name, BiFunction<Supplier<S>, Supplier<F>, Properties> fluidProperties, Function<Properties, S> still, Function<Properties, F> flowing, Function<Supplier<S>, B> block, Function<Supplier<S>, I> bucket) {
		return registerFluid(name, name, fluidProperties, still, flowing, block, bucket);
	}

	public FluidObject<Source, Flowing, LiquidBlock, BucketItem> registerFluid(String name, String forgeName, Supplier<FluidType> fluidType, Consumer<Properties> properties, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties) {
		return registerFluid(name, forgeName,
				(still, flowing) -> {
					Properties props = new Properties(fluidType, still, flowing);
					properties.accept(props);
					return props;
				},
				Source::new, Flowing::new,
				(still) -> new LiquidBlock(still, blockProperties),
				(still) -> new BucketItem(still, itemProperties)
				);
	}

	public FluidObject<Source, Flowing, LiquidBlock, BucketItem> registerFluid(String name, Supplier<FluidType> fluidType, Consumer<Properties> properties, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties) {
		return registerFluid(name, name, fluidType, properties, blockProperties, itemProperties);
	}

	public FluidObject<Source, Flowing, LiquidBlock, BucketItem> registerFluid(String name, String forgeName, Supplier<FluidType> fluidType, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties) {
		return registerFluid(name, forgeName,
				(still, flowing) -> new Properties(fluidType, still, flowing),
				Source::new, Flowing::new,
				(still) -> new LiquidBlock(still, blockProperties),
				(still) -> new BucketItem(still, itemProperties)
				);
	}

	public FluidObject<Source, Flowing, LiquidBlock, BucketItem> registerFluid(String name, Supplier<FluidType> fluidType, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties) {
		return registerFluid(name, name, fluidType, blockProperties, itemProperties);
	}

	public FluidObject<Source, Flowing, LiquidBlock, Item> registerFluidNoBucket(String name, String forgeName, Supplier<FluidType> fluidType, Consumer<Properties> properties, BlockBehaviour.Properties blockProperties) {
		return registerFluid(name, forgeName,
				(still, flowing) -> {
					Properties props = new Properties(fluidType, still, flowing);
					properties.accept(props);
					return props;
				},
				Source::new, Flowing::new,
				(still) -> new LiquidBlock(still, blockProperties),
				(Function<Supplier<Source>, Item>) null
				);
	}

	public FluidObject<Source, Flowing, LiquidBlock, Item> registerFluidNoBucket(String name, Supplier<FluidType> fluidType, Consumer<Properties> properties, BlockBehaviour.Properties blockProperties) {
		return registerFluidNoBucket(name, name, fluidType, properties, blockProperties);
	}

	public FluidObject<Source, Flowing, LiquidBlock, Item> registerFluidNoBucket(String name, String forgeName, Supplier<FluidType> fluidType, BlockBehaviour.Properties blockProperties) {
		return registerFluid(name, forgeName,
				(still, flowing) -> new Properties(fluidType, still, flowing),
				Source::new, Flowing::new,
				(still) -> new LiquidBlock(still, blockProperties),
				(Function<Supplier<Source>, Item>) null
				);
	}

	public FluidObject<Source, Flowing, LiquidBlock, Item> registerFluidNoBucket(String name, Supplier<FluidType> fluidType, BlockBehaviour.Properties blockProperties) {
		return registerFluidNoBucket(name, name, fluidType, blockProperties);
	}

	public FluidObject<Source, Flowing, ?, BucketItem> registerFluidNoBlock(String name, String forgeName, Supplier<FluidType> fluidType, Consumer<Properties> properties, Item.Properties itemProperties) {
		return registerFluid(name, forgeName,
				(still, flowing) -> {
					Properties props = new Properties(fluidType, still, flowing);
					properties.accept(props);
					return props;
				},
				Source::new, Flowing::new,
				(Function<Supplier<Source>, LiquidBlock>) null,
				(still) -> new BucketItem(still, itemProperties)
				);
	}

	public FluidObject<Source, Flowing, ?, BucketItem> registerFluidNoBlock(String name, Supplier<FluidType> fluidType, Consumer<Properties> properties, Item.Properties itemProperties) {
		return registerFluidNoBlock(name, name, fluidType, properties, itemProperties);
	}

	public FluidObject<Source, Flowing, ?, BucketItem> registerFluidNoBlock(String name, String forgeName, Supplier<FluidType> fluidType, Item.Properties itemProperties) {
		return registerFluid(name, forgeName,
				(still, flowing) -> new Properties(fluidType, still, flowing),
				Source::new, Flowing::new,
				(Function<Supplier<Source>, LiquidBlock>) null,
				(still) -> new BucketItem(still, itemProperties)
				);
	}

	public FluidObject<Source, Flowing, ?, BucketItem> registerFluidNoBlock(String name, Supplier<FluidType> fluidType, Item.Properties itemProperties) {
		return registerFluidNoBlock(name, name, fluidType, itemProperties);
	}

	public FluidObject<Source, Flowing, LiquidBlock, Item> registerFluidNoBlockOrBucket(String name, String forgeName, Supplier<FluidType> fluidType, Consumer<Properties> properties) {
		return registerFluid(name, forgeName,
				(still, flowing) -> {
					Properties props = new Properties(fluidType, still, flowing);
					properties.accept(props);
					return props;
				},
				Source::new, Flowing::new,
				(Function<Supplier<Source>, LiquidBlock>) null,
				(Function<Supplier<Source>, Item>) null
				);
	}

	public FluidObject<Source, Flowing, LiquidBlock, Item> registerFluidNoBlockOrBucket(String name, Supplier<FluidType> fluidType, Consumer<Properties> properties) {
		return registerFluidNoBlockOrBucket(name, name, fluidType, properties);
	}

	public FluidObject<Source, Flowing, LiquidBlock, Item> registerFluidNoBlockOrBucket(String name, String forgeName, Supplier<FluidType> fluidType) {
		return registerFluid(name, forgeName,
				(still, flowing) -> new Properties(fluidType, still, flowing),
				Source::new, Flowing::new,
				(Function<Supplier<Source>, LiquidBlock>) null,
				(Function<Supplier<Source>, Item>) null
				);
	}

	public FluidObject<Source, Flowing, LiquidBlock, Item> registerFluidNoBlockOrBucket(String name, Supplier<FluidType> fluidType) {
		return registerFluidNoBlockOrBucket(name, name, fluidType);
	}

	public <E extends BlockEntity, B extends Block, I extends Item> BlockEntityObject<E, B, I> registerBlockEntity(String name, Supplier<B> block, Function<Supplier<B>, I> item, BlockEntitySupplier<E> blockEntity) {
		RegistryObject<B> blockReg = blockRegistry.register(name, block);
		RegistryObject<I> itemSup;
		if (item != null) itemSup = itemRegistry.register(name, () -> item.apply(blockReg));
		else itemSup = null;
		RegistryObject<BlockEntityType<E>> blockEntityReg = blockEntityRegistry.register(name, () -> new BlockEntityType<>(blockEntity, Set.of(blockReg.get()), null));
		return new BlockEntityObject<>(ResourceLocation.fromNamespaceAndPath(modId, name), blockEntityReg, blockReg, itemSup);
	}

	public <E extends BlockEntity, B extends Block> BlockEntityObject<E, B, BlockItem> registerBlockEntity(String name, Supplier<B> block, Item.Properties itemProperties, BlockEntitySupplier<E> blockEntity) {
		return registerBlockEntity(name, block, (b) -> new BlockItem(b.get(), itemProperties), blockEntity);
	}

	public <E extends BlockEntity, B extends Block> BlockEntityObject<E, B, BlockItem> registerBlockEntity(String name, Supplier<B> block, BlockEntitySupplier<E> blockEntity) {
		return registerBlockEntity(name, block, new Item.Properties(), blockEntity);
	}

	public <E extends BlockEntity, B extends Block, I extends Item> BlockEntityObject<E, B, I> registerBlockEntityNoItem(String name, Supplier<B> block, BlockEntitySupplier<E> blockEntity) {
		return registerBlockEntity(name, block, (Function<Supplier<B>, I>) null, blockEntity);
	}

	public <B extends Block, I extends Item> BlockObject<B, I> registerBlock(String name, Supplier<B> block, Function<Supplier<B>, I> item) {
		RegistryObject<B> blockReg = blockRegistry.register(name, block);
		RegistryObject<I> itemSup;
		if (item != null) itemSup = itemRegistry.register(name, () -> item.apply(blockReg));
		else itemSup = null;
		return new BlockObject<>(ResourceLocation.fromNamespaceAndPath(modId, name), blockReg, itemSup);
	}

	public <B extends Block> BlockObject<B, BlockItem> registerBlock(String name, Supplier<B> block, Item.Properties itemProperties) {
		return registerBlock(name, block, (b) -> new BlockItem(b.get(), itemProperties));
	}

	public <B extends Block> BlockObject<B, BlockItem> registerBlock(String name, Supplier<B> block, int stackSize) {
		return registerBlock(name, block, new Item.Properties().stacksTo(stackSize));
	}

	public <B extends Block> BlockObject<B, BlockItem> registerBlock(String name, Supplier<B> block) {
		return registerBlock(name, block, 64);
	}

	public <I extends Item> BlockObject<Block, I> registerBlock(String name, BlockBehaviour.Properties blockProperties, Function<Supplier<Block>, I> item) {
		return registerBlock(name, () -> new Block(blockProperties), item);
	}

	public BlockObject<Block, BlockItem> registerBlock(String name, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties) {
		return registerBlock(name, () -> new Block(blockProperties), (b) -> new BlockItem(b.get(), itemProperties));
	}

	public BlockObject<Block, BlockItem> registerBlock(String name, BlockBehaviour.Properties blockProperties, int stackSize) {
		return registerBlock(name, blockProperties, new Item.Properties().stacksTo(stackSize));
	}

	public BlockObject<Block, BlockItem> registerBlock(String name, BlockBehaviour.Properties blockProperties) {
		return registerBlock(name, blockProperties, 64);
	}

	public <B extends Block> BlockObject<B, Item> registerBlockNoItem(String name, Supplier<B> block) {
		return registerBlock(name, block, (Function<Supplier<B>, Item>) null);
	}

	public BlockObject<Block, Item> registerBlockNoItem(String name, BlockBehaviour.Properties blockProperties) {
		return registerBlockNoItem(name, () -> new Block(blockProperties));
	}

	public <I extends Item> ItemObject<I> registerItem(String name, Supplier<I> item) {
		RegistryObject<I> itemSup = itemRegistry.register(name, item);
		return new ItemObject<>(ResourceLocation.fromNamespaceAndPath(modId, name), itemSup);
	}

	public <I extends Item> ItemObject<I> registerBasicItem(String name, Function<Item.Properties, I> item, int stackSize) {
		return registerItem(name, () -> item.apply(new Item.Properties().stacksTo(stackSize)));
	}

	public <I extends Item> ItemObject<I> registerBasicItem(String name, Function<Item.Properties, I> item) {
		return registerBasicItem(name, item, 64);
	}

	public  ItemObject<Item> registerItem(String name, Item.Properties itemProperties) {
		return registerItem(name, () -> new Item(itemProperties));
	}

	public  ItemObject<Item> registerItem(String name) {
		return registerItem(name, new Item.Properties());
	}
}