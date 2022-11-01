package com.firemerald.fecore.util.distribution;

import java.util.*;

import javax.annotation.Nonnull;

public class EmptyDistribution<T> implements IDistribution<T>
{
	public static final EmptyDistribution<?> INSTANCE = new EmptyDistribution<>();
	
	@SuppressWarnings("unchecked")
	public static <T> EmptyDistribution<T> get()
	{
		return (EmptyDistribution<T>) INSTANCE;
	}
	
	private EmptyDistribution() {};
	
	public float size()
	{
		return 0;
	}
	
	public T get(float value)
	{
		return null;
	}
	
	public Collection<T> getValues()
	{
		return Collections.emptyList();
	}
	
	public boolean isEmpty()
	{
		return true;
	}
	
	public T getRandom(@Nonnull Random rand)
	{
		return null;
	}
	
	public T getRandom()
	{
		return null;
	}
	
	public boolean contains(@Nonnull T value)
	{
		return false;
	}
}