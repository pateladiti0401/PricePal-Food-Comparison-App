package Mavericks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Yupik_HTML_Parser {

    public static void parseHtmlFiles(String folderPath, String outputPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            boolean headerWritten = false;

            try (FileWriter fileWriter = new FileWriter(outputPath, true)) { // Append mode
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".html")) {
                        // Parse HTML content
                        String filePath = file.getAbsolutePath();

                        // Write CSV header if it hasn't been written yet
                        if (!headerWritten) {
                            fileWriter.write("Title,weight,Price\n");
                            headerWritten = true;
                        }

                        parseHtml(filePath, fileWriter);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void parseHtml(String filePath, FileWriter fileWriter) {
        try {
            // Parse HTML document
            Document document = Jsoup.parse(new File(filePath), "UTF-8");

            Elements titleElements = document.select("a.product-item-link");
            Elements priceElements = document.select("span.price");

            int maxSize = Math.min(titleElements.size(), priceElements.size());
            for (int i = 0; i < maxSize; i++) {
                String title = titleElements.get(i).text().replaceAll("[^a-zA-Z0-9\\s]", ""); // Remove special
                                                                                              // characters except
                                                                                              // spaces
                String priceText = priceElements.get(i).text();
                String price = "";

                // Extract the price
                Pattern pattern = Pattern.compile("CA\\$(\\d+(\\.\\d+)?)");
                Matcher matcher = pattern.matcher(priceText);
                if (matcher.find()) {
                    price = matcher.group(1);
                }

                // Remove the "$" sign from the extracted price
                price = price.replace("$", "");

                if (!title.isEmpty() && !price.isEmpty()) {
                    fileWriter.write("\"" + title + "\",,\"" + price + "\"\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
