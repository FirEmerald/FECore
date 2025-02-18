package com.firemerald.fecore.codec;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import org.joml.Vector3d;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.PrimitiveCodec;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.IForgeRegistry;

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
    public static final Codec<Vector3d> VECTOR3D = DOUBLE_STREAM.comapFlatMap(
    		doubles -> fixedSize(doubles, 3).map(Vector3d::new),
    		vec3 -> DoubleStream.of(vec3.x(), vec3.y(), vec3.z())
    		);
    public static final Codec<List<Vector3d>> VECTOR3D_LIST = VECTOR3D.listOf();
    public static final Codec<Vector3d[]> VECTOR3D_ARRAY = new ArrayCodec<>(VECTOR3D_LIST, Vector3d[]::new);

    public static final Codec<MinMaxBounds.Doubles> DOUBLE_BOUNDS = CodedCodec.ofJson(MinMaxBounds.Doubles::serializeToJson, MinMaxBounds.Doubles::fromJson);
    public static final Codec<MinMaxBounds.Ints> INT_BOUNDS = CodedCodec.ofJson(MinMaxBounds.Ints::serializeToJson, MinMaxBounds.Ints::fromJson);

    public static final Codec<ICondition> CONDITION = CodedCodec.<ICondition>ofJson(
    		CraftingHelper::serialize,
    		json -> CraftingHelper.getCondition((JsonObject) json));
    public static final Codec<List<ICondition>> CONDITIONS = CONDITION.listOf();

	public static boolean isSuccess(DataResult<?> result) {
		return result.result().isPresent();
	}

	public static boolean isError(DataResult<?> result) {
		return !isSuccess(result);
	}

    public static <T> DataResult<DoubleStream> getDoubleStream(final DynamicOps<T> ops, final T input) {
        return ops.getStream(input).flatMap(stream -> {
            final List<T> list = stream.collect(Collectors.toList());
            if (list.stream().allMatch(element -> ops.getNumberValue(element).result().isPresent())) {
                return DataResult.success(list.stream().mapToDouble(element -> ops.getNumberValue(element).result().get().doubleValue()));
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

	public static <S, T, U> DataResult<Pair<T, U>> mapResult(DataResult<Pair<S, U>> result, Function<S, T> mapper) {
		return result.map(res -> Pair.of(mapper.apply(res.getFirst()), res.getSecond()));
	}

	public static final Codec<ResourceLocation[]> RL_ARRAY_CODEC = new ArrayCodec<>(compactListCodec(ResourceLocation.CODEC), ResourceLocation[]::new);

	@SuppressWarnings("unchecked")
	public static final <T> Codec<TagKey<T>[]> tagArrayCodec(ResourceKey<Registry<T>> resourceKey) {
		return new ArrayCodec<>(compactListCodec(TagKey.codec(resourceKey)), length -> (TagKey<T>[]) new TagKey[length]);
	}

	public static <T, U> U getOrDefault(DynamicOps<T> ops, MapLike<T> map, String name, U def, Decoder<U> decoder, Consumer<String> onError) {
		T loopObj = map.get(name);
		if (loopObj != null) {
			DataResult<Pair<U, T>> decode = decoder.decode(ops, loopObj);
			if (isError(decode)) {
				onError.accept(decode.error().get().message());
				return def;
			} else return decode.getOrThrow(false, null).getFirst();
		}
		else return def;
	}

    public static <E> Codec<List<E>> compactListCodec(Codec<E> elementCodec) {
        return compactListCodec(elementCodec, elementCodec.listOf());
    }

    public static <E> Codec<List<E>> compactListCodec(Codec<E> elementCodec, Codec<List<E>> listCodec) {
        return Codec.either(listCodec, elementCodec)
            .xmap(
                decoded -> decoded.map(single -> single, List::of),
                list -> list.size() == 1 ? Either.right(list.get(0)) : Either.left(list)
            );
    }

    public static <T> Codec<T> byNameCodec(Supplier<IForgeRegistry<T>> registrySup) {
       return ResourceLocation.CODEC.flatXmap((id) -> {
    	   IForgeRegistry<T> registry = registrySup.get();
    	   return Optional.ofNullable(registry.getValue(id)).map(DataResult::success).orElseGet(() -> {
    		   return DataResult.error(() -> {
    			   return "Unknown registry key in " + registry.getRegistryKey() + ": " + id;
    		   });
    	   });
       }, (object) -> {
    	   IForgeRegistry<T> registry = registrySup.get();
    	   return registry.getResourceKey(object).map(ResourceKey::location).map(DataResult::success).orElseGet(() -> {
    		   return DataResult.error(() -> {
    			   return "Unknown registry element in " + registry.getRegistryKey() + ":" + object;
    		   });
    	   });
       });
    }

    public static <T> Codec<Holder<T>> holderByNameCodec(Supplier<IForgeRegistry<T>> registrySup) {
       return ResourceLocation.CODEC.flatXmap((id) -> {
    	   IForgeRegistry<T> registry = registrySup.get();
          return registry.getHolder(ResourceKey.create(registry.getRegistryKey(), id)).map(DataResult::success).orElseGet(() -> {
             return DataResult.error(() -> {
                return "Unknown registry key in " + registry.getRegistryKey() + ": " + id;
             });
          });
       }, (holder) -> {
    	   IForgeRegistry<T> registry = registrySup.get();
          return holder.unwrapKey().map(ResourceKey::location).map(DataResult::success).orElseGet(() -> {
             return DataResult.error(() -> {
                return "Unknown registry element in " + registry.getRegistryKey() + ":" + holder;
             });
          });
       });
    }

    public static <T> Codec<T> withAlternative(final Codec<T> primary, final Codec<? extends T> alternative) {
        return Codec.either(
            primary,
            alternative
        ).xmap(
        	either -> either.map(Function.identity(), Function.identity()),
            Either::left
        );
    }

    public static <T, U> Codec<T> withAlternative(final Codec<T> primary, final Codec<U> alternative, final Function<U, T> converter) {
        return Codec.either(
            primary,
            alternative
        ).xmap(
            either -> either.map(Function.identity(), converter),
            Either::left
        );
    }

    public static <K, V> Codec<Map<K, V>> dispatchedMap(final Codec<K> keyCodec, final Function<K, Codec<? extends V>> valueCodecFunction) {
        return new DispatchedMapCodec<>(keyCodec, valueCodecFunction);
    }


    static <E> Codec<E> stringResolver(final Function<E, String> toString, final Function<String, E> fromString) {
        return Codec.STRING.flatXmap(
            name -> Optional.ofNullable(fromString.apply(name)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown element name:" + name)),
            e -> Optional.ofNullable(toString.apply(e)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Element with unknown name: " + e))
        );
    }
}
