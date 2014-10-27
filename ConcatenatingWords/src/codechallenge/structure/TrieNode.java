package codechallenge.structure;

import java.util.HashMap;

public class TrieNode {
	private char value;
	private HashMap<Character, TrieNode> children;
	private boolean isEnd;

	public TrieNode(char value) {
		this.value = value;
		this.isEnd = false;
		this.children = new HashMap<Character, TrieNode>();
	}

	public void setIsEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	public boolean getIsEnd() {
		return this.isEnd;
	}

	public char getValue() {
		return this.value;
	}

	public HashMap<Character, TrieNode> getChildren() {
		return this.children;
	}
}
