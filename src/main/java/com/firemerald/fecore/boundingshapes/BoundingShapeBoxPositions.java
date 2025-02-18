package com.firemerald.fecore.boundingshapes;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

import javax.annotation.Nullable;

import com.firemerald.fecore.client.gui.components.Button;
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

public class BoundingShapeBoxPositions extends BoundingShapeShaped implements IRenderableBoundingShape, IConfigurableBoundingShape
{
	public static final MapCodec<BoundingShapeBoxPositions> CODEC = RecordCodecBuilder.mapCodec(instance ->
		instance.group(
				Codec.DOUBLE.fieldOf("x1").forGetter(box -> box.x1),
				Codec.DOUBLE.fieldOf("y1").forGetter(box -> box.y1),
				Codec.DOUBLE.fieldOf("z1").forGetter(box -> box.z1),
				Codec.DOUBLE.fieldOf("x2").forGetter(box -> box.x2),
				Codec.DOUBLE.fieldOf("y2").forGetter(box -> box.y2),
				Codec.DOUBLE.fieldOf("z2").forGetter(box -> box.z2),
				Codec.BOOL.optionalFieldOf("isRelative", true).forGetter(box -> box.isRelative)
				)
		.apply(instance, BoundingShapeBoxPositions::new)
	);
	public static final StreamCodec<BoundingShapeBoxPositions> STREAM_CODEC = StreamCodec.composite(
			StreamCodec.DOUBLE, box -> box.x1,
			StreamCodec.DOUBLE, box -> box.y1,
			StreamCodec.DOUBLE, box -> box.z1,
			StreamCodec.DOUBLE, box -> box.x2,
			StreamCodec.DOUBLE, box -> box.y2,
			StreamCodec.DOUBLE, box -> box.z2,
			StreamCodec.BOOL, box -> box.isRelative,
			BoundingShapeBoxPositions::new
			);

	public double x1, y1, z1, x2, y2, z2;

	public BoundingShapeBoxPositions(double x1, double y1, double z1, double x2, double y2, double z2, boolean isRelative) {
		super(isRelative);
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
	}

	public BoundingShapeBoxPositions(double x1, double y1, double z1, double x2, double y2, double z2) {
		this(x1, y1, z1, x2, y2, z2, true);
	}

	public BoundingShapeBoxPositions(AABB box, boolean isRelative) {
		this(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, true);
	}

	public BoundingShapeBoxPositions(AABB box) {
		this(box, true);
	}

	public BoundingShapeBoxPositions(boolean isRelative) {
		this(-10, -10, -10, 10, 10, 10, isRelative);
	}

	public BoundingShapeBoxPositions() {
		this(true);
	}

	@Override
	public String getUnlocalizedName()
	{
		return "fecore.shape.box.positions";
	}

	@Override
	public boolean isWithin(@Nullable Entity entity, double posX, double posY, double posZ, double testerX, double testerY, double testerZ)
	{
		double x1, y1, z1, x2, y2, z2;
		if (isRelative)
		{
			x1 = this.x1 + testerX;
			y1 = this.y1 + testerY;
			z1 = this.z1 + testerZ;
			x2 = this.x2 + testerX;
			y2 = this.y2 + testerY;
			z2 = this.z2 + testerZ;
		}
		else
		{
			x1 = this.x1;
			y1 = this.y1;
			z1 = this.z1;
			x2 = this.x2;
			y2 = this.y2;
			z2 = this.z2;
		}
		return (x2 >= x1 ? (posX >= x1 && posX <= x2 + 1) : (posX <= x1 + 1 && posX >= x2)) && (y2 >= y1 ? (posY >= y1 && posY <= y2 + 1) : (posY <= y1 + 1 && posY >= y2)) && (z2 >= z1 ? (posZ >= z1 && posZ <= z2 + 1) : (posZ <= z1 + 1 && posZ >= z2));
	}

	@Override
	public AABB getBoundsOffset(double x, double y, double z)
	{
		return new AABB(x1 + x, y1 + y, z1 + z, x2 + x, y2 + y, z2 + z);
	}

	@Override
	public void addGuiElements(Vec3 pos, IShapeGui gui, Font font, Consumer<IComponent> addElement, int width)
	{
		int offX = (width - 200) >> 1;
		final DoubleField
		posX1 = new DoubleField(font, offX, 20, 67, 20, x1, Component.translatable("fecore.shapesgui.position.1.x"), (DoubleConsumer) (val -> x1 = val)),
		posY1 = new DoubleField(font, offX + 67, 20, 66, 20, y1, Component.translatable("fecore.shapesgui.position.1.y"), (DoubleConsumer) (val -> y1 = val)),
		posZ1 = new DoubleField(font, offX + 133, 20, 67, 20, z1, Component.translatable("fecore.shapesgui.position.1.z"), (DoubleConsumer) (val -> z1 = val)),
		posX2 = new DoubleField(font, offX, 60, 67, 20, x2, Component.translatable("fecore.shapesgui.position.2.x"), (DoubleConsumer) (val -> x2 = val)),
		posY2 = new DoubleField(font, offX + 67, 60, 66, 20, y2, Component.translatable("fecore.shapesgui.position.2.y"), (DoubleConsumer) (val -> y2 = val)),
		posZ2 = new DoubleField(font, offX + 133, 60, 67, 20, z2, Component.translatable("fecore.shapesgui.position.2.z"), (DoubleConsumer) (val -> z2 = val));
		addElement.accept(new FloatingText(offX, 0, offX + 100, 20, font, I18n.get("fecore.shapesgui.position.1")));
		addElement.accept(new Button(offX + 100, 0, 100, 20, Component.translatable(isRelative ? "fecore.shapesgui.operator.relative" : "fecore.shapesgui.operator.absolute"), null).setAction(button -> () -> {
			button.displayString = Component.translatable(toggleRelative(pos) ? "fecore.shapesgui.operator.relative" : "fecore.shapesgui.operator.absolute");
			posX1.setDouble(x1);
			posY1.setDouble(y1);
			posZ1.setDouble(z1);
			posX2.setDouble(x2);
			posY2.setDouble(y2);
			posZ2.setDouble(z2);
		}));
		addElement.accept(posX1);
		addElement.accept(posY1);
		addElement.accept(posZ1);
		addElement.accept(new FloatingText(offX, 40, offX + 200, 60, font, I18n.get("fecore.shapesgui.position.2")));
		addElement.accept(posX2);
		addElement.accept(posY2);
		addElement.accept(posZ2);
	}

	@Override
	public int addPosition(Player player, BlockPos blockPos, int num)
	{
		setRelative(false, player.position());
		if (num == 0)
		{
			x1 = blockPos.getX() + .5;
			y1 = blockPos.getY() + .5;
			z1 = blockPos.getZ() + .5;
			sendMessage(player, Component.translatable("fecore.shapetool.position.2.selected"));
			return 1;
		}
		else
		{
			x2 = blockPos.getX() + .5;
			y2 = blockPos.getY() + .5;
			z2 = blockPos.getZ() + .5;
			sendMessage(player, Component.translatable("fecore.shapetool.position.2.set", new Vec3(x2, y2, z2).toString()));
			sendMessage(player, Component.translatable("fecore.shapetool.position.1.selected"));
			return 0;
		}
	}

	@Override
	public int removePosition(Player player, int num)
	{
		if (num == 0)
		{
			sendMessage(player, Component.translatable("fecore.shapetool.position.2.selected"));
			return 1;
		}
		else
		{
			sendMessage(player, Component.translatable("fecore.shapetool.position.1.selected"));
			return 0;
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
	{
		tooltipComponents.add(Component.translatable(isRelative ? "fecore.shapetool.tooltip.relative" : "fecore.shapetool.tooltip.absolute"));
		tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.position.1", x1, y1, z1));
		tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.position.2", x2, y2, z2));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderIntoWorld(PoseStack pose, double x, double y, double z, float partialTick)
	{
		IRenderableBoundingShape.renderCube(pose.last().pose(), x1 + x, y1 + y, z1 + z, x2 + x, y2 + y, z2 + z, .5f, .5f, 1f, .5f);
	}

	@Override
	public int hashCode() {
		double sizeX = x2 - x1;
		double sizeY = y2 - y1;
		double sizeZ = z2 - z1;
		return
				((((int) x1   ) & 0b111111) << 00) | //x = 6 @ 0
				((((int) y1   ) & 0b011111) << 06) | //y = 5 @ 6
				((((int) z1   ) & 0b111111) << 11) | //z = 6 @ 11
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
			BoundingShapeBoxPositions box = (BoundingShapeBoxPositions) o;
			return
					box.isRelative == isRelative &&
					box.x1 == x1 &&
					box.y1 == y1 &&
					box.z1 == z1 &&
					box.x2 == x2 &&
					box.y2 == y2 &&
					box.z2 == z2;
		}
	}

	@Override
	public BoundingShapeBoxPositions clone() {
		return new BoundingShapeBoxPositions(x1, y1, z1, x2, y2, z2, isRelative);
	}

	@Override
	public BoundingShapeDefinition<BoundingShapeBoxPositions> definition() {
		return FECoreBoundingShapes.BOX_POSITIONS.get();
	}

	@Override
	public void getPropertiesFrom(BoundingShape other) {
		if (other instanceof BoundingShapeShaped shaped) {
			this.isRelative = shaped.isRelative;
			AABB bounds = shaped.getLocalBounds();
			this.x1 = bounds.minX;
			this.y1 = bounds.minY;
			this.z1 = bounds.minZ;
			this.x2 = bounds.maxX;
			this.y2 = bounds.maxY;
			this.z2 = bounds.maxZ;
		}
	}

	@Override
	public void offset(double x, double y, double z) {
		x1 += x;
		y1 += y;
		z1 += z;
		x2 += x;
		y2 += y;
		z2 += z;
	}
}