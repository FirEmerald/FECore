package com.firemerald.fecore.selectionshapes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.firemerald.fecore.betterscreens.components.IComponent;
import com.firemerald.fecore.util.Translator;

import net.minecraft.client.gui.Font;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class BoundingShape
{
	private static final Map<String, Supplier<? extends BoundingShape>> SUPPLIERS = new LinkedHashMap<>();
	private static final Map<Class<? extends BoundingShape>, String> CLASSES = new HashMap<>();
	private static final List<Pair<Class<? extends BoundingShape>, Supplier<? extends BoundingShape>>> FORLIST = new ArrayList<>();
	private static final List<Pair<Class<? extends BoundingShapeConfigurable>, Supplier<? extends BoundingShapeConfigurable>>> CONFIGURABLES = new ArrayList<>();

	static
	{
		register("all", BoundingShapeAll.class, BoundingShapeAll::new);
		register("boxOffsets", BoundingShapeBoxOffsets.class, BoundingShapeBoxOffsets::new);
		register("boxPositions", BoundingShapeBoxPositions.class, BoundingShapeBoxPositions::new);
		register("cylinder", BoundingShapeCylinder.class, BoundingShapeCylinder::new);
		register("sphere", BoundingShapeSphere.class, BoundingShapeSphere::new);
		register("polygon", BoundingShapePolygon.class, BoundingShapePolygon::new);
		register("addition", BoundingShapeAddition.class, BoundingShapeAddition::new);
		register("intersection", BoundingShapeIntersection.class, BoundingShapeIntersection::new);
		register("inversion", BoundingShapeInversion.class, BoundingShapeInversion::new);

		registerConfigurable(BoundingShapeBoxPositions.class, () -> {
			BoundingShapeBoxPositions box = new BoundingShapeBoxPositions();
			box.isRelative = false;
			return box;
		});
		registerConfigurable(BoundingShapeBoxOffsets.class, () -> {
			BoundingShapeBoxOffsets box = new BoundingShapeBoxOffsets();
			box.isRelative = false;
			return box;
		});
		registerConfigurable(BoundingShapeSphere.class, () -> {
			BoundingShapeSphere sphere = new BoundingShapeSphere();
			sphere.isRelative = false;
			return sphere;
		});
		registerConfigurable(BoundingShapeCylinder.class, () -> {
			BoundingShapeCylinder cylinder = new BoundingShapeCylinder();
			cylinder.isRelative = false;
			return cylinder;
		});
		registerConfigurable(BoundingShapePolygon.class, () -> {
			BoundingShapePolygon polygon = new BoundingShapePolygon();
			polygon.isRelative = false;
			return polygon;
		});
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

	public static <T extends BoundingShape> boolean register(ResourceLocation id, Class<T> clazz, Supplier<T> supplier)
	{
		return register(id.getNamespace() + "." + id.getPath(), clazz, supplier);
	}

	public static <T extends BoundingShapeConfigurable> void registerConfigurable(Class<T> clazz, Supplier<T> supplier)
	{
		CONFIGURABLES.add(Pair.of(clazz, supplier));
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

	public static List<BoundingShapeConfigurable> getConfigurableShapeList(BoundingShapeConfigurable existing)
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