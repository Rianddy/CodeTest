package codechallenge.constants;

import java.util.Comparator;

public class Comparators {
	public static class MinHeapComparator implements Comparator<String> {

		@Override
		public int compare(String left, String right) {
			return left.length() - right.length();
		}
	}
}
