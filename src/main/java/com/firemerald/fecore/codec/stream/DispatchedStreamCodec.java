package com.firemerald.fecore.codec.stream;

import java.util.function.Function;

import net.minecraft.network.FriendlyByteBuf;

public record DispatchedStreamCodec<T, U>(StreamCodec<T> codec, Function<? super U, ? extends T> keyGetter, Function<? super T, ? extends StreamCodec<? extends U>> codecGetter) implements StreamCodec<U> {
    @Override
    public U decode(FriendlyByteBuf buf) {
        T obj = codec.decode(buf);
        StreamCodec<? extends U> streamcodec = codecGetter.apply(obj);
        return streamcodec.decode(buf);
    }

    @Override
    public void encode(FriendlyByteBuf buf, U object) {
        T obj = keyGetter.apply(object);
        @SuppressWarnings("unchecked")
		StreamCodec<U> streamcodec = (StreamCodec<U>) codecGetter.apply(obj);
        codec.encode(buf, obj);
        streamcodec.encode(buf, object);
    }
}
