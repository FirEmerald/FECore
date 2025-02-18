package com.firemerald.fecore.boundingshapes;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

import javax.annotation.Nullable;

import com.firemerald.fecore.client.gui.components.IComponent;
import com.firemerald.fecore.client.gui.components.decoration.FloatingText;
import com.firemerald.fecore.client.gui.components.text.DoubleField;
import com.firemerald.fecore.codec.stream.StreamCodec;
import com.firemerald.fecore.init.FECoreBoundingShapes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BoundingShapeSphere extends BoundingShapeOriginShaped implements IRenderableBoundingShape, IConfigurableBoundingShape
{
	public static final MapCodec<BoundingShapeSphere> CODEC = RecordCodecBuilder.mapCodec(instance ->
		instance.group(
				Codec.DOUBLE.fieldOf("x").forGetter(sphere -> sphere.x),
				Codec.DOUBLE.fieldOf("y").forGetter(sphere -> sphere.y),
				Codec.DOUBLE.fieldOf("z").forGetter(sphere -> sphere.z),
				Codec.doubleRange(0, Double.MAX_VALUE).fieldOf("r").forGetter(sphere -> sphere.r),
				Codec.BOOL.optionalFieldOf("isRelative", true).forGetter(sphere -> sphere.isRelative)
				)
		.apply(instance, BoundingShapeSphere::new)
	);
	public static final StreamCodec<BoundingShapeSphere> STREAM_CODEC = StreamCodec.composite(
			StreamCodec.DOUBLE, sphere -> sphere.x,
			StreamCodec.DOUBLE, sphere -> sphere.y,
			StreamCodec.DOUBLE, sphere -> sphere.z,
			StreamCodec.DOUBLE, sphere -> sphere.r,
			StreamCodec.BOOL, sphere -> sphere.isRelative,
			BoundingShapeSphere::new
			);

	public double r = 10;

	public BoundingShapeSphere(double x, double y, double z, double r, boolean isRelative) {
		super(x, y, z, isRelative);
		this.r = r;
	}

	public BoundingShapeSphere(double x, double y, double z, double r) {
		this(x, y, z, r, true);
	}

	public BoundingShapeSphere() {
		this(0.5, 0.5, 0.5, 10, true);
	}

	@Override
	public String getUnlocalizedName()
	{
		return "fecore.shape.sphere";
	}

	@Override
	public boolean isWithinOffset(@Nullable Entity entity, double dX, double dY, double dZ)
	{
		return (dX * dX) + (dY * dY) + (dZ * dZ) <= r * r;
	}

	@Override
	public AABB getOffsetBounds(double x, double y, double z)
	{
		return new AABB(x - r, y - r, z - r, x + r, y + r, z + r);
	}

	@Override
	public void addGuiElements(Vec3 pos, IShapeGui gui, Font font, Consumer<IComponent> addElement, int width)
	{
		super.addGuiElements(pos, gui, font, addElement, width);
		int offX = (width - 200) >> 1;
		final DoubleField
		radius = new DoubleField(font, offX + 100, 40, 100, 20, r, Component.translatable("fecore.shapesgui.radius"), (DoubleConsumer) (val -> r = val));
		addElement.accept(new FloatingText(offX, 40, offX + 100, 60, font, I18n.get("fecore.shapesgui.radius")));
		addElement.accept(radius);
	}

	@Override
	public int addPosition(Player player, BlockPos blockPos, int num)
	{
		onAddPosition(player);
		if (num == 0)
		{
			x = blockPos.getX() + .5;
			y = blockPos.getY() + .5;
			z = blockPos.getZ() + .5;
			sendMessage(player, Component.translatable("fecore.shapetool.position.set", new Vec3(x, y, z).toString()));
			sendMessage(player, Component.translatable("fecore.shapetool.radius.selected"));
			return 1;
		}
		else
		{
			double dx = blockPos.getX() + .5 - x;
			double dy = blockPos.getY() + .5 - y;
			double dz = blockPos.getZ() + .5 - z;
			r = Math.sqrt(dx * dx + dy * dy + dz * dz);
			sendMessage(player, Component.translatable("fecore.shapetool.radius.set", r));
			sendMessage(player, Component.translatable("fecore.shapetool.position.selected"));
			return 0;
		}
	}

	@Override
	public int removePosition(Player player, int num)
	{
		if (num == 0)
		{
			sendMessage(player, Component.translatable("fecore.shapetool.radius.selected"));
			return 1;
		}
		else
		{
			sendMessage(player, Component.translatable("fecore.shapetool.position.selected"));
			return 0;
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
	{
		tooltipComponents.add(Component.translatable(isRelative ? "fecore.shapetool.tooltip.relative" : "fecore.shapetool.tooltip.absolute"));
		tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.position", x, y, z));
		tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.radius", r));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderIntoWorld(PoseStack pose, double x, double y, double z, float partialTick)
	{
		IRenderableBoundingShape.renderSphere(pose.last().pose(), x, y, z, r, .5f, .5f, 1f, .5f);
	}

	@Override
	public int hashCode() {
		return
				((((int) x) & 0b11111111) << 00) | //x = 8 @ 0
				((((int) y) & 0b11111111) <<  8) | //y = 8 @ 8
				((((int) z) & 0b11111111) << 16) | //z = 8 @ 16
				((((int) r) & 0b11111111) << 24);  //r = 8 @ 24
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		else if (o == this) return true;
		else if (o.getClass() != this.getClass()) return false;
		else {
			BoundingShapeSphere sphere = (BoundingShapeSphere) o;
			return
					sphere.isRelative == isRelative &&
					sphere.x == x &&
					sphere.y == y &&
					sphere.z == z &&
					sphere.r == r;
		}
	}

	@Override
	public BoundingShapeSphere clone() {
		return new BoundingShapeSphere(x, y, z, r, isRelative);
	}

	@Override
	public BoundingShapeDefinition<BoundingShapeSphere> definition() {
		return FECoreBoundingShapes.SPHERE.get();
	}

	@Override
	public void getPropertiesFrom(BoundingShape other) {
		if (other instanceof BoundingShapeSphere sphere) {
			isRelative = sphere.isRelative;
			x = sphere.x - sphere.r;
			y = sphere.y;
			z = sphere.z;
			r = sphere.r;
		}  else if (other instanceof BoundingShapeCylinder cylinder) {
			isRelative = cylinder.isRelative;
			double hh = cylinder.h * 0.5;
			x = cylinder.x;
			y = cylinder.y + hh;
			z = cylinder.z;
			r = Math.cbrt(cylinder.r * cylinder.r * hh); //TODO is the the ideal mean?
		} else if (other instanceof BoundingShapeShaped shaped) {
			isRelative = shaped.isRelative;
			AABB box = shaped.getLocalBounds();
			Vec3 center = box.getCenter();
			x = center.x;
			y = center.y;
			z = center.z;
			r = Math.cbrt(box.getXsize() * box.getYsize() * box.getZsize()) * 0.5; //TODO is this the ideal mean?
		}
	}
}