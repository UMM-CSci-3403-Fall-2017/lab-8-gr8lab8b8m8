package mpd;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {
	public int minimum = Integer.MAX_VALUE;

	@Override
	public int minimumPairwiseDistance(int[] values) {
		RangeSplitter[] splitters = new RangeSplitter[4];

		for (int i = 0; i < 4; i++) {
			splitters[i] = new RangeSplitter(values, i);
			splitters[i].start();
		}

		for (int i = 0; i < 4; i++) {
			try {
				splitters[i].join();
			} catch (InterruptedException e) {
				System.out.println("oops something wierd happened");
				e.printStackTrace();
			}
		}
		return minimum;
	}

	public synchronized void updateMinimum(int threadMin) {
		if (threadMin < this.minimum) {
			this.minimum = threadMin;
		}
	}

	private class RangeSplitter extends Thread {
		private int[] values;
		private int length;
		private int mode;

		public RangeSplitter(int[] arr, int mode) {
			this.values = arr;
			this.length = arr.length;
			this.mode = mode;
		}

		private int lowerLeft() {
			int result = Integer.MAX_VALUE;
			for (int i = 1; i < this.length / 2; i++) {
				for (int j = 0; j < i; j++) {
					int diff = Math.abs(this.values[i] - this.values[j]);
					if(diff < result){
						result = diff;
					}
				}
			}
			
			return result;
		}

		private int bottomRight() {
			int result = Integer.MAX_VALUE;
			for (int i = this.length / 2 + 1; i < this.length; i++) {
				for (int j = 0; j + this.length / 2 < i; j++) {
					int diff = Math.abs(this.values[i] - this.values[j]);
					if(diff < result){
						result = diff;
					}
				}
			}
			
			return result;
		}

		private int topRight() {
			int result = Integer.MAX_VALUE;
			for (int i = this.length / 2 + 1; i < this.length; i++) {
				for (int j = this.length / 2; j < i; j++) {
					int diff = Math.abs(this.values[i] - this.values[j]);
					if(diff < result){
						result = diff;
					}
				}
			}
			
			return result;
		}

		private int center() {
			int result = Integer.MAX_VALUE;
			for (int j = 0; j + this.length / 2 < this.length; j++) {
				for (int i = this.length / 2; i <= j + this.length/2; i++) {
					int diff = Math.abs(this.values[i] - this.values[j]);
					if(diff < result){
						result = diff;
					}
				}
			}
			
			return result;
		}

		public void run() {
			if (this.mode == 0) {
				System.out.println("starting lower left");
				updateMinimum(lowerLeft());
			} else if (this.mode == 1) {
				System.out.println("starting bottom right");
				updateMinimum(bottomRight());
			} else if (this.mode == 2) {
				System.out.println("starting top right");
				updateMinimum(topRight());
			} else {
				System.out.println("starting center");
				updateMinimum(center());
			}
		}
	}
}
