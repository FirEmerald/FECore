package com.firemerald.fecore.util.function;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface SexaFunction<A, B, C, D, E, F, Z> {
    Z apply(A a, B b, C c, D d, E e, F f);

    default <Y> SexaFunction<A, B, C, D, E, F, Y> andThen(Function<? super Z, ? extends Y> after) {
        Objects.requireNonNull(after);
        return (A a, B b, C c, D d, E e, F f) -> after.apply(apply(a, b, c, d, e, f));
    }
}
