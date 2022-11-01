package com.firemerald.fecore.boundingshapes;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.firemerald.fecore.client.Translator;
import com.firemerald.fecore.client.gui.components.IComponent;

import net.minecraft.client.gui.Font;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class BoundingShape
{
	private static final Map<String, Supplier<? extends BoundingShape>> SUPPLIERS = new LinkedHashMap<>();
	private static final Map<Class<? extends BoundingShape>, String> CLASSES = new HashMap<>();
	private static final List<Pair<Class<? extends BoundingShape>, Supplier<? extends BoundingShape>>> FORLIST = new ArrayList<>();
	private static final List<Pair<Class<? extends BoundingShape>, Supplier<? extends BoundingShape>>> CONFIGURABLES = new ArrayList<>();

	static
	{
		register("all", BoundingShapeAll.class, BoundingShapeAll::new);
		registerConfigurable("boxOffsets", BoundingShapeBoxOffsets.class, BoundingShapeBoxOffsets::new, () -> {
			BoundingShapeBoxOffsets box = new BoundingShapeBoxOffsets();
			box.isRelative = false;
			return box;
		});
		registerConfigurable("boxPositions", BoundingShapeBoxPositions.class, BoundingShapeBoxPositions::new, () -> {
			BoundingShapeBoxPositions box = new BoundingShapeBoxPositions();
			box.isRelative = false;
			return box;
		});
		registerConfigurable("sphere", BoundingShapeSphere.class, BoundingShapeSphere::new, () -> {
			BoundingShapeSphere sphere = new BoundingShapeSphere();
			sphere.isRelative = false;
			return sphere;
		});
		registerConfigurable("cylinder", BoundingShapeCylinder.class, BoundingShapeCylinder::new, () -> {
			BoundingShapeCylinder cylinder = new BoundingShapeCylinder();
			cylinder.isRelative = false;
			return cylinder;
		});
		registerConfigurable("polygon", BoundingShapePolygon.class, BoundingShapePolygon::new, () -> {
			BoundingShapePolygon polygon = new BoundingShapePolygon();
			polygon.isRelative = false;
			return polygon;
		});
		register("addition", BoundingShapeAddition.class, BoundingShapeAddition::new);
		register("intersection", BoundingShapeIntersection.class, BoundingShapeIntersection::new);
		register("inversion", BoundingShapeInversion.class, BoundingShapeInversion::new);

	}

	private static <T extends BoundingShape> boolean register(String id, Class<T> clazz, Supplier<T> supplier)
	{
		if (SUPPLIERS.containsKey(id) || CLASSES.containsKey(clazz)) return false;
		else
		{
			SUPPLIERS.put(id, supplier);
			CLASSES.put(clazz, id);
			FORLIST.add(Pair.of(clazz, supplier));
			return true;
		}
	}

	private static <T extends BoundingShape> boolean registerConfigurable(String id, Class<T> clazz, Supplier<T> supplier, Supplier<T> supplierConfigurable)
	{
		if (SUPPLIERS.containsKey(id) || CLASSES.containsKey(clazz)) return false;
		else
		{
			SUPPLIERS.put(id, supplier);
			CLASSES.put(clazz, id);
			FORLIST.add(Pair.of(clazz, supplier));
			CONFIGURABLES.add(Pair.of(clazz, supplier));
			return true;
		}
	}

	public static <T extends BoundingShape> boolean register(ResourceLocation id, Class<T> clazz, Supplier<T> supplier)
	{
		return register(id.getNamespace() + "." + id.getPath(), clazz, supplier);
	}

	public static <T extends BoundingShape> boolean registerConfigurable(ResourceLocation id, Class<T> clazz, Supplier<T> supplier, Supplier<T> supplierConfigurable)
	{
		return register(id.getNamespace() + "." + id.getPath(), clazz, supplier);
	}

	public static String getId(Class<? extends BoundingShape> clazz)
	{
		return CLASSES.get(clazz);
	}

	public static String getId(BoundingShape shape)
	{
		return getId(shape.getClass());
	}

	public static BoundingShape construct(String id)
	{
		Supplier<? extends BoundingShape> sup = SUPPLIERS.get(id);
		return sup == null ? null : sup.get();
	}

	public static List<BoundingShape> getShapeList(BoundingShape existing)
	{
		return FORLIST.stream().map(pair -> (existing == null || existing.getClass() != pair.getLeft() ? pair.getRight().get() : existing)).collect(Collectors.toList());
	}

	public static List<BoundingShape> getConfigurableShapeList(BoundingShape existing)
	{
		return CONFIGURABLES.stream().map(pair -> (existing == null || existing.getClass() != pair.getLeft() ? pair.getRight().get() : existing)).collect(Collectors.toList());
	}

	public abstract String getUnlocalizedName();

	@OnlyIn(Dist.CLIENT)
	public String getLocalizedName()
	{
		return Translator.translate(getUnlocalizedName());
	}

	public abstract boolean isWithin(@Nullable Entity entity, double posX, double posY, double posZ, double testerX, double testerY, double testerZ);

	public abstract Stream<Entity> getEntities(LevelAccessor level, @Nullable Entity entity, Predicate<? super Entity> filter, double testerX, double testerY, double testerZ);

	public abstract <T extends Entity> Stream<T> getEntities(LevelAccessor level, EntityTypeTest<Entity, T> typeTest, Predicate<? super T> filter, double testerX, double testerY, double testerZ);

	public <T extends Entity> Stream<T> getEntitiesOfClass(LevelAccessor level, Class<T> clazz, double testerX, double testerY, double testerZ)
	{
		return this.getEntitiesOfClass(level, clazz, EntitySelector.NO_SPECTATORS, testerX, testerY, testerZ);
	}

	public Stream<Entity> getEntities(LevelAccessor level, @Nullable Entity entity, double testerX, double testerY, double testerZ)
	{
		return this.getEntities(level, entity, EntitySelector.NO_SPECTATORS, testerX, testerY, testerZ);
	}

	public <T extends Entity> Stream<T> getEntitiesOfClass(LevelAccessor level, Class<T> clazz, Predicate<? super T> filter, double testerX, double testerY, double testerZ)
	{
		return this.getEntities(level, EntityTypeTest.forClass(clazz), filter, testerX, testerY, testerZ);
	}

	public void saveToNBT(CompoundTag tag)
	{
		tag.putString("id", BoundingShape.getId(this));
	}

	public static BoundingShape constructFromNBT(CompoundTag tag)
	{
		BoundingShape shape = BoundingShape.construct(tag.getString("id"));
		if (shape == null) shape = new BoundingShapeSphere();
		else shape.loadFromNBT(tag);
		return shape;
	}

	public static BoundingShape constructFromNBTOptional(CompoundTag tag)
	{
		if (tag.contains("id", 8))
		{
			BoundingShape shape = BoundingShape.construct(tag.getString("id"));
			if (shape != null)
			{
				shape.loadFromNBT(tag);
				return shape;
			}
		}
		return null;
	}

	public void loadFromNBT(CompoundTag tag) {}

	public void saveToBuffer(FriendlyByteBuf buf)
	{
		buf.writeUtf(getId(this));
	}

	public static BoundingShape constructFromBuffer(FriendlyByteBuf buf)
	{
		BoundingShape shape = BoundingShape.construct(buf.readUtf());
		shape.loadFromBuffer(buf);
		return shape;
	}

	public void loadFromBuffer(FriendlyByteBuf buf) {}

	@OnlyIn(Dist.CLIENT)
	public abstract void addGuiElements(Vec3 pos, IShapeGui gui, Font font, Consumer<IComponent> addElement, int width);

	public static BoundingShape copy(BoundingShape shape)
	{
		CompoundTag tag = new CompoundTag();
		shape.saveToNBT(tag);
		return constructFromNBT(tag);
	}
}