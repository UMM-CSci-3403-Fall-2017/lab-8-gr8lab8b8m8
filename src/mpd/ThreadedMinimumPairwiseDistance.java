package mpd;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {
	private int minimum = Integer.MAX_VALUE;

	@Override
	public int minimumPairwiseDistance(int[] values) {
		RangeSplitter lowerLeft = new RangeSplitter(values, 0, values.length/2);
		RangeSplitter bottomRight = new RangeSplitter(values, values.length/2, values.length);
		return minimum;
	}

	public synchronized void updateMinimum(int threadMin) {
		if (threadMin < this.minimum) {
			this.minimum = threadMin;
		}
	}

	public class RangeSplitter extends Thread {
		private int minimum = Integer.MAX_VALUE;
		private int[] arr;
		private int start;
		private int end;

		public RangeSplitter(int[] arr, int i, int j) {
			this.arr = arr;
			if(j < i){
				this.start = j;
				this.end = i;
			}else{
				this.start = i;
				this.end = j;
			}
		}

		public void run() {
			for (int i = this.start; i < this.end; i++) {
				for (int j = this.start; j < this.end; j++) {
					updateMinimum(Math.abs(arr[i] - arr[j]));
				}
			}
		}
	}
}
