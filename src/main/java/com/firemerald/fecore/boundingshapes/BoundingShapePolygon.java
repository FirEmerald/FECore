package com.firemerald.fecore.boundingshapes;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;
import org.joml.Vector3d;

import com.firemerald.fecore.client.gui.components.Button;
import com.firemerald.fecore.client.gui.components.IComponent;
import com.firemerald.fecore.client.gui.components.decoration.FloatingText;
import com.firemerald.fecore.client.gui.components.text.DoubleField;
import com.firemerald.fecore.codec.Codecs;
import com.firemerald.fecore.init.FECoreBoundingShapes;
import com.firemerald.fecore.util.Constants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BoundingShapePolygon extends BoundingShapeShaped implements IRenderableBoundingShape, IConfigurableBoundingShape
{
	public static final MapCodec<BoundingShapePolygon> CODEC = RecordCodecBuilder.mapCodec(instance ->
		instance.group(
				Codecs.VECTOR3D_ARRAY.fieldOf("points").forGetter(polygon -> polygon.positions),
				Codec.BOOL.optionalFieldOf("isRelative", true).forGetter(polygon -> polygon.isRelative)
				)
		.apply(instance, BoundingShapePolygon::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, BoundingShapePolygon> STREAM_CODEC = StreamCodec.composite(
			Codecs.VECTOR3D_ARRAY_STREAM, polygon -> polygon.positions,
			ByteBufCodecs.BOOL, polygon -> polygon.isRelative,
			BoundingShapePolygon::new
			);

	private double y1, y2;
	public Vector3d[] positions = new Vector3d[0];

	public BoundingShapePolygon(Collection<Vector3d> positions, boolean isRelative) {
		BlockPos.CODEC.getClass();
		this.positions = positions.toArray(Vector3d[]::new);
		if (positions.isEmpty()) {
			y1 = 0;
			y2 = 0;
		} else {
			y1 = Double.POSITIVE_INFINITY;
			y2 = Double.NEGATIVE_INFINITY;
			for (Vector3d pos : positions) {
				if (pos.y < y1) y1 = pos.y;
				if (pos.y > y2) y2 = pos.y;
			}
		}
		this.isRelative = isRelative;
	}

	public BoundingShapePolygon(Collection<Vector3d> positions) {
		this(positions, true);
	}

	public BoundingShapePolygon(Vector3d[] positions, boolean isRelative) {
		this.positions = positions;
		if (positions.length == 0) {
			y1 = 0;
			y2 = 0;
		} else {
			y1 = Double.POSITIVE_INFINITY;
			y2 = Double.NEGATIVE_INFINITY;
			for (Vector3d pos : positions) {
				if (pos.y < y1) y1 = pos.y;
				if (pos.y > y2) y2 = pos.y;
			}
		}
		this.isRelative = isRelative;
	}

	public BoundingShapePolygon(Vector3d... positions) {
		this(positions, true);
	}

	public BoundingShapePolygon(boolean isRelative, Vector3d... positions) {
		this(positions, isRelative);
	}

	public BoundingShapePolygon(boolean isRelative) {
		y1 = y2 = 0;
		positions = new Vector3d[0];
		this.isRelative = isRelative;
	}

	public BoundingShapePolygon() {
		this(true);
	}

	@Override
	public String getUnlocalizedName()
	{
		return "fecore.shape.polygon";
	}

	@Override
	public boolean isWithin(@Nullable Entity entity, double posX, double posY, double posZ, double testerX, double testerY, double testerZ)
	{
		if (isRelative) {
			posX -= testerX;
			posY -= testerY;
			posZ -= testerZ;
		}
		double y1, y2;
		if (this.y1 > this.y2)
		{
			y1 = this.y2;
			y2 = this.y1;
		}
		else
		{
			y1 = this.y1;
			y2 = this.y2;
		}
		if (posY < y1 || posY >= y2) return false; //out of Y bounds
		for (Vector3d pos : positions) if (pos.x() == posX && pos.z() == posZ) return true; //point equivalence
		if (positions.length <= 1) return false; //must have at least two positions for non-point-equivalence test
		Vector3d lastPos = positions[positions.length - 1];
		double totalAng = 0, prevX = lastPos.x() - posX, prevY = lastPos.z() - posZ, curX, curY;
		for (Vector3d pos : positions)
		{
			curX = pos.x() - posX;
			curY = pos.z() - posZ;
			totalAng += Math.atan2((prevX * curY) - (curX * prevY), (curX * prevX) + (curY * prevY)); //quick and easy angle between points method
			prevX = curX;
			prevY = curY;
		}
		return Math.abs(Math.abs(totalAng) - Constants.TAU) <= 0.001;
	}

	@Override
	public AABB getLocalBounds() {
		return getBoundsOffset(0, 0, 0);
	}

	@Override
	public AABB getBoundsOffset(double offX, double offY, double offZ)
	{
		if (positions.length == 0) return new AABB(offX, offY, offZ, offX, offY, offZ);
		double vx1, vz1, vx2, vz2;
		vx1 = vx2 = positions[0].x();
		vz1 = vz2 = positions[0].z();
		for (int i = 1; i < positions.length; ++i)
		{
			double x = positions[i].x();
			double z = positions[i].z();
			if (x < vx1) vx1 = x;
			else if (x > vx2) vx2 = x;
			if (z < vz1) vz1 = z;
			else if (z > vz2) vz2 = z;
		}
		double x1, y1, z1, x2, y2, z2;
		x1 = vx1 + offX;
		y1 = this.y1 + offY;
		z1 = vz1 + offZ;
		x2 = vx2 + offX;
		y2 = this.y2 + offY;
		z2 = vz2 + offZ;
		return new AABB(x1, y1, z1, x2, y2, z2);
	}

	private void updateCachedHeight()
	{
		if (positions.length > 0)
		{
			y1 = y2 = positions[0].y();
			for (int i = 1; i < positions.length; ++i)
			{
				double y = positions[i].y();
				if (y < y1) y1 = y;
				if (y > y2) y2 = y;
			}
		}
	}

	@Override
	public void addGuiElements(Vec3 pos, IShapeGui gui, Font font, Consumer<IComponent> addElement, int width)
	{
		int offX = (width - 200) >> 1;
		DoubleField[] posXs = new DoubleField[positions.length];
		DoubleField[] posYs = new DoubleField[positions.length];
		DoubleField[] posZs = new DoubleField[positions.length];
		addElement.accept(new FloatingText(offX, 0, offX + 100, 20, font, I18n.get("fecore.shapesgui.position")));
		addElement.accept(new Button(offX + 100, 0, 100, 20, Component.translatable(isRelative ? "fecore.shapesgui.operator.relative" : "fecore.shapesgui.operator.absolute"), null).setAction(button -> () -> {
			button.displayString = Component.translatable(toggleRelative(pos) ? "fecore.shapesgui.operator.relative" : "fecore.shapesgui.operator.absolute");
			for (int i = 0; i < positions.length; ++i) {
				Vector3d position = positions[i];
				posXs[i].setDouble(position.x);
				posYs[i].setDouble(position.y);
				posZs[i].setDouble(position.z);
			}
		}));
		int y = 20;
		for (int i = 0; i < positions.length; ++i)
		{
			final int index = i;
			Vector3d posi = positions[i];
			DoubleField posX, posY, posZ;
			addElement.accept(new FloatingText(offX, y, offX + 100, y + 20, font, I18n.get("fecore.shapesgui.point.index", index)));
			addElement.accept(new Button(offX + 100, y, 100, 20, Component.translatable("fecore.shapesgui.point.remove"), null).setAction(button -> () -> {
				Vector3d[] old = BoundingShapePolygon.this.positions;
				BoundingShapePolygon.this.positions = new Vector3d[old.length - 1];
				System.arraycopy(old, 0, BoundingShapePolygon.this.positions, 0, index);
				System.arraycopy(old, index + 1, BoundingShapePolygon.this.positions, index, old.length - index - 1);
				this.updateCachedHeight();
				gui.updateGuiButtonsList();
			}));
			y += 20;
			addElement.accept(posX = new DoubleField(font, offX, y, 67, 20, posi.x(), Component.translatable("fecore.shapesgui.position.x"), (DoubleConsumer) (val -> BoundingShapePolygon.this.positions[index].x = val)));
			addElement.accept(posY = new DoubleField(font, offX + 67, y, 66, 20, posi.y(), Component.translatable("fecore.shapesgui.position.y"), (DoubleConsumer) (val -> {
				BoundingShapePolygon.this.positions[index].y = val;
				this.updateCachedHeight();
			})));
			addElement.accept(posZ = new DoubleField(font, offX + 133, y, 67, 20, posi.z(), Component.translatable("fecore.shapesgui.position.z"), (DoubleConsumer) (val -> BoundingShapePolygon.this.positions[index].z = val)));
			y += 20;
			addElement.accept(new Button(offX, y, 100, 20, Component.translatable("fecore.shapesgui.point.insert.before"), null).setAction(button -> () -> {
				Vector3d[] old = BoundingShapePolygon.this.positions;
				BoundingShapePolygon.this.positions = new Vector3d[old.length + 1];
				System.arraycopy(old, 0, BoundingShapePolygon.this.positions, 0, index);
				BoundingShapePolygon.this.positions[index] = new Vector3d(old[index]);
				System.arraycopy(old, index, BoundingShapePolygon.this.positions, index + 1, old.length - index);
				gui.updateGuiButtonsList();
			}));
			addElement.accept(new Button(offX + 100, y, 100, 20, Component.translatable("fecore.shapesgui.point.insert.after"), null).setAction(button -> () -> {
				Vector3d[] old = BoundingShapePolygon.this.positions;
				BoundingShapePolygon.this.positions = new Vector3d[old.length + 1];
				System.arraycopy(old, 0, BoundingShapePolygon.this.positions, 0, index + 1);
				BoundingShapePolygon.this.positions[index + 1] = new Vector3d(old[index]);
				System.arraycopy(old, index + 1, BoundingShapePolygon.this.positions, index + 2, old.length - index - 1);
				gui.updateGuiButtonsList();
			}));
			y += 20;
			posXs[i] = posX;
			posYs[i] = posY;
			posZs[i] = posZ;
		}
		addElement.accept(new Button(offX + 50, y, 100, 20, Component.translatable("fecore.shapesgui.point.add"), null).setAction(button -> () -> {
			Vector3d[] old = BoundingShapePolygon.this.positions;
			BoundingShapePolygon.this.positions = new Vector3d[old.length + 1];
			System.arraycopy(old, 0, BoundingShapePolygon.this.positions, 0, old.length);
			BoundingShapePolygon.this.positions[old.length] = old.length > 0 ?new Vector3d( old[old.length - 1]) : new Vector3d();
			gui.updateGuiButtonsList();
		}));
	}

	@Override
	public int addPosition(Player player, BlockPos blockPos, int num)
	{
		setRelative(false, player.position());
		double x = blockPos.getX() + .5;
		double y = blockPos.getY() + .5;
		double z = blockPos.getZ() + .5;
		if (num == 0) //initial set
		{
			y1 = y2 = y;
		}
		else
		{
			if (y < y1) y1 = y;
			if (y > y2) y2 = y;
		}
		Vector3d v;
		positions = ArrayUtils.add(positions, v = new Vector3d(x, y, z));
		sendMessage(player, Component.translatable("fecore.shapetool.position.added", v.toString()));
		return positions.length;
	}

	@Override
	public int removePosition(Player player, int num)
	{
		if (num == 0) return 0;
		Vector3d[] old = positions;
		positions = new Vector3d[old.length - 1];
		System.arraycopy(old, 0, positions, 0, positions.length);
		sendMessage(player, Component.translatable("fecore.shapetool.position.removed", old[old.length - 1].toString()));
		this.updateCachedHeight();
		return positions.length;
	}

	@Override
	public void offset(double x, double y, double z) {
		y1 += y;
		y2 += y;
		for (Vector3d position : positions)
			position.add(x, y, z);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
	{
		tooltipComponents.add(Component.translatable(isRelative ? "fecore.shapetool.tooltip.relative" : "fecore.shapetool.tooltip.absolute"));
		if (positions.length > 0) {
			tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.position.limits.y", y1, y2));
			tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.position.coordinates"));
			if (tooltipFlag.hasControlDown()) {
				for (Vector3d pos : positions) tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.position.coordinate", pos.x(), pos.z()));
			} else {
				tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.ctrl_for_positions"));
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderIntoWorld(PoseStack pose, double x, double y, double z, DeltaTracker delta)
	{
		IRenderableBoundingShape.renderPolygon(pose.last().pose(), y1 + y, y2 + y, x, z, positions, .5f, .5f, 1f, .5f);
	}

	@Override
	public int hashCode() {
		return
				((((int) y1            ) & 0b0000000000000000011111) << 00) | //x = 05 @ 00
				((((int) y2            ) & 0b0000000000000000011111) << 05) | //y = 05 @ 05
				(((positions.hashCode()) & 0b1111111111111111111111) << 10);  //z = 22 @ 10
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		else if (o == this) return true;
		else if (o.getClass() != this.getClass()) return false;
		else {
			BoundingShapePolygon polygon = (BoundingShapePolygon) o;
			return
					polygon.isRelative == isRelative &&
					polygon.y1 == y1 &&
					polygon.y2 == y2 &&
					Arrays.equals(polygon.positions, positions);
		}
	}

	public Vector3d[] clonePositions() {
		Vector3d[] newArray = new Vector3d[positions.length];
		for (int i = 0; i < positions.length; ++i) newArray[i] = new Vector3d(positions[i]);
		return newArray;
	}

	@Override
	public BoundingShapePolygon clone() {
		return new BoundingShapePolygon(clonePositions(), isRelative);
	}

	@Override
	public BoundingShapeDefinition<BoundingShapePolygon> definition() {
		return FECoreBoundingShapes.POLYGON.get();
	}

	@Override
	public void getPropertiesFrom(BoundingShape other) {
		if (other instanceof BoundingShapePolygon polygon) {
			this.isRelative = polygon.isRelative;
			this.positions = polygon.clonePositions();
		} else if (other instanceof BoundingShapeCylinder cylinder) {
			this.isRelative = cylinder.isRelative;
			if (cylinder.h < 0) {
				this.y1 = cylinder.y + cylinder.h;
				this.y2 = cylinder.y;
			} else {
				this.y1 = cylinder.y;
				this.y2 = cylinder.y + cylinder.h;
			}
			makeCylinder(cylinder.x, cylinder.z, cylinder.r);
		} else if (other instanceof BoundingShapeSphere sphere) {
			this.isRelative = sphere.isRelative;
			this.y1 = sphere.y - sphere.r;
			this.y2 = sphere.y + sphere.r;
			makeCylinder(sphere.x, sphere.z, sphere.r);
		} else if (other instanceof BoundingShapeShaped shaped) {
			AABB box = shaped.getLocalBounds();
			this.y1 = box.minY;
			this.y2 = box.maxY;
			this.positions = new Vector3d[] {
					new Vector3d(box.minX, y1, box.minZ),
					new Vector3d(box.maxX, y2, box.minZ),
					new Vector3d(box.maxX, y1, box.maxZ),
					new Vector3d(box.minX, y2, box.maxZ)
			};
		}
	}

	public void makeCylinder(double x, double z, double r) {
		boolean top = false;
		this.positions = new Vector3d[Constants.CIRCLE_MESH_CACHE.length];
		for (int i = 0; i < positions.length; ++i) {
			Vec2 offset = Constants.CIRCLE_MESH_CACHE[i];
			positions[i] = new Vector3d(x + (offset.x * r), top ? y2 : y1, z + (offset.y * r));
			top = !top;
		}
	}
}