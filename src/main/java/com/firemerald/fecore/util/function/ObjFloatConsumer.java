package com.firemerald.fecore.util.function;

@FunctionalInterface
public interface ObjFloatConsumer<T>
{
    void accept(T t, float value);
}
