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

public class BoundingShapeCylinder extends BoundingShapeOriginShaped implements IRenderableBoundingShape, IConfigurableBoundingShape
{
	public static final MapCodec<BoundingShapeCylinder> CODEC = RecordCodecBuilder.mapCodec(instance ->
		instance.group(
				Codec.DOUBLE.fieldOf("x").forGetter(cylinder -> cylinder.x),
				Codec.DOUBLE.fieldOf("y").forGetter(cylinder -> cylinder.y),
				Codec.DOUBLE.fieldOf("z").forGetter(cylinder -> cylinder.z),
				Codec.doubleRange(0, Double.MAX_VALUE).fieldOf("r").forGetter(cylinder -> cylinder.r),
				Codec.DOUBLE.fieldOf("h").forGetter(cylinder -> cylinder.h),
				Codec.BOOL.optionalFieldOf("isRelative", true).forGetter(cylinder -> cylinder.isRelative)
				)
		.apply(instance, BoundingShapeCylinder::new)
	);
	public static final StreamCodec<BoundingShapeCylinder> STREAM_CODEC = StreamCodec.composite(
			StreamCodec.DOUBLE, cylinder -> cylinder.x,
			StreamCodec.DOUBLE, cylinder -> cylinder.y,
			StreamCodec.DOUBLE, cylinder -> cylinder.z,
			StreamCodec.DOUBLE, cylinder -> cylinder.r,
			StreamCodec.DOUBLE, cylinder -> cylinder.h,
			StreamCodec.BOOL, cylinder -> cylinder.isRelative,
			BoundingShapeCylinder::new
			);

	public double r, h;

	public BoundingShapeCylinder(double x, double y, double z, double r, double h, boolean isRelative) {
		super(x, y, z, isRelative);
		this.r = r;
		this.h = h;
	}

	public BoundingShapeCylinder(double x, double y, double z, double r, double h) {
		this(x, y, z, r, h, true);
	}

	public BoundingShapeCylinder() {
		this(0, 0, 0, 10, 20, true);
	}

	@Override
	public String getUnlocalizedName()
	{
		return "fecore.shape.cylinder";
	}

	@Override
	public boolean isWithinOffset(@Nullable Entity entity, double dX, double dY, double dZ)
	{
		return (dX * dX) + (dZ * dZ) <= r * r && (h >= 0 ? (dY >=0 && dY <= h) : (dY <= 0 && dY >= h));
	}

	@Override
	public AABB getOffsetBounds(double x, double y, double z)
	{
		return new AABB(x - r, y, z - r, x + r, y + h, z + r);
	}

	@Override
	public void addGuiElements(Vec3 pos, IShapeGui gui, Font font, Consumer<IComponent> addElement, int width)
	{
		super.addGuiElements(pos, gui, font, addElement, width);
		int offX = (width - 200) >> 1;
		final DoubleField
		radius = new DoubleField(font, offX + 100, 40, 100, 20, r, Component.translatable("fecore.shapesgui.radius"), (DoubleConsumer) (val -> r = val)),
		height = new DoubleField(font, offX + 100, 60, 100, 20, h, Component.translatable("fecore.shapesgui.height"), (DoubleConsumer) (val -> h = val));
		addElement.accept(new FloatingText(offX, 40, offX + 100, 60, font, I18n.get("fecore.shapesgui.radius")));
		addElement.accept(radius);
		addElement.accept(new FloatingText(offX, 60, offX + 100, 80, font, I18n.get("fecore.shapesgui.height")));
		addElement.accept(height);
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
		else if (num == 1)
		{
			double dx = blockPos.getX() + .5 - x;
			double dz = blockPos.getZ() + .5 - z;
			r = Math.sqrt(dx * dx + dz * dz);
			sendMessage(player, Component.translatable("fecore.shapetool.radius.set", r));
			sendMessage(player, Component.translatable("fecore.shapetool.height.selected"));
			return 2;
		}
		else
		{
			h = blockPos.getY() + .5 - y;
			sendMessage(player, Component.translatable("fecore.shapetool.height.set", h));
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
		else if (num == 1)
		{
			sendMessage(player, Component.translatable("fecore.shapetool.height.selected"));
			return 2;
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
		tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.height", h));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderIntoWorld(PoseStack pose, double x, double y, double z, float partialTick)
	{
		IRenderableBoundingShape.renderCylinder(pose.last().pose(), x, y, z, r, h, .5f, .5f, 1f, .5f);
	}

	@Override
	public int hashCode() {
		return
				((((int) x) & 0b1111111) << 00) | //x = 7 @ 0
				((((int) y) & 0b0111111) << 07) | //y = 6 @ 7
				((((int) z) & 0b1111111) << 13) | //z = 7 @ 13
				((((int) r) & 0b0111111) << 20) | //r = 6 @ 20
				((((int) h) & 0b0111111) << 26);  //h = 6 @ 26
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		else if (o == this) return true;
		else if (o.getClass() != this.getClass()) return false;
		else {
			BoundingShapeCylinder cylinder = (BoundingShapeCylinder) o;
			return
					cylinder.isRelative == isRelative &&
					cylinder.x == x &&
					cylinder.y == y &&
					cylinder.z == z &&
					cylinder.r == r &&
					cylinder.h == h;
		}
	}

	@Override
	public BoundingShapeCylinder clone() {
		return new BoundingShapeCylinder(x, y, z, r, h, isRelative);
	}

	@Override
	public BoundingShapeDefinition<BoundingShapeCylinder> definition() {
		return FECoreBoundingShapes.CYLINDER.get();
	}

	@Override
	public void getPropertiesFrom(BoundingShape other) {
		if (other instanceof BoundingShapeCylinder cylinder) {
			isRelative = cylinder.isRelative;
			x = cylinder.x;
			y = cylinder.y;
			z = cylinder.z;
			r = cylinder.r;
			h = cylinder.h;
		} else if (other instanceof BoundingShapeSphere sphere) {
			isRelative = sphere.isRelative;
			x = sphere.x;
			y = sphere.y - sphere.r;
			z = sphere.z;
			r = sphere.r;
			h = 2 * sphere.r;
		}  else if (other instanceof BoundingShapeShaped shaped) {
			isRelative = shaped.isRelative;
			AABB box = shaped.getLocalBounds();
			Vec3 center = new Vec3((box.minX + box.maxX) * 0.5, box.minY, (box.minZ + box.maxZ) * 0.5);
			x = center.x;
			y = center.y;
			z = center.z;
			r = Math.sqrt(box.getXsize() * box.getZsize()) * 0.5; //TODO is this the ideal mean?
			h = box.getYsize();
		}
	}
}