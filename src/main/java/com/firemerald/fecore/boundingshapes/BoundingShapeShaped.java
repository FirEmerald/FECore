package com.firemerald.fecore.boundingshapes;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.DeltaTracker;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public abstract class BoundingShapeShaped extends BoundingShapeBounded
{
	public boolean isRelative;

	public BoundingShapeShaped(boolean isRelative) {
		this.isRelative = isRelative;
	}

	public BoundingShapeShaped() {
		this(true);
	}

	public AABB getLocalBounds() {
		return getBoundsOffset(0, 0, 0);
	}

	public abstract AABB getBoundsOffset(double x, double y, double z);

	@Override
	public AABB getBounds(double testerX, double testerY, double testerZ) {
		return isRelative ? getBoundsOffset(testerX, testerY, testerZ) : getLocalBounds();
	}

	//this will override IRenderableBoundingShape.renderIntoWorld(PoseStack, Vec3, float), and should be used by overriding renderIntoWorld(PoseStack, double, double, double, float) instead
	@OnlyIn(Dist.CLIENT)
	public void renderIntoWorld(PoseStack pose, Vec3 pos, DeltaTracker delta) {
		double x, y, z;
		if (this.isRelative)
		{
			x = pos.x;
			y = pos.y;
			z = pos.z;
		}
		else
		{
			x = 0;
			y = 0;
			z = 0;
		}
		renderIntoWorld(pose, x, y, z, delta);
	}

	@OnlyIn(Dist.CLIENT)
	public void renderIntoWorld(PoseStack pose, double x, double y, double z, DeltaTracker delta) {}

	public boolean toggleRelative(Vec3 testerPos) {
		if (this.isRelative) {
			offset(testerPos.x, testerPos.y, testerPos.z);
			this.isRelative = false;
			return false;
		} else {
			offset(-testerPos.x, -testerPos.y, -testerPos.z);
			this.isRelative = true;
			return true;
		}
	}

	public void setRelative(boolean isRelative, Vec3 testerPos) {
		if (this.isRelative) {
			if (!isRelative) {
				offset(testerPos.x, testerPos.y, testerPos.z);
				this.isRelative = false;
			}
		} else if (isRelative) {
			offset(-testerPos.x, -testerPos.y, -testerPos.z);
			this.isRelative = true;
		}
	}

	public abstract void offset(double x, double y, double z);

	@Override
	public abstract BoundingShapeShaped clone();

	@Override
	public BoundingShapeShaped asAbsolute(Vec3 pos) {
		if (isRelative) {
			BoundingShapeShaped clone = clone();
			clone.setRelative(false, pos);
			return clone;
		} else return this;
	}

	@Override
	public boolean isAbsolute() {
		return !isRelative;
	}
}