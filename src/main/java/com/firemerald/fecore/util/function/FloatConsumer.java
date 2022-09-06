package com.firemerald.fecore.util.function;

import java.util.Objects;

@FunctionalInterface
public interface FloatConsumer
{
    void accept(float value);

    default FloatConsumer andThen(FloatConsumer after)
    {
        Objects.requireNonNull(after);
        return v -> { 
        	accept(v); 
        	after.accept(v); 
        	};
    }
}
