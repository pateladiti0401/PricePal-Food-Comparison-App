package Mavericks;

import java.util.HashSet;
import java.util.Set;

class TrieNode {
    TrieNode[] children;
    Set<Integer> docIds;
	public boolean isEndOfWord;

    public TrieNode() {
        children = new TrieNode[26]; // Assuming only lowercase letters
        docIds = new HashSet<>();
    }
}
