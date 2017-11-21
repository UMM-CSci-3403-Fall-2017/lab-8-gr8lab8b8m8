package mpd;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {
	public int minimum = Integer.MAX_VALUE;

	@Override
	// Finds the minimum pairwise distance in an array of ints
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
	//Used to update the global minimum
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
		// Searches the lower left section of the array from 0 ≤ j < i < N/2
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
		//Searches the bottom right section of the array from N/2 ≤ j + N/2 < i < N
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
		//Searches the top right section of the array from N/2 ≤ j < i < N
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
		//Searches the center of the array from N/2 ≤ i ≤ j + N/2 < N
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
		//Modified run function to split the threads into the ranges
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
