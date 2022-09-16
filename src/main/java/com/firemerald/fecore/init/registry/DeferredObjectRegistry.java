package com.firemerald.fecore.init.registry;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.datafixers.types.Type;

import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid.Properties;
import net.minecraftforge.registries.ForgeRegistries;

public class DeferredObjectRegistry
{
	public final String modId;
	public final DeferredDeferredRegister<Fluid> fluidRegistry;
	public final DeferredDeferredRegister<BlockEntityType<?>> blockEntityRegistry;
	public final DeferredDeferredRegister<Block> blockRegistry;
	public final DeferredDeferredRegister<Item> itemRegistry;
	
	public DeferredObjectRegistry(String modId)
	{
		this.modId = modId;
		fluidRegistry = new DeferredDeferredRegister<>(ForgeRegistries.FLUIDS, modId);
		blockEntityRegistry = new DeferredDeferredRegister<>(ForgeRegistries.BLOCK_ENTITIES, modId);
		blockRegistry = new DeferredDeferredRegister<>(ForgeRegistries.BLOCKS, modId);
		itemRegistry = new DeferredDeferredRegister<>(ForgeRegistries.ITEMS, modId);
	}
	
	public void register(IEventBus bus)
	{
		fluidRegistry.register(bus);
		blockEntityRegistry.register(bus);
		blockRegistry.register(bus);
		itemRegistry.register(bus);
	}
	
	@SuppressWarnings("unchecked")
	public <S extends Fluid, F extends Fluid, B extends Block, I extends Item> FluidObject<S, F, B, I> registerFluid(String name, String forgeName, BiFunction<Supplier<S>, Supplier<F>, Properties> fluidProperties, Function<Properties, S> still, Function<Properties, F> flowing, Function<Supplier<S>, B> block, Function<Supplier<S>, I> bucket)
	{
		MutableSupplier<S> stillMutable = new MutableSupplier<>();
		MutableSupplier<F> flowingMutable = new MutableSupplier<>();
		Properties properties = fluidProperties.apply(stillMutable, flowingMutable);
		Supplier<S> stillReg = fluidRegistry.register(name, () -> still.apply(properties));
		stillMutable.set(stillReg);
		Supplier<F> flowingReg = fluidRegistry.register(name + "_flowing", () -> flowing.apply(properties));
		flowingMutable.set(flowingReg);
		Supplier<B> blockSup;
		if (block != null)
		{
			blockSup = blockRegistry.register(name, () -> block.apply(stillReg));
			properties.block((Supplier<? extends LiquidBlock>) blockSup);
		}
		else blockSup = (Supplier<B>) MutableSupplier.DEFAULT_BLOCK;
		Supplier<I> bucketSup;
		if (bucket != null)
		{
			bucketSup = itemRegistry.register(name + "_bucket", () -> bucket.apply(stillReg));
			properties.bucket(bucketSup);
		}
		else bucketSup = (Supplier<I>) MutableSupplier.DEFAULT_ITEM;
		return new FluidObject<>(new ResourceLocation(modId, name), forgeName, stillReg, flowingReg, blockSup, bucketSup);
	}

	public <S extends Fluid, F extends Fluid, B extends Block, I extends Item> FluidObject<S, F, B, I> registerFluid(String name, BiFunction<Supplier<S>, Supplier<F>, Properties> fluidProperties, Function<Properties, S> still, Function<Properties, F> flowing, Function<Supplier<S>, B> block, Function<Supplier<S>, I> bucket)
	{
		return registerFluid(name, name, fluidProperties, still, flowing, block, bucket);
	}

	public FluidObject<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing, LiquidBlock, BucketItem> registerFluid(String name, String forgeName, FluidAttributes.Builder attributes, Consumer<Properties> properties, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties)
	{
		return registerFluid(name, forgeName, 
				(still, flowing) -> {
					Properties props = new Properties(still, flowing, attributes);
					properties.accept(props);
					return props;
				},
				ForgeFlowingFluid.Source::new, ForgeFlowingFluid.Flowing::new,
				(still) -> new LiquidBlock(still, blockProperties),
				(still) -> new BucketItem(still, itemProperties)
				);
	}

	public FluidObject<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing, LiquidBlock, BucketItem> registerFluid(String name, FluidAttributes.Builder attributes, Consumer<Properties> properties, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties)
	{
		return registerFluid(name, name, attributes, properties, blockProperties, itemProperties);
	}

	public FluidObject<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing, LiquidBlock, BucketItem> registerFluid(String name, String forgeName, FluidAttributes.Builder attributes, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties)
	{
		return registerFluid(name, forgeName, 
				(still, flowing) -> new Properties(still, flowing, attributes),
				ForgeFlowingFluid.Source::new, ForgeFlowingFluid.Flowing::new,
				(still) -> new LiquidBlock(still, blockProperties),
				(still) -> new BucketItem(still, itemProperties)
				);
	}

	public FluidObject<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing, LiquidBlock, BucketItem> registerFluid(String name, FluidAttributes.Builder attributes, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties)
	{
		return registerFluid(name, name, attributes, blockProperties, itemProperties);
	}

	public FluidObject<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing, LiquidBlock, Item> registerFluidNoBucket(String name, String forgeName, FluidAttributes.Builder attributes, Consumer<Properties> properties, BlockBehaviour.Properties blockProperties)
	{
		return registerFluid(name, forgeName, 
				(still, flowing) -> {
					Properties props = new Properties(still, flowing, attributes);
					properties.accept(props);
					return props;
				},
				ForgeFlowingFluid.Source::new, ForgeFlowingFluid.Flowing::new,
				(still) -> new LiquidBlock(still, blockProperties),
				null
				);
	}

	public FluidObject<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing, LiquidBlock, Item> registerFluidNoBucket(String name, FluidAttributes.Builder attributes, Consumer<Properties> properties, BlockBehaviour.Properties blockProperties)
	{
		return registerFluidNoBucket(name, name, attributes, properties, blockProperties);
	}

	public FluidObject<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing, LiquidBlock, Item> registerFluidNoBucket(String name, String forgeName, FluidAttributes.Builder attributes, BlockBehaviour.Properties blockProperties)
	{
		return registerFluid(name, forgeName, 
				(still, flowing) -> new Properties(still, flowing, attributes),
				ForgeFlowingFluid.Source::new, ForgeFlowingFluid.Flowing::new,
				(still) -> new LiquidBlock(still, blockProperties),
				null
				);
	}

	public FluidObject<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing, LiquidBlock, Item> registerFluidNoBucket(String name, FluidAttributes.Builder attributes, BlockBehaviour.Properties blockProperties)
	{
		return registerFluidNoBucket(name, name, attributes, blockProperties);
	}

	public FluidObject<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing, Block, Item> registerFluidNoBlock(String name, String forgeName, FluidAttributes.Builder attributes, Consumer<Properties> properties)
	{
		return registerFluid(name, forgeName, 
				(still, flowing) -> {
					Properties props = new Properties(still, flowing, attributes);
					properties.accept(props);
					return props;
				},
				ForgeFlowingFluid.Source::new, ForgeFlowingFluid.Flowing::new,
				null, 
				null
				);
	}

	public FluidObject<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing, Block, Item> registerFluidNoBlock(String name, FluidAttributes.Builder attributes, Consumer<Properties> properties)
	{
		return registerFluidNoBlock(name, name, attributes, properties);
	}

	public FluidObject<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing, Block, Item> registerFluidNoBlock(String name, String forgeName, FluidAttributes.Builder attributes)
	{
		return registerFluid(name, forgeName, 
				(still, flowing) -> new Properties(still, flowing, attributes),
				ForgeFlowingFluid.Source::new, ForgeFlowingFluid.Flowing::new,
				null, 
				null
				);
	}

	public FluidObject<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing, Block, Item> registerFluidNoBlock(String name, FluidAttributes.Builder attributes)
	{
		return registerFluidNoBlock(name, name, attributes);
	}
	
	@SuppressWarnings("unchecked")
	public <E extends BlockEntity, B extends Block, I extends Item> BlockEntityObject<E, B, I> registerBlockEntity(String name, Supplier<B> block, Function<Supplier<B>, I> item, BlockEntitySupplier<E> blockEntity, Type<?> dataFixerType)
	{
		Supplier<B> blockReg = blockRegistry.register(name, block);
		Supplier<I> itemSup;
		if (item != null) itemSup = itemRegistry.register(name, () -> item.apply(blockReg));
		else itemSup = (Supplier<I>) MutableSupplier.DEFAULT_ITEM;
		Supplier<BlockEntityType<E>> blockEntityReg = blockEntityRegistry.register(name, () -> BlockEntityType.Builder.of(blockEntity).build(dataFixerType));
		return new BlockEntityObject<>(new ResourceLocation(modId, name), blockEntityReg, blockReg, itemSup);
	}

	public <E extends BlockEntity, B extends Block, I extends Item> BlockEntityObject<E, B, I> registerBlockEntity(String name, Supplier<B> block, Function<Supplier<B>, I> item, BlockEntitySupplier<E> blockEntity)
	{
		return registerBlockEntity(name, block, item, blockEntity, null);
	}

	public <E extends BlockEntity, B extends Block, I extends Item> BlockEntityObject<E, B, I> registerBlockEntityNoItem(String name, Supplier<B> block, BlockEntitySupplier<E> blockEntity, Type<?> dataFixerType)
	{
		return registerBlockEntity(name, block, null, blockEntity, dataFixerType);
	}

	public <E extends BlockEntity, B extends Block, I extends Item> BlockEntityObject<E, B, I> registerBlockEntityNoItem(String name, Supplier<B> block, BlockEntitySupplier<E> blockEntity)
	{
		return registerBlockEntityNoItem(name, block, blockEntity, null);
	}
	
	@SuppressWarnings("unchecked")
	public <B extends Block, I extends Item> BlockObject<B, I> registerBlock(String name, Supplier<B> block, Function<Supplier<B>, I> item)
	{
		Supplier<B> blockReg = blockRegistry.register(name, block);
		Supplier<I> itemSup;
		if (item != null) itemSup = itemRegistry.register(name, () -> item.apply(blockReg));
		else itemSup = (Supplier<I>) MutableSupplier.DEFAULT_ITEM;
		return new BlockObject<>(new ResourceLocation(modId, name), blockReg, itemSup);
	}
	
	public <B extends Block> BlockObject<B, BlockItem> registerBlock(String name, Supplier<B> block, Item.Properties itemProperties)
	{
		return registerBlock(name, block, (b) -> new BlockItem(b.get(), itemProperties));
	}
	
	public <I extends Item> BlockObject<Block, I> registerBlock(String name, BlockBehaviour.Properties blockProperties, Function<Supplier<Block>, I> item)
	{
		return registerBlock(name, () -> new Block(blockProperties), item);
	}
	
	public BlockObject<Block, BlockItem> registerBlock(String name, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties)
	{
		return registerBlock(name, () -> new Block(blockProperties), (b) -> new BlockItem(b.get(), itemProperties));
	}

	public <B extends Block> BlockObject<B, Item> registerBlockNoItem(String name, Supplier<B> block)
	{
		return registerBlock(name, block, (Function<Supplier<B>, Item>) null);
	}

	public BlockObject<Block, Item> registerBlockNoItem(String name, BlockBehaviour.Properties blockProperties)
	{
		return registerBlockNoItem(name, () -> new Block(blockProperties));
	}
	
	public <I extends Item> ItemObject<I> registerItem(String name, Supplier<I> item)
	{
		return new ItemObject<>(new ResourceLocation(modId, name), itemRegistry.register(name, item));
	}
	
	public  ItemObject<Item> registerItem(String name, Item.Properties itemProperties)
	{
		return registerItem(name, () -> new Item(itemProperties));
	}
}