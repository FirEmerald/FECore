package com.firemerald.fecore.boundingshapes;

import java.util.function.Predicate;
import java.util.stream.Stream;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

public interface IBounded {
	public abstract AABB getBounds(double testerX, double testerY, double testerZ);

	public default Stream<Entity> getEntityList(LevelAccessor level, Entity entity, Predicate<? super Entity> filter, double testerX, double testerY, double testerZ) {
		return getEntityList(level, entity, filter, getBounds(testerX, testerY, testerZ));
	}

	public default <T extends Entity> Stream<T> getEntityList(LevelAccessor level, EntityTypeTest<Entity, T> typeTest, Predicate<? super T> filter, double testerX, double testerY, double testerZ) {
		return getEntityList(level, typeTest, filter, getBounds(testerX, testerY, testerZ));
	}

	public default Stream<Entity> getEntityList(LevelAccessor level, Entity entity, Predicate<? super Entity> filter, AABB bounds) {
		return level.getEntities(entity, bounds, filter).stream();
	}

	public default <T extends Entity> Stream<T> getEntityList(LevelAccessor level, EntityTypeTest<Entity, T> typeTest, Predicate<? super T> filter, AABB bounds) {
		return level.getEntities(typeTest, bounds, filter).stream();
	}
}
