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

public class FloatBounds extends MinMaxBounds<Float> {
    public static final FloatBounds ANY = new FloatBounds((Float)null, (Float)null);
    public static final Codec<FloatBounds> CODEC = CodedCodec.ofJson(FloatBounds::serializeToJson, FloatBounds::fromJson);
    @Nullable
    private final Float minSq;
    @Nullable
    private final Float maxSq;

    private static FloatBounds create(StringReader p_154796_, @Nullable Float p_154797_, @Nullable Float p_154798_) throws CommandSyntaxException {
       if (p_154797_ != null && p_154798_ != null && p_154797_ > p_154798_) {
          throw ERROR_SWAPPED.createWithContext(p_154796_);
       } else {
          return new FloatBounds(p_154797_, p_154798_);
       }
    }

    @Nullable
    private static Float squareOpt(@Nullable Float pValue) {
       return pValue == null ? null : pValue * pValue;
    }

    private FloatBounds(@Nullable Float p_154784_, @Nullable Float p_154785_) {
       super(p_154784_, p_154785_);
       this.minSq = squareOpt(p_154784_);
       this.maxSq = squareOpt(p_154785_);
    }

    public static FloatBounds exactly(Float pValue) {
       return new FloatBounds(pValue, pValue);
    }

    public static FloatBounds between(Float pMin, Float pMax) {
       return new FloatBounds(pMin, pMax);
    }

    public static FloatBounds atLeast(Float pMin) {
       return new FloatBounds(pMin, (Float)null);
    }

    public static FloatBounds atMost(Float pMax) {
       return new FloatBounds((Float)null, pMax);
    }

    public boolean matches(Float pValue) {
       if (this.min != null && this.min > pValue) {
          return false;
       } else {
          return this.max == null || !(this.max < pValue);
       }
    }

    public boolean matchesSqr(Float pValue) {
       if (this.minSq != null && this.minSq > pValue) {
          return false;
       } else {
          return this.maxSq == null || !(this.maxSq < pValue);
       }
    }

    public static FloatBounds fromJson(@Nullable JsonElement pJson) {
       return fromJson(pJson, ANY, GsonHelper::convertToFloat, FloatBounds::new);
    }

    public static FloatBounds fromReader(StringReader pReader) throws CommandSyntaxException {
       return fromReader(pReader, (p_154807_) -> {
          return p_154807_;
       });
    }

    public static FloatBounds fromReader(StringReader pReader, Function<Float, Float> pFormatter) throws CommandSyntaxException {
       return fromReader(pReader, FloatBounds::create, Float::parseFloat, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidFloat, pFormatter);
    }
}
