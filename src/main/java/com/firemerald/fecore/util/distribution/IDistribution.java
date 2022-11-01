package com.firemerald.fecore.util.distribution;

import java.util.Collection;
import java.util.Random;

public interface IDistribution<T>
{
	public float size();
	
	public T get(float value);
	
	public Collection<T> getValues();
	
	public default boolean isEmpty()
	{
		return size() == 0;
	}
	
	public default T getRandom(Random rand)
	{
		return get(rand.nextFloat(size()));
	}
	
	public default T getRandom()
	{
		return get((float) (Math.random() * size()));
	}
	
	public default boolean contains(T value)
	{
		return getValues().contains(value);
	}
}