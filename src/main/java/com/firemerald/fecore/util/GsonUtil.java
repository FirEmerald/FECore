package com.firemerald.fecore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.*;

import com.google.gson.*;

import net.minecraft.util.GsonHelper;

public class GsonUtil extends GsonHelper
{
	public static boolean getAsBoolean(JsonObject json, String memberName, BooleanSupplier def)
	{
		return json.has(memberName) ? convertToBoolean(json.get(memberName), memberName) : def.getAsBoolean();
	}

	public static byte getAsByte(JsonObject json, String memberName, IntSupplier def)
	{
		return json.has(memberName) ? convertToByte(json.get(memberName), memberName) : (byte) def.getAsInt();
	}

	public static short getAsShort(JsonObject json, String memberName, IntSupplier def)
	{
		return json.has(memberName) ? convertToShort(json.get(memberName), memberName) : (short) def.getAsInt();
	}

	public static int getAsInt(JsonObject json, String memberName, IntSupplier def)
	{
		return json.has(memberName) ? convertToInt(json.get(memberName), memberName) : def.getAsInt();
	}

	public static long getAsLong(JsonObject json, String memberName, LongSupplier def)
	{
		return json.has(memberName) ? convertToLong(json.get(memberName), memberName) : def.getAsLong();
	}

	public static float getAsFloat(JsonObject json, String memberName, DoubleSupplier def)
	{
		return json.has(memberName) ? convertToFloat(json.get(memberName), memberName) : (float) def.getAsDouble();
	}

	public static double getAsDouble(JsonObject json, String memberName, DoubleSupplier def)
	{
		return json.has(memberName) ? convertToDouble(json.get(memberName), memberName) : def.getAsDouble();
	}

	public static String getAsString(JsonObject json, String memberName, Supplier<String> def)
	{
		return json.has(memberName) ? convertToString(json.get(memberName), memberName) : def.get();
	}
	
	@SafeVarargs
	public static <T> JsonElement toArrayOrPrimitive(T... values)
	{
		return toArrayOrPrimitiveString(Object::toString, values);
	}

	public static <T> JsonElement toArrayOrPrimitive(Collection<T> values)
	{
		return toArrayOrPrimitiveString(values, Object::toString);
	}

	public static <T> JsonElement toArrayOrPrimitive(List<T> values)
	{
		return toArrayOrPrimitiveString(values, Object::toString);
	}
	
	@SafeVarargs
	public static <T> JsonElement toArrayOrPrimitiveString(Function<T, String> converter, T... values)
	{
		return toArrayOrPrimitive(v -> new JsonPrimitive(converter.apply(v)), values);
	}

	public static <T> JsonElement toArrayOrPrimitiveString(Collection<T> values, Function<T, String> converter)
	{
		return toArrayOrPrimitive(values, v -> new JsonPrimitive(converter.apply(v)));
	}

	public static <T> JsonElement toArrayOrPrimitiveString(Function<T, String> converter, List<T> values)
	{
		return toArrayOrPrimitive(values, v -> new JsonPrimitive(converter.apply(v)));
	}
	
	@SafeVarargs
	public static <T> JsonElement toArrayOrPrimitive(Function<T, JsonElement> converter, T... values)
	{
		if (values.length == 0) return new JsonArray();
		else if (values.length == 1) return converter.apply(values[0]);
		else
		{
			JsonArray array = new JsonArray(values.length);
			for (int i = 0; i < values.length; ++i) array.set(i, converter.apply(values[i]));
			return array;
		}
	}

	public static <T> JsonElement toArrayOrPrimitive(Collection<T> values, Function<T, JsonElement> converter)
	{
		if (values.isEmpty()) return new JsonArray();
		else if (values.size() == 1) return converter.apply(values.iterator().next());
		else
		{
			JsonArray array = new JsonArray(values.size());
			values.stream().map(converter).forEach(array::add);
			return array;
		}
	}

	public static <T> JsonElement toArrayOrPrimitive(List<T> values, Function<T, JsonElement> converter)
	{
		if (values.isEmpty()) return new JsonArray();
		else if (values.size() == 1) return new JsonPrimitive(values.get(0).toString());
		else
		{
			JsonArray array = new JsonArray(values.size());
			values.stream().map(converter).forEach(array::add);
			return array;
		}
	}
	
	public static <T> T[] arrayFromArrayOrSingle(JsonObject obj, String name, String expected, Function<JsonElement, T> converter, IntFunction<T[]> arrayConstructor)
	{
		if (!obj.has(name)) throw new JsonSyntaxException("Missing \"" + name + "\", expected to find " + expected);
		else return arrayFromArrayOrSingle(obj.get(name), name, expected, converter, arrayConstructor);
	}
	
	public static <T> T[] arrayFromArrayOrSingle(JsonElement el, String name, String expected, Function<JsonElement, T> converter, IntFunction<T[]> arrayConstructor)
	{
		T[] array;
		if (el.isJsonArray())
		{
			JsonArray jsonArray = el.getAsJsonArray();
			if (jsonArray.isEmpty()) throw new JsonSyntaxException("Invalid \"" + name + "\", expected to find " + expected);
			array = arrayConstructor.apply(jsonArray.size());
			for (int i = 0; i < array.length; ++i)
			{
				JsonElement el2 = jsonArray.get(i);
				if (!el2.isJsonPrimitive()) throw new JsonSyntaxException("Invalid \"" + name + "\", expected to find " + expected);
				else array[i] = converter.apply(el2);
			}
		}
		else
		{
			array = arrayConstructor.apply(1);
			array[0] = converter.apply(el);
		}
		return array;
	}
	
	public static <T> List<T> listFromArrayOrSingle(JsonObject obj, String name, String expected, Function<JsonElement, T> converter)
	{
		if (!obj.has(name)) throw new JsonSyntaxException("Missing \"" + name + "\", expected to find " + expected);
		else return listFromArrayOrSingle(obj.get(name), name, expected, converter);
	}
	
	public static <T> List<T> listFromArrayOrSingle(JsonElement el, String name, String expected, Function<JsonElement, T> converter)
	{
		List<T> list;
		if (el.isJsonArray())
		{
			JsonArray jsonArray = el.getAsJsonArray();
			if (jsonArray.isEmpty()) throw new JsonSyntaxException("Invalid \"" + name + "\", expected to find " + expected);
			list = new ArrayList<>(jsonArray.size());
			for (int i = 0; i < jsonArray.size(); ++i)
			{
				JsonElement el2 = jsonArray.get(i);
				if (!el2.isJsonPrimitive()) throw new JsonSyntaxException("Invalid \"" + name + "\", expected to find " + expected);
				else list.add(converter.apply(el2));
			}
		}
		else
		{
			list = new ArrayList<>(1);
			list.add(converter.apply(el));
		}
		return list;
	}
	
	public static <T> T[] arrayFromArrayOrSingle(JsonObject obj, String name, Function<String, T> converter, IntFunction<T[]> arrayConstructor)
	{
		if (!obj.has(name)) throw new JsonSyntaxException("Missing \"" + name + "\", expected to find a string or array of strings");
		else return arrayFromArrayOrSingle(obj.get(name), name, converter, arrayConstructor);
	}
	
	public static <T> T[] arrayFromArrayOrSingle(JsonElement el, String name, Function<String, T> converter, IntFunction<T[]> arrayConstructor)
	{
		return arrayFromArrayOrSingle(el, name, "a string or array of strings", (JsonElement el2) -> {
			if (!el2.isJsonPrimitive()) throw new JsonSyntaxException("Invalid \"" + name + "\", expected to find a string or array of strings");
			return converter.apply(el2.getAsString());
		}, arrayConstructor);
	}
	
	public static <T> List<T> listFromArrayOrSingle(JsonObject obj, String name, Function<String, T> converter)
	{
		if (!obj.has(name)) throw new JsonSyntaxException("Missing \"" + name + "\", expected to find a string or array of strings");
		else return listFromArrayOrSingle(obj.get(name), name, converter);
	}
	
	public static <T> List<T> listFromArrayOrSingle(JsonElement el, String name, Function<String, T> converter)
	{
		return listFromArrayOrSingle(el, name, "a string or array of strings", (JsonElement el2) -> {
			if (!el2.isJsonPrimitive()) throw new JsonSyntaxException("Invalid \"" + name + "\", expected to find a string or array of strings");
			return converter.apply(el2.getAsString());
		});
	}
}