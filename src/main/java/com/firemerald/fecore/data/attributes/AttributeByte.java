package com.firemerald.fecore.data.attributes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.firemerald.fecore.data.BinaryFormat;
import com.firemerald.fecore.data.FileUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class AttributeByte implements IAttribute
{
	public final byte val;

	public AttributeByte(byte val)
	{
		this.val = val;
	}

	@Override
	public int getID()
	{
		return ID_BYTE;
	}

	@Override
	public String getString()
	{
		return Byte.toString(val);
	}

	@Override
	public boolean getBoolean()
	{
		return val > 0;
	}

	@Override
	public byte getByte()
	{
		return val;
	}

	@Override
	public short getShort()
	{
		return val;
	}

	@Override
	public int getInt()
	{
		return val;
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
		throw new Exception("Value is a byte, not an enum");
	}

	@Override
	public void write(OutputStream out, BinaryFormat format) throws IOException
	{
		FileUtil.writeByte(out, val);
	}

	public static AttributeByte read(InputStream in) throws IOException
	{
		return new AttributeByte(FileUtil.readByte(in));
	}

	@Override
	public JsonElement makeElement()
	{
		return new JsonPrimitive(val);
	}
}