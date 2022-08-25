package com.firemerald.fecore.data;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.server.ServerLifecycleHooks;

/** to facilitate the loading of resources that must also persist on servers.
 * follows the same rules as the Loot Table loader for in-world resources. **/
//TODO DATAPACKS
public class ResourceLoader
{
	public static final LevelResource MAP_DATA_DIR = new LevelResource("data");

	public static MinecraftServer getServer()
	{
		return ServerLifecycleHooks.getCurrentServer();
	}

	public static InputStream getResource(ResourceLocation resource, String subfolder) throws IOException
	{
		try //get from world
		{
			MinecraftServer server = getServer();
			if (server != null)
			{
				File file = new File(server.getWorldPath(MAP_DATA_DIR).toFile(), subfolder + "/" + resource.getNamespace() + "/" + resource.getPath());
				return new FileInputStream(file);
			}
			else throw new IOException("MinecraftServer not found!");
		}
		catch (Throwable e)
		{
			if (FMLEnvironment.dist.isClient()) //get from resource packs
			{
				InputStream in = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(resource.getNamespace(), subfolder + "/" + resource.getPath())).getInputStream();
				if (in != null) return in;
				else throw (FileNotFoundException) new FileNotFoundException("Resource not found: " + resource).initCause(e);
			}
			else //get from classpath
			{
		        URL url = ResourceLoader.class.getResource("/assets/" + resource.getNamespace() + "/" + subfolder + "/" + resource.getPath());
		        if (url != null) return url.openStream();
				else throw (FileNotFoundException) new FileNotFoundException("Resource not found: " + resource).initCause(e);
			}
		}
	}

	public static InputStream getResource(ResourceLocation resource) throws IOException
	{
		try //get from world
		{
			MinecraftServer server = getServer();
			if (server != null)
			{
				File file = new File(server.getWorldPath(MAP_DATA_DIR).toFile(), resource.getNamespace() + "/" + resource.getPath());
				return new FileInputStream(file);
			}
			else throw new IOException("MinecraftServer not found!");
		}
		catch (Throwable e)
		{
			if (FMLEnvironment.dist.isClient()) //get from resource packs
			{
				InputStream in = Minecraft.getInstance().getResourceManager().getResource(resource).getInputStream();
				if (in != null) return in;
				else throw (FileNotFoundException) new FileNotFoundException("Resource not found: " + resource).initCause(e);
			}
			else //get from classpath
			{
		        URL url = ResourceLoader.class.getResource("/assets/" + resource.getNamespace() + "/" + resource.getPath());
		        if (url != null) return url.openStream();
				else throw (FileNotFoundException) new FileNotFoundException("Resource not found: " + resource).initCause(e);
			}
		}
	}

	public static InputStream getResourceWithoutWorld(ResourceLocation resource) throws IOException
	{
		if (FMLEnvironment.dist.isClient()) //get from resource packs
		{
			InputStream in = Minecraft.getInstance().getResourceManager().getResource(resource).getInputStream();
			if (in != null) return in;
			else throw new FileNotFoundException("Resource not found: " + resource);
		}
		else //get from classpath
		{
	        URL url = ResourceLoader.class.getResource("/assets/" + resource.getNamespace() + "/" + resource.getPath());
	        if (url != null) return url.openStream();
	        else throw new FileNotFoundException("Resource not found: " + resource);
		}
	}

	public static List<InputStream> getResources(ResourceLocation resource, String subfolder)
	{
		List<InputStream> list = new ArrayList<>();
		if (FMLEnvironment.dist.isClient()) //get from resource packs
		{
			try
			{
				Minecraft.getInstance().getResourceManager().getResources(new ResourceLocation(resource.getNamespace(), subfolder + "/" + resource.getPath())).forEach(iResource -> list.add(iResource.getInputStream()));
			}
			catch (Throwable e) {}
		}
		else //get from classpath
		{
	        URL url = ResourceLoader.class.getResource("/assets/" + resource.getNamespace() + "/" + subfolder + "/" + resource.getPath());
	        if (url != null) try
	        {
	        	list.add(url.openStream());
	        }
	        catch (Throwable e) {}
		}
		try //get from world
		{
			MinecraftServer server = getServer();
			if (server != null) try
			{
				File file = new File(server.getWorldPath(MAP_DATA_DIR).toFile(), subfolder + "/" + resource.getNamespace() + "/" + resource.getPath());
				list.add(new FileInputStream(file));
			}
			catch (ArrayIndexOutOfBoundsException e) {} //no entity world
		}
		catch (Throwable e) {}
		return list;
	}

	public static List<InputStream> getResources(ResourceLocation resource)
	{
		List<InputStream> list = new ArrayList<>();
		if (FMLEnvironment.dist.isClient()) //get from resource packs
		{
			try
			{
				Minecraft.getInstance().getResourceManager().getResources(resource).forEach(iResource -> list.add(iResource.getInputStream()));
			}
			catch (Throwable e) {}
		}
		else //get from classpath
		{
	        URL url = ResourceLoader.class.getResource("/assets/" + resource.getNamespace() + "/" + resource.getPath());
	        if (url != null) try
	        {
	        	list.add(url.openStream());
	        }
	        catch (Throwable e) {}
		}
		try //get from world
		{
			MinecraftServer server = getServer();
			if (server != null)
			{
				File file = new File(server.getWorldPath(MAP_DATA_DIR).toFile(), resource.getNamespace() + "/" + resource.getPath());
				list.add(new FileInputStream(file));
			}
		}
		catch (Throwable e) {}
		return list;
	}

	public static List<InputStream> getResourcesWithoutWorld(ResourceLocation resource)
	{
		List<InputStream> list = new ArrayList<>();
		if (FMLEnvironment.dist.isClient()) //get from resource packs
		{
			try
			{
				Minecraft.getInstance().getResourceManager().getResources(resource).forEach(iResource -> list.add(iResource.getInputStream()));
			}
			catch (Throwable e) {}
		}
		else //get from classpath
		{
	        URL url = ResourceLoader.class.getResource("/assets/" + resource.getNamespace() + "/" + resource.getPath());
	        if (url != null) try
	        {
	        	list.add(url.openStream());
	        }
	        catch (Throwable e) {}
		}
		return list;
	}

	public static Map<String, List<InputStream>> getResources(String resourcePath, String subfolder)
	{
		final Map<String, List<InputStream>> map = new HashMap<>();
		FileUtil.getDomains().forEach(domain -> {
			List<InputStream> list = getResources(new ResourceLocation(domain, resourcePath), subfolder);
			if (list != null && !list.isEmpty()) map.put(domain, list);
		});
		return map;
	}

	public static Map<String, List<InputStream>> getResources(String resourcePath)
	{
		final Map<String, List<InputStream>> map = new HashMap<>();
		FileUtil.getDomains().forEach(domain -> {
			List<InputStream> list = getResources(new ResourceLocation(domain, resourcePath));
			if (list != null && !list.isEmpty()) map.put(domain, list);
		});
		return map;
	}

	public static Map<String, List<InputStream>> getResourcesWithoutWorld(String resourcePath)
	{
		final Map<String, List<InputStream>> map = new HashMap<>();
		FileUtil.getDomains().forEach(domain -> {
			List<InputStream> list = getResourcesWithoutWorld(new ResourceLocation(domain, resourcePath));
			if (list != null && !list.isEmpty()) map.put(domain, list);
		});
		return map;
	}
}