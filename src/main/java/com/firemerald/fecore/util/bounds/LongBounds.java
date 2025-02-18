package com.firemerald.fecore.util.bounds;

import java.util.function.Function;

import javax.annotation.Nullable;

import com.firemerald.fecore.codec.CodedCodec;
import com.google.gson.JsonElement;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.util.GsonHelper;

public class LongBounds extends MinMaxBounds<Long> {
    public static final LongBounds ANY = new LongBounds((Long)null, (Long)null);
    public static final Codec<LongBounds> CODEC = CodedCodec.ofJson(LongBounds::serializeToJson, LongBounds::fromJson);
    @Nullable
    private final Long minSq;
    @Nullable
    private final Long maxSq;

    private static LongBounds create(StringReader p_154796_, @Nullable Long p_154797_, @Nullable Long p_154798_) throws CommandSyntaxException {
       if (p_154797_ != null && p_154798_ != null && p_154797_ > p_154798_) {
          throw ERROR_SWAPPED.createWithContext(p_154796_);
       } else {
          return new LongBounds(p_154797_, p_154798_);
       }
    }

    @Nullable
    private static Long squareOpt(@Nullable Long pValue) {
       return pValue == null ? null : pValue * pValue;
    }

    private LongBounds(@Nullable Long p_154784_, @Nullable Long p_154785_) {
       super(p_154784_, p_154785_);
       this.minSq = squareOpt(p_154784_);
       this.maxSq = squareOpt(p_154785_);
    }

    public static LongBounds exactly(Long pValue) {
       return new LongBounds(pValue, pValue);
    }

    public static LongBounds between(Long pMin, Long pMax) {
       return new LongBounds(pMin, pMax);
    }

    public static LongBounds atLeast(Long pMin) {
       return new LongBounds(pMin, (Long)null);
    }

    public static LongBounds atMost(Long pMax) {
       return new LongBounds((Long)null, pMax);
    }

    public boolean matches(Long pValue) {
       if (this.min != null && this.min > pValue) {
          return false;
       } else {
          return this.max == null || !(this.max < pValue);
       }
    }

    public boolean matchesSqr(Long pValue) {
       if (this.minSq != null && this.minSq > pValue) {
          return false;
       } else {
          return this.maxSq == null || !(this.maxSq < pValue);
       }
    }

    public static LongBounds fromJson(@Nullable JsonElement pJson) {
       return fromJson(pJson, ANY, GsonHelper::convertToLong, LongBounds::new);
    }

    public static LongBounds fromReader(StringReader pReader) throws CommandSyntaxException {
       return fromReader(pReader, (p_154807_) -> {
          return p_154807_;
       });
    }

    public static LongBounds fromReader(StringReader pReader, Function<Long, Long> pFormatter) throws CommandSyntaxException {
       return fromReader(pReader, LongBounds::create, Long::parseLong, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidLong, pFormatter);
    }
}
