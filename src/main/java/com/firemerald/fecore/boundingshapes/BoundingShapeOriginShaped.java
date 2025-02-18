package com.firemerald.fecore.boundingshapes;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

import javax.annotation.Nullable;

import com.firemerald.fecore.client.gui.components.Button;
import com.firemerald.fecore.client.gui.components.IComponent;
import com.firemerald.fecore.client.gui.components.decoration.FloatingText;
import com.firemerald.fecore.client.gui.components.text.DoubleField;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class BoundingShapeOriginShaped extends BoundingShapeShaped
{
	public double x, y, z;

	public BoundingShapeOriginShaped(double x, double y, double z, boolean isRelative) {
		super(isRelative);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BoundingShapeOriginShaped(double x, double y, double z) {
		this(x, y, z, true);
	}

	public BoundingShapeOriginShaped(boolean isRelative) {
		this(0.5, 0.5, 0.5, isRelative);
	}

	public BoundingShapeOriginShaped() {
		this(true);
	}

	@Override
	public AABB getBoundsOffset(double offX, double offY, double offZ) {
		return getOffsetBounds(x + offX, y + offY, z + offZ);
	}

	public abstract AABB getOffsetBounds(double originX, double originY, double originZ);

	@Override
	public void addGuiElements(Vec3 pos, IShapeGui gui, Font font, Consumer<IComponent> addElement, int width)
	{
		int offX = (width - 200) >> 1;
		final DoubleField
		posX = new DoubleField(font, offX, 20, 67, 20, x, Component.translatable("fecore.shapesgui.position.x"), (DoubleConsumer) (val -> x = val)),
		posY = new DoubleField(font, offX + 67, 20, 66, 20, y, Component.translatable("fecore.shapesgui.position.y"), (DoubleConsumer) (val -> y = val)),
		posZ = new DoubleField(font, offX + 133, 20, 67, 20, z, Component.translatable("fecore.shapesgui.position.z"), (DoubleConsumer) (val -> z = val));
		addElement.accept(new FloatingText(offX, 0, offX + 100, 20, font, I18n.get("fecore.shapesgui.position")));
		addElement.accept(new Button(offX + 100, 0, 100, 20, Component.translatable(isRelative ? "fecore.shapesgui.operator.relative" : "fecore.shapesgui.operator.absolute"), null).setAction(button -> () -> {
			button.displayString = Component.translatable(toggleRelative(pos) ? "fecore.shapesgui.operator.relative" : "fecore.shapesgui.operator.absolute");
			posX.setDouble(x);
			posY.setDouble(y);
			posZ.setDouble(z);
		}));
		addElement.accept(posX);
		addElement.accept(posY);
		addElement.accept(posZ);
	}

	@Override
	public boolean isWithin(@Nullable Entity entity, double posX, double posY, double posZ, double testerX, double testerY, double testerZ)
	{
		double dX, dY, dZ;
		if (isRelative)
		{
			dX = posX - (x + testerX);
			dY = posY - (y + testerY);
			dZ = posZ - (z + testerZ);
		}
		else
		{
			dX = posX - x;
			dY = posY - y;
			dZ = posZ - z;
		}
		return isWithinOffset(entity, dX, dY, dZ);
	}

	public abstract boolean isWithinOffset(@Nullable Entity entity, double dX, double dY, double dZ);

	//this can be used in IConfigurableBoundingShape.addPosition(Player, BlockPos, int)
	public void onAddPosition(Player player) {
		this.setRelative(false, player.position());
	}

	//this will override IRenderableBoundingShape.renderIntoWorld(PoseStack, Vec3, float), and should be used by overriding renderIntoWorld(PoseStack, double, double, double, float) instead
	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderIntoWorld(PoseStack pose, Vec3 pos, float partialTick) {
		double x, y, z;
		if (this.isRelative)
		{
			x = this.x + pos.x;
			y = this.y + pos.y;
			z = this.z + pos.z;
		}
		else
		{
			x = this.x;
			y = this.y;
			z = this.z;
		}
		renderIntoWorld(pose, x, y, z, partialTick);
	}

	@Override
	public void offset(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}
}