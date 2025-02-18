package com.firemerald.fecore.codec;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.firemerald.fecore.distribution.DistributionUtil;
import com.firemerald.fecore.distribution.IDistribution;
import com.firemerald.fecore.distribution.SingletonUnweightedDistribution;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

public class DistributionCodec<S> implements Codec<IDistribution<S>> {
	public final Codec<S> singletonUnweightedCodec;
	public final Codec<List<S>> listCodec;
	public final Codec<Map<S, Float>> mapCodec;

	public DistributionCodec(Codec<S> keyCodec) {
		this.singletonUnweightedCodec = keyCodec;
		listCodec = keyCodec.listOf();
		mapCodec = Codec.unboundedMap(keyCodec, Codec.FLOAT);
	}

	@Override
	public <T> DataResult<T> encode(IDistribution<S> input, DynamicOps<T> ops, T prefix) {
		if (input.isEmpty()) return listCodec.encode(Collections.emptyList(), ops, prefix);
		else if (input.hasWeights()) return mapCodec.encode(input.getWeightedValues(), ops, prefix);
		else if (input.collectionSize() == 1) return singletonUnweightedCodec.encode(input.getUnweightedValues().get(0), ops, prefix);
		else return listCodec.encode(input.getUnweightedValues(), ops, prefix);
	}

	@Override
	public <T> DataResult<Pair<IDistribution<S>, T>> decode(DynamicOps<T> ops, T input) {
		DataResult<Pair<S, T>> singletonUnweightedResult = singletonUnweightedCodec.decode(ops, input);
		if (Codecs.isSuccess(singletonUnweightedResult)) return Codecs.mapResult(singletonUnweightedResult, SingletonUnweightedDistribution::new);
		DataResult<Pair<List<S>, T>> listResult = listCodec.decode(ops, input);
		if (Codecs.isSuccess(listResult)) return Codecs.mapResult(listResult, DistributionUtil::get);
		DataResult<Pair<Map<S, Float>, T>> mapResult = mapCodec.decode(ops, input);
		if (Codecs.isSuccess(mapResult)) return Codecs.mapResult(mapResult, DistributionUtil::get);
		return DataResult.error(() -> "Could not parse distribution: Expected a String, List of Strings, or Map of Strings to Floats");
	}
}
