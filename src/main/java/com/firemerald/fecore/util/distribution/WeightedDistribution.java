package com.firemerald.fecore.util.distribution;

import java.util.*;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class WeightedDistribution<T> implements IDistribution<T>
{
	private final Map<T, Float> weights = new HashMap<>();
	private final NavigableMap<Float, T> objects = new TreeMap<>();
	private float size = 0;
	
	public WeightedDistribution() {}
	
	public WeightedDistribution(@Nonnull Map<T, Float> objects)
	{
		set(objects);
	}
	
	public void set(@Nonnull Map<T, Float> objects)
	{
		weights.clear();
		weights.putAll(objects);
		updateObjects();
	}
	
	public void add(@Nonnull T object, float weight)
	{
		weights.compute(object, (o, w) -> w == null ? weight : (weight + w));
		updateObjects();
	}
	
	public void remove(@Nonnull T object, float weight)
	{
		add(object, -weight);
		updateObjects();
	}
	
	public void remove(@Nonnull T object)
	{
		weights.remove(object);
		updateObjects();
	}
	
	public void updateObjects()
	{
		this.objects.clear();
		size = 0;
		for (Map.Entry<T, Float> entry : weights.entrySet())
		{
			size += entry.getValue();
			this.objects.put(size, entry.getKey());
		}
	}

	@Override
	public T get(float value)
	{
		return value < 0 || value >= size ? null : objects.ceilingEntry(value).getValue();
	}
	
	@Override
	public Collection<T> getValues()
	{
		return weights.keySet();
	}
	
	public Map<T, Float> getWeights()
	{
		return ImmutableMap.copyOf(weights);
	}

	@Override
	public float size()
	{
		return size;
	}
	
	public Builder<T> toBuilder()
	{
		return new Builder<>(weights);
	}
	
	public static <T> Builder<T> builder()
	{
		return new Builder<>();
	}
	
	public static class Builder<T>
	{
		private final Map<T, Float> weights = new HashMap<>();
		
		public Builder() {}
		
		public Builder(@Nonnull Map<T, Float> weights)
		{
			this.weights.putAll(weights);
		}
		
		public Builder<T> add(@Nonnull T object, float weight)
		{
			weights.compute(object, (o, w) -> w == null ? weight : (weight + w));
			return this;
		}
		
		public Builder<T> remove(@Nonnull T object, float weight)
		{
			add(object, -weight);
			return this;
		}
		
		public Builder<T> remove(@Nonnull T object)
		{
			weights.remove(object);
			return this;
		}
		
		public WeightedDistribution<T> build()
		{
			return new WeightedDistribution<>(weights);
		}
	}

	@Override
	public JsonElement toJson(Function<T, String> converter)
	{
		JsonObject obj = new JsonObject();
		weights.forEach((v, w) -> obj.addProperty(converter.apply(v), w));
		return obj;
	}
}