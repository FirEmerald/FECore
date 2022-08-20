package com.firemerald.fecore.selectionshapes;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

import javax.annotation.Nullable;

import com.firemerald.fecore.betterscreens.components.Button;
import com.firemerald.fecore.betterscreens.components.IComponent;
import com.firemerald.fecore.betterscreens.components.decoration.FloatingText;
import com.firemerald.fecore.betterscreens.components.text.DoubleField;
import com.firemerald.fecore.util.Vec3d;

import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BoundingShapeSphere extends BoundingShapeConfigurable
{
	public boolean isRelative = true;
	public double x = .5, y = .5, z = .5, r = 10;

	@Override
	public String getUnlocalizedName()
	{
		return "fecore.shape.sphere";
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
		double dx = posX - x;
		double dy = posY - y;
		double dz = posZ - z;
		return (dx * dx) + (dy * dy) + (dz * dz) <= r * r;
	}

	@Override
	public void saveToNBT(CompoundTag tag)
	{
		super.saveToNBT(tag);
		tag.putBoolean("isRelative", isRelative);
		tag.putDouble("x", x);
		tag.putDouble("y", y);
		tag.putDouble("z", z);
		tag.putDouble("r", r);
	}

	@Override
	public void loadFromNBT(CompoundTag tag)
	{
		super.loadFromNBT(tag);
		isRelative = tag.getBoolean("isRelative");
		if (tag.contains("x", 99)) x = tag.getDouble("x");
		else x = .5;
		if (tag.contains("y", 99)) y = tag.getDouble("y");
		else y = .5;
		if (tag.contains("z", 99)) z = tag.getDouble("z");
		else z = .5;
		if (tag.contains("r", 99)) r = tag.getDouble("r");
		else r = 10;
	}

	@Override
	public void saveToBuffer(FriendlyByteBuf buf)
	{
		super.saveToBuffer(buf);
		buf.writeBoolean(isRelative);
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeDouble(r);
	}

	@Override
	public void loadFromBuffer(FriendlyByteBuf buf)
	{
		super.loadFromBuffer(buf);
		isRelative = buf.readBoolean();
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		r = buf.readDouble();
	}

	@Override
	public void addGuiElements(Vec3d pos, IShapeGui gui, Font font, Consumer<IComponent> addElement, int width)
	{
		int offX = (width - 200) >> 1;
		final DoubleField
		posX = new DoubleField(font, offX, 20, 67, 20, x, new TranslatableComponent("fecore.shapesgui.position.x"), (DoubleConsumer) (val -> x = val)),
		posY = new DoubleField(font, offX + 67, 20, 66, 20, y, new TranslatableComponent("fecore.shapesgui.position.y"), (DoubleConsumer) (val -> y = val)),
		posZ = new DoubleField(font, offX + 133, 20, 67, 20, z, new TranslatableComponent("fecore.shapesgui.position.z"), (DoubleConsumer) (val -> z = val)),
		radius = new DoubleField(font, offX + 100, 40, 100, 20, r, new TranslatableComponent("fecore.shapesgui.radius"), (DoubleConsumer) (val -> r = val));
		addElement.accept(new Button(offX + 100, 0, 100, 20, new TranslatableComponent(isRelative ? "fecore.shapesgui.operator.relative" : "fecore.shapesgui.operator.absolute"), null).setAction(button -> () -> {
			if (isRelative)
			{
				isRelative = false;
				x += pos.x();
				y += pos.y();
				z += pos.z();
				posX.setDouble(x);
				posY.setDouble(y);
				posZ.setDouble(z);
				button.displayString = new TranslatableComponent("fecore.shapesgui.operator.absolute");
			}
			else
			{
				isRelative = true;
				x -= pos.x();
				y -= pos.y();
				z -= pos.z();
				posX.setDouble(x);
				posY.setDouble(y);
				posZ.setDouble(z);
				button.displayString = new TranslatableComponent("fecore.shapesgui.operator.relative");
			}
		}));
		addElement.accept(new FloatingText(offX, 0, offX + 100, 20, font, Language.getInstance().getOrDefault("fecore.shapesgui.position")));
		addElement.accept(posX);
		addElement.accept(posY);
		addElement.accept(posZ);
		addElement.accept(new FloatingText(offX, 40, offX + 100, 60, font, Language.getInstance().getOrDefault("fecore.shapesgui.radius")));
		addElement.accept(radius);
	}

	@Override
	public int addPosition(Player player, BlockPos blockPos, int num)
	{
		if (num == 0)
		{
			x = blockPos.getX() + .5;
			y = blockPos.getY() + .5;
			z = blockPos.getZ() + .5;
			player.sendMessage(new TranslatableComponent("fecore.shapetool.position.set", new Vec3d(blockPos.getX() + .5, blockPos.getY() + .5, blockPos.getZ() + .5).toString()), Util.NIL_UUID);
			player.sendMessage(new TranslatableComponent("fecore.shapetool.radius.selected"), Util.NIL_UUID);
			return 1;
		}
		else
		{
			double dx = blockPos.getX() + .5 - x;
			double dy = blockPos.getY() + .5 - y;
			double dz = blockPos.getZ() + .5 - z;
			r = Math.sqrt(dx * dx + dy * dy + dz * dz);
			player.sendMessage(new TranslatableComponent("fecore.shapetool.radius.set", r), Util.NIL_UUID);
			player.sendMessage(new TranslatableComponent("fecore.shapetool.position.selected"), Util.NIL_UUID);
			return 0;
		}
	}

	@Override
	public int removePosition(Player player, int num)
	{
		if (num == 0)
		{
			player.sendMessage(new TranslatableComponent("fecore.shapetool.radius.selected"), Util.NIL_UUID);
			return 1;
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
		tooltip.add(new TranslatableComponent("fecore.shapetool.tooltip.position", new Vec3d(x, y, z)));
		tooltip.add(new TranslatableComponent("fecore.shapetool.tooltip.radius", r));
	}
}