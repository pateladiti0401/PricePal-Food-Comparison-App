package Mavericks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class FrequencyCount {

    public static int searchFrequency(File file, String searchWord) {
        int frequency = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Remove double quotes from splitting characters
                String[] parts = line.split("[\\s,\\-\\$\"]+"); // Add any special characters you want to use as
                                                                // delimiters
                for (String part : parts) {
                    // Convert the word to lowercase for case-insensitive comparison
                    String lowerCaseWord = part.trim().toLowerCase();
                    // Check for both singular and plural forms
                    if (lowerCaseWord.equals(searchWord) || lowerCaseWord.equals(searchWord + "s")) {
                        frequency++;
                        // Write the word to a new file

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return frequency;
    }

    public static void frequency(String wordToSearch, Set<String> filePaths) {
        int totalFrequency = 0;

        for (String filePath : filePaths) {
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("Invalid file path: " + filePath);
                continue;
            }
            int frequency = searchFrequency(file, wordToSearch);
            if (frequency > 0) { // Check if frequency is not null
                totalFrequency += frequency;
                System.out.println("Frequency of " + wordToSearch + " in " + file.getName() + " : " + frequency);
                // Write the results to a new file
            } else {
                System.out.println("Word not found in the file -> " + file.getName());
            }
        }
        if (totalFrequency > 0) {
            System.out.println(MainScraper.ANSI_YELLOW + "Total frequency of " + wordToSearch + " : " + totalFrequency
                    + MainScraper.ANSI_RESET);
        } else {
            System.out.println("The word '" + wordToSearch + "' was not found in stored data.");
        }
    }
}