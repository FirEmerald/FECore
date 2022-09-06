package com.firemerald.fecore.util.function;

import java.util.Objects;

@FunctionalInterface
public interface FloatUnaryOperator
{
    float applyAsFloat(float operand);

    default FloatUnaryOperator compose(FloatUnaryOperator before)
    {
        Objects.requireNonNull(before);
        return v -> applyAsFloat(before.applyAsFloat(v));
    }

    default FloatUnaryOperator andThen(FloatUnaryOperator after)
    {
        Objects.requireNonNull(after);
        return v -> after.applyAsFloat(applyAsFloat(v));
    }

    static FloatUnaryOperator identity()
    {
        return v -> v;
    }
}
