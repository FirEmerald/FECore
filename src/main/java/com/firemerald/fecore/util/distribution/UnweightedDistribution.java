package com.firemerald.fecore.util.distribution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.util.Mth;

public class UnweightedDistribution<T> implements IDistribution<T>
{
	private final List<T> objects = new ArrayList<T>();
	
	public UnweightedDistribution() {}
	
	public UnweightedDistribution(Collection<T> objects)
	{
		objects.addAll(objects);
	}
	
	public void add(T object)
	{
		objects.add(object);
	}
	
	public void remove(T object)
	{
		objects.remove(object);
	}

	@Override
	public float size()
	{
		return objects.size();
	}

	@Override
	public T get(float value)
	{
		return value < 0 || value >= objects.size() ? null : objects.get(Mth.floor(value));
	}

	@Override
	public Collection<T> getValues()
	{
		return ImmutableList.copyOf(objects);
	}
	
}