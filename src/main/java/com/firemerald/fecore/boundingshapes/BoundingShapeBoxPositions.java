package com.firemerald.fecore.boundingshapes;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

import javax.annotation.Nullable;

import com.firemerald.fecore.client.Translator;
import com.firemerald.fecore.client.gui.components.Button;
import com.firemerald.fecore.client.gui.components.IComponent;
import com.firemerald.fecore.client.gui.components.decoration.FloatingText;
import com.firemerald.fecore.client.gui.components.text.DoubleField;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BoundingShapeBoxPositions extends BoundingShapeConfigurable implements IRenderableBoundingShape
{
	public boolean isRelative = true;
	public double x1 = -10, y1 = -10, z1 = -10, x2 = 10, y2 = 10, z2 = 10;

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
	public void saveToNBT(CompoundTag tag)
	{
		super.saveToNBT(tag);
		tag.putBoolean("isRelative", isRelative);
		tag.putDouble("x1", x1);
		tag.putDouble("y1", y1);
		tag.putDouble("z1", z1);
		tag.putDouble("x2", x2);
		tag.putDouble("y2", y2);
		tag.putDouble("z2", z2);
	}

	@Override
	public void loadFromNBT(CompoundTag tag)
	{
		super.loadFromNBT(tag);
		isRelative = tag.getBoolean("isRelative");
		if (tag.contains("x1", 99)) x1 = tag.getDouble("x1");
		else x1 = -10;
		if (tag.contains("y1", 99)) y1 = tag.getDouble("y1");
		else y1 = -10;
		if (tag.contains("z1", 99)) z1 = tag.getDouble("z1");
		else z1 = -10;
		if (tag.contains("x2", 99)) x2 = tag.getDouble("x2");
		else x2 = 10;
		if (tag.contains("y2", 99)) y2 = tag.getDouble("y2");
		else y2 = 10;
		if (tag.contains("z2", 99)) z2 = tag.getDouble("z2");
		else z2 = 10;
	}

	@Override
	public void saveToBuffer(FriendlyByteBuf buf)
	{
		super.saveToBuffer(buf);
		buf.writeBoolean(isRelative);
		buf.writeDouble(x1);
		buf.writeDouble(y1);
		buf.writeDouble(z1);
		buf.writeDouble(x2);
		buf.writeDouble(y2);
		buf.writeDouble(z2);
	}

	@Override
	public void loadFromBuffer(FriendlyByteBuf buf)
	{
		super.loadFromBuffer(buf);
		isRelative = buf.readBoolean();
		x1 = buf.readDouble();
		y1 = buf.readDouble();
		z1 = buf.readDouble();
		x2 = buf.readDouble();
		y2 = buf.readDouble();
		z2 = buf.readDouble();
	}

	@Override
	public void addGuiElements(Vec3 pos, IShapeGui gui, Font font, Consumer<IComponent> addElement, int width)
	{
		int offX = (width - 200) >> 1;
		final DoubleField
		posX1 = new DoubleField(font, offX, 20, 67, 20, x1, new TranslatableComponent("fecore.shapesgui.position.1.x"), (DoubleConsumer) (val -> x1 = val)),
		posY1 = new DoubleField(font, offX + 67, 20, 66, 20, y1, new TranslatableComponent("fecore.shapesgui.position.1.y"), (DoubleConsumer) (val -> y1 = val)),
		posZ1 = new DoubleField(font, offX + 133, 20, 67, 20, z1, new TranslatableComponent("fecore.shapesgui.position.1.z"), (DoubleConsumer) (val -> z1 = val)),
		posX2 = new DoubleField(font, offX, 60, 67, 20, x2, new TranslatableComponent("fecore.shapesgui.position.2.x"), (DoubleConsumer) (val -> x2 = val)),
		posY2 = new DoubleField(font, offX + 67, 60, 66, 20, y2, new TranslatableComponent("fecore.shapesgui.position.2.y"), (DoubleConsumer) (val -> y2 = val)),
		posZ2 = new DoubleField(font, offX + 133, 60, 67, 20, z2, new TranslatableComponent("fecore.shapesgui.position.2.z"), (DoubleConsumer) (val -> z2 = val));
		addElement.accept(new FloatingText(offX, 0, offX + 100, 20, font, Translator.translate("fecore.shapesgui.position.1")));
		addElement.accept(new Button(offX + 100, 0, 100, 20, new TranslatableComponent(isRelative ? "fecore.shapesgui.operator.relative" : "fecore.shapesgui.operator.absolute"), null).setAction(button -> () -> {
			if (isRelative)
			{
				isRelative = false;
				x1 += pos.x;
				y1 += pos.y;
				z1 += pos.z;
				x2 += pos.x;
				y2 += pos.y;
				z2 += pos.z;
				posX1.setDouble(x1);
				posY1.setDouble(y1);
				posZ1.setDouble(z1);
				posX2.setDouble(x2);
				posY2.setDouble(y2);
				posZ2.setDouble(z2);
				button.displayString = new TranslatableComponent("fecore.shapesgui.operator.absolute");
			}
			else
			{
				isRelative = true;
				x1 -= pos.x;
				y1 -= pos.y;
				z1 -= pos.z;
				x2 -= pos.x;
				y2 -= pos.y;
				z2 -= pos.z;
				posX1.setDouble(x1);
				posY1.setDouble(y1);
				posZ1.setDouble(z1);
				posX2.setDouble(x2);
				posY2.setDouble(y2);
				posZ2.setDouble(z2);
				button.displayString = new TranslatableComponent("fecore.shapesgui.operator.relative");
			}
		}));
		addElement.accept(posX1);
		addElement.accept(posY1);
		addElement.accept(posZ1);
		addElement.accept(new FloatingText(offX, 40, offX + 200, 60, font, Translator.translate("fecore.shapesgui.position.2")));
		addElement.accept(posX2);
		addElement.accept(posY2);
		addElement.accept(posZ2);
	}

	@Override
	public int addPosition(Player player, BlockPos blockPos, int num)
	{
		if (isRelative)
		{
			isRelative = false;
			x1 += player.position().x;
			y1 += player.position().y;
			z1 += player.position().z;
			x2 += player.position().x;
			y2 += player.position().y;
			z2 += player.position().z;
		}
		if (num == 0)
		{
			x1 = blockPos.getX() + .5;
			y1 = blockPos.getY() + .5;
			z1 = blockPos.getZ() + .5;
			player.sendMessage(new TranslatableComponent("fecore.shapetool.position.1.set", new Vec3(x1, y1, z1).toString()), Util.NIL_UUID);
			player.sendMessage(new TranslatableComponent("fecore.shapetool.position.2.selected"), Util.NIL_UUID);
			return 1;
		}
		else
		{
			x2 = blockPos.getX() + .5;
			y2 = blockPos.getY() + .5;
			z2 = blockPos.getZ() + .5;
			player.sendMessage(new TranslatableComponent("fecore.shapetool.position.2.set", new Vec3(x2, y2, z2).toString()), Util.NIL_UUID);
			player.sendMessage(new TranslatableComponent("fecore.shapetool.position.1.selected"), Util.NIL_UUID);
			return 0;
		}
	}

	@Override
	public int removePosition(Player player, int num)
	{
		if (num == 0)
		{
			player.sendMessage(new TranslatableComponent("fecore.shapetool.position.2.selected"), Util.NIL_UUID);
			return 1;
		}
		else
		{
			player.sendMessage(new TranslatableComponent("fecore.shapetool.position.1.selected"), Util.NIL_UUID);
			return 0;
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
	{
		tooltip.add(new TranslatableComponent(isRelative ? "fecore.shapetool.tooltip.relative" : "fecore.shapetool.tooltip.absolute"));
		tooltip.add(new TranslatableComponent("fecore.shapetool.tooltip.position.1", new Vec3(x1, y1, z1)));
		tooltip.add(new TranslatableComponent("fecore.shapetool.tooltip.position.2", new Vec3(x2, y2, z2)));
	}

	@Override
	public void renderIntoWorld(PoseStack pose, Vec3 pos, float partialTick)
	{
		float x1, y1, z1, x2, y2, z2;
		if (this.isRelative)
		{
			x1 = (float) (this.x1 + pos.x);
			y1 = (float) (this.y1 + pos.y);
			z1 = (float) (this.z1 + pos.z);
			x2 = (float) (this.x2 + pos.x);
			y2 = (float) (this.y2 + pos.y);
			z2 = (float) (this.z2 + pos.z);
		}
		else
		{
			x1 = (float) this.x1;
			y1 = (float) this.y1;
			z1 = (float) this.z1;
			x2 = (float) this.x2;
			y2 = (float) this.y2;
			z2 = (float) this.z2;
		}
		IRenderableBoundingShape.renderCube(pose.last().pose(), pose.last().normal(), x1, y1, z1, x2, y2, z2, .5f, .5f, 1f, .5f);
	}
}