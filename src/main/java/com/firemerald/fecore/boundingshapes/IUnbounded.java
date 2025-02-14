package com.firemerald.fecore.boundingshapes;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

public interface IUnbounded {
	public static final AABB ALL = new AABB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

	public default Stream<Entity> getEntityList(LevelAccessor level, Entity entity, Predicate<? super Entity> filter) {
		if (level instanceof ServerLevel)
			return StreamSupport.stream(((ServerLevel) level).getAllEntities().spliterator(), false).filter(e -> e != entity && filter.test(e));
		else
			return level.getEntities(entity, ALL, filter).stream();
	}

	public default <T extends Entity> Stream<T> getEntityList(LevelAccessor level, EntityTypeTest<Entity, T> typeTest, Predicate<? super T> filter)
	{
		if (level instanceof ServerLevel)
			return StreamSupport.stream(((ServerLevel) level).getAllEntities().spliterator(), false).map(typeTest::tryCast).filter(Objects::nonNull).filter(filter);
		else
			return level.getEntities(typeTest, ALL, filter).stream();
	}
}
