package com.firemerald.fecore.util;

public enum VerticalAlignment {
	TOP {
		@Override
		public int getTop(int containerHeight, int itemHeight, int offset) {
			return offset;
		}

		@Override
		public double getTop(double containerHeight, double itemHeight, double offset) {
			return offset;
		}

		@Override
		public int getBottom(int containerHeight, int itemHeight, int offset) {
			return offset + itemHeight;
		}

		@Override
		public double getBottom(double containerHeight, double itemHeight, double offset) {
			return offset + itemHeight;
		}
	},
	MIDDLE {
		@Override
		public int getTop(int containerHeight, int itemHeight, int offset) {
			return ((containerHeight - itemHeight) / 2) + offset;
		}

		@Override
		public double getTop(double containerHeight, double itemHeight, double offset) {
			return ((containerHeight - itemHeight) / 2) + offset;
		}

		@Override
		public int getBottom(int containerHeight, int itemHeight, int offset) {
			return ((containerHeight + itemHeight) / 2) + offset;
		}

		@Override
		public double getBottom(double containerHeight, double itemHeight, double offset) {
			return ((containerHeight + itemHeight) / 2) + offset;
		}
	},
	BOTTOM {
		@Override
		public int getTop(int containerHeight, int itemHeight, int offset) {
			return containerHeight - (offset + itemHeight);
		}

		@Override
		public double getTop(double containerHeight, double itemHeight, double offset) {
			return containerHeight - (offset + itemHeight);
		}

		@Override
		public int getBottom(int containerHeight, int itemHeight, int offset) {
			return containerHeight - offset;
		}

		@Override
		public double getBottom(double containerHeight, double itemHeight, double offset) {
			return containerHeight - offset;
		}
	};
	
	public abstract int getTop(int containerHeight, int itemHeight, int offset);
	
	public abstract int getBottom(int containerHeight, int itemHeight, int offset);
	
	public abstract double getTop(double containerHeight, double itemHeight, double offset);
	
	public abstract double getBottom(double containerHeight, double itemHeight, double offset);
}
