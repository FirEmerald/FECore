package com.firemerald.fecore.codec.stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import org.joml.Vector3d;

import com.firemerald.fecore.codec.stream.composite.Composite1StreamCodec;
import com.firemerald.fecore.codec.stream.composite.Composite2StreamCodec;
import com.firemerald.fecore.codec.stream.composite.Composite3StreamCodec;
import com.firemerald.fecore.codec.stream.composite.Composite4StreamCodec;
import com.firemerald.fecore.codec.stream.composite.Composite5StreamCodec;
import com.firemerald.fecore.codec.stream.composite.Composite6StreamCodec;
import com.firemerald.fecore.codec.stream.composite.Composite7StreamCodec;
import com.firemerald.fecore.codec.stream.composite.Composite8StreamCodec;
import com.firemerald.fecore.util.function.OctaFunction;
import com.firemerald.fecore.util.function.PentaFunction;
import com.firemerald.fecore.util.function.QuadFunction;
import com.firemerald.fecore.util.function.SeptaFunction;
import com.firemerald.fecore.util.function.SexaFunction;
import com.firemerald.fecore.util.function.TriFunction;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public interface StreamCodec<T> {
	public static final StreamCodec<Boolean> BOOL = of(FriendlyByteBuf::readBoolean, FriendlyByteBuf::writeBoolean);
	public static final StreamCodec<Integer> INT = of(FriendlyByteBuf::readInt, FriendlyByteBuf::writeInt);
	public static final StreamCodec<Integer> VAR_INT = of(FriendlyByteBuf::readVarInt, FriendlyByteBuf::writeVarInt);
	public static final StreamCodec<Long> LONG = of(FriendlyByteBuf::readLong, FriendlyByteBuf::writeLong);
	public static final StreamCodec<Float> FLOAT = of(FriendlyByteBuf::readFloat, FriendlyByteBuf::writeFloat);
	public static final StreamCodec<Double> DOUBLE = of(FriendlyByteBuf::readDouble, FriendlyByteBuf::writeDouble);
	public static final StreamCodec<String> STRING = of(FriendlyByteBuf::readUtf, FriendlyByteBuf::writeUtf);
	public static final StreamCodec<ResourceLocation> RL = of(FriendlyByteBuf::readResourceLocation, FriendlyByteBuf::writeResourceLocation);
    public static final StreamCodec<Vector3d> VECTOR3D = composite(
    		DOUBLE, Vector3d::x,
    		DOUBLE, Vector3d::y,
    		DOUBLE, Vector3d::z,
    		Vector3d::new
    		);
    public static final StreamCodec<Component> COMPONENT = of(FriendlyByteBuf::readComponent, FriendlyByteBuf::writeComponent);

	public static StreamCodec<String> ofString(int maxLength) {
		return of(buf -> buf.readUtf(maxLength), (buf, str) -> buf.writeUtf(str, maxLength));
	}

	public static <T> StreamCodec<T> of(FriendlyByteBuf.Reader<T> reader, FriendlyByteBuf.Writer<T> writer) {
		return new WrapperStreamCodec<>(reader, writer);
	}

	public static <T, U> StreamCodec<Map<T, U>> mapOf(StreamCodec<T> keyCodec, StreamCodec<U> valCodec) {
		return new MapStreamCodec<>(keyCodec, valCodec);
	}

	public static <T, U, V extends Map<T, U>> StreamCodec<V> mapOf(IntFunction<? extends V> newMap, StreamCodec<T> keyCodec, StreamCodec<U> valCodec) {
		return new MapStreamCodec<>(newMap, keyCodec, valCodec);
	}

	public static <T> StreamCodec<T> unit(T constant) {
		return new UnitStreamCodec<>(constant);
	}

	public static <T, A> StreamCodec<T> composite(StreamCodec<A> codecA, Function<T, A> getA, Function<A, T> construct) {
		return new Composite1StreamCodec<>(
				codecA, getA,
				construct);
	}

	public static <T, A, B> StreamCodec<T> composite(
			StreamCodec<A> codecA, Function<T, A> getA,
			StreamCodec<B> codecB, Function<T, B> getB,
			BiFunction<A, B, T> construct) {
		return new Composite2StreamCodec<>(
				codecA, getA,
				codecB, getB,
				construct);
	}

	public static <T, A, B, C> StreamCodec<T> composite(
			StreamCodec<A> codecA, Function<T, A> getA,
			StreamCodec<B> codecB, Function<T, B> getB,
			StreamCodec<C> codecC, Function<T, C> getC,
			TriFunction<A, B, C, T> construct) {
		return new Composite3StreamCodec<>(
				codecA, getA,
				codecB, getB,
				codecC, getC,
				construct);
	}

	public static <T, A, B, C, D> StreamCodec<T> composite(
			StreamCodec<A> codecA, Function<T, A> getA,
			StreamCodec<B> codecB, Function<T, B> getB,
			StreamCodec<C> codecC, Function<T, C> getC,
			StreamCodec<D> codecD, Function<T, D> getD,
			QuadFunction<A, B, C, D, T> construct) {
		return new Composite4StreamCodec<>(
				codecA, getA,
				codecB, getB,
				codecC, getC,
				codecD, getD,
				construct);
	}

	public static <T, A, B, C, D, E> StreamCodec<T> composite(
			StreamCodec<A> codecA, Function<T, A> getA,
			StreamCodec<B> codecB, Function<T, B> getB,
			StreamCodec<C> codecC, Function<T, C> getC,
			StreamCodec<D> codecD, Function<T, D> getD,
			StreamCodec<E> codecE, Function<T, E> getE,
			PentaFunction<A, B, C, D, E, T> construct) {
		return new Composite5StreamCodec<>(
				codecA, getA,
				codecB, getB,
				codecC, getC,
				codecD, getD,
				codecE, getE,
				construct);
	}

	public static <T, A, B, C, D, E, F> StreamCodec<T> composite(
			StreamCodec<A> codecA, Function<T, A> getA,
			StreamCodec<B> codecB, Function<T, B> getB,
			StreamCodec<C> codecC, Function<T, C> getC,
			StreamCodec<D> codecD, Function<T, D> getD,
			StreamCodec<E> codecE, Function<T, E> getE,
			StreamCodec<F> codecF, Function<T, F> getF,
			SexaFunction<A, B, C, D, E, F, T> construct) {
		return new Composite6StreamCodec<>(
				codecA, getA,
				codecB, getB,
				codecC, getC,
				codecD, getD,
				codecE, getE,
				codecF, getF,
				construct);
	}

	public static <T, A, B, C, D, E, F, G> StreamCodec<T> composite(
			StreamCodec<A> codecA, Function<T, A> getA,
			StreamCodec<B> codecB, Function<T, B> getB,
			StreamCodec<C> codecC, Function<T, C> getC,
			StreamCodec<D> codecD, Function<T, D> getD,
			StreamCodec<E> codecE, Function<T, E> getE,
			StreamCodec<F> codecF, Function<T, F> getF,
			StreamCodec<G> codecG, Function<T, G> getG,
			SeptaFunction<A, B, C, D, E, F, G, T> construct) {
		return new Composite7StreamCodec<>(
				codecA, getA,
				codecB, getB,
				codecC, getC,
				codecD, getD,
				codecE, getE,
				codecF, getF,
				codecG, getG,
				construct);
	}

	public static <T, A, B, C, D, E, F, G, H> StreamCodec<T> composite(
			StreamCodec<A> codecA, Function<T, A> getA,
			StreamCodec<B> codecB, Function<T, B> getB,
			StreamCodec<C> codecC, Function<T, C> getC,
			StreamCodec<D> codecD, Function<T, D> getD,
			StreamCodec<E> codecE, Function<T, E> getE,
			StreamCodec<F> codecF, Function<T, F> getF,
			StreamCodec<G> codecG, Function<T, G> getG,
			StreamCodec<H> codecH, Function<T, H> getH,
			OctaFunction<A, B, C, D, E, F, G, H, T> construct) {
		return new Composite8StreamCodec<>(
				codecA, getA,
				codecB, getB,
				codecC, getC,
				codecD, getD,
				codecE, getE,
				codecF, getF,
				codecG, getG,
				codecH, getH,
				construct);
	}

	public static <T> StreamCodec<T> registry(Registry<T> registry) {
		return INT.map(registry::byIdOrThrow, registry::getId);
	}

	public static <T> StreamCodec<T> registry(Supplier<IForgeRegistry<T>> registry) {
		return RL.map(id -> registry.get().getValue(id), obj -> registry.get().getKey(obj));
	}

	public T decode(FriendlyByteBuf buf);

	public void encode(FriendlyByteBuf buf, T object);

	public default FriendlyByteBuf.Reader<T> reader() {
		return this::decode;
	}

	public default FriendlyByteBuf.Writer<T> writer() {
		return this::encode;
	}

	public default <U> StreamCodec<U> map(Function<T, U> from, Function<U, T> to) {
		return new MappedStreamCodec<>(this, from, to);
	}

	public default <U> StreamCodec<U> dispatch(Function<? super U, ? extends T> keyGetter, Function<? super T, ? extends StreamCodec<? extends U>> codecGetter) {
		return new DispatchedStreamCodec<>(this, keyGetter, codecGetter);
    }

	public default StreamCodec<List<T>> asList() {
		return asCollection(ArrayList::new);
	}

	public default <U extends Collection<T>> StreamCodec<U> asCollection(IntFunction<U> newCollection) {
		return new CollectionStreamCodec<>(this, newCollection);
	}

	public default StreamCodec<T[]> asArray(IntFunction<T[]> newArray) {
		return new ArrayStreamCodec<>(this, newArray);
	}
}
