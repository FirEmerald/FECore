package com.firemerald.fecore.util.function;

import java.util.Objects;

@FunctionalInterface
public interface CharUnaryOperator
{
    char applyAsChar(char operand);

    default CharUnaryOperator compose(CharUnaryOperator before)
    {
        Objects.requireNonNull(before);
        return v -> applyAsChar(before.applyAsChar(v));
    }

    default CharUnaryOperator andThen(CharUnaryOperator after)
    {
        Objects.requireNonNull(after);
        return v -> after.applyAsChar(applyAsChar(v));
    }

    static CharUnaryOperator identity()
    {
        return v -> v;
    }
}
