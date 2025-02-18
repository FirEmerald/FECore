package com.firemerald.fecore.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

public class WithConditions<U> implements Codec<U> {

	@Override
	public <T> DataResult<T> encode(U input, DynamicOps<T> ops, T prefix) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> DataResult<Pair<U, T>> decode(DynamicOps<T> ops, T input) {
		// TODO Auto-generated method stub
		return null;
	}

}
