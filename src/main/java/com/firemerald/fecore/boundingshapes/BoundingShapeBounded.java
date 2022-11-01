package com.firemerald.fecore.boundingshapes;

import java.util.function.Predicate;
import java.util.stream.Stream;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;

public abstract class BoundingShapeBounded extends BoundingShape implements IBounded
{
	@Override
	public Stream<Entity> getEntities(LevelAccessor level, Entity entity, Predicate<? super Entity> filter, double testerX, double testerY, double testerZ)
	{
		return getEntityList(level, entity, filter, testerX, testerY, testerZ).filter(e -> {
			Vec3 pos = e.position();
			return this.isWithin(e, pos.x, pos.y, pos.z, testerX, testerY, testerZ);
		});
	}

	@Override
	public <T extends Entity> Stream<T> getEntities(LevelAccessor level, EntityTypeTest<Entity, T> typeTest, Predicate<? super T> filter, double testerX, double testerY, double testerZ)
	{
		return getEntityList(level, typeTest, filter, testerX, testerY, testerZ).filter(e -> {
			Vec3 pos = e.position();
			return this.isWithin(e, pos.x, pos.y, pos.z, testerX, testerY, testerZ);
		});
	}
}