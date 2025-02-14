package com.firemerald.fecore.util.bounds;

import java.util.Optional;
import java.util.function.Function;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;

import net.minecraft.advancements.critereon.MinMaxBounds;

public record FloatBounds(Optional<Float> min, Optional<Float> max, Optional<Float> minSq, Optional<Float> maxSq) implements MinMaxBounds<Float> {
    public static final FloatBounds ANY = new FloatBounds(Optional.empty(), Optional.empty());
    public static final Codec<FloatBounds> CODEC = MinMaxBounds.<Float, FloatBounds>createCodec(Codec.FLOAT, FloatBounds::new);

    private FloatBounds(Optional<Float> p_298243_, Optional<Float> p_299159_) {
        this(p_298243_, p_299159_, squareOpt(p_298243_), squareOpt(p_299159_));
    }

    private static FloatBounds create(StringReader reader, Optional<Float> min, Optional<Float> max) throws CommandSyntaxException {
        if (min.isPresent() && max.isPresent() && min.get() > max.get()) {
            throw ERROR_SWAPPED.createWithContext(reader);
        } else {
            return new FloatBounds(min, max);
        }
    }

    private static Optional<Float> squareOpt(Optional<Float> value) {
        return value.map(p_297908_ -> p_297908_ * p_297908_);
    }

    public static FloatBounds exactly(Float value) {
        return new FloatBounds(Optional.of(value), Optional.of(value));
    }

    public static FloatBounds between(Float min, Float max) {
        return new FloatBounds(Optional.of(min), Optional.of(max));
    }

    public static FloatBounds atLeast(Float min) {
        return new FloatBounds(Optional.of(min), Optional.empty());
    }

    public static FloatBounds atMost(Float max) {
        return new FloatBounds(Optional.empty(), Optional.of(max));
    }

    public boolean matches(Float value) {
        return this.min.isPresent() && this.min.get() > value ? false : this.max.isEmpty() || !(this.max.get() < value);
    }

    public boolean matchesSqr(Float value) {
        return this.minSq.isPresent() && this.minSq.get() > value ? false : this.maxSq.isEmpty() || !(this.maxSq.get() < value);
    }

    public static FloatBounds fromReader(StringReader reader) throws CommandSyntaxException {
        return fromReader(reader, p_154807_ -> p_154807_);
    }

    public static FloatBounds fromReader(StringReader reader, Function<Float, Float> formatter) throws CommandSyntaxException {
        return MinMaxBounds.fromReader(
            reader, FloatBounds::create, Float::parseFloat, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidFloat, formatter
        );
    }
}
