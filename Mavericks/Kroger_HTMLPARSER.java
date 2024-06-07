package Mavericks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Kroger_HTMLPARSER {
    public static void parseHtmlFiles(String folderPath, String outputPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        Boolean headerWritten = false;
        if (files != null) {
            try (FileWriter fileWriter = new FileWriter(outputPath, true)) { // Append mode

                File outputFile = new File(outputPath);
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
                        parseHtml(filePath, fileWriter);
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

    private static void parseHtml(String filePath, FileWriter fileWriter) {
        try {
            // Parse HTML document
            Document document = Jsoup.parse(new File(filePath), "UTF-8");
            Elements dataElements = document.select("data[data-qa=cart-page-item-unit-price]");
            Elements titleElements = document.select("span[data-qa=cart-page-item-description]");
            Elements descriptionElements = document.select("span[data-qa=cart-page-item-sizing]");

            for (int i = 0; i < dataElements.size(); i++) {
                String price = dataElements.get(i).attr("value");
                String title = titleElements.get(i).text();
                String description = descriptionElements.size() > i ? descriptionElements.get(i).text() : "each";
                fileWriter.write("\"" + title + "\",\"" + description + "\",\"" + price + "\"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
