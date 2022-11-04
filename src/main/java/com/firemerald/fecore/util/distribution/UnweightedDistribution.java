package com.firemerald.fecore.util.distribution;

import java.util.*;
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

	
	public Builder<T> toBuilder()
	{
		return new Builder<>(objects);
	}
	
	public static <T> Builder<T> builder()
	{
		return new Builder<>();
	}
	
	public static class Builder<T>
	{
		private final List<T> objects = new ArrayList<>();
		
		public Builder() {}
		
		public Builder(@Nonnull List<T> objects)
		{
			this.objects.addAll(objects);
		}
		
		public Builder<T> add(@Nonnull T object)
		{
			objects.add(object);
			return this;
		}
		
		public Builder<T> remove(@Nonnull T object)
		{
			objects.remove(object);
			return this;
		}
		
		public UnweightedDistribution<T> build()
		{
			return new UnweightedDistribution<>(objects);
		}
	}
}