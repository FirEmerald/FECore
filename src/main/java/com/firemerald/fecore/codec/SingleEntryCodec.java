package com.firemerald.fecore.codec;

import org.apache.commons.lang3.mutable.MutableObject;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;

public record SingleEntryCodec<K, V>(Codec<K> keyCodec, Codec<V> elementCodec) implements Codec<Pair<K, V>> {
    @Override
    public <T> DataResult<Pair<Pair<K, V>, T>> decode(final DynamicOps<T> ops, final T input) {
        return ops.getMap(input).setLifecycle(Lifecycle.stable()).flatMap(map -> decode(ops, map)).map(r -> Pair.of(r, input));
    }

    public <T> DataResult<Pair<K, V>> decode(final DynamicOps<T> ops, final MapLike<T> input) {
    	final MutableObject<DataResult<Pair<K, V>>> result = new MutableObject<>();
    	input.entries().forEach(pair -> {
    		if (result.getValue() == null) {
    			keyCodec().parse(ops, pair.getFirst()).get().ifLeft(key -> {
    				elementCodec().parse(ops, pair.getSecond()).get().ifLeft(value -> {
                    	result.setValue(DataResult.success(Pair.of(key, value)));
    				}).ifRight(error -> {
                    	result.setValue(DataResult.error(error::message));
    				});
    			}).ifRight(error -> {
                	result.setValue(DataResult.error(error::message));
    			});
    		} else {
    			result.setValue(DataResult.error(() -> "Expected a single key-value pair"));
        	}
    	});
        return result.getValue();
    }

    @Override
    public <T> DataResult<T> encode(final Pair<K, V> input, final DynamicOps<T> ops, final T prefix) {
        return encode(input, ops, ops.mapBuilder()).build(prefix);
    }

    public <T> RecordBuilder<T> encode(final Pair<K, V> input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
    	prefix.add(keyCodec().encodeStart(ops, input.getFirst()), elementCodec().encodeStart(ops, input.getSecond()));
        return prefix;
    }

    @Override
    public String toString() {
        return "SingleEntryCodec[" + keyCodec + " -> " + elementCodec + ']';
    }

}
