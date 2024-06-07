package Mavericks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WordCompletion {

    private static final int MAX_DISTANCE = 5; // Maximum Levenshtein distance allowed

    // File names containing words data
    private static final List<String> FILE_NAMES = Arrays.asList("kroger.csv", "Yupik.csv", "Saveonfoods.csv");

    // Method to calculate Levenshtein distance between two strings
    private static int getLevenshteinDistance(String s, String t) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        int n = s.length();
        int m = t.length();

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        int[] d = new int[m + 1];

        for (int j = 0; j <= m; j++) {
            d[j] = j;
        }

        for (int i = 1; i <= n; i++) {
            int prev = i;
            for (int j = 1; j <= m; j++) {
                int temp = d[j - 1];
                d[j - 1] = prev;
                prev = Math.min(Math.min(prev + 1, d[j] + 1), temp + (s.charAt(i - 1) == t.charAt(j - 1) ? 0 : 1));
            }
            d[m] = prev;
        }

        // Use MAX_DISTANCE here
        return d[m] <= MAX_DISTANCE ? d[m] : Integer.MAX_VALUE; // Threshold check
    }

    // Method to read titles from file and return as list of strings
    private static List<String> readTitlesFromFile(String fileName) {
        List<String> titles = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.trim().split("[^a-zA-Z]+");
                titles.addAll(Arrays.asList(words)); // Add words instead of full line
            }
        } catch (IOException e) {
            System.out.println(
                    MainScraper.ANSI_RED + "Error: File not found or cannot be accessed." + MainScraper.ANSI_RESET);
            // e.printStackTrace();
        }
        return titles;
    }

    // Method to find similar words for a given search term
    public static List<String> findSimilarWords(String searchTerm) {
        Set<String> allTitles = new HashSet<>();
        for (String fileName : FILE_NAMES) {
            allTitles.addAll(readTitlesFromFile(fileName));
        }

        // Convert set to list for sorting
        List<String> similarTitles = new ArrayList<>(allTitles);

        // Call the quickSort method to sort the list
        quickSort(similarTitles, searchTerm.toLowerCase(), 0, similarTitles.size() - 1);

        // Return only top 2 similar titles
        return similarTitles.subList(0, Math.min(similarTitles.size(), 2));
    }

    // QuickSort implementation
    private static void quickSort(List<String> similarTitles, String searchTerm, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(similarTitles, searchTerm, low, high);
            quickSort(similarTitles, searchTerm, low, pivotIndex - 1);
            quickSort(similarTitles, searchTerm, pivotIndex + 1, high);
        }
    }

    // Partition method for QuickSort
    private static int partition(List<String> similarTitles, String searchTerm, int low, int high) {
        String pivot = similarTitles.get(high); // Choose the last element as the pivot
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (getLevenshteinDistance(searchTerm, similarTitles.get(j)) <= getLevenshteinDistance(searchTerm, pivot)) {
                i++;
                // Swap similarTitles[i] and similarTitles[j]
                String temp = similarTitles.get(i);
                similarTitles.set(i, similarTitles.get(j));
                similarTitles.set(j, temp);
            }
        }
        // Swap similarTitles[i+1] and similarTitles[high] (pivot)
        String temp = similarTitles.get(i + 1);
        similarTitles.set(i + 1, similarTitles.get(high));
        similarTitles.set(high, temp);
        return i + 1;
    }
}
