package com.firemerald.fecore.util.distribution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import net.minecraft.util.Mth;

public class UnweightedDistribution<T> implements IDistribution<T>
{
	private final List<T> objects = new ArrayList<T>();
	
	public UnweightedDistribution() {}
	
	public UnweightedDistribution(@Nonnull Collection<T> objects)
	{
		this.objects.addAll(objects);
	}
	
	public void add(@Nonnull T object)
	{
		objects.add(object);
	}
	
	public void remove(@Nonnull T object)
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

	@Override
	public JsonElement toJson(Function<T, String> converter)
	{
		JsonArray array = new JsonArray(objects.size());
		objects.stream().map(converter).forEach(array::add);
		return array;
	}
	
}