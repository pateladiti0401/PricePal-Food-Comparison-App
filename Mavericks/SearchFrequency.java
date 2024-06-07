package Mavericks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

public class SearchFrequency {
    private static final String CSV_FILE_PATH = "search_frequency.csv";
    private static TreeMap<String, Integer> searchFrequencyMap = new TreeMap<>();

    public static void updateSearchFrequency(String searchWord) {
        readSearchFrequencyFromFile(); // Read data from file into searchFrequencyMap
        Integer frequency = searchFrequencyMap.get(searchWord);
        if (frequency == null) {
            frequency = 0;
        }
        searchFrequencyMap.put(searchWord, frequency + 1);
        writeSearchFrequencyToFile(); // Write data from searchFrequencyMap to file

        String st = "------------------------------------------------------";
        System.out.println("\n" + st);
        System.out.println("Search frequency updated for '" + searchWord + "'.");
        printFrequency(searchWord);
    }

    private static void printFrequency(String searchWord) {
        int frequency = searchFrequencyMap.getOrDefault(searchWord, 0);

        System.out.println("Frequency of '" + searchWord + "' is " + frequency);
    }

    private static void readSearchFrequencyFromFile() {
        searchFrequencyMap.clear(); // Clear the map before reading to avoid duplicate entries
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                searchFrequencyMap.put(parts[0], Integer.parseInt(parts[1]));
            }
        } catch (IOException e) {
            // File not found or unable to read, no need to handle for now
        }
    }

    private static void writeSearchFrequencyToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            for (String word : searchFrequencyMap.keySet()) {
                bw.write(word + "," + searchFrequencyMap.get(word));
                bw.newLine();
            }
        } catch (IOException e) {
            // Unable to write to file, no need to handle for now
        }
    }

    public static void printFrequency() {
        // Clear the existing searchFrequencyMap
        searchFrequencyMap.clear();

        // Read the CSV file again to update the searchFrequencyMap
        readSearchFrequencyFromFile();

        // Create a TreeMap with reversed order comparator to sort by value (frequency)
        TreeMap<Integer, String> sortedMap = new TreeMap<>(java.util.Collections.reverseOrder());
        for (String word : searchFrequencyMap.keySet()) {
            int frequency = searchFrequencyMap.get(word);
            sortedMap.put(frequency, word);
        }

        System.out.print(MainScraper.ANSI_YELLOW + "\nTrending Products: " + MainScraper.ANSI_RESET);
        int count = 0;
        for (Integer frequency : sortedMap.keySet()) {
            String word = sortedMap.get(frequency);
            System.out.print(word + " , ");
            count++;
            if (count >= 3) {
                break;
            }
        }
    }

}