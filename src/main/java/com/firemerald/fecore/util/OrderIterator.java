package com.firemerald.fecore.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class OrderIterator<T> implements Iterator<T[]>
{
	private final T[] values, current;
	private final boolean[] grabbed;
	private final int[] iterations;
	private boolean first = true;

	@SuppressWarnings("unchecked")
	@SafeVarargs
	public OrderIterator(T... values)
	{
		this.values = values;
		this.current = (T[]) Array.newInstance(values.getClass().getComponentType(), values.length);
		this.grabbed = new boolean[values.length];
		this.iterations = new int[values.length];
	}

	@Override
	public boolean hasNext()
	{
		for (int i = 0; i < values.length; ++i) if (iterations[i] < (values.length - i - 1)) return true;
		return false;
	}

	@Override
	public T[] next()
	{
		if (!hasNext()) throw new NoSuchElementException("Reached the end of all possible orders for " + Arrays.deepToString(values));
		if (first)
		{
			System.arraycopy(values, 0, current, 0, values.length);
			first = false;
		}
		else
		{
			for (int i = 0; i < values.length; ++i) grabbed[i] = false;
			for (int i = 0; i < values.length; ++i)
			{
				int iter = ++iterations[i];
				if (iter == (values.length - i)) iterations[i] = 0;
				else break;
			}
			for (int i = 0; i < values.length; ++i)
			{
				int j = 0;
				int index = 0;
				while (grabbed[index] || j < iterations[i])
				{
					if (!grabbed[index]) j++;
					index++;
				}
				current[i] = values[index];
				grabbed[index] = true;
			}
		}
		return current;
	}
}
