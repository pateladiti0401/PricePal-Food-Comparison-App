package Mavericks;

import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YupikScrapper {
    public List<String> scrape(String baseUrl, String searchTerm, int totalPages)
            throws InterruptedException, IOException {
        WebDriver driver = MainScraper.setupEdgeDriver();
        String category_new = searchTerm;
        String folderPath = "Yupik_files" + File.separator;
        List<String> filePaths = new ArrayList<>();

        try {
            for (int page_number = 1; page_number <= totalPages; ++page_number) {
                String pageUrl = baseUrl + page_number + "&q=" + category_new;
                driver.get(pageUrl);
                // Scrape the page and save the HTML content to a file
                String fileName = "Yupik_" + category_new + "_" + page_number + ".html";
                String filePath = createFile(folderPath + fileName, driver.getPageSource());
                filePaths.add(filePath);

                // Parse the HTML file to extract product information
                Yupik_HTML_Parser.parseHtmlFiles(folderPath, "Yupik.csv");
                Thread.sleep(10000);
            }
        } catch (NoSuchWindowException e) {
            System.out.println(MainScraper.ANSI_RED + "Error occurred: The target window is already closed."
                    + MainScraper.ANSI_RESET);
            // Handle the exception or take appropriate action
        } catch (IndexOutOfBoundsException e) {
            System.out.println(MainScraper.ANSI_RED + "Error occurred: " + e.getMessage() + MainScraper.ANSI_RESET);
            // Handle the exception or take appropriate action
        } catch (Exception e) {
            System.out.println(
                    MainScraper.ANSI_RED + "An unexpected error occurred: " + e.getMessage() + MainScraper.ANSI_RESET);
            // Handle the exception or take appropriate action
        }

        driver.quit(); // Close the WebDriver instance
        return filePaths;
    }

    private String createFile(String fileName, String content) {
        String filePath = fileName;

        // Create the directory if it doesn't exist
        File file = new File(fileName);
        file.getParentFile().mkdirs();

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(content);
            System.out.println("File created: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }
}
