package com.firemerald.fecore.boundingshapes;

import com.firemerald.fecore.util.Constants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IRenderableBoundingShape
{
	@OnlyIn(Dist.CLIENT)
	public abstract void renderIntoWorld(PoseStack pose, Vec3 pos, float partialTick);

	public static void renderCube(Matrix4f m, Matrix3f n, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a)
	{
		if (x1 > x2)
		{
			float t = x1;
			x1 = x2;
			x2 = t;
		}
		if (y1 > y2)
		{
			float t = y1;
			y1 = y2;
			y2 = t;
		}
		if (z1 > z2)
		{
			float t = z1;
			z1 = z2;
			z2 = t;
		}
		//System.out.println(x1 + ", " + y1 + ", " + z1 + " -> " + x2 + ", " + y2 + ", " + z2);
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.getBuilder();
		RenderSystem.disableTexture();
        /*
		RenderSystem.lineWidth(4.0F);
        RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
		builder.begin(Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
		r = g = b = 0;
		a = 1;
		builder.vertex(m, x1, y1, z1).color(r, g, b, a).normal(n, 0, 0, -1).endVertex();
		builder.vertex(m, x2, y1, z1).color(r, g, b, a).normal(n, 0, 0, -1).endVertex();
		builder.vertex(m, x2, y1, z1).color(r, g, b, a).normal(n, 0, 0, -1).endVertex();
		builder.vertex(m, x2, y2, z1).color(r, g, b, a).normal(n, 0, 0, -1).endVertex();
		builder.vertex(m, x2, y2, z1).color(r, g, b, a).normal(n, 0, 0, -1).endVertex();
		builder.vertex(m, x1, y2, z1).color(r, g, b, a).normal(n, 0, 0, -1).endVertex();
		builder.vertex(m, x1, y2, z1).color(r, g, b, a).normal(n, 0, 0, -1).endVertex();
		builder.vertex(m, x1, y1, z1).color(r, g, b, a).normal(n, 0, 0, -1).endVertex();


		builder.vertex(m, x1, y1, z2).color(r, g, b, a).normal(n, 0, 0, 1).endVertex();
		builder.vertex(m, x2, y1, z2).color(r, g, b, a).normal(n, 0, 0, 1).endVertex();
		builder.vertex(m, x2, y1, z2).color(r, g, b, a).normal(n, 0, 0, 1).endVertex();
		builder.vertex(m, x2, y2, z2).color(r, g, b, a).normal(n, 0, 0, 1).endVertex();
		builder.vertex(m, x2, y2, z2).color(r, g, b, a).normal(n, 0, 0, 1).endVertex();
		builder.vertex(m, x1, y2, z2).color(r, g, b, a).normal(n, 0, 0, 1).endVertex();
		builder.vertex(m, x1, y2, z2).color(r, g, b, a).normal(n, 0, 0, 1).endVertex();
		builder.vertex(m, x1, y1, z2).color(r, g, b, a).normal(n, 0, 0, 1).endVertex();

		tesselator.end();
		*/


		RenderSystem.setShader(GameRenderer::getPositionColorShader);
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

	public static void renderCylinder(Matrix4f m, Matrix3f n, float x, float y, float z, float rad, float h, float r, float g, float b, float a)
	{
		float y2;
		if (h < 0)
		{
			y2 = y;
			y += h;
			h = -h;
		}
		else y2 = y + h;
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.getBuilder();
		RenderSystem.disableTexture();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);

		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		builder.vertex(m, x, y, z).color(r, g, b, a).endVertex();
		for (int i = Constants.CIRCLE_MESH_CACHE.length - 1; i >= 0; --i)
		{
			Vec2 v = Constants.CIRCLE_MESH_CACHE[i];
			float curX = x + rad * v.x;
			float curZ = z + rad * v.y;
			builder.vertex(m, curX, y, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();
		builder.begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
		for (Vec2 v : Constants.CIRCLE_MESH_CACHE) {
			float curX = x + rad * v.x;
			float curZ = z + rad * v.y;
			builder.vertex(m, curX, y2, curZ).color(r, g, b, a).endVertex();
			builder.vertex(m, curX, y, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();
		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		builder.vertex(m, x, y2, z).color(r, g, b, a).endVertex();
		for (Vec2 v : Constants.CIRCLE_MESH_CACHE) {
			float curX = x + rad * v.x;
			float curZ = z + rad * v.y;
			builder.vertex(m, curX, y2, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();

		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		builder.vertex(m, x, y, z).color(r, g, b, a).endVertex();
		for (Vec2 v : Constants.CIRCLE_MESH_CACHE) {
			float curX = x + rad * v.x;
			float curZ = z + rad * v.y;
			builder.vertex(m, curX, y, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();
		builder.begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
		for (Vec2 v : Constants.CIRCLE_MESH_CACHE) {
			float curX = x + rad * v.x;
			float curZ = z + rad * v.y;
			builder.vertex(m, curX, y, curZ).color(r, g, b, a).endVertex();
			builder.vertex(m, curX, y2, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();
		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		builder.vertex(m, x, y2, z).color(r, g, b, a).endVertex();
		for (int i = Constants.CIRCLE_MESH_CACHE.length - 1; i >= 0; --i)
		{
			Vec2 v = Constants.CIRCLE_MESH_CACHE[i];
			float curX = x + rad * v.x;
			float curZ = z + rad * v.y;
			builder.vertex(m, curX, y2, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();
	}

	public static void renderSphere(Matrix4f m, Matrix3f n, float x, float y, float z, float rad, float r, float g, float b, float a)
	{
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.getBuilder();
		RenderSystem.disableTexture();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		Vector3f[] cache1 = Constants.SPHERE_MESH_CACHE[0];
		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		builder.vertex(m, x, y - rad, z).color(r, g, b, a).endVertex();
		for (int i = cache1.length - 1; i >= 0; --i)
		{
			Vector3f v = cache1[i];
			float curX = x + rad * v.x();
			float curY = y + rad * v.y();
			float curZ = z + rad * v.z();
			builder.vertex(m, curX, curY, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();
		for (int j = 1; j < Constants.SPHERE_MESH_CACHE.length; j++)
		{
			Vector3f[] cache2 = Constants.SPHERE_MESH_CACHE[j];
			builder.begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
			for (int i = 0; i < cache1.length; ++i)
			{
				Vector3f v1 = cache1[i];
				float curX1 = x + rad * v1.x();
				float curY1 = y + rad * v1.y();
				float curZ1 = z + rad * v1.z();
				Vector3f v2 = cache2[i];
				float curX2 = x + rad * v2.x();
				float curY2 = y + rad * v2.y();
				float curZ2 = z + rad * v2.z();
				builder.vertex(m, curX2, curY2, curZ2).color(r, g, b, a).endVertex();
				builder.vertex(m, curX1, curY1, curZ1).color(r, g, b, a).endVertex();
			}
			tesselator.end();
			cache1 = cache2;
		}
		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		builder.vertex(m, x, y + rad, z).color(r, g, b, a).endVertex();
		for (Vector3f v : cache1) {
			float curX = x + rad * v.x();
			float curY = y + rad * v.y();
			float curZ = z + rad * v.z();
			builder.vertex(m, curX, curY, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();


		cache1 = Constants.SPHERE_MESH_CACHE[0];
		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		builder.vertex(m, x, y - rad, z).color(r, g, b, a).endVertex();
		for (Vector3f v : cache1) {
			float curX = x + rad * v.x();
			float curY = y + rad * v.y();
			float curZ = z + rad * v.z();
			builder.vertex(m, curX, curY, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();
		for (int j = 1; j < Constants.SPHERE_MESH_CACHE.length; j++)
		{
			Vector3f[] cache2 = Constants.SPHERE_MESH_CACHE[j];
			builder.begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
			for (int i = 0; i < cache1.length; ++i)
			{
				Vector3f v1 = cache1[i];
				float curX1 = x + rad * v1.x();
				float curY1 = y + rad * v1.y();
				float curZ1 = z + rad * v1.z();
				Vector3f v2 = cache2[i];
				float curX2 = x + rad * v2.x();
				float curY2 = y + rad * v2.y();
				float curZ2 = z + rad * v2.z();
				builder.vertex(m, curX1, curY1, curZ1).color(r, g, b, a).endVertex();
				builder.vertex(m, curX2, curY2, curZ2).color(r, g, b, a).endVertex();
			}
			tesselator.end();
			cache1 = cache2;
		}
		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		builder.vertex(m, x, y + rad, z).color(r, g, b, a).endVertex();
		for (int i = cache1.length - 1; i >= 0; --i)
		{
			Vector3f v = cache1[i];
			float curX = x + rad * v.x();
			float curY = y + rad * v.y();
			float curZ = z + rad * v.z();
			builder.vertex(m, curX, curY, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();
	}

	public static void renderPolygon(Matrix4f m, Matrix3f n, float y1, float y2, float x, float z, Vector3f[] points, float r, float g, float b, float a)
	{
		if (points.length < 3) return;
		if (y1 > y2)
		{
			float t = y1;
			y1 = y2;
			y2 = t;
		}
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.getBuilder();
		RenderSystem.disableTexture();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);

		Vector3f v;
		float curX, curZ;

		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		for (int i = points.length - 1; i >= 0; --i)
		{
			v = points[i];
			curX = x + v.x();
			curZ = z + v.z();
			builder.vertex(m, curX, y1, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();

		builder.begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
		v = points[points.length - 1];
		curX = x + v.x();
		curZ = z + v.z();
		builder.vertex(m, curX, y2, curZ).color(r, g, b, a).endVertex();
		builder.vertex(m, curX, y1, curZ).color(r, g, b, a).endVertex();
		for (Vector3f point : points) {
			v = point;
			curX = x + v.x();
			curZ = z + v.z();
			builder.vertex(m, curX, y2, curZ).color(r, g, b, a).endVertex();
			builder.vertex(m, curX, y1, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();

		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		for (Vector3f point : points) {
			v = point;
			curX = x + v.x();
			curZ = z + v.z();
			builder.vertex(m, curX, y2, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();


		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		for (Vector3f point : points) {
			v = point;
			curX = x + v.x();
			curZ = z + v.z();
			builder.vertex(m, curX, y1, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();

		builder.begin(Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
		v = points[points.length - 1];
		curX = x + v.x();
		curZ = z + v.z();
		builder.vertex(m, curX, y1, curZ).color(r, g, b, a).endVertex();
		builder.vertex(m, curX, y2, curZ).color(r, g, b, a).endVertex();
		for (Vector3f point : points) {
			v = point;
			curX = x + v.x();
			curZ = z + v.z();
			builder.vertex(m, curX, y1, curZ).color(r, g, b, a).endVertex();
			builder.vertex(m, curX, y2, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();

		builder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		for (int i = points.length - 1; i >= 0; --i)
		{
			v = points[i];
			curX = x + v.x();
			curZ = z + v.z();
			builder.vertex(m, curX, y2, curZ).color(r, g, b, a).endVertex();
		}
		tesselator.end();
	}
}
