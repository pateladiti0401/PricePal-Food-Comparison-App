package Mavericks;

import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SaveOnFoodsScraper {

    public List<String> scrape(String baseUrl, int totalPages) throws IOException {
        // Specify the folder path where you want to save the HTML files
        String folderPath = "SaveonfoodsFiles" + File.separator;

        WebDriver driver = MainScraper.setupEdgeDriver();
        List<String> filePaths = new ArrayList<>();

        try {
            for (int page_number = 1; page_number <= totalPages; ++page_number) {
                String pageUrl = baseUrl + page_number + "&skip=" + ((page_number - 1) * 48);

                // Load the page
                driver.get(pageUrl);

                // Instead of extracting, save the HTML content to a file
                String fileName = "SOFDirect_page" + page_number + ".html";
                String filePath = createFile(folderPath + fileName, driver.getPageSource());
                filePaths.add(filePath);
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

        // Quit the WebDriver
        driver.quit();

        // You can call the HTMLParser here if needed
        SaveOnFoodsHTMLParser.parseHtmlFiles(folderPath, "Saveonfoods.csv");

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
