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

public class BoundingShapeBoxOffsets extends BoundingShapeConfigurable implements IRenderableBoundingShape
{
	public boolean isRelative = true;
	public double x = 0, y = 0, z = 0, sizeX = 20, sizeY = 20, sizeZ = 20;

	@Override
	public String getUnlocalizedName()
	{
		return "fecore.shape.box.offsets";
	}

	@Override
	public boolean isWithin(@Nullable Entity entity, double posX, double posY, double posZ, double testerX, double testerY, double testerZ)
	{
		double x, y, z;
		if (isRelative)
		{
			x = this.x + testerX;
			y = this.y + testerY;
			z = this.z + testerZ;
		}
		else
		{
			x = this.x;
			y = this.y;
			z = this.z;
		}
		double dx = entity.getX() - x;
		double dy = entity.getY() - y;
		double dz = entity.getZ() - z;
		return (sizeX >= 0 ? (dx >=0 && dx <= sizeX) : (dx <= 0 && dx >= sizeX)) && (sizeY >= 0 ? (dy >=0 && dy <= sizeY) : (dy <= 0 && dy >= sizeY)) && (sizeZ >= 0 ? (dz >=0 && dz <= sizeZ) : (dz <= 0 && dz >= sizeZ));
	}

	@Override
	public void saveToNBT(CompoundTag tag)
	{
		super.saveToNBT(tag);
		tag.putBoolean("isRelative", isRelative);
		tag.putDouble("x", x);
		tag.putDouble("y", y);
		tag.putDouble("z", z);
		tag.putDouble("sizeX", sizeX);
		tag.putDouble("sizeY", sizeY);
		tag.putDouble("sizeZ", sizeZ);
	}

	@Override
	public void loadFromNBT(CompoundTag tag)
	{
		super.loadFromNBT(tag);
		isRelative = tag.getBoolean("isRelative");
		x = tag.getDouble("x");
		y = tag.getDouble("y");
		z = tag.getDouble("z");
		if (tag.contains("sizeX", 99)) sizeX = tag.getDouble("sizeX");
		else sizeX = 20;
		if (tag.contains("sizeY", 99)) sizeY = tag.getDouble("sizeY");
		else sizeY = 20;
		if (tag.contains("sizeZ", 99)) sizeZ = tag.getDouble("sizeZ");
		else sizeZ = 20;
	}

	@Override
	public void saveToBuffer(FriendlyByteBuf buf)
	{
		super.saveToBuffer(buf);
		buf.writeBoolean(isRelative);
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeDouble(sizeX);
		buf.writeDouble(sizeY);
		buf.writeDouble(sizeZ);
	}

	@Override
	public void loadFromBuffer(FriendlyByteBuf buf)
	{
		super.loadFromBuffer(buf);
		isRelative = buf.readBoolean();
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		sizeX = buf.readDouble();
		sizeY = buf.readDouble();
		sizeZ = buf.readDouble();
	}

	@Override
	public void addGuiElements(Vec3 pos, IShapeGui gui, Font font, Consumer<IComponent> addElement, int width)
	{
		int offX = (width - 200) >> 1;
		final DoubleField
		posX = new DoubleField(font, offX, 20, 67, 20, x, new TranslatableComponent("fecore.shapesgui.position.x"), (DoubleConsumer) (val -> x = val)),
		posY = new DoubleField(font, offX + 67, 20, 66, 20, y, new TranslatableComponent("fecore.shapesgui.position.y"), (DoubleConsumer) (val -> y = val)),
		posZ = new DoubleField(font, offX + 133, 20, 67, 20, z, new TranslatableComponent("fecore.shapesgui.position.z"), (DoubleConsumer) (val -> z = val));
		addElement.accept(new FloatingText(offX, 0, offX + 100, 20, font, Translator.translate("fecore.shapesgui.position")));
		addElement.accept(new Button(offX + 100, 0, 100, 20, new TranslatableComponent(isRelative ? "fecore.shapesgui.operator.relative" : "fecore.shapesgui.operator.absolute"), null).setAction(button -> () -> {
			if (isRelative)
			{
				isRelative = false;
				x += pos.x;
				y += pos.y;
				z += pos.z;
				posX.setDouble(x);
				posY.setDouble(y);
				posZ.setDouble(z);
				button.displayString = new TranslatableComponent("fecore.shapesgui.operator.absolute");
			}
			else
			{
				x -= pos.x;
				y -= pos.y;
				z -= pos.z;
				posX.setDouble(x);
				posY.setDouble(y);
				posZ.setDouble(z);
				isRelative = true;
				button.displayString = new TranslatableComponent("fecore.shapesgui.operator.relative");
			}
		}));
		addElement.accept(posX);
		addElement.accept(posY);
		addElement.accept(posZ);
		addElement.accept(new FloatingText(offX, 40, offX + 200, 60, font, Translator.translate("fecore.shapesgui.size")));
		addElement.accept(new DoubleField(font, offX, 60, 67, 20, sizeX, new TranslatableComponent("fecore.shapesgui.size.x"), (DoubleConsumer) (val -> sizeX = val)));
		addElement.accept(new DoubleField(font, offX + 67, 60, 66, 20, sizeY, new TranslatableComponent("fecore.shapesgui.size.y"), (DoubleConsumer) (val -> sizeY = val)));
		addElement.accept(new DoubleField(font, offX + 133, 60, 67, 20, sizeZ, new TranslatableComponent("fecore.shapesgui.size.z"), (DoubleConsumer) (val -> sizeZ = val)));
	}

	@Override
	public int addPosition(Player player, BlockPos blockPos, int num)
	{
		if (isRelative)
		{
			isRelative = false;
			x += player.position().x;
			y += player.position().y;
			z += player.position().z;
		}
		if (num == 0)
		{
			x = blockPos.getX() + .5;
			y = blockPos.getY() + .5;
			z = blockPos.getZ() + .5;
			player.sendMessage(new TranslatableComponent("fecore.shapetool.position.set", new Vec3(x, y, z).toString()), Util.NIL_UUID);
			player.sendMessage(new TranslatableComponent("fecore.shapetool.size.x.selected"), Util.NIL_UUID);
			return 1;
		}
		else if (num == 1)
		{
			sizeX = blockPos.getX() + .5 - x;
			player.sendMessage(new TranslatableComponent("fecore.shapetool.size.x.set", sizeX), Util.NIL_UUID);
			player.sendMessage(new TranslatableComponent("fecore.shapetool.size.y.selected"), Util.NIL_UUID);
			return 2;
		}
		else if (num == 2)
		{
			sizeY = blockPos.getY() + .5 - y;
			player.sendMessage(new TranslatableComponent("fecore.shapetool.size.y.set", sizeY), Util.NIL_UUID);
			player.sendMessage(new TranslatableComponent("fecore.shapetool.size.z.selected"), Util.NIL_UUID);
			return 3;
		}
		else
		{
			sizeZ = blockPos.getZ() + .5 - z;
			player.sendMessage(new TranslatableComponent("fecore.shapetool.size.z.set", sizeZ), Util.NIL_UUID);
			player.sendMessage(new TranslatableComponent("fecore.shapetool.position.selected"), Util.NIL_UUID);
			return 0;
		}
	}

	@Override
	public int removePosition(Player player, int num)
	{
		if (num == 0)
		{
			player.sendMessage(new TranslatableComponent("fecore.shapetool.size.x.selected"), Util.NIL_UUID);
			return 1;
		}
		else if (num == 1)
		{
			player.sendMessage(new TranslatableComponent("fecore.shapetool.size.y.selected"), Util.NIL_UUID);
			return 2;
		}
		else if (num == 2)
		{
			player.sendMessage(new TranslatableComponent("fecore.shapetool.size.z.selected"), Util.NIL_UUID);
			return 3;
		}
		else
		{
			player.sendMessage(new TranslatableComponent("fecore.shapetool.position.selected"), Util.NIL_UUID);
			return 0;
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
	{
		tooltip.add(new TranslatableComponent(isRelative ? "fecore.shapetool.tooltip.relative" : "fecore.shapetool.tooltip.absolute"));
		tooltip.add(new TranslatableComponent("fecore.shapetool.tooltip.position", new Vec3(x, y, z)));
		tooltip.add(new TranslatableComponent("fecore.shapetool.tooltip.size", new Vec3(sizeX, sizeY, sizeZ)));
	}

	@Override
	public void renderIntoWorld(PoseStack pose, Vec3 pos, float partialTick)
	{
		float x1, y1, z1;
		if (this.isRelative)
		{
			x1 = (float) (x + pos.x);
			y1 = (float) (y + pos.y);
			z1 = (float) (z + pos.z);
		}
		else
		{
			x1 = (float) x;
			y1 = (float) y;
			z1 = (float) z;
		}
		float x2 = (float) (x1 + sizeX);
		float y2 = (float) (y1 + sizeY);
		float z2 = (float) (z1 + sizeZ);
		IRenderableBoundingShape.renderCube(pose.last().pose(), pose.last().normal(), x1, y1, z1, x2, y2, z2, .5f, .5f, 1f, .5f);
	}
}