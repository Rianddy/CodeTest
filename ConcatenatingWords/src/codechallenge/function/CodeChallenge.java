package codechallenge.function;

import codechallenge.constants.Comparators;
import codechallenge.structure.Trie;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;

/***
 * 
 * I used structure Trie to store all words so that it will save a lot of space.
 * And I also used PriorityQueue to show Nth longest words made of other words.
 * Assume the word average length is k and the number of words is n. Then the
 * time complexity to build Trie will be O(nk). Then I used a recursion method
 * to judge whether a word can be constructed by other words. In the worst case
 * here, a word need O(k^2) because the program may go into recursion at every
 * character if the character is a word in that file. So the final time
 * complexity of my solution is O(nk^2+nk)=O(n).
 * 
 * As to space, trie really helped compressed the space of storing words. Though
 * I used the recursion method, the max stack used to check a word will not be
 * larger than k^2. So this solution saved a lot of space.
 * 
 * @author rianddy
 * 
 */
public class CodeChallenge {

	public static void main(String[] args) {

		if (args == null || args.length < 1) {
			System.out.println("Usage:");
			System.out.println("  args[0]: path of the test file.");
			System.exit(0);
		}

		String path_input = args[0];
		FileInputStream instream_collection = null;
		Trie trie = new Trie();
		try {
			// Loading the collection file
			instream_collection = new FileInputStream(path_input);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					instream_collection));

			// Reading File line by line using BufferedReader
			String line = reader.readLine();
			while (line != null) {
				// Get words and store them into the trie
				trie.addWord(line);
				line = reader.readLine();
			}

			// You can change the capacity to N to get first N longest concat
			// words
			PriorityQueue<String> pq = new PriorityQueue<String>(2,
					new Comparators.MinHeapComparator());

			// Reread the file
			instream_collection.getChannel().position(0);
			reader = new BufferedReader(new InputStreamReader(
					instream_collection));
			line = reader.readLine();

			// Count how many words can be constructed by other words
			int count = 0;
			while (line != null) {
				if (trie.concat(line)) {
					if (pq.size() < 2)
						pq.add(line);
					else if (line.length() > pq.peek().length()) {
						pq.poll();
						pq.add(line);
					}
					count++;
				}
				line = reader.readLine();
			}

			// Close the BufferReader
			reader.close();

			// Print the result
			String secondLongest = pq.poll();
			String firstLongest = pq.poll();
			System.out.println(firstLongest);
			System.out.println(secondLongest);
			System.out.println(count);
		} catch (IOException e) {
			System.out.println("ERROR: cannot load test file.");
			e.printStackTrace();
		}
	}
}
