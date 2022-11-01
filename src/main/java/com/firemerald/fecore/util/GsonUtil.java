package com.firemerald.fecore.util;

import java.util.function.*;

import com.google.gson.JsonObject;

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
}