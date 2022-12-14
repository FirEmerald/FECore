package com.firemerald.fecore.data.attributes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.firemerald.fecore.data.BinaryFormat;
import com.firemerald.fecore.data.FileUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class AttributeLong implements IAttribute
{
	public final long val;

	public AttributeLong(long val)
	{
		this.val = val;
	}

	@Override
	public int getID()
	{
		return ID_LONG;
	}

	@Override
	public String getString()
	{
		return Long.toString(val);
	}

	@Override
	public boolean getBoolean()
	{
		return val > 0;
	}

	@Override
	public byte getByte()
	{
		return (byte) val;
	}

	@Override
	public short getShort()
	{
		return (short) val;
	}

	@Override
	public int getInt()
	{
		return (int) val;
	}

	@Override
	public long getLong()
	{
		return val;
	}

	@Override
	public float getFloat()
	{
		return val;
	}

	@Override
	public double getDouble()
	{
		return val;
	}

	@Override
	public <T extends Enum<?>> T getEnum(T[] values) throws Exception
	{
		throw new Exception("Value is a long, not an enum");
	}

	@Override
	public void write(OutputStream out, BinaryFormat format) throws IOException
	{
		FileUtil.writeLong(out, val);
	}

	public static AttributeLong read(InputStream in) throws IOException
	{
		return new AttributeLong(FileUtil.readLong(in));
	}

	@Override
	public JsonElement makeElement()
	{
		return new JsonPrimitive(val);
	}
}