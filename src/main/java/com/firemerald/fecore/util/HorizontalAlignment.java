package com.firemerald.fecore.util;

public enum HorizontalAlignment {
	LEFT {
		@Override
		public int getLeft(int containerWidth, int itemWidth, int offset) {
			return offset;
		}

		@Override
		public double getLeft(double containerWidth, double itemWidth, double offset) {
			return offset;
		}

		@Override
		public int getRight(int containerWidth, int itemWidth, int offset) {
			return offset + itemWidth;
		}

		@Override
		public double getRight(double containerWidth, double itemWidth, double offset) {
			return offset + itemWidth;
		}
	},
	CENTER {
		@Override
		public int getLeft(int containerWidth, int itemWidth, int offset) {
			return ((containerWidth - itemWidth) / 2) + offset;
		}

		@Override
		public double getLeft(double containerWidth, double itemWidth, double offset) {
			return ((containerWidth - itemWidth) / 2) + offset;
		}

		@Override
		public int getRight(int containerWidth, int itemWidth, int offset) {
			return ((containerWidth + itemWidth) / 2) + offset;
		}

		@Override
		public double getRight(double containerWidth, double itemWidth, double offset) {
			return ((containerWidth + itemWidth) / 2) + offset;
		}
	},
	RIGHT {
		@Override
		public int getLeft(int containerWidth, int itemWidth, int offset) {
			return containerWidth - (offset + itemWidth);
		}

		@Override
		public double getLeft(double containerWidth, double itemWidth, double offset) {
			return containerWidth - (offset + itemWidth);
		}

		@Override
		public int getRight(int containerWidth, int itemWidth, int offset) {
			return containerWidth - offset;
		}

		@Override
		public double getRight(double containerWidth, double itemWidth, double offset) {
			return containerWidth - offset;
		}
	};
	
	public abstract int getLeft(int containerWidth, int itemWidth, int offset);
	
	public abstract int getRight(int containerWidth, int itemWidth, int offset);
	
	public abstract double getLeft(double containerWidth, double itemWidth, double offset);
	
	public abstract double getRight(double containerWidth, double itemWidth, double offset);
}
