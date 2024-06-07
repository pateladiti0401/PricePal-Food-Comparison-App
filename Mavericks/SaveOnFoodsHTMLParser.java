package Mavericks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;

public class SaveOnFoodsHTMLParser {

    public static void parseHtmlFiles(String folderPath, String outputPat) {

        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        Boolean headerWritten = false;
        if (files != null) {
            try (FileWriter fileWriter = new FileWriter(outputPat, true)) { // Append mode

                File outputFile = new File(outputPat);
                if (outputFile.exists() && outputFile.length() > 0) {
                    headerWritten = true; // Set the flag to true if the file already contains data
                }

                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".html")) {
                        // Parse HTML content
                        String filePath = file.getAbsolutePath();
                        if (!headerWritten) {
                            fileWriter.write("Title,Weight,Price\n");
                            headerWritten = true; // Set the flag to true after writing the header
                        }
                        processHtmlFile(filePath, fileWriter);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(
                    MainScraper.ANSI_RED + "No files found in the folder: " + folderPath + MainScraper.ANSI_RESET);
        }
    }

    private static void processHtmlFile(String filePath, FileWriter writer) {
        try {
            // Parse HTML document
            Document doc = Jsoup.parse(new File(filePath), "UTF-8");

            // Select all elements matching the CSS selector
            Elements elements = doc.select("div.sc-dlnjPT.cuIYFB");
            Elements price = doc.select("span.ProductCardPrice--xq2y7a.ihyHsI, span.ProductCardPrice--xq2y7a.jzIfcR");
            int price_length = price.size();
            // Iterate over the selected elements
            for (int i = 0; i < elements.size(); i++) {
                // Extract data from each element and write it to the file
                String data = elements.get(i).text(); // Change this as per your requirement

                if (i < price_length) {
                    if (data.endsWith("Open product description")) {
                        data = data.substring(0, data.length() - "Open product description".length()).trim();
                    }
                    String pricew = price.get(i).text();
                    // Extract numerical part of the price (remove dollar sign)
                    String priceValue = pricew.replaceAll("[^0-9.]", "");

                    // Split data into title and weight by dividing comma
                    String[] parts = data.split(",");
                    String title = parts[0].trim();
                    String weight = parts.length > 1 ? parts[1].trim() : ""; // Handle case where weight is not present

                    writer.write("\"" + title + "\",\"" + weight + "\",\"" + priceValue + "\"");
                    writer.write(System.lineSeparator()); // Add newline after each data entry
                } else {
                    if (data.endsWith("Open product description")) {
                        data = data.substring(0, data.length() - "Open product description".length()).trim();
                    }
                    writer.write("\"" + data + "\",\"");
                    writer.write(System.lineSeparator()); // Add newline after each data entry
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
