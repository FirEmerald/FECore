package com.firemerald.fecore.codec;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;

import org.joml.Vector3d;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.PrimitiveCodec;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;

public class Codecs {
	public static final PrimitiveCodec<DoubleStream> DOUBLE_STREAM = new PrimitiveCodec<>() {
        @Override
        public <T> DataResult<DoubleStream> read(final DynamicOps<T> ops, final T input) {
            return getDoubleStream(ops, input);
        }

        @Override
        public <T> T write(final DynamicOps<T> ops, final DoubleStream value) {
            return createDoubleList(ops, value);
        }

        @Override
        public String toString() {
            return "DoubleStream";
        }
    };

    public static <T> DataResult<DoubleStream> getDoubleStream(final DynamicOps<T> ops, final T input) {
        return ops.getStream(input).flatMap(stream -> {
            final List<T> list = stream.toList();
            if (list.stream().allMatch(element -> ops.getNumberValue(element).isSuccess())) {
                return DataResult.success(list.stream().mapToDouble(element -> ops.getNumberValue(element).getOrThrow().doubleValue()));
            }
            return DataResult.error(() -> "Some elements are not doubles: " + input);
        });
    }

    public static <T> T createDoubleList(final DynamicOps<T> ops, final DoubleStream input) {
        return ops.createList(input.mapToObj(ops::createDouble));
    }

    public static DataResult<double[]> fixedSize(DoubleStream stream, int size) {
    	double[] adouble = stream.limit(size + 1).toArray();
        if (adouble.length != size) {
            Supplier<String> supplier = () -> "Input is not a list of " + size + " doubles";
            return adouble.length >= size ? DataResult.error(supplier, Arrays.copyOf(adouble, size)) : DataResult.error(supplier);
        } else {
            return DataResult.success(adouble);
        }
    }

    public static final Codec<Vector3d> VECTOR3D = DOUBLE_STREAM
        .<Vector3d>comapFlatMap(
            stream -> fixedSize(stream, 3).map(array -> new Vector3d(array[0], array[1], array[2])),
            vec3 -> DoubleStream.of(vec3.x(), vec3.y(), vec3.z())
        )
        .stable();
    public static final Codec<List<Vector3d>> VECTOR3D_LIST = VECTOR3D.listOf();
    public static final Codec<Vector3d[]> VECTOR3D_ARRAY = new ArrayCodec<>(VECTOR3D_LIST, Vector3d[]::new);

    public static final StreamCodec<ByteBuf, Vector3d> VECTOR3D_STREAM = StreamCodec.composite(
    		ByteBufCodecs.DOUBLE, Vector3d::x,
    		ByteBufCodecs.DOUBLE, Vector3d::y,
    		ByteBufCodecs.DOUBLE, Vector3d::z,
    		Vector3d::new
    		);
    public static final StreamCodec<ByteBuf, List<Vector3d>> VECTOR3D_LIST_STREAM = VECTOR3D_STREAM.apply(ByteBufCodecs.list());
    public static final StreamCodec<ByteBuf, Vector3d[]> VECTOR3D_ARRAY_STREAM = new ArrayStreamCodec<>(VECTOR3D_STREAM, Vector3d[]::new);

	public static <S, T, U> DataResult<Pair<T, U>> mapResult(DataResult<Pair<S, U>> result, Function<S, T> mapper) {
		return result.map(res -> Pair.of(mapper.apply(res.getFirst()), res.getSecond()));
	}

	public static final Codec<ResourceLocation[]> RL_ARRAY_CODEC = new ArrayCodec<>(ExtraCodecs.compactListCodec(ResourceLocation.CODEC), ResourceLocation[]::new);

	@SuppressWarnings("unchecked")
	public static final <T> Codec<TagKey<T>[]> tagArrayCodec(ResourceKey<Registry<T>> resourceKey) {
		return new ArrayCodec<>(ExtraCodecs.compactListCodec(TagKey.codec(resourceKey)), length -> (TagKey<T>[]) new TagKey[length]);
	}

	public static <T, U> U getOrDefault(DynamicOps<T> ops, MapLike<T> map, String name, U def, Decoder<U> decoder, Consumer<String> onError) {
		T loopObj = map.get(name);
		if (loopObj != null) {
			DataResult<Pair<U, T>> decode = decoder.decode(ops, loopObj);
			if (decode.isError()) {
				decode.ifError(error -> onError.accept(error.message()));
				return def;
			} else return decode.getOrThrow().getFirst();
		}
		else return def;
	}
}
