package com.firemerald.fecore.boundingshapes;

import java.util.function.Predicate;
import java.util.stream.Stream;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

public interface IBounded
{
	public abstract AABB getBounds(double testerX, double testerY, double testerZ);

	public default Stream<Entity> getEntityList(LevelAccessor level, Entity entity, Predicate<? super Entity> filter, double testerX, double testerY, double testerZ)
	{
		return level.getEntities(entity, getBounds(testerX, testerY, testerZ), filter).stream();
	}

	public default <T extends Entity> Stream<T> getEntityList(LevelAccessor level, EntityTypeTest<Entity, T> typeTest, Predicate<? super T> filter, double testerX, double testerY, double testerZ)
	{
		return level.getEntities(typeTest, getBounds(testerX, testerY, testerZ), filter).stream();
	}
}
