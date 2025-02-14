package com.firemerald.fecore.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CollectionUtils {
	private static class Count {
		int count = 0;

		public void add() {
			count++;
		}

		public boolean remove() {
			return --count < 0;
		}

		public boolean empty() {
			return count == 0;
		}
	}

	public static boolean equalUnordered(Collection<?> a, Collection<?> b) {
		if (a == b) return true;
		else if (a == null) return b == null;
		else if (b == null || a.size() != b.size()) return false;
		else {
			Map<Object, Count> counts = new HashMap<>();
			for (Object aObj : a) counts.computeIfAbsent(aObj, a2 -> new Count()).add();
			for (Object bObj : b) if (!counts.containsKey(bObj) || counts.get(bObj).remove()) return false;
			return counts.values().stream().allMatch(Count::empty);
		}
	}
}
