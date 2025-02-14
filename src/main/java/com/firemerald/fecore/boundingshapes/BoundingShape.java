package com.firemerald.fecore.boundingshapes;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.firemerald.fecore.client.gui.components.IComponent;
import com.firemerald.fecore.init.FECoreRegistries;
import com.mojang.serialization.Codec;

import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public abstract class BoundingShape {
    public static final Codec<BoundingShape> CODEC = FECoreRegistries.BOUNDING_SHAPE_DEFINITIONS.byNameCodec().dispatch("id", BoundingShape::definition, definition -> definition.codec);
    public static final Codec<List<BoundingShape>> LIST_CODEC = CODEC.listOf();
    public static final StreamCodec<RegistryFriendlyByteBuf, BoundingShape> STREAM_CODEC = ByteBufCodecs.registry(FECoreRegistries.Keys.BOUNDING_SHAPE_DEFINITIONS).dispatch(BoundingShape::definition, definition -> definition.streamCodec);
    public static final StreamCodec<RegistryFriendlyByteBuf, List<BoundingShape>> STREAM_LIST_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

    public static Stream<BoundingShapeDefinition<?>> getShapeDefinitions() {
    	return FECoreRegistries.BOUNDING_SHAPE_DEFINITIONS.stream();
    }

    public static Stream<BoundingShapeDefinition<?>> getConfigurableShapeDefinitions() {
    	return getShapeDefinitions().filter(def -> def.configurable);
    }

	// TODO make this always based on the key
	public abstract String getUnlocalizedName();

	@OnlyIn(Dist.CLIENT)
	public String getLocalizedName() {
		return I18n.get(getUnlocalizedName());
	}

	public abstract boolean isWithin(@Nullable Entity entity, double posX, double posY, double posZ, double testerX, double testerY, double testerZ);

	public abstract Stream<Entity> getEntities(LevelAccessor level, @Nullable Entity entity, Predicate<? super Entity> filter, double testerX, double testerY, double testerZ);

	public abstract <T extends Entity> Stream<T> getEntities(LevelAccessor level, EntityTypeTest<Entity, T> typeTest, Predicate<? super T> filter, double testerX, double testerY, double testerZ);

	public <T extends Entity> Stream<T> getEntitiesOfClass(LevelAccessor level, Class<T> clazz, double testerX, double testerY, double testerZ) {
		return this.getEntitiesOfClass(level, clazz, EntitySelector.NO_SPECTATORS, testerX, testerY, testerZ);
	}

	public Stream<Entity> getEntities(LevelAccessor level, @Nullable Entity entity, double testerX, double testerY, double testerZ) {
		return this.getEntities(level, entity, EntitySelector.NO_SPECTATORS, testerX, testerY, testerZ);
	}

	public <T extends Entity> Stream<T> getEntitiesOfClass(LevelAccessor level, Class<T> clazz, Predicate<? super T> filter, double testerX, double testerY, double testerZ) {
		return this.getEntities(level, EntityTypeTest.forClass(clazz), filter, testerX, testerY, testerZ);
	}

	@Override
	public abstract BoundingShape clone();

	@OnlyIn(Dist.CLIENT)
	public abstract void addGuiElements(Vec3 pos, IShapeGui gui, Font font, Consumer<IComponent> addElement, int width);

	public void sendMessage(Player player, Component message) {
		if (player instanceof ServerPlayer serverPlayer) serverPlayer.sendSystemMessage(message);
	}

	public abstract BoundingShapeDefinition<? extends BoundingShape> definition();

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object o);

	public abstract void getPropertiesFrom(BoundingShape other);

	//MAY OR MAY NOT RETURN A NEW SHAPE! ONLY GUARANTEES NO CHANGE TO THE CURRENT SHAPE!
	public abstract BoundingShape asAbsolute(Vec3 testerPos);

	public abstract boolean isAbsolute();
}