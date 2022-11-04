package com.firemerald.fecore.util.distribution;

import java.util.*;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class SingletonDistribution<T> implements IDistribution<T>
{
	@Nonnull
	private final T object;
	
	public SingletonDistribution(@Nonnull T object)
	{
		this.object = object;
	}
	
	public float size()
	{
		return 1;
	}
	
	public T get(float value)
	{
		return value < 0 || value >= 1 ? null : object;
	}
	
	public Collection<T> getValues()
	{
		return Collections.singleton(object);
	}
	
	public boolean isEmpty()
	{
		return false;
	}
	
	public T getRandom(@Nonnull Random rand)
	{
		return object;
	}
	
	public T getRandom()
	{
		return object;
	}
	
	public boolean contains(@Nonnull T value)
	{
		return object.equals(value);
	}

	@Override
	public JsonElement toJson(Function<T, String> converter)
	{
		return new JsonPrimitive(converter.apply(object));
	}
}