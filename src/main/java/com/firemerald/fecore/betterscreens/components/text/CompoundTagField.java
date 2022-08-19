package com.firemerald.fecore.betterscreens.components.text;

import java.util.function.Consumer;
import java.util.function.Predicate;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.client.gui.Font;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;

public class CompoundTagField extends BetterTextField
{
	public CompoundTagField(Font fontrendererObj, int x, int y, int w, int h, Component message, Consumer<CompoundTag> onChanged)
	{
		super(fontrendererObj, x, y, w, h, message, v -> {
			try
			{
				onChanged.accept(toTag(v));
				return true;
			}
			catch (CommandSyntaxException e)
			{
				return false;
			}
		});
	}

	public CompoundTagField(Font fontrendererObj, int x, int y, int w, int h, Component message, Predicate<CompoundTag> onChanged)
	{
		super(fontrendererObj, x, y, w, h, message, v -> {
			try
			{
				return onChanged.test(toTag(v));
			}
			catch (CommandSyntaxException e)
			{
				return false;
			}
		});
	}

	public CompoundTagField(Font fontrendererObj, int x, int y, int w, int h, CompoundTag val, Component message, Consumer<CompoundTag> onChanged)
	{
		super(fontrendererObj, x, y, w, h, message, v -> {
			try
			{
				onChanged.accept(toTag(v));
				return true;
			}
			catch (CommandSyntaxException e)
			{
				return false;
			}
		});
		this.setNBT(val);
	}

	public CompoundTagField(Font fontrendererObj, int x, int y, int w, int h, CompoundTag val, Component message, Predicate<CompoundTag> onChanged)
	{
		super(fontrendererObj, x, y, w, h, message, v -> {
			try
			{
				return onChanged.test(toTag(v));
			}
			catch (CommandSyntaxException e)
			{
				return false;
			}
		});
		this.setNBT(val);
	}

	public static CompoundTag toTag(String s) throws CommandSyntaxException
	{
		return s.isEmpty() ? new CompoundTag() : TagParser.parseTag(s);
	}

    public void setNBT(CompoundTag v)
    {
    	setString(v.toString()); //bypass setting val again
    }
}
