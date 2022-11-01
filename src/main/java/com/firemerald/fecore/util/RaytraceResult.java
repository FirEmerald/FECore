package com.firemerald.fecore.util;

import java.util.function.DoublePredicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.phys.Vec3;

public class RaytraceResult<T>
{
	public static final DoublePredicate CULL_NONE = v -> v != 0;
	public static final DoublePredicate CULL_BACK = v -> v < 0;
	public static final DoublePredicate CULL_FRONT = v -> v > 0;

	public static final DoublePredicate ALONG_AXIS = v -> true;
	public static final DoublePredicate ALONG_RAY = v -> v >= 0;
	public static final DoublePredicate INTERSECTS = v -> v >= 0 && v <= 1;

	@Nullable
	public static <T> RaytraceResult<T> testTriangle(@Nullable T target, @Nonnull Triangle triangle, @Nonnull Ray ray, @Nonnull DoublePredicate detPred, @Nonnull DoublePredicate aVPred, boolean isInside)
	{
		Vec3 cross = triangle.getCross();
		double det = cross.dot(ray.direction);
		if (!detPred.test(det)) return null;
		Vec3 p1 = triangle.getA();
		double afX = ray.from.x - p1.x;
		double afY = ray.from.y - p1.y;
		double afZ = ray.from.z - p1.z;
		double aV = -((afX * cross.x + afY * cross.y + afZ * cross.z) / det);
		if (!aVPred.test(aV)) return null;
		Vec3 ab = triangle.getAB();
		Vec3 ac = triangle.getAC();
		double a2 = (afX * ((ac.y * ray.direction.z) - (ac.z * ray.direction.y)) + afY * ((ac.z * ray.direction.x) - (ac.x * ray.direction.z)) + afZ * ((ac.x * ray.direction.y) - (ac.y * ray.direction.x))) / det;
		double a3 = (afX * ((ab.z * ray.direction.y) - (ab.y * ray.direction.z)) + afY * ((ab.x * ray.direction.z) - (ab.z * ray.direction.x)) + afZ * ((ab.y * ray.direction.x) - (ab.x * ray.direction.y))) / det;
		double a1 = 1 - (a2 + a3);
		if (isInside ^ (a1 >= 0 && a2 >= 0 && a3 >= 0)) return null;
		return new RaytraceResult<>(target, triangle, ray, det, afX, afY, afZ, aV, a1, a2, a3);
	}

	@Nullable
	public static <T> RaytraceResult<T> testPlane(@Nullable T target, @Nonnull Triangle triangle, @Nonnull Ray ray, @Nonnull DoublePredicate detPred, @Nonnull DoublePredicate aVPred)
	{
		Vec3 cross = triangle.getCross();
		double det = cross.dot(ray.direction);
		if (!detPred.test(det)) return null;
		Vec3 p1 = triangle.getA();
		double afX = ray.from.x - p1.x;
		double afY = ray.from.y - p1.y;
		double afZ = ray.from.z - p1.z;
		double aV = -((afX * cross.x + afY * cross.y + afZ * cross.z) / det);
		if (!aVPred.test(aV)) return null;
		return new RaytraceResult<>(target, triangle, ray, det, afX, afY, afZ, aV);
	}

	@Nullable
	public static <T> RaytraceResult<T> test(@Nullable T target, @Nonnull Triangle triangle, @Nonnull Ray ray, @Nonnull DoublePredicate detPred)
	{
		Vec3 cross = triangle.getCross();
		double det = cross.dot(ray.direction);
		if (!detPred.test(det)) return null;
		return new RaytraceResult<>(target, triangle, ray, det);
	}

	protected T target;
	protected Triangle triangle;
	protected Ray ray;
	protected boolean initDet = false, initAF = false, initAV = false, initDis = false, initTrace = false, initPos = false;
	protected boolean parallel;
	protected double det, afX, afY, afZ, aV, dis, a1, a2, a3;
	protected Vec3 hitPos;

	public RaytraceResult(Ray ray)
	{
		this(null, Triangle.NONE, ray);
	}

	public RaytraceResult(@Nullable T target, @Nonnull Triangle triangle, @Nonnull Ray ray)
	{
		this.triangle = triangle;
		this.ray = ray;
	}

	protected RaytraceResult(@Nullable T target, @Nonnull Triangle triangle, @Nonnull Ray ray, double det)
	{
		this(target, triangle, ray);
		this.det = det;
		this.parallel = det == 0;
		this.initDet = true;
	}

	protected RaytraceResult(@Nullable T target, @Nonnull Triangle triangle, @Nonnull Ray ray, double det, double afX, double afY, double afZ, double aV)
	{
		this(target, triangle, ray, det);
		this.afX = afX;
		this.afY = afY;
		this.afZ = afZ;
		this.initAF = true;
		this.aV = aV;
		this.initAV = true;
	}

	protected RaytraceResult(@Nullable T target, @Nonnull Triangle triangle, @Nonnull Ray ray, double det, double afX, double afY, double afZ, double aV, double a1, double a2, double a3)
	{
		this(target, triangle, ray, det, afX, afY, afZ, aV);
		this.a1 = a1;
		this.a2 = a2;
		this.a3 = a3;
		this.initTrace = true;
	}

	public T getTarget()
	{
		return target;
	}

	public Triangle getTriangle()
	{
		return triangle;
	}

	public Ray getRay()
	{
		return ray;
	}

	protected void set(@Nullable T target, @Nonnull Triangle triangle, double det)
	{
		this.target = target;
		this.triangle = triangle;
		this.initDet = true;
		this.det = det;
		this.parallel = det == 0;
		this.initAF = false;
		this.afX = 0;
		this.afY = 0;
		this.afZ = 0;
		this.initAV = false;
		this.aV = 0;
		this.initTrace = false;
		this.a1 = 0;
		this.a2 = 0;
		this.a3 = 0;
		this.initPos = this.initDis = false;
		this.hitPos = null;
		this.dis = 0;
	}

	protected void set(@Nullable T target, @Nonnull Triangle triangle, double det, double afX, double afY, double afZ, double aV)
	{
		this.target = target;
		this.triangle = triangle;
		this.initDet = true;
		this.det = det;
		this.parallel = det == 0;
		this.initAF = true;
		this.afX = afX;
		this.afY = afY;
		this.afZ = afZ;
		this.initAV = true;
		this.aV = aV;
		this.initTrace = false;
		this.a1 = 0;
		this.a2 = 0;
		this.a3 = 0;
		this.initPos = this.initDis = false;
		this.hitPos = null;
		this.dis = 0;
	}

	protected void set(@Nullable T target, @Nonnull Triangle triangle, double det, double afX, double afY, double afZ, double aV, double a1, double a2, double a3)
	{
		this.target = target;
		this.triangle = triangle;
		this.initDet = true;
		this.det = det;
		this.parallel = det == 0;
		this.initAF = true;
		this.afX = afX;
		this.afY = afY;
		this.afZ = afZ;
		this.initAV = true;
		this.aV = aV;
		this.initTrace = true;
		this.a1 = a1;
		this.a2 = a2;
		this.a3 = a3;
		this.initPos = this.initDis = false;
		this.hitPos = null;
		this.dis = 0;
	}

	public boolean setIfCloser(@Nullable T target, @Nonnull Triangle triangle, @Nonnull DoublePredicate detPred, @Nonnull DoublePredicate aVPred, boolean isInside)
	{
		Vec3 cross = triangle.getCross();
		double det = cross.dot(ray.direction);
		if (!detPred.test(det)) return false;
		Vec3 p1 = triangle.getA();
		double afX = ray.from.x - p1.x;
		double afY = ray.from.y - p1.y;
		double afZ = ray.from.z - p1.z;
		double aV = -((afX * cross.x + afY * cross.y + afZ * cross.z) / det);
		if (!aVPred.test(aV)) return false;
		this.initAV();
		if (!this.isParallel() && aV >= this.aV) return false;
		Vec3 ab = triangle.getAB();
		Vec3 ac = triangle.getAC();
		double a2 = (afX * ((ac.y * ray.direction.z) - (ac.z * ray.direction.y)) + afY * ((ac.z * ray.direction.x) - (ac.x * ray.direction.z)) + afZ * ((ac.x * ray.direction.y) - (ac.y * ray.direction.x))) / det;
		double a3 = (afX * ((ab.z * ray.direction.y) - (ab.y * ray.direction.z)) + afY * ((ab.x * ray.direction.z) - (ab.z * ray.direction.x)) + afZ * ((ab.y * ray.direction.x) - (ab.x * ray.direction.y))) / det;
		double a1 = 1 - (a2 + a3);
		if (isInside ^ (a1 >= 0 && a2 >= 0 && a3 >= 0)) return false;
		set(target, triangle, det, afX, afY, afZ, aV, a1, a2, a3);
		return true;
	}

	public boolean setIfCloser(@Nullable T target, @Nonnull Triangle triangle, @Nonnull DoublePredicate detPred, @Nonnull DoublePredicate aVPred)
	{
		Vec3 cross = triangle.getCross();
		double det = cross.dot(ray.direction);
		if (!detPred.test(det)) return false;
		Vec3 p1 = triangle.getA();
		double afX = ray.from.x - p1.x;
		double afY = ray.from.y - p1.y;
		double afZ = ray.from.z - p1.z;
		double aV = -((afX * cross.x + afY * cross.y + afZ * cross.z) / det);
		if (!aVPred.test(aV)) return false;
		this.initAV();
		if (!this.isParallel() && aV >= this.aV) return false;
		set(target, triangle, det, afX, afY, afZ, aV);
		return true;
	}

	/**
	 * Computes the determinant of the inverse matrix used in the calculation of the raytrace, and also sets parallel, if needed.
	 */
	public void initDet()
	{
		if (initDet) return;
		det = triangle.getCross().dot(ray.direction);
		parallel = det == 0;
		initDet = true;
	}

	/**
	 * @return whether the ray and triangle are parallel. the raytrace is considered a failure if they are.
	 */
	public boolean isParallel()
	{
		initDet();
		return parallel;
	}

	/**
	 * @return whether the trace fails backface culling
	 */
	public boolean failBackfaceCulling()
	{
		initDet();
		return det >= 0;
	}

	/**
	 * @return whether the trace fails frontface culling
	 */
	public boolean failFrontfaceCulling()
	{
		initDet();
		return det <= 0;
	}

	/**
	 * Computes the vector ray.from - triangle.p1 if needed
	 */
	public void initAF()
	{
		if (initAF) return;
		Vec3 p1 = triangle.getA();
		afX = ray.from.x - p1.x;
		afY = ray.from.y - p1.y;
		afZ = ray.from.z - p1.z;
		initAF = true;
	}

	/**
	 * Computes the hit distance from the ray's origin to the plane of the triangle, normalized against the ray's direction length, if needed
	 */
	public void initAV()
	{
		if (initAV) return;
		initDet();
		if (!parallel)
		{
			initAF();
			//double inv02 = cross.x;
			//double inv12 = cross.y;
			//double inv22 = cross.z;
			//double aV = -((afX * inv02 + afY * inv12 + afZ * inv22) / det);
			Vec3 cross = triangle.getCross();
			aV = -((afX * cross.x + afY * cross.y + afZ * cross.z) / det);
		}
		initAV = true;
	}

	/**
	 * @return the distance from the ray's origin to the plane of the triangle, normalized against the ray's direction length
	 */
	public double getScaledDistance()
	{
		initAV();
		return aV;
	}

	/**
	 * Computes the distance from the ray's origin to the plane of the triangle, in units, if needed
	 */
	public void initDis()
	{
		if (initDis) return;
		initAV();
		if (!parallel)
		{
			double length = ray.direction.length();
			dis = aV / length;
		}
		initDis = true;
	}

	/**
	 * @return the distance from the ray's origin to the plane of the triangle, in units
	 */
	public double getUnitDistance()
	{
		initDis();
		return dis;
	}

	/**
	 * @return if the ray would hit the plane of the triangle, starting from the ray's origin and moving in the ray's direction
	 */
	public boolean hitsPlane()
	{
		initAV();
		return !parallel && aV >= 0;
	}

	/**
	 * @return if the ray would intersect the plane of the triangle, starting from the ray's origin and ending at the ray's origin plus it's direction
	 */
	public boolean intersectsPlane()
	{
		initAV();
		return !parallel && aV >= 0 && aV <= 1;
	}

	/**
	 * Computes the vertex weights a1, a2, and a3 for the triangle's points p1, p2, and p3, respectively, for the intersection point, if needed.
	 */
	public void initTrace()
	{
		if (initTrace) return;
		initDet();
		if (!parallel)
		{
			initAF();
			//double inv00 = (ac.y * ray.direction.z) - (ac.z * ray.direction.y);
			//double inv10 = (ac.z * ray.direction.x) - (ac.x * ray.direction.z);
			//double inv20 = (ac.x * ray.direction.y) - (ac.y * ray.direction.x);
			//double inv01 = (ab.z * ray.direction.y) - (ab.y * ray.direction.z);
			//double inv11 = (ab.x * ray.direction.z) - (ab.z * ray.direction.x);
			//double inv21 = (ab.y * ray.direction.x) - (ab.x * ray.direction.y);
			//double a2 = (afX * inv00 + afY * inv10 + afZ * inv20) / det;
			//double a3 = (afX * inv01 + afY * inv11 + afZ * inv21) / det;
			Vec3 ab = triangle.getAB();
			Vec3 ac = triangle.getAC();
			a2 = (afX * ((ac.y * ray.direction.z) - (ac.z * ray.direction.y)) + afY * ((ac.z * ray.direction.x) - (ac.x * ray.direction.z)) + afZ * ((ac.x * ray.direction.y) - (ac.y * ray.direction.x))) / det;
			a3 = (afX * ((ab.z * ray.direction.y) - (ab.y * ray.direction.z)) + afY * ((ab.x * ray.direction.z) - (ab.z * ray.direction.x)) + afZ * ((ab.y * ray.direction.x) - (ab.x * ray.direction.y))) / det;
			a1 = 1 - (a2 + a3);
		}
		initTrace = true;
	}

	/**
	 * @return if the ray would hit the triangle in either direction
	 */
	public boolean inTriangle()
	{
		initTrace();
		return !parallel && a1 >= 0 && a2 >= 0 && a3 >= 0;
	}

	/**
	 * @return if the ray would hit the triangle, starting from it's origin and moving in it's direction
	 */
	public boolean hitsTriangle()
	{
		initAV();
		if (parallel || aV < 0) return false;
		initTrace();
		return a1 >= 0 && a2 >= 0 && a3 >= 0;
	}

	/**
	 * @return if the ray would intersect the triangle, starting from the ray's origin and ending at the ray's origin plus it's direction
	 */
	public boolean intersectsTriangle()
	{
		initAV();
		if (parallel || aV < 0 || aV > 1) return false;
		initTrace();
		return a1 >= 0 && a2 >= 0 && a3 >= 0;
	}

	/**
	 * @return the vertex weight a1 for the triangle's point p1 for the intersection point
	 */
	public double getA1()
	{
		initTrace();
		return a1;
	}

	/**
	 * @return the vertex weight a2 for the triangle's point p2 for the intersection point
	 */
	public double getA2()
	{
		initTrace();
		return a2;
	}

	/**
	 * @return the vertex weight a3 for the triangle's point p3 for the intersection point
	 */
	public double getA3()
	{
		initTrace();
		return a3;
	}

	/**
	 * Computes the intersection point, if needed
	 */
	public void initPos()
	{
		if (initPos) return;
		initAV();
		if (!parallel) hitPos = new Vec3(ray.from.x + ray.direction.x * aV, ray.from.y + ray.direction.y * aV, ray.from.z + ray.direction.z * aV);
		initPos = true;
	}

	/**
	 * @return the intersection point, or null if the ray is parallel to the triangle's plane
	 */
	@Nullable
	public Vec3 getHitPos()
	{
		initPos();
		return hitPos;
	}
}