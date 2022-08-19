package com.firemerald.fecore.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ReadOnlyList<E> implements List<E>
{
	public final List<E> list;
	
	public ReadOnlyList(List<E> list)
	{
		this.list = list;
	}
	
	@Override
	public int size()
	{
		return list.size();
	}

	@Override
	public boolean isEmpty()
	{
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o)
	{
		return list.contains(o);
	}

	@Override
	public Iterator<E> iterator()
	{
		return list.iterator();
	}

	@Override
	public Object[] toArray()
	{
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return list.toArray(a);
	}

	@Override
	public boolean add(E e)
	{
		throw new UnsupportedOperationException("Attempted to modify a read-only list");
	}

	@Override
	public boolean remove(Object o)
	{
		throw new UnsupportedOperationException("Attempted to modify a read-only list");
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		throw new UnsupportedOperationException("Attempted to modify a read-only list");
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		throw new UnsupportedOperationException("Attempted to modify a read-only list");
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		throw new UnsupportedOperationException("Attempted to modify a read-only list");
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		throw new UnsupportedOperationException("Attempted to modify a read-only list");
	}

	@Override
	public void clear()
	{
		throw new UnsupportedOperationException("Attempted to modify a read-only list");
	}

	@Override
	public E get(int index)
	{
		return list.get(index);
	}

	@Override
	public E set(int index, E element)
	{
		throw new UnsupportedOperationException("Attempted to modify a read-only list");
	}

	@Override
	public void add(int index, E element)
	{
		throw new UnsupportedOperationException("Attempted to modify a read-only list");
	}

	@Override
	public E remove(int index)
	{
		throw new UnsupportedOperationException("Attempted to modify a read-only list");
	}

	@Override
	public int indexOf(Object o)
	{
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator()
	{
		return list.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index)
	{
		return list.listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		return list.subList(fromIndex, toIndex);
	}
}
