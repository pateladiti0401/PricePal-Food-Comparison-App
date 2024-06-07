package Mavericks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class spell_checker {

    private TrieNode root;

    // Constructor to initialize the root of the Trie
    public spell_checker() {
        this.root = new TrieNode();
    }

    // Inserts a word into the Trie
    private void insert(String word) {
        TrieNode current = root;
        // Traverse through each character in the word
        for (char c : word.toCharArray()) {
            // Skip characters that are not lowercase letters
            if (!Character.isLowerCase(c)) {
                continue;
            }
            // Calculate the index of the character in the TrieNode array
            int index = c - 'a';
            // Skip characters outside the range of letters
            if (index < 0 || index >= 26) {
                continue;
            }
            // Create a new TrieNode if the current character's node is null
            if (current.children[index] == null) {
                current.children[index] = new TrieNode();
            }
            // Move to the next node
            current = current.children[index];
        }
        // Mark the end of the word
        current.isEndOfWord = true;
    }

    // Reads titles from a CSV file and constructs Trie
    public void constructTrieFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Read each line from the file
            while ((line = br.readLine()) != null) {
                // Split the line by comma to get words
                String[] parts = line.split(",");
                // Insert each word into the Trie
                for (String word : parts[0].split("\\s+")) {
                    insert(word.toLowerCase()); // Convert to lowercase for consistency
                }
            }
        } catch (IOException e) {
            // Handle file not found or inaccessible error
            System.out.println(
                    MainScraper.ANSI_RED + "Error: File not found or cannot be accessed." + MainScraper.ANSI_RESET);
        }
    }

    // Checks if a word is spelled correctly
    public boolean isSpelledCorrectly(String word) {
        TrieNode current = root;
        // Traverse through each character in the word
        for (char c : word.toLowerCase().toCharArray()) {
            // Calculate the index of the character in the TrieNode array
            int index = c - 'a';
            // If the character's node is null, the word is misspelled
            if (current.children[index] == null) {
                return false;
            }
            // Move to the next node
            current = current.children[index];
        }
        // Check if the last node represents the end of a valid word
        return current.isEndOfWord;
    }

}
