package com.firemerald.fecore.util.distribution;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.IntFunction;

import javax.annotation.Nonnull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public interface IDistribution<T>
{
	public static <T> IDistribution<T> get(JsonObject json, String name, Function<String, T> converter, IntFunction<T[]> arrayConstructor)
	{
		if (!json.has(name)) throw new JsonSyntaxException("Missing \"" + name + "\", expected to find a string, array of strings, or set of float values");
		JsonElement el = json.get(name);
		if (el.isJsonObject())
		{
			Set<Entry<String, JsonElement>> musicEntries = el.getAsJsonObject().entrySet();
			if (musicEntries.isEmpty()) throw new JsonSyntaxException("Invalid \"" + name + "\", expected to find a string, array of strings, or set of float values");
			Map<T, Float> tracksMap = new HashMap<>(musicEntries.size());
			for (Entry<String, JsonElement> entry : musicEntries)
			{
				if (!entry.getValue().isJsonPrimitive()) throw new JsonSyntaxException("Invalid \"" + name + "\", expected to find a string, array of strings, or set of float values");
				else try
				{
					float weight = entry.getValue().getAsJsonPrimitive().getAsFloat();
					tracksMap.compute(converter.apply(entry.getKey()), (o, w) -> w == null ? weight : (weight + w));
				}
				catch (NumberFormatException e)
				{
					throw new JsonSyntaxException("Invalid \"" + name + "\", expected to find a string, array of strings, or set of float values", e);
				}
			}
			return get(tracksMap);
		}
		else if (el.isJsonArray())
		{
			JsonArray musicAr = el.getAsJsonArray();
			if (musicAr.isEmpty()) throw new JsonSyntaxException("Invalid \"" + name + "\", expected to find a string, array of strings, or set of float values");
			T[] tracksAr = arrayConstructor.apply(musicAr.size());
			for (int i = 0; i < tracksAr.length; ++i)
			{
				JsonElement el2 = musicAr.get(i);
				if (!el2.isJsonPrimitive()) throw new JsonSyntaxException("Invalid \"" + name + "\", expected to find a string, array of strings, or set of float values");
				else tracksAr[i] = converter.apply(el2.getAsString());
			}
			return get(tracksAr);
		}
		else if (el.isJsonPrimitive()) return new SingletonDistribution<>(converter.apply(el.getAsString()));
		else throw new JsonSyntaxException("Invalid \"" + name + "\", expected to find a string, array of strings, or set of float values");
	}
	
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
	
	public abstract JsonElement toJson(Function<T, String> converter);
}