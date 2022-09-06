package com.firemerald.fecore.util.function;

import java.util.Objects;

@FunctionalInterface
public interface ByteUnaryOperator
{
    byte applyAsByte(byte operand);

    default ByteUnaryOperator compose(ByteUnaryOperator before)
    {
        Objects.requireNonNull(before);
        return v -> applyAsByte(before.applyAsByte(v));
    }

    default ByteUnaryOperator andThen(ByteUnaryOperator after)
    {
        Objects.requireNonNull(after);
        return v -> after.applyAsByte(applyAsByte(v));
    }

    static ByteUnaryOperator identity()
    {
        return v -> v;
    }
}
