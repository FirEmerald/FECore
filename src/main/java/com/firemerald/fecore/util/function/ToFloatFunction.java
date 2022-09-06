package com.firemerald.fecore.util.function;

@FunctionalInterface
public interface ToFloatFunction<T>
{
    float applyAsFloat(T t);
}
