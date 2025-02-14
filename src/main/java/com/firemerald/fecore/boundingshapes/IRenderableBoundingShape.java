package com.firemerald.fecore.boundingshapes;

import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.firemerald.fecore.util.Constants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface IRenderableBoundingShape
{
	@OnlyIn(Dist.CLIENT)
	public abstract void renderIntoWorld(PoseStack pose, Vec3 pos, DeltaTracker delta);

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
		RenderSystem.setShader(CoreShaders.POSITION_COLOR);
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		builder.addVertex(m, x1, y1, z1).setColor(r, g, b, a);
		builder.addVertex(m, x2, y1, z1).setColor(r, g, b, a);
		builder.addVertex(m, x2, y2, z1).setColor(r, g, b, a);
		builder.addVertex(m, x1, y2, z1).setColor(r, g, b, a);

		builder.addVertex(m, x1, y1, z2).setColor(r, g, b, a);
		builder.addVertex(m, x1, y2, z2).setColor(r, g, b, a);
		builder.addVertex(m, x2, y2, z2).setColor(r, g, b, a);
		builder.addVertex(m, x2, y1, z2).setColor(r, g, b, a);

		builder.addVertex(m, x1, y1, z1).setColor(r, g, b, a);
		builder.addVertex(m, x1, y2, z1).setColor(r, g, b, a);
		builder.addVertex(m, x1, y2, z2).setColor(r, g, b, a);
		builder.addVertex(m, x1, y1, z2).setColor(r, g, b, a);

		builder.addVertex(m, x2, y1, z1).setColor(r, g, b, a);
		builder.addVertex(m, x2, y1, z2).setColor(r, g, b, a);
		builder.addVertex(m, x2, y2, z2).setColor(r, g, b, a);
		builder.addVertex(m, x2, y2, z1).setColor(r, g, b, a);

		builder.addVertex(m, x1, y1, z1).setColor(r, g, b, a);
		builder.addVertex(m, x1, y1, z2).setColor(r, g, b, a);
		builder.addVertex(m, x2, y1, z2).setColor(r, g, b, a);
		builder.addVertex(m, x2, y1, z1).setColor(r, g, b, a);

		builder.addVertex(m, x1, y2, z1).setColor(r, g, b, a);
		builder.addVertex(m, x2, y2, z1).setColor(r, g, b, a);
		builder.addVertex(m, x2, y2, z2).setColor(r, g, b, a);
		builder.addVertex(m, x1, y2, z2).setColor(r, g, b, a);


		builder.addVertex(m, x1, y1, z1).setColor(r, g, b, a);
		builder.addVertex(m, x1, y2, z1).setColor(r, g, b, a);
		builder.addVertex(m, x2, y2, z1).setColor(r, g, b, a);
		builder.addVertex(m, x2, y1, z1).setColor(r, g, b, a);

		builder.addVertex(m, x1, y1, z2).setColor(r, g, b, a);
		builder.addVertex(m, x2, y1, z2).setColor(r, g, b, a);
		builder.addVertex(m, x2, y2, z2).setColor(r, g, b, a);
		builder.addVertex(m, x1, y2, z2).setColor(r, g, b, a);

		builder.addVertex(m, x1, y1, z1).setColor(r, g, b, a);
		builder.addVertex(m, x1, y1, z2).setColor(r, g, b, a);
		builder.addVertex(m, x1, y2, z2).setColor(r, g, b, a);
		builder.addVertex(m, x1, y2, z1).setColor(r, g, b, a);

		builder.addVertex(m, x2, y1, z1).setColor(r, g, b, a);
		builder.addVertex(m, x2, y2, z1).setColor(r, g, b, a);
		builder.addVertex(m, x2, y2, z2).setColor(r, g, b, a);
		builder.addVertex(m, x2, y1, z2).setColor(r, g, b, a);

		builder.addVertex(m, x1, y1, z1).setColor(r, g, b, a);
		builder.addVertex(m, x2, y1, z1).setColor(r, g, b, a);
		builder.addVertex(m, x2, y1, z2).setColor(r, g, b, a);
		builder.addVertex(m, x1, y1, z2).setColor(r, g, b, a);

		builder.addVertex(m, x1, y2, z1).setColor(r, g, b, a);
		builder.addVertex(m, x1, y2, z2).setColor(r, g, b, a);
		builder.addVertex(m, x2, y2, z2).setColor(r, g, b, a);
		builder.addVertex(m, x2, y2, z1).setColor(r, g, b, a);

        BufferUploader.drawWithShader(builder.buildOrThrow());
	}

	@OnlyIn(Dist.CLIENT)
	public static VertexConsumer addVertex(VertexConsumer builder, Matrix4f m, double x, double y, double z) {
		return builder.addVertex(m, (float) x, (float) y, (float) z);
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
		RenderSystem.setShader(CoreShaders.POSITION_COLOR);
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		addVertex(builder, m, x, y, z).setColor(r, g, b, a);
		for (int i = Constants.CIRCLE_MESH_CACHE.length - 1; i >= 0; --i)
		{
			Vec2 v = Constants.CIRCLE_MESH_CACHE[i];
			double curX = x + rad * v.x;
			double curZ = z + rad * v.y;
			addVertex(builder, m, curX, y, curZ).setColor(r, g, b, a);
		}
		BufferUploader.drawWithShader(builder.buildOrThrow());
		builder = tesselator.begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
		for (Vec2 v : Constants.CIRCLE_MESH_CACHE) {
			double curX = x + rad * v.x;
			double curZ = z + rad * v.y;
			addVertex(builder, m, curX, y2, curZ).setColor(r, g, b, a);
			addVertex(builder, m, curX, y, curZ).setColor(r, g, b, a);
		}
		BufferUploader.drawWithShader(builder.buildOrThrow());
		builder = tesselator.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		addVertex(builder, m, x, y2, z).setColor(r, g, b, a);
		for (Vec2 v : Constants.CIRCLE_MESH_CACHE) {
			double curX = x + rad * v.x;
			double curZ = z + rad * v.y;
			addVertex(builder, m, curX, y2, curZ).setColor(r, g, b, a);
		}
		BufferUploader.drawWithShader(builder.buildOrThrow());

		builder = tesselator.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		addVertex(builder, m, x, y, z).setColor(r, g, b, a);
		for (Vec2 v : Constants.CIRCLE_MESH_CACHE) {
			double curX = x + rad * v.x;
			double curZ = z + rad * v.y;
			addVertex(builder, m, curX, y, curZ).setColor(r, g, b, a);
		}
		BufferUploader.drawWithShader(builder.buildOrThrow());
		builder = tesselator.begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
		for (Vec2 v : Constants.CIRCLE_MESH_CACHE) {
			double curX = x + rad * v.x;
			double curZ = z + rad * v.y;
			addVertex(builder, m, curX, y, curZ).setColor(r, g, b, a);
			addVertex(builder, m, curX, y2, curZ).setColor(r, g, b, a);
		}
		BufferUploader.drawWithShader(builder.buildOrThrow());
		builder = tesselator.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		addVertex(builder, m, x, y2, z).setColor(r, g, b, a);
		for (int i = Constants.CIRCLE_MESH_CACHE.length - 1; i >= 0; --i)
		{
			Vec2 v = Constants.CIRCLE_MESH_CACHE[i];
			double curX = x + rad * v.x;
			double curZ = z + rad * v.y;
			addVertex(builder, m, curX, y2, curZ).setColor(r, g, b, a);
		}
		BufferUploader.drawWithShader(builder.buildOrThrow());
	}

	@OnlyIn(Dist.CLIENT)
	public static void renderSphere(Matrix4f m, double x, double y, double z, double rad, float r, float g, float b, float a)
	{
		RenderSystem.setShader(CoreShaders.POSITION_COLOR);
		Tesselator tesselator = Tesselator.getInstance();
		Vector3f[] cache1 = Constants.SPHERE_MESH_CACHE[0];
		BufferBuilder builder = tesselator.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		addVertex(builder, m, x, y - rad, z).setColor(r, g, b, a);
		for (int i = cache1.length - 1; i >= 0; --i)
		{
			Vector3f v = cache1[i];
			double curX = x + rad * v.x();
			double curY = y + rad * v.y();
			double curZ = z + rad * v.z();
			addVertex(builder, m, curX, curY, curZ).setColor(r, g, b, a);
		}
		BufferUploader.drawWithShader(builder.buildOrThrow());
		for (int j = 1; j < Constants.SPHERE_MESH_CACHE.length; j++)
		{
			Vector3f[] cache2 = Constants.SPHERE_MESH_CACHE[j];
			builder = tesselator.begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
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
				addVertex(builder, m, curX2, curY2, curZ2).setColor(r, g, b, a);
				addVertex(builder, m, curX1, curY1, curZ1).setColor(r, g, b, a);
			}
			BufferUploader.drawWithShader(builder.buildOrThrow());
			cache1 = cache2;
		}
		builder = tesselator.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		addVertex(builder, m, x, y + rad, z).setColor(r, g, b, a);
		for (Vector3f v : cache1) {
			double curX = x + rad * v.x();
			double curY = y + rad * v.y();
			double curZ = z + rad * v.z();
			addVertex(builder, m, curX, curY, curZ).setColor(r, g, b, a);
		}
		BufferUploader.drawWithShader(builder.buildOrThrow());


		cache1 = Constants.SPHERE_MESH_CACHE[0];
		builder = tesselator.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		addVertex(builder, m, x, y - rad, z).setColor(r, g, b, a);
		for (Vector3f v : cache1) {
			double curX = x + rad * v.x();
			double curY = y + rad * v.y();
			double curZ = z + rad * v.z();
			addVertex(builder, m, curX, curY, curZ).setColor(r, g, b, a);
		}
		BufferUploader.drawWithShader(builder.buildOrThrow());
		for (int j = 1; j < Constants.SPHERE_MESH_CACHE.length; j++)
		{
			Vector3f[] cache2 = Constants.SPHERE_MESH_CACHE[j];
			builder = tesselator.begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
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
				addVertex(builder, m, curX1, curY1, curZ1).setColor(r, g, b, a);
				addVertex(builder, m, curX2, curY2, curZ2).setColor(r, g, b, a);
			}
			BufferUploader.drawWithShader(builder.buildOrThrow());
			cache1 = cache2;
		}
		builder = tesselator.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		addVertex(builder, m, x, y + rad, z).setColor(r, g, b, a);
		for (int i = cache1.length - 1; i >= 0; --i)
		{
			Vector3f v = cache1[i];
			double curX = x + rad * v.x();
			double curY = y + rad * v.y();
			double curZ = z + rad * v.z();
			addVertex(builder, m, curX, curY, curZ).setColor(r, g, b, a);
		}
		BufferUploader.drawWithShader(builder.buildOrThrow());
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
		RenderSystem.setShader(CoreShaders.POSITION_COLOR);
		Tesselator tesselator = Tesselator.getInstance();

		Vector3d v;
		double curX, curZ;
		BufferBuilder builder;
		/*
		builder = tesselator.begin(Mode.LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
		for (int i = 0; i < points.length; ++i) {
			v = points[i];
			curX = x + v.x();
			curZ = z + v.z();
			addVertex(builder, m, curX, y1, curZ).setColor(r, g, b, 1);
		}
		{
			v = points[0];
			curX = x + v.x();
			curZ = z + v.z();
			addVertex(builder, m, curX, y1, curZ).setColor(r, g, b, 1);
		}
		BufferUploader.drawWithShader(builder.buildOrThrow());
		*/
		builder = tesselator.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		for (int i = points.length - 1; i >= 0; --i)
		{
			v = points[i];
			curX = x + v.x();
			curZ = z + v.z();
			addVertex(builder, m, curX, y1, curZ).setColor(r, g, b, a);
		}
		BufferUploader.drawWithShader(builder.buildOrThrow());

		builder = tesselator.begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
		v = points[points.length - 1];
		curX = x + v.x();
		curZ = z + v.z();
		addVertex(builder, m, curX, y2, curZ).setColor(r, g, b, a);
		addVertex(builder, m, curX, y1, curZ).setColor(r, g, b, a);
		for (Vector3d point : points) {
			v = point;
			curX = x + v.x();
			curZ = z + v.z();
			addVertex(builder, m, curX, y2, curZ).setColor(r, g, b, a);
			addVertex(builder, m, curX, y1, curZ).setColor(r, g, b, a);
		}
		BufferUploader.drawWithShader(builder.buildOrThrow());

		builder = tesselator.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		for (Vector3d point : points) {
			v = point;
			curX = x + v.x();
			curZ = z + v.z();
			addVertex(builder, m, curX, y2, curZ).setColor(r, g, b, a);
		}
		BufferUploader.drawWithShader(builder.buildOrThrow());


		builder = tesselator.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		for (Vector3d point : points) {
			v = point;
			curX = x + v.x();
			curZ = z + v.z();
			addVertex(builder, m, curX, y1, curZ).setColor(r, g, b, a);
		}
		BufferUploader.drawWithShader(builder.buildOrThrow());

		builder = tesselator.begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
		v = points[points.length - 1];
		curX = x + v.x();
		curZ = z + v.z();
		addVertex(builder, m, curX, y1, curZ).setColor(r, g, b, a);
		addVertex(builder, m, curX, y2, curZ).setColor(r, g, b, a);
		for (Vector3d point : points) {
			v = point;
			curX = x + v.x();
			curZ = z + v.z();
			addVertex(builder, m, curX, y1, curZ).setColor(r, g, b, a);
			addVertex(builder, m, curX, y2, curZ).setColor(r, g, b, a);
		}
		BufferUploader.drawWithShader(builder.buildOrThrow());

		builder = tesselator.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		for (int i = points.length - 1; i >= 0; --i)
		{
			v = points[i];
			curX = x + v.x();
			curZ = z + v.z();
			addVertex(builder, m, curX, y2, curZ).setColor(r, g, b, a);
		}
		BufferUploader.drawWithShader(builder.buildOrThrow());
	}
}
