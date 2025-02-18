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

public class BoundingShapeBoxOffsets extends BoundingShapeOriginShaped implements IRenderableBoundingShape, IConfigurableBoundingShape
{
	public static final MapCodec<BoundingShapeBoxOffsets> CODEC = RecordCodecBuilder.mapCodec(instance ->
		instance.group(
				Codec.DOUBLE.fieldOf("x").forGetter(box -> box.x),
				Codec.DOUBLE.fieldOf("y").forGetter(box -> box.y),
				Codec.DOUBLE.fieldOf("z").forGetter(box -> box.z),
				Codec.DOUBLE.fieldOf("sizeX").forGetter(box -> box.sizeX),
				Codec.DOUBLE.fieldOf("sizeY").forGetter(box -> box.sizeY),
				Codec.DOUBLE.fieldOf("sizeZ").forGetter(box -> box.sizeZ),
				Codec.BOOL.optionalFieldOf("isRelative", true).forGetter(box -> box.isRelative)
				)
		.apply(instance, BoundingShapeBoxOffsets::new)
	);
	public static final StreamCodec<BoundingShapeBoxOffsets> STREAM_CODEC = StreamCodec.composite(
			StreamCodec.DOUBLE, box -> box.x,
			StreamCodec.DOUBLE, box -> box.y,
			StreamCodec.DOUBLE, box -> box.z,
			StreamCodec.DOUBLE, box -> box.sizeX,
			StreamCodec.DOUBLE, box -> box.sizeY,
			StreamCodec.DOUBLE, box -> box.sizeZ,
			StreamCodec.BOOL, box -> box.isRelative,
			BoundingShapeBoxOffsets::new
			);

	public double sizeX, sizeY, sizeZ;

	public BoundingShapeBoxOffsets(double x, double y, double z, double sizeX, double sizeY, double sizeZ, boolean isRelative) {
		super(x, y, z, isRelative);
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
	}

	public BoundingShapeBoxOffsets(double x, double y, double z, double sizeX, double sizeY, double sizeZ) {
		this(x, y, z, sizeX, sizeY, sizeZ, true);
	}

	public BoundingShapeBoxOffsets(AABB box, boolean isRelative) {
		this(box.minX, box.minY, box.minZ, box.getXsize(), box.getYsize(), box.getZsize(), true);
	}

	public BoundingShapeBoxOffsets(AABB box) {
		this(box, true);
	}

	public BoundingShapeBoxOffsets() {
		this(0, 0, 0, 20, 20, 20, true);
	}

	@Override
	public String getUnlocalizedName()
	{
		return "fecore.shape.box.offsets";
	}

	@Override
	public boolean isWithinOffset(@Nullable Entity entity, double dX, double dY, double dZ)
	{
		double dx = entity.getX() - x;
		double dy = entity.getY() - y;
		double dz = entity.getZ() - z;
		return (sizeX >= 0 ? (dx >=0 && dx <= sizeX) : (dx <= 0 && dx >= sizeX)) && (sizeY >= 0 ? (dy >=0 && dy <= sizeY) : (dy <= 0 && dy >= sizeY)) && (sizeZ >= 0 ? (dz >=0 && dz <= sizeZ) : (dz <= 0 && dz >= sizeZ));
	}

	@Override
	public AABB getOffsetBounds(double x, double y, double z) {
		return new AABB(x, y, z, x + sizeX, y + sizeY, z + sizeZ);
	}

	@Override
	public void addGuiElements(Vec3 pos, IShapeGui gui, Font font, Consumer<IComponent> addElement, int width)
	{
		super.addGuiElements(pos, gui, font, addElement, width);
		int offX = (width - 200) >> 1;
		addElement.accept(new FloatingText(offX, 40, offX + 200, 60, font, I18n.get("fecore.shapesgui.size")));
		addElement.accept(new DoubleField(font, offX, 60, 67, 20, sizeX, Component.translatable("fecore.shapesgui.size.x"), (DoubleConsumer) (val -> sizeX = val)));
		addElement.accept(new DoubleField(font, offX + 67, 60, 66, 20, sizeY, Component.translatable("fecore.shapesgui.size.y"), (DoubleConsumer) (val -> sizeY = val)));
		addElement.accept(new DoubleField(font, offX + 133, 60, 67, 20, sizeZ, Component.translatable("fecore.shapesgui.size.z"), (DoubleConsumer) (val -> sizeZ = val)));
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
			sendMessage(player, Component.translatable("fecore.shapetool.size.x.selected"));
			return 1;
		}
		else if (num == 1)
		{
			sizeX = blockPos.getX() + .5 - x;
			sendMessage(player, Component.translatable("fecore.shapetool.size.x.set", sizeX));
			sendMessage(player, Component.translatable("fecore.shapetool.size.y.selected"));
			return 2;
		}
		else if (num == 2)
		{
			sizeY = blockPos.getY() + .5 - y;
			sendMessage(player, Component.translatable("fecore.shapetool.size.y.set", sizeY));
			sendMessage(player, Component.translatable("fecore.shapetool.size.z.selected"));
			return 3;
		}
		else
		{
			sizeZ = blockPos.getZ() + .5 - z;
			sendMessage(player, Component.translatable("fecore.shapetool.size.z.set", sizeZ));
			sendMessage(player, Component.translatable("fecore.shapetool.position.selected"));
			return 0;
		}
	}

	@Override
	public int removePosition(Player player, int num)
	{
		if (num == 0)
		{
			sendMessage(player, Component.translatable("fecore.shapetool.size.x.selected"));
			return 1;
		}
		else if (num == 1)
		{
			sendMessage(player, Component.translatable("fecore.shapetool.size.y.selected"));
			return 2;
		}
		else if (num == 2)
		{
			sendMessage(player, Component.translatable("fecore.shapetool.size.z.selected"));
			return 3;
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
		tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.size", sizeX, sizeY, sizeZ));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderIntoWorld(PoseStack pose, double x, double y, double z, float partialTick)
	{
		IRenderableBoundingShape.renderCube(pose.last().pose(), x, y, z, x + sizeX, y + sizeY, z + sizeZ, .5f, .5f, 1f, .5f);
	}

	@Override
	public int hashCode() {
		return
				((((int) x    ) & 0b111111) << 00) | //x = 6 @ 0
				((((int) y    ) & 0b011111) << 06) | //y = 5 @ 6
				((((int) z    ) & 0b111111) << 11) | //z = 6 @ 11
				((((int) sizeX) & 0b011111) << 17) | //sx = 5 @ 17
				((((int) sizeY) & 0b011111) << 22) | //sy = 5 @ 22
				((((int) sizeZ) & 0b011111) << 27);  //sz = 5 @ 27
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		else if (o == this) return true;
		else if (o.getClass() != this.getClass()) return false;
		else {
			BoundingShapeBoxOffsets box = (BoundingShapeBoxOffsets) o;
			return
					box.isRelative == isRelative &&
					box.x == x &&
					box.y == y &&
					box.z == z &&
					box.sizeX == sizeX &&
					box.sizeY == sizeY &&
					box.sizeZ == sizeZ;
		}
	}

	@Override
	public BoundingShapeBoxOffsets clone() {
		return new BoundingShapeBoxOffsets(x, y, z, sizeX, sizeY, sizeZ, isRelative);
	}

	@Override
	public BoundingShapeDefinition<BoundingShapeBoxOffsets> definition() {
		return FECoreBoundingShapes.BOX_OFFSETS.get();
	}

	@Override
	public void getPropertiesFrom(BoundingShape other) {
		if (other instanceof BoundingShapeShaped shaped) {
			this.isRelative = shaped.isRelative;
			AABB bounds = shaped.getLocalBounds();
			this.x = bounds.minX;
			this.y = bounds.minY;
			this.z = bounds.minZ;
			this.sizeX = bounds.getXsize();
			this.sizeY = bounds.getYsize();
			this.sizeZ = bounds.getZsize();
		}
	}
}