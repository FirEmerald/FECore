package com.firemerald.fecore.util.distribution;

import java.util.*;

import javax.annotation.Nonnull;

public interface IDistribution<T>
{
	@SafeVarargs
	public static <T> IDistribution<T> get(T... values)
	{
		if (values.length == 0) return EmptyDistribution.get();
		else if (values.length == 1) return new SingletonDistribution<>(values[0]);
		else return new UnweightedDistribution<>(Arrays.asList(values));
	}

	public static <T> IDistribution<T> get(Collection<T> values)
	{
		if (values.isEmpty()) return EmptyDistribution.get();
		else if (values.size() == 1) return new SingletonDistribution<>(values.iterator().next());
		else return new UnweightedDistribution<>(values);
	}

	public static <T> IDistribution<T> get(Map<T, Float> weights)
	{
		if (weights.isEmpty()) return EmptyDistribution.get();
		else if (weights.size() == 1) return new SingletonDistribution<>(weights.keySet().iterator().next());
		else
		{
			Iterator<Float> it = weights.values().iterator();
			float weight = it.next();
			while (it.hasNext()) if (it.next() != weight) return new WeightedDistribution<>(weights);
			return new UnweightedDistribution<>(weights.keySet()); //all weights equal, return unweighted for performance
		}
	}
	
	public float size();
	
	public T get(float value);
	
	public Collection<T> getValues();
	
	public default boolean isEmpty()
	{
		return size() == 0;
	}
	
	public default T getRandom(@Nonnull Random rand)
	{
		return get(rand.nextFloat(size()));
	}
	
	public default T getRandom()
	{
		return get((float) (Math.random() * size()));
	}
	
	public default boolean contains(@Nonnull T value)
	{
		return getValues().contains(value);
	}
}