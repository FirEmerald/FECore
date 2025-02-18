package com.firemerald.fecore.codec.stream;

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

import net.minecraft.network.FriendlyByteBuf;

public class DistributionStreamCodec<S> implements StreamCodec<IDistribution<S>> {
	public final StreamCodec<S> keyCodec;

	public DistributionStreamCodec(StreamCodec<S> keyCodec) {
		this.keyCodec = keyCodec;
	}

	public static final int
	FLAG_WEIGHTED = 1 << 0;

	@Override
	public void encode(FriendlyByteBuf buffer, IDistribution<S> value) {
		int encodeSize = value.collectionSize() << 1;
		if (value.hasWeights()) encodeSize |= FLAG_WEIGHTED;
		buffer.writeVarInt(encodeSize);
		if (value.hasWeights()) {
			value.getWeightedValues().forEach((key, val) -> {
				keyCodec.encode(buffer, key);
				buffer.writeFloat(val);
			});
		} else {
			value.getUnweightedValues().forEach(val -> {
				keyCodec.encode(buffer, val);
			});
		}
	}

	@Override
	public IDistribution<S> decode(FriendlyByteBuf buffer) {
		int size = buffer.readVarInt();
		boolean hasWeights = (size & FLAG_WEIGHTED) != 0;
		size >>>= 1;
		if (size == 0) return EmptyDistribution.get();
		else if (size == 1) {
			if (hasWeights) return new SingletonWeightedDistribution<>(keyCodec.decode(buffer), buffer.readFloat());
			else return new SingletonUnweightedDistribution<>(keyCodec.decode(buffer));
		} else {
			if (hasWeights) {
				Map<S, Float> values = new HashMap<>(size);
				for (int i = 0; i < size; ++i) values.put(keyCodec.decode(buffer), buffer.readFloat());
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
