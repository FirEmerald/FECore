package com.firemerald.fecore.util.function;

import java.util.function.UnaryOperator;

import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface CharUnaryOperator extends UnaryOperator<Character> {
	public static final CharUnaryOperator IDENTITY = c -> c;
	public static final CharUnaryOperator POSITIVE_INTEGERS = c -> {
		if (c >= '0' && c <= '9') return c;
		else return 0;
	};
	public static final CharUnaryOperator INTEGERS = c -> {
		if ((c >= '0' && c <= '9') || c == '-') return c;
		else return 0;
	};
	public static final CharUnaryOperator POSITIVE_NUMBERS = c -> {
		if ((c >= '0' && c <= '9') || c == '.') return c;
		else return 0;
	};
	public static final CharUnaryOperator NUMBERS = c -> {
		if ((c >= '0' && c <= '9') || c == '-' || c == '.') return c;
		else return 0;
	};
	public static final CharUnaryOperator LOWERCASE = c -> {
		return Character.toLowerCase(c);
	};
	public static final CharUnaryOperator RESOURCE_LOCATION = c -> {
		if (c == ':') return c;
		c = Character.toLowerCase(c);
		if (ResourceLocation.validPathChar(c)) return c;
		else return 0;
	};

	public char apply(char input);

	@Override
	public default Character apply(Character input) {
		return apply(input.charValue());
	}
}
