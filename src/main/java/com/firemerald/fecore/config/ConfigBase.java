package com.firemerald.fecore.config;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public abstract class ConfigBase
{
	private boolean isBuilding = false;
	private Stack<String> currentPath = new Stack<>();
	private String currentKey;
	private ForgeConfigSpec.Builder builder;
	public final String name;
	public final ForgeConfigSpec spec;
	
	public ConfigBase(ModLoadingContext context, ModConfig.Type type)
	{
		this(context, type, false);
	}
	
	public ConfigBase(ModLoadingContext context, ModConfig.Type type, boolean subfolder)
	{
		this(context, type.extension(), type, subfolder);
	}
	
	public ConfigBase(ModLoadingContext context, String name, ModConfig.Type type)
	{
		this(context, name, type, false);
	}
	
	public ConfigBase(ModLoadingContext context, String name, ModConfig.Type type, boolean subfolder)
	{
		this.name = name;
		this.currentKey = context.getActiveNamespace() + ".config." + name;
		isBuilding = true;
		builder = new ForgeConfigSpec.Builder();
		build();
		spec = builder.build();
		finishBuilding();
		context.registerConfig(type, spec, context.getActiveNamespace() + (subfolder ? "/" : "-") + name + ".toml");
	}
	
	public abstract void build();
	
	public final void assertBuilding()
	{
		if (!isBuilding) throw new IllegalStateException("Config is not building!");
	}
	
	public String getCurrentKey()
	{
		return currentKey;
	}
	
	public String getKey(String name)
	{
		return currentKey + "." + name;
	}
	
	protected void beginSection(String name, String... comment)
	{
		assertBuilding();
		currentPath.push(name);
		currentKey += "." + name;
		builder
		.comment(comment)
		.translation(currentKey)
		.push(name);
	}
	
	protected void endBeginSection(String name, String... comment)
	{
		assertBuilding();
		if (currentPath.isEmpty()) throw new IllegalStateException("Failed to build config: attempted to end a section before one has started");
		String old = currentPath.pop();
		currentPath.push(name);
		currentKey = currentKey.substring(0, currentKey.length() - old.length()) + name;
		builder
		.pop()
		.comment(comment)
		.translation(currentKey)
		.push(name);
	}
	
	protected void endSection()
	{
		assertBuilding();
		if (currentPath.isEmpty()) throw new IllegalStateException("Failed to build config: attempted to end a section before one has started");
		String old = currentPath.pop();
		currentKey = currentKey.substring(0, currentKey.length() - old.length() - 1);
		builder.pop();
	}
	
	private void finishBuilding()
	{
		assertBuilding();
		isBuilding = false;
		this.builder = null;
		this.currentPath = null;
		this.currentKey = null;
	}
	
	protected String[] append(String[] comment, String append)
	{
		String[] newComment = new String[comment.length + 1];
		System.arraycopy(comment, 0, newComment, 0, comment.length);
		newComment[comment.length] = append;
		return newComment;
	}
	
	protected BooleanValue define(String name, boolean def, String... comment)
	{
		return define(name, () -> def, comment);
	}
	
	protected BooleanValue define(String name, Supplier<Boolean> def, String... comment)
	{
		assertBuilding();
		return builder
				.comment(comment)
				.translation(getKey(name))
				.define(name, def);
	}
	
	protected IntValue define(String name, int def, int min, int max, String... comment)
	{
		return define(name, () -> def, min, max, comment);
	}
	
	protected IntValue define(String name, Supplier<Integer> def, int min, int max, String... comment)
	{
		assertBuilding();
		return builder
				.comment(append(comment, "min: " + min + ", max:" + max))
				.translation(getKey(name))
				.defineInRange(name, def, min, max);
	}
	
	protected LongValue define(String name, long def, long min, long max, String... comment)
	{
		return define(name, () -> def, min, max, comment);
	}
	
	protected LongValue define(String name, Supplier<Long> def, long min, long max, String... comment)
	{
		assertBuilding();
		return builder
				.comment(append(comment, "min: " + min + ", max:" + max))
				.translation(getKey(name))
				.defineInRange(name, def, min, max);
	}
	
	protected DoubleValue define(String name, double def, double min, double max, String... comment)
	{
		return define(name, () -> def, min, max, comment);
	}
	
	protected DoubleValue define(String name, Supplier<Double> def, double min, double max, String... comment)
	{
		assertBuilding();
		return builder
				.comment(append(comment, "min: " + min + ", max:" + max))
				.translation(getKey(name))
				.defineInRange(name, def, min, max);
	}
	
	protected <E extends Enum<E>> EnumValue<E> define(String name, E def, E[] values, String... comment)
	{
		return define(name, (Supplier<E>) () -> def, values, comment);
	}
	
	@SuppressWarnings("unchecked")
	protected <E extends Enum<E>> EnumValue<E> define(String name, Supplier<E> def, E[] values, String... comment)
	{
		assertBuilding();
		final Collection<E> collection = Arrays.asList(values);
		return builder
				.comment(append(comment, "Allowed Values (ignore the list below this one): " + Arrays.stream(values).map(Enum::name).collect(Collectors.joining(", "))))
				.translation(getKey(name))
				.defineEnum(name, def, collection::contains, (Class<E>) values.getClass().componentType());
	}
	
	protected <E extends Enum<E>> EnumValue<E> define(String name, E def, Class<E> clazz, String... comment)
	{
		return define(name, (Supplier<E>) () -> def, clazz, comment);
	}
	
	protected <E extends Enum<E>> EnumValue<E> define(String name, Supplier<E> def, Class<E> clazz, String... comment)
	{
		assertBuilding();
		return builder
				.comment(comment)
				.translation(getKey(name))
				.defineEnum(name, def, clazz::isInstance, clazz);
	}
}