package Mavericks;

import java.io.File;
import java.util.*;

public class PageRanking {

    public static void pageRank(Set<String> urls, String searchWord) {
        // Check if URLs set is empty
        if (urls.isEmpty()) {
            System.out.println("No URLs provided.");
            return;
        }
        // Priority queue to store URLs based on total frequency
        PriorityQueue<PageRankEntry> pq = new PriorityQueue<>((a, b) -> b.totalFrequency - a.totalFrequency);

        // Iterate through each URL
        for (String url : urls) {
            File file = new File(url);
            // Check if file exists
            if (!file.exists()) {
                // Print error message if file doesn't exist
                System.err.println(MainScraper.ANSI_RED + "Invalid file path: " + url + MainScraper.ANSI_RESET);
                continue;
            }
            // Calculate total frequency of search word in the file
            int totalFrequency = FrequencyCount.searchFrequency(file, searchWord);
            // Add URL and its total frequency to priority queue
            pq.add(new PageRankEntry(url, totalFrequency));
        }

        // Print page ranking for the search word
        System.out.println("\nPage ranking for'" + searchWord + "':");
        String st = "------------------------------------------------------";
        System.out.println(st);

        // Print ranked URLs and their total frequencies
        int rank = 1;
        while (!pq.isEmpty()) {
            PageRankEntry entry = pq.poll();
            System.out.println(rank + ". URL: " + entry.url
                    + ", Total Frequency: " + entry.totalFrequency);
            rank++;
        }
    }

    /**
     * Inner class representing a PageRank entry, consisting of a URL and its total
     * frequency.
     */
    private static class PageRankEntry {
        String url;
        int totalFrequency;

        public PageRankEntry(String url, int totalFrequency) {
            this.url = url;
            this.totalFrequency = totalFrequency;
        }
    }
}