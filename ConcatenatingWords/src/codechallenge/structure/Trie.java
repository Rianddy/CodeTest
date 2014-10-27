package codechallenge.structure;

import codechallenge.structure.TrieNode;
import java.util.HashMap;

public class Trie {
	private TrieNode root;

	public Trie() {
		root = new TrieNode((char) 0);
	}

	public void addWord(String newWord) {
		int length = newWord.length();
		TrieNode curNode = root;
		for (int i = 0; i < length; i++) {
			char curChar = newWord.charAt(i);
			HashMap<Character, TrieNode> children = curNode.getChildren();
			if (!children.containsKey(curChar)) {
				children.put(curChar, new TrieNode(curChar));
			}
			curNode = children.get(curChar);
		}
		curNode.setIsEnd(true);
	}

	public boolean concat(String curWord) {
		int length = curWord.length();
		return concatHelper(curWord, 0, length);
	}

	/**
	 * A helper that use backtrack recursion method to judge whether the word is
	 * constructed by other words in the trie.
	 * 
	 * @param curWord
	 * @param startIndex
	 * @param length
	 * @return
	 */
	private boolean concatHelper(String curWord, int startIndex, int length) {
		if (startIndex >= length) {
			return true;
		}
		TrieNode curNode = root;
		for (int i = startIndex; i < length; i++) {
			char curChar = curWord.charAt(i);
			HashMap<Character, TrieNode> children = curNode.getChildren();
			if (children.containsKey(curChar)) {
				curNode = children.get(curChar);
				if (curNode.getIsEnd()) {
					// This is to avoid self construct
					if (startIndex == 0 && i == length - 1)
						return false;
					if (concatHelper(curWord, i + 1, length))
						return true;
				}
			} else
				return false;
		}
		return false;
	}
}
