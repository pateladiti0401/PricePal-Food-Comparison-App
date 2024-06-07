package Mavericks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CombinedTextData {

    public static void combineCSVFiles(String[] fileNames) {
        Set<String> headers = new HashSet<>();
        try (FileWriter fileWriter = new FileWriter("combined.csv")) {
            for (String fileName : fileNames) {
                File file = new File(fileName);
                if (file.exists() && file.isFile() && file.getName().endsWith(".csv")) {
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        // Skip the header if it's not the first file
                        if (!headers.isEmpty()) {
                            br.readLine();
                        }

                        String line;
                        while ((line = br.readLine()) != null) {
                            if (headers.isEmpty()) {
                                fileWriter.write(line + ",filename\n");
                                headers.add("filename");
                            } else {
                                fileWriter.write(line + "," + fileName + "\n");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("File not found: " + fileName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
