package com.firemerald.fecore.selectionshapes;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;

import com.firemerald.fecore.betterscreens.components.Button;
import com.firemerald.fecore.betterscreens.components.IComponent;
import com.firemerald.fecore.betterscreens.components.decoration.FloatingText;
import com.firemerald.fecore.betterscreens.components.text.FloatField;
import com.firemerald.fecore.util.Constants;
import com.firemerald.fecore.util.Translator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.nbt.*;
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

public class BoundingShapePolygon extends BoundingShapeConfigurable implements IRenderableBoundingShape
{
	public boolean isRelative = true;
	private float y1, y2;
	public Vector3f[] positions = new Vector3f[0];

	@Override
	public String getUnlocalizedName()
	{
		return "fecore.shape.polygon";
	}

	@Override
	public boolean isWithin(@Nullable Entity entity, double posX, double posY, double posZ, double testerX, double testerY, double testerZ)
	{
		double x, y, z;
		if (isRelative)
		{
			x = posX - testerX;
			y = posY - testerY;
			z = posZ - testerZ;
		}
		else
		{
			x = posX;
			y = posY;
			z = posZ;
		}
		float y1, y2;
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
		if (y < y1 || y >= y2) return false; //out of Y bounds
		for (Vector3f pos : positions) if (pos.x() == x && pos.z() == z) return true; //point equivalence
		if (positions.length <= 1) return false; //must have at least two positions for non-point-equivalence test
		Vector3f lastPos = positions[positions.length - 1];
		double totalAng = 0, prevX = lastPos.x() - x, prevY = lastPos.z() - z, curX, curY;
		for (Vector3f pos : positions)
		{
			curX = pos.x() - x;
			curY = pos.z() - z;
			totalAng += Math.atan2((prevX * curY) - (curX * prevY), (curX * prevX) + (curY * prevY)); //quick and easy angle between points method
		}
		return totalAng == Constants.TAU || totalAng == -Constants.TAU;
	}

	@Override
	public void saveToNBT(CompoundTag tag)
	{
		super.saveToNBT(tag);
		tag.putBoolean("isRelative", isRelative);
		ListTag points = new ListTag();
		for (Vector3f pos : positions)
		{
			ListTag point = new ListTag();
			point.add(FloatTag.valueOf(pos.x()));
			point.add(FloatTag.valueOf(pos.y()));
			point.add(FloatTag.valueOf(pos.z()));
			points.add(point);
		}
		tag.put("points", points);
	}
	
	public void updateCachedHeight()
	{
		if (positions.length > 0)
		{
			y1 = y2 = positions[0].y();
			for (int i = 1; i < positions.length; ++i)
			{
				float y = positions[i].y();
				if (y < y1) y1 = y;
				if (y > y2) y2 = y;
			}
		}
	}

	@Override
	public void loadFromNBT(CompoundTag tag)
	{
		super.loadFromNBT(tag);
		isRelative = tag.getBoolean("isRelative");
		if (tag.contains("points", 9))
		{
			ListTag points = (ListTag) tag.get("points");
			if (points.getElementType() == 9) //list of lists
			{
				boolean isValid = true;
				for (int i = 0; i < points.size(); ++i)
				{
					ListTag point = points.getList(i);
					if (!(point.size() == 3 && point.get(0) instanceof NumericTag && point.get(1) instanceof NumericTag && point.get(2) instanceof NumericTag))
					{
						isValid = false;
						break;
					}
				}
				if (isValid)
				{
					positions = new Vector3f[points.size()];
					for (int i = 0; i < points.size(); ++i)
					{
						ListTag point = points.getList(i);
						float y;
						positions[i] = new Vector3f(((NumericTag) point.get(0)).getAsFloat(), y = ((NumericTag) point.get(1)).getAsFloat(), ((NumericTag) point.get(2)).getAsFloat());
						if (i == 0) y1 = y2 = y;
						else
						{
							if (y < y1) y1 = y;
							if (y > y2) y2 = y;
						}
					}
				}
				else positions = new Vector3f[0];
			}
			else positions = new Vector3f[0];
		}
		else positions = new Vector3f[0];
	}

	@Override
	public void saveToBuffer(FriendlyByteBuf buf)
	{
		super.saveToBuffer(buf);
		buf.writeBoolean(isRelative);
		buf.writeVarInt(positions.length);
		for (Vector3f pos : positions)
		{
			buf.writeFloat(pos.x());
			buf.writeFloat(pos.y());
			buf.writeFloat(pos.z());
		}
	}

	@Override
	public void loadFromBuffer(FriendlyByteBuf buf)
	{
		super.loadFromBuffer(buf);
		isRelative = buf.readBoolean();
		int length = buf.readVarInt();
		positions = new Vector3f[length];
		for (int i = 0; i < length; ++i)
		{
			float y;
			positions[i] = new Vector3f(buf.readFloat(), y = buf.readFloat(), buf.readFloat());
			if (i == 0) y1 = y2 = y;
			else
			{
				if (y < y1) y1 = y;
				if (y > y2) y2 = y;
			}
		}
	}

	@Override
	public void addGuiElements(Vec3 pos, IShapeGui gui, Font font, Consumer<IComponent> addElement, int width)
	{
		int offX = (width - 200) >> 1;
		addElement.accept(new FloatingText(offX, 0, offX + 100, 20, font, Translator.translate("fecore.shapesgui.position")));
		addElement.accept(new Button(offX + 100, 0, 100, 20, new TranslatableComponent(isRelative ? "fecore.shapesgui.operator.relative" : "fecore.shapesgui.operator.absolute"), null).setAction(button -> () -> {
			if (isRelative)
			{
				isRelative = false;
				y1 += pos.y;
				y2 += pos.y;
				for (int i = 0; i < positions.length; ++i)
				{
					Vector3f posi = positions[i];
					positions[i] = new Vector3f((float) (posi.x() + pos.x), (float) (posi.y() + pos.y), (float) (posi.z() + pos.z));
				}
				gui.updateGuiButtonsList();
				button.displayString = new TranslatableComponent("fecore.shapesgui.operator.absolute");
			}
			else
			{
				isRelative = true;
				y1 -= pos.y;
				y2 -= pos.y;
				for (int i = 0; i < positions.length; ++i)
				{
					Vector3f posi = positions[i];
					positions[i] = new Vector3f((float) (posi.x() - pos.x), (float) (posi.y() - pos.y), (float) (posi.z() - pos.z));
				}
				gui.updateGuiButtonsList();
				button.displayString = new TranslatableComponent("fecore.shapesgui.operator.relative");
			}
		}));
		int y = 20;
		for (int i = 0; i < positions.length; ++i)
		{
			final int index = i;
			Vector3f posi = positions[i];
			addElement.accept(new FloatingText(offX, y, offX + 100, y + 20, font, Translator.format("fecore.shapesgui.point.index", index)));
			addElement.accept(new Button(offX + 100, y, 100, 20, new TranslatableComponent("fecore.shapesgui.point.remove"), null).setAction(button -> () -> {
				Vector3f[] old = BoundingShapePolygon.this.positions;
				BoundingShapePolygon.this.positions = new Vector3f[old.length - 1];
				System.arraycopy(old, 0, BoundingShapePolygon.this.positions, 0, index);
				System.arraycopy(old, index + 1, BoundingShapePolygon.this.positions, index, old.length - index - 1);
				this.updateCachedHeight();
				gui.updateGuiButtonsList();
			}));
			y += 20;
			addElement.accept(new FloatField(font, offX, y, 67, 20, posi.x(), new TranslatableComponent("fecore.shapesgui.position.x"), (FloatConsumer) (val -> BoundingShapePolygon.this.positions[index].setX(val))));
			addElement.accept(new FloatField(font, offX + 67, y, 66, 20, posi.y(), new TranslatableComponent("fecore.shapesgui.position.y"), (FloatConsumer) (val -> {
				BoundingShapePolygon.this.positions[index].setY(val);
				this.updateCachedHeight();
			})));
			addElement.accept(new FloatField(font, offX + 133, y, 67, 20, posi.z(), new TranslatableComponent("fecore.shapesgui.position.z"), (FloatConsumer) (val -> BoundingShapePolygon.this.positions[index].setZ(val))));
			y += 20;
			addElement.accept(new Button(offX, y, 100, 20, new TranslatableComponent("fecore.shapesgui.point.insert.before"), null).setAction(button -> () -> {
				Vector3f[] old = BoundingShapePolygon.this.positions;
				BoundingShapePolygon.this.positions = new Vector3f[old.length + 1];
				System.arraycopy(old, 0, BoundingShapePolygon.this.positions, 0, index);
				BoundingShapePolygon.this.positions[index] = old[index].copy();
				System.arraycopy(old, index, BoundingShapePolygon.this.positions, index + 1, old.length - index);
				gui.updateGuiButtonsList();
			}));
			addElement.accept(new Button(offX + 100, y, 100, 20, new TranslatableComponent("fecore.shapesgui.point.insert.after"), null).setAction(button -> () -> {
				Vector3f[] old = BoundingShapePolygon.this.positions;
				BoundingShapePolygon.this.positions = new Vector3f[old.length + 1];
				System.arraycopy(old, 0, BoundingShapePolygon.this.positions, 0, index + 1);
				BoundingShapePolygon.this.positions[index + 1] = old[index].copy();
				System.arraycopy(old, index + 1, BoundingShapePolygon.this.positions, index + 2, old.length - index - 1);
				gui.updateGuiButtonsList();
			}));
			y += 20;
		}
		addElement.accept(new Button(offX + 50, y, 100, 20, new TranslatableComponent("fecore.shapesgui.point.add"), null).setAction(button -> () -> {
			Vector3f[] old = BoundingShapePolygon.this.positions;
			BoundingShapePolygon.this.positions = new Vector3f[old.length + 1];
			System.arraycopy(old, 0, BoundingShapePolygon.this.positions, 0, old.length);
			BoundingShapePolygon.this.positions[old.length] = old.length > 0 ? old[old.length - 1].copy() : new Vector3f();
			gui.updateGuiButtonsList();
		}));
	}

	@Override
	public int addPosition(Player player, BlockPos blockPos, int num)
	{
		if (isRelative)
		{
			isRelative = false;
			Position pos = player.position();
			y1 += pos.y();
			y2 += pos.y();
			for (int i = 0; i < positions.length; ++i)
			{
				Vector3f posi = positions[i];
				positions[i] = new Vector3f((float) (posi.x() + pos.x()), (float) (posi.y() + pos.y()), (float) (posi.z() + pos.z()));
			}
		}
		float x = blockPos.getX() + .5f;
		float y = blockPos.getY() + .5f;
		float z = blockPos.getZ() + .5f;
		if (num == 0) //initial set
		{
			y1 = y2 = y;
		}
		else
		{
			if (y < y1) y1 = y;
			if (y > y2) y2 = y;
		}
		Vector3f v;
		positions = ArrayUtils.add(positions, v = new Vector3f(x, y, z));
		player.sendMessage(new TranslatableComponent("fecore.shapetool.position.added", v), Util.NIL_UUID);
		return positions.length;
	}

	@Override
	public int removePosition(Player player, int num)
	{
		if (num == 0) return 0;
		Vector3f[] old = positions;
		positions = new Vector3f[old.length - 1];
		System.arraycopy(old, 0, positions, 0, positions.length);
		player.sendMessage(new TranslatableComponent("fecore.shapetool.position.removed", old[old.length - 1]), Util.NIL_UUID);
		this.updateCachedHeight();
		return positions.length;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
	{
		tooltip.add(new TranslatableComponent(isRelative ? "fecore.shapetool.tooltip.relative" : "fecore.shapetool.tooltip.absolute"));
		if (positions.length > 0)
		{
			tooltip.add(new TranslatableComponent("fecore.shapetool.tooltip.position.limits.y", y1, y2));
			tooltip.add(new TranslatableComponent("fecore.shapetool.tooltip.position.coordinates"));
			for (Vector3f pos : positions) tooltip.add(new TranslatableComponent("fecore.shapetool.tooltip.position.coordinate", pos.x(), pos.z()));
		}
	}

	@Override
	public void renderIntoWorld(PoseStack pose, Vec3 pos, float partialTick)
	{
		float x, y1, y2, z;
		if (this.isRelative)
		{
			x = (float) pos.x;
			y1 = (float) (this.y1 + pos.y);
			y2 = (float) (this.y2 + pos.y);
			z = (float) pos.z;
		}
		else
		{
			x = 0;
			y1 = (float) this.y1;
			y2 = (float) this.y2;
			z = 0;
		}
		IRenderableBoundingShape.renderPolygon(pose.last().pose(), pose.last().normal(), y1, y2, x, z, positions, .5f, .5f, 1f, .5f);
	}
}