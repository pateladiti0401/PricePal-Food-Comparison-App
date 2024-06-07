// App.java
package Mavericks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;



public class inverted_InDExInG {
    private TrieNode root;
    private Map<Integer, String> docIdToFilePath; // Map to store document IDs to file paths

    public inverted_InDExInG() {
        root = new TrieNode();
        docIdToFilePath = new HashMap<>();
    }

    public void addDocument(int docId, String filePath) {
        docIdToFilePath.put(docId, filePath);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.toLowerCase().split("\\s+");
                for (String word : words) {
                    TrieNode node = root;
                    for (char c : word.toCharArray()) {
                        if (c < 'a' || c > 'z') {
                            continue; // Skip non-lowercase letters
                        }
                        int index = c - 'a';
                        if (node.children[index] == null) {
                            node.children[index] = new TrieNode();
                        }
                        node = node.children[index];
                    }
                    try {
                        node.docIds.add(docId);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<Integer> search(String term) {
        Set<Integer> result = new HashSet<>();
        TrieNode node = root;
        for (char c : term.toLowerCase().toCharArray()) {
            if (c < 'a' || c > 'z') {
                continue; // Skip non-lowercase letters
            }
            int index = c - 'a';
            node = node.children[index];
            if (node == null) {
                return Collections.emptySet();
            }
        }
        result.addAll(node.docIds);
        return result;
    }

    public Set<String> searchUrls(List<String> urls, String word) {
        Set<String> resultUrls = new HashSet<>();
        int i = 1;
        for (String url : urls) {
            addDocument(i++, url);
        }
        Set<Integer> docIds = search(word);
        for (Integer docId : docIds) {
            if (docIdToFilePath.containsKey(docId)) {
                resultUrls.add(docIdToFilePath.get(docId));
            }
        }
        return resultUrls;
    }
    
}
