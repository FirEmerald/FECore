package com.firemerald.fecore.util.bounds;

import java.util.Optional;
import java.util.function.Function;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;

import net.minecraft.advancements.critereon.MinMaxBounds;

public record LongBounds(Optional<Long> min, Optional<Long> max, Optional<Long> minSq, Optional<Long> maxSq) implements MinMaxBounds<Long> {
    public static final LongBounds ANY = new LongBounds(Optional.empty(), Optional.empty());
    public static final Codec<LongBounds> CODEC = MinMaxBounds.<Long, LongBounds>createCodec(Codec.LONG, LongBounds::new);

    private LongBounds(Optional<Long> p_298243_, Optional<Long> p_299159_) {
        this(p_298243_, p_299159_, squareOpt(p_298243_), squareOpt(p_299159_));
    }

    private static LongBounds create(StringReader reader, Optional<Long> min, Optional<Long> max) throws CommandSyntaxException {
        if (min.isPresent() && max.isPresent() && min.get() > max.get()) {
            throw ERROR_SWAPPED.createWithContext(reader);
        } else {
            return new LongBounds(min, max);
        }
    }

    private static Optional<Long> squareOpt(Optional<Long> value) {
        return value.map(p_297908_ -> p_297908_ * p_297908_);
    }

    public static LongBounds exactly(Long value) {
        return new LongBounds(Optional.of(value), Optional.of(value));
    }

    public static LongBounds between(Long min, Long max) {
        return new LongBounds(Optional.of(min), Optional.of(max));
    }

    public static LongBounds atLeast(Long min) {
        return new LongBounds(Optional.of(min), Optional.empty());
    }

    public static LongBounds atMost(Long max) {
        return new LongBounds(Optional.empty(), Optional.of(max));
    }

    public boolean matches(Long value) {
        return this.min.isPresent() && this.min.get() > value ? false : this.max.isEmpty() || !(this.max.get() < value);
    }

    public boolean matchesSqr(Long value) {
        return this.minSq.isPresent() && this.minSq.get() > value ? false : this.maxSq.isEmpty() || !(this.maxSq.get() < value);
    }

    public static LongBounds fromReader(StringReader reader) throws CommandSyntaxException {
        return fromReader(reader, p_154807_ -> p_154807_);
    }

    public static LongBounds fromReader(StringReader reader, Function<Long, Long> formatter) throws CommandSyntaxException {
        return MinMaxBounds.fromReader(
            reader, LongBounds::create, Long::parseLong, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidLong, formatter
        );
    }
}
