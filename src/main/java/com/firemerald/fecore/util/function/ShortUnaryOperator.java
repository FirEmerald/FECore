package com.firemerald.fecore.util.function;

import java.util.Objects;

@FunctionalInterface
public interface ShortUnaryOperator
{
    short applyAsShort(short operand);

    default ShortUnaryOperator compose(ShortUnaryOperator before)
    {
        Objects.requireNonNull(before);
        return v -> applyAsShort(before.applyAsShort(v));
    }

    default ShortUnaryOperator andThen(ShortUnaryOperator after)
    {
        Objects.requireNonNull(after);
        return v -> after.applyAsShort(applyAsShort(v));
    }

    static ShortUnaryOperator identity()
    {
        return v -> v;
    }
}
