package com.firemerald.fecore.boundingshapes;

import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.firemerald.fecore.util.Constants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IRenderableBoundingShape
{
	@OnlyIn(Dist.CLIENT)
	public abstract void renderIntoWorld(PoseStack pose, Vec3 pos, float partialTick);

	@OnlyIn(Dist.CLIENT)
	public static void renderCube(Matrix4f m, double x1d, double y1d, double z1d, double x2d, double y2d, double z2d, float r, float g, float b, float a)
	{
		float x1, y1, z1, x2, y2, z2;
		if (x1d > x2d) {
			x1 = (float) x2d;
			x2 = (float) x1d;
		} else {
			x1 = (float) x1d;
			x2 = (float) x2d;
		}
		if (y1d > y2d) {
			y1 = (float) y2d;
			y2 = (float) y1d;
		} else {
			y1 = (float) y1d;
			y2 = (float) y2d;
		}
		if (z1d > z2d) {
			z1 = (float) z2d;
			z2 = (float) z1d;
		} else {
			z1 = (float) z1d;
			z2 = (float) z2d;
		}
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.getBuilder();
		builder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		builder.vertex(m, x1, y1, z1).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y1, z1).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y2, z1).color(r, g, b, a).endVertex();
		builder.vertex(m, x1, y2, z1).color(r, g, b, a).endVertex();

		builder.vertex(m, x1, y1, z2).color(r, g, b, a).endVertex();
		builder.vertex(m, x1, y2, z2).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y2, z2).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y1, z2).color(r, g, b, a).endVertex();

		builder.vertex(m, x1, y1, z1).color(r, g, b, a).endVertex();
		builder.vertex(m, x1, y2, z1).color(r, g, b, a).endVertex();
		builder.vertex(m, x1, y2, z2).color(r, g, b, a).endVertex();
		builder.vertex(m, x1, y1, z2).color(r, g, b, a).endVertex();

		builder.vertex(m, x2, y1, z1).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y1, z2).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y2, z2).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y2, z1).color(r, g, b, a).endVertex();

		builder.vertex(m, x1, y1, z1).color(r, g, b, a).endVertex();
		builder.vertex(m, x1, y1, z2).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y1, z2).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y1, z1).color(r, g, b, a).endVertex();

		builder.vertex(m, x1, y2, z1).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y2, z1).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y2, z2).color(r, g, b, a).endVertex();
		builder.vertex(m, x1, y2, z2).color(r, g, b, a).endVertex();


		builder.vertex(m, x1, y1, z1).color(r, g, b, a).endVertex();
		builder.vertex(m, x1, y2, z1).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y2, z1).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y1, z1).color(r, g, b, a).endVertex();

		builder.vertex(m, x1, y1, z2).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y1, z2).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y2, z2).color(r, g, b, a).endVertex();
		builder.vertex(m, x1, y2, z2).color(r, g, b, a).endVertex();

		builder.vertex(m, x1, y1, z1).color(r, g, b, a).endVertex();
		builder.vertex(m, x1, y1, z2).color(r, g, b, a).endVertex();
		builder.vertex(m, x1, y2, z2).color(r, g, b, a).endVertex();
		builder.vertex(m, x1, y2, z1).color(r, g, b, a).endVertex();

		builder.vertex(m, x2, y1, z1).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y2, z1).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y2, z2).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y1, z2).color(r, g, b, a).endVertex();

		builder.vertex(m, x1, y1, z1).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y1, z1).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y1, z2).color(r, g, b, a).endVertex();
		builder.vertex(m, x1, y1, z2).color(r, g, b, a).endVertex();

		builder.vertex(m, x1, y2, z1).color(r, g, b, a).endVertex();
		builder.vertex(m, x1, y2, z2).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y2, z2).color(r, g, b, a).endVertex();
		builder.vertex(m, x2, y2, z1).color(r, g, b, a).endVertex();

        tesselator.end();
	}

	@OnlyIn(Dist.CLIENT)
	public static VertexConsumer vertex(VertexConsumer builder, Matrix4f m, double x, double y, double z) {
		return builder.vertex(m, (float) x, (float) y, (float) z);
	}

	@OnlyIn(Dist.CLIENT)
	public static void renderCylinder(Matrix4f m, double x, double y, double z, double rad, double h, float r, float g, float b, float a)
	{
		double y2;
		if (h < 0)
		{
			y2 = y;
			y += h;
			h = -h;
		}
		else y2 = y + h;
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.getBuilder();
		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		vertex(builder, m, x, y, z).color(r, g, b, a).endVertex();
		for (int i = Constants.CIRCLE_MESH_CACHE.length - 1; i >= 0; --i)
		{
			Vec2 v = Constants.CIRCLE_MESH_CACHE[i];
			double curX = x + rad * v.x;
			double curZ = z + rad * v.y;
			vertex(builder, m, curX, y, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();
		builder.begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
		for (Vec2 v : Constants.CIRCLE_MESH_CACHE) {
			double curX = x + rad * v.x;
			double curZ = z + rad * v.y;
			vertex(builder, m, curX, y2, curZ).color(r, g, b, a).endVertex();
			vertex(builder, m, curX, y, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();
		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		vertex(builder, m, x, y2, z).color(r, g, b, a).endVertex();
		for (Vec2 v : Constants.CIRCLE_MESH_CACHE) {
			double curX = x + rad * v.x;
			double curZ = z + rad * v.y;
			vertex(builder, m, curX, y2, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();

		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		vertex(builder, m, x, y, z).color(r, g, b, a).endVertex();
		for (Vec2 v : Constants.CIRCLE_MESH_CACHE) {
			double curX = x + rad * v.x;
			double curZ = z + rad * v.y;
			vertex(builder, m, curX, y, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();
		builder.begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
		for (Vec2 v : Constants.CIRCLE_MESH_CACHE) {
			double curX = x + rad * v.x;
			double curZ = z + rad * v.y;
			vertex(builder, m, curX, y, curZ).color(r, g, b, a).endVertex();
			vertex(builder, m, curX, y2, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();
		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		vertex(builder, m, x, y2, z).color(r, g, b, a).endVertex();
		for (int i = Constants.CIRCLE_MESH_CACHE.length - 1; i >= 0; --i)
		{
			Vec2 v = Constants.CIRCLE_MESH_CACHE[i];
			double curX = x + rad * v.x;
			double curZ = z + rad * v.y;
			vertex(builder, m, curX, y2, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();
	}

	@OnlyIn(Dist.CLIENT)
	public static void renderSphere(Matrix4f m, double x, double y, double z, double rad, float r, float g, float b, float a)
	{
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.getBuilder();
		Vector3f[] cache1 = Constants.SPHERE_MESH_CACHE[0];
		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		vertex(builder, m, x, y - rad, z).color(r, g, b, a).endVertex();
		for (int i = cache1.length - 1; i >= 0; --i)
		{
			Vector3f v = cache1[i];
			double curX = x + rad * v.x();
			double curY = y + rad * v.y();
			double curZ = z + rad * v.z();
			vertex(builder, m, curX, curY, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();
		for (int j = 1; j < Constants.SPHERE_MESH_CACHE.length; j++)
		{
			Vector3f[] cache2 = Constants.SPHERE_MESH_CACHE[j];
			builder.begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
			for (int i = 0; i < cache1.length; ++i)
			{
				Vector3f v1 = cache1[i];
				double curX1 = x + rad * v1.x();
				double curY1 = y + rad * v1.y();
				double curZ1 = z + rad * v1.z();
				Vector3f v2 = cache2[i];
				double curX2 = x + rad * v2.x();
				double curY2 = y + rad * v2.y();
				double curZ2 = z + rad * v2.z();
				vertex(builder, m, curX2, curY2, curZ2).color(r, g, b, a).endVertex();
				vertex(builder, m, curX1, curY1, curZ1).color(r, g, b, a).endVertex();
			}
			tesselator.end();
			cache1 = cache2;
		}
		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		vertex(builder, m, x, y + rad, z).color(r, g, b, a).endVertex();
		for (Vector3f v : cache1) {
			double curX = x + rad * v.x();
			double curY = y + rad * v.y();
			double curZ = z + rad * v.z();
			vertex(builder, m, curX, curY, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();


		cache1 = Constants.SPHERE_MESH_CACHE[0];
		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		vertex(builder, m, x, y - rad, z).color(r, g, b, a).endVertex();
		for (Vector3f v : cache1) {
			double curX = x + rad * v.x();
			double curY = y + rad * v.y();
			double curZ = z + rad * v.z();
			vertex(builder, m, curX, curY, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();
		for (int j = 1; j < Constants.SPHERE_MESH_CACHE.length; j++)
		{
			Vector3f[] cache2 = Constants.SPHERE_MESH_CACHE[j];
			builder.begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
			for (int i = 0; i < cache1.length; ++i)
			{
				Vector3f v1 = cache1[i];
				double curX1 = x + rad * v1.x();
				double curY1 = y + rad * v1.y();
				double curZ1 = z + rad * v1.z();
				Vector3f v2 = cache2[i];
				double curX2 = x + rad * v2.x();
				double curY2 = y + rad * v2.y();
				double curZ2 = z + rad * v2.z();
				vertex(builder, m, curX1, curY1, curZ1).color(r, g, b, a).endVertex();
				vertex(builder, m, curX2, curY2, curZ2).color(r, g, b, a).endVertex();
			}
			tesselator.end();
			cache1 = cache2;
		}
		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		vertex(builder, m, x, y + rad, z).color(r, g, b, a).endVertex();
		for (int i = cache1.length - 1; i >= 0; --i)
		{
			Vector3f v = cache1[i];
			double curX = x + rad * v.x();
			double curY = y + rad * v.y();
			double curZ = z + rad * v.z();
			vertex(builder, m, curX, curY, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();
	}

	@OnlyIn(Dist.CLIENT)
	public static void renderPolygon(Matrix4f m, double y1, double y2, double x, double z, Vector3d[] points, float r, float g, float b, float a)
	{
		if (points.length < 2) return;
		if (y1 > y2)
		{
			double t = y1;
			y1 = y2;
			y2 = t;
		}
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.getBuilder();

		Vector3d v;
		double curX, curZ;
		/*
		builder.begin(Mode.LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
		for (int i = 0; i < points.length; ++i) {
			v = points[i];
			curX = x + v.x();
			curZ = z + v.z();
			vertex(builder, m, curX, y1, curZ).setColor(r, g, b, 1);
		}
		{
			v = points[0];
			curX = x + v.x();
			curZ = z + v.z();
			vertex(builder, m, curX, y1, curZ).setColor(r, g, b, 1);
		}
		tesselator.end();
		*/
		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		for (int i = points.length - 1; i >= 0; --i)
		{
			v = points[i];
			curX = x + v.x();
			curZ = z + v.z();
			vertex(builder, m, curX, y1, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();

		builder.begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
		v = points[points.length - 1];
		curX = x + v.x();
		curZ = z + v.z();
		vertex(builder, m, curX, y2, curZ).color(r, g, b, a).endVertex();
		vertex(builder, m, curX, y1, curZ).color(r, g, b, a).endVertex();
		for (Vector3d point : points) {
			v = point;
			curX = x + v.x();
			curZ = z + v.z();
			vertex(builder, m, curX, y2, curZ).color(r, g, b, a).endVertex();
			vertex(builder, m, curX, y1, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();

		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		for (Vector3d point : points) {
			v = point;
			curX = x + v.x();
			curZ = z + v.z();
			vertex(builder, m, curX, y2, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();


		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		for (Vector3d point : points) {
			v = point;
			curX = x + v.x();
			curZ = z + v.z();
			vertex(builder, m, curX, y1, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();

		builder.begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
		v = points[points.length - 1];
		curX = x + v.x();
		curZ = z + v.z();
		vertex(builder, m, curX, y1, curZ).color(r, g, b, a).endVertex();
		vertex(builder, m, curX, y2, curZ).color(r, g, b, a).endVertex();
		for (Vector3d point : points) {
			v = point;
			curX = x + v.x();
			curZ = z + v.z();
			vertex(builder, m, curX, y1, curZ).color(r, g, b, a).endVertex();
			vertex(builder, m, curX, y2, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();

		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		for (int i = points.length - 1; i >= 0; --i)
		{
			v = points[i];
			curX = x + v.x();
			curZ = z + v.z();
			vertex(builder, m, curX, y2, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();
	}
}
