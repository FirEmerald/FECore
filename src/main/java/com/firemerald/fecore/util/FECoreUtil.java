package com.firemerald.fecore.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FECoreUtil
{
	@OnlyIn(Dist.CLIENT)
    public static URL getURLForResource(final ResourceLocation resource)
    {
        String s = String.format("resource:%s:%s", resource.getNamespace(), resource.getPath());
        URLStreamHandler urlstreamhandler = new URLStreamHandler()
        {
            @Override
			protected URLConnection openConnection(URL p_openConnection_1_)
            {
                return new URLConnection(p_openConnection_1_)
                {
                    @Override
					public void connect() throws IOException {}

                    @Override
					public InputStream getInputStream() throws IOException
                    {
                        return Minecraft.getInstance().getResourceManager().getResource(resource).getInputStream();
                    }
                };
            }
        };
        try
        {
            return new URL((URL)null, s, urlstreamhandler);
        }
        catch (MalformedURLException var4)
        {
            throw new Error("TODO: Sanely handle url exception! :D"); //TODO Sanely handle url exception!
        }
    }

	public static long factorial(long v)
	{
		long r = v;
		while (v > 2) r *= --v;
		return r;
	}

	@SafeVarargs
	public static <T> Stream<T[]> allOrders(T... values)
	{
		return streamOf(new OrderIterator<>(values));
	}

	public static Stream<int[]> allOrders(int... values)
	{
		return streamOf(new IntOrderIterator(values));
	}

	public static Stream<long[]> allOrders(long... values)
	{
		return streamOf(new LongOrderIterator(values));
	}

	public static Stream<double[]> allOrders(double... values)
	{
		return streamOf(new DoubleOrderIterator(values));
	}

	public static <T> Stream<T> streamOf(Iterator<T> iterator)
	{
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
	}
}