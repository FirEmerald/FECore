package com.firemerald.fecore.codec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.firemerald.fecore.distribution.EmptyDistribution;
import com.firemerald.fecore.distribution.IDistribution;
import com.firemerald.fecore.distribution.SingletonUnweightedDistribution;
import com.firemerald.fecore.distribution.SingletonWeightedDistribution;
import com.firemerald.fecore.distribution.UnweightedDistribution;
import com.firemerald.fecore.distribution.WeightedDistribution;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class DistributionStreamCodec<B extends ByteBuf, S> implements StreamCodec<B, IDistribution<S>> {
	public final StreamCodec<B, S> keyCodec;

	public DistributionStreamCodec(StreamCodec<B, S> keyCodec) {
		this.keyCodec = keyCodec;
	}

	public static final int
	FLAG_WEIGHTED = 1 << 0;

	@Override
	public void encode(B buffer, IDistribution<S> value) {
		int encodeSize = value.collectionSize() << 1;
		if (value.hasWeights()) encodeSize |= FLAG_WEIGHTED;
		ByteBufCodecs.writeCount(buffer, encodeSize, Integer.MAX_VALUE);
		if (value.hasWeights()) {
			value.getWeightedValues().forEach((key, val) -> {
				keyCodec.encode(buffer, key);
				ByteBufCodecs.FLOAT.encode(buffer, val);
			});
		} else {
			value.getUnweightedValues().forEach(val -> {
				keyCodec.encode(buffer, val);
			});
		}
	}

	@Override
	public IDistribution<S> decode(B buffer) {
		int size = ByteBufCodecs.readCount(buffer, Integer.MAX_VALUE);
		boolean hasWeights = (size & FLAG_WEIGHTED) != 0;
		size >>>= 1;
		if (size == 0) return EmptyDistribution.get();
		else if (size == 1) {
			if (hasWeights) return new SingletonWeightedDistribution<>(keyCodec.decode(buffer), ByteBufCodecs.FLOAT.decode(buffer));
			else return new SingletonUnweightedDistribution<>(keyCodec.decode(buffer));
		} else {
			if (hasWeights) {
				Map<S, Float> values = new HashMap<>(size);
				for (int i = 0; i < size; ++i) values.put(keyCodec.decode(buffer), ByteBufCodecs.FLOAT.decode(buffer));
				return new WeightedDistribution<>(values);
			}
			else {
				List<S> values = new ArrayList<>(size);
				for (int i = 0; i < size; ++i) values.add(keyCodec.decode(buffer));
				return new UnweightedDistribution<>(values);
			}
		}
	}
}
