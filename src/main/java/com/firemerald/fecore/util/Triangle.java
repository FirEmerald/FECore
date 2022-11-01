package com.firemerald.fecore.util;

import net.minecraft.world.phys.Vec3;

public class Triangle
{
	public static final ImmutableTriangle NONE = new ImmutableTriangle(Vec3.ZERO, Vec3.ZERO, Vec3.ZERO);

	public static class ImmutableTriangle extends Triangle
	{
		public ImmutableTriangle(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3)
		{
			super(x1, y1, z1, x2, y2, z2, x3, y3, z3);
		}

		public ImmutableTriangle(Vec3 p1, Vec3 p2, Vec3 p3)
		{
			super(p1, p2, p3);
		}

		public ImmutableTriangle(Triangle of)
		{
			super(of);
		}

		@Override
		@Deprecated
		public void set(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3)
		{
			throw new IllegalArgumentException("Tried to modify an immutable triangle");
		}

		@Override
		@Deprecated
		public void set(Vec3 p1, Vec3 p2, Vec3 p3)
		{
			throw new IllegalArgumentException("Tried to modify an immutable triangle");
		}
	}

	protected Vec3 p1, p2, p3;
	protected boolean initDelta = false, initCross = false, initArea = false;
	protected Vec3 ab, ac, cross;
	protected double area;

	public Triangle()
	{
		this(Vec3.ZERO, Vec3.ZERO, Vec3.ZERO);
	}

	public Triangle(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3)
	{
		this(new Vec3(x1, y1, z1), new Vec3(x2, y2, z2), new Vec3(x3, y3, z3));
	}

	public Triangle(Vec3 p1, Vec3 p2, Vec3 p3)
	{
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}

	public Triangle(Triangle of)
	{
		this.p1 = of.p1;
		this.p2 = of.p2;
		this.p3 = of.p3;
		this.initDelta = of.initDelta;
		this.ab = of.ab;
		this.ac = of.ac;
		this.initCross = of.initCross;
		this.cross = of.cross;
		this.initArea = of.initArea;
		this.area = of.area;
	}

	@Override
	public Triangle clone()
	{
		return new Triangle(this);
	}

	public void set(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3)
	{
		set(new Vec3(x1, y1, z1), new Vec3(x2, y2, z2), new Vec3(x3, y3, z3));
	}

	public void set(Vec3 p1, Vec3 p2, Vec3 p3)
	{
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		initDelta = initCross = initArea = false;
		ab = ac = cross = null;
		area = 0;
	}

	public void initDelta()
	{
		if (initDelta) return;
		ab = p2.subtract(p1);
		ac = p3.subtract(p1);
		initDelta = true;
	}

	public Vec3 getA()
	{
		return p1;
	}

	public Vec3 getB()
	{
		return p2;
	}

	public Vec3 getC()
	{
		return p3;
	}

	public Vec3 getAB()
	{
		initDelta();
		return ab;
	}

	public Vec3 getAC()
	{
		initDelta();
		return ac;
	}

	public void initCross()
	{
		if (initCross) return;
		initDelta();
		cross = ab.cross(ac);
		initCross = true;
	}

	public Vec3 getCross()
	{
		initCross();
		return cross;
	}

	public void initArea()
	{
		if (initArea) return;
		initCross();
		area = .5 * cross.length();
		initArea = true;
	}

	public double getArea()
	{
		initArea();
		return area;
	}
}