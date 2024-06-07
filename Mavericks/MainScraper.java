package Mavericks;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class MainScraper {

    // ANSI escape codes for colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_MAGENTA = "\u001B[35m";

    public static void main(String[] args) throws Exception {
        Scanner inp = new Scanner(System.in);

        System.out.println("\n*************************************************************");
        System.out.println("* " + ANSI_BOLD + ANSI_MAGENTA + "      W e l c o m e   t o   M a v e r i c k s "
                + ANSI_RESET + "           *");
        System.out.println(
                "*   " + ANSI_BOLD + ANSI_MAGENTA + "      -   F o o d   P r i c e   M a t c h e r   -   " + ANSI_RESET
                        + "   *");
        System.out.println("*************************************************************");

        boolean performScraping = getYesNoInput(ANSI_CYAN + "\nWeb Scraping Options" + ANSI_RESET, inp);
        List<String> filePaths = new ArrayList<>();
        String st = "------------------------------------------------------";
        if (performScraping) {
            // Hashtable<String, String> url_Map = new Hashtable<String, String>();

            // Display category options
            System.out.println("\n" + st);
            System.out.println(ANSI_YELLOW + "List of categories" + ANSI_RESET);
            System.out.println(st);

            List<String> categories = Arrays.asList("Dairy", "Vegetables", "Meat", "Fruits", "Bakery", "Beverages",
                    "Snacks", "Frozen", "Pantry", "Canned", "Breakfast");
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i));
            }

            int categoryNumber = readCategoryNumberInput(inp, categories.size());

            String category = categories.get(categoryNumber - 1);
            int totalpages = readIntegerInputoftotalpages(inp);

            removeFiles("Kroger_Files");
            removeFiles("Yupik_files");
            removeFiles("SaveonfoodsFiles");

            YupikScrapper YupikScrapper = new YupikScrapper();
            List<String> YUPIKfilepath = YupikScrapper.scrape(
                    "https://yupik.com/en/catalogsearch/result/index/?p=", category, totalpages);
            filePaths.addAll(YUPIKfilepath);

            Kroger_Scrapper KrogerScraper = new Kroger_Scrapper();
            List<String> KrogerScraperFilePaths = KrogerScraper.scrape(
                    "https://www.kroger.com/search?query=" + category + "&searchType=default_search&page=",
                    totalpages);
            filePaths.addAll(KrogerScraperFilePaths);

            SaveOnFoodsScraper saveOnFoodsScraper = new SaveOnFoodsScraper();
            List<String> saveOnFoodsFilePaths = saveOnFoodsScraper
                    .scrape("https://www.saveonfoods.com/sm/pickup/rsid/1982/results?q=" + category + "&page=",
                            totalpages);
            filePaths.addAll(saveOnFoodsFilePaths);

            // Print all file paths
            System.out.println("\n" + st);
            System.out.println(ANSI_YELLOW + "All file paths:" + ANSI_RESET);
            System.out.println(st);

            for (String filePath : filePaths) {
                System.out.println(filePath);
            }
        }
        boolean isValidInputValidation = getYesNoInput(ANSI_CYAN + "\nValidate data" + ANSI_RESET, inp);

        if (isValidInputValidation) {
            SimpleDataValidation.validateData();
            System.out.println(ANSI_GREEN + "Validation done.." + ANSI_RESET);
        } else {
            System.out.println("Validation skipped...");
        }
        boolean repeat = true;
        while (repeat) {
            String product = getProductNameInput(inp);

            spell_checker spellChecker = new spell_checker();
            spellChecker.constructTrieFromFile("Saveonfoods.csv");
            spellChecker.constructTrieFromFile("Yupik.csv");
            spellChecker.constructTrieFromFile("kroger.csv");

            if (!spellChecker.isSpelledCorrectly(product)) {
                System.out.println(ANSI_RED + product + " is misspelled." + ANSI_RESET);
                System.out.println(ANSI_YELLOW + "\nSuggestions:" + ANSI_RESET);

                List<String> suggestions = WordCompletion.findSimilarWords(product);
                for (String suggestion : suggestions) {
                    System.out.println("- " + suggestion);
                }
                continue;
            }
            String[] fileNames = { "kroger.csv", "Yupik.csv", "Saveonfoods.csv" };

            System.out.println("\n" + st);
            System.out.println(ANSI_YELLOW + "List of products: " + ANSI_RESET);
            System.out.println(st);

            List<String> fn = List.of("kroger.csv", "Yupik.csv", "Saveonfoods.csv");

            FoodPriceAnalysis analyzer = new FoodPriceAnalysis();
            analyzer.analyzeFoodPrices(product, 3, fn);

            CombinedTextData.combineCSVFiles(fileNames);
            CombinedFoodpriceAnalysis.showTopDeals(product, "combined.csv");

            boolean isValidInputPageRank = getYesNoInput(ANSI_CYAN + "\nPage rank" + ANSI_RESET, inp);

            if (isValidInputPageRank) {

                inverted_InDExInG A = new inverted_InDExInG();

                Set<String> resultUrls = A.searchUrls(filePaths, product);
                if (resultUrls.isEmpty()) {
                    System.out.println(ANSI_RED + "No URLs contain the word '" + product + "'" + ANSI_RESET);
                } else {
                    System.out.println(ANSI_YELLOW + "URLs containing the word '" + product + "':" + ANSI_RESET);
                    for (String url : resultUrls) {
                        System.out.println(url);
                    }
                }

                SearchFrequency.updateSearchFrequency(product);

                System.out.println("\n" + st);
                System.out.println(ANSI_YELLOW + "Search Frequency Analysis:" + ANSI_RESET);
                System.out.println(st);
                FrequencyCount.frequency(product, resultUrls);

                PageRanking pageRanking = new PageRanking();
                pageRanking.pageRank(resultUrls, product);
                // }
            }

            boolean isValidInputAnotherProduct = getYesNoInput(ANSI_CYAN + "\nEnter another product" + ANSI_RESET, inp);
            if (!isValidInputAnotherProduct) {

                System.out.println("\n" + st);
                System.out.println(ANSI_MAGENTA
                        + "Looks like you're trying to leave. Would you like me to pack your bags in a byte-sized suitcase?"
                        + ANSI_RESET);
                System.out.println(st);
                break;
            }

        }
        inp.close(); // Close the Scanner object at the end of the program
    }

    public static WebDriver setupEdgeDriver() {
        System.setProperty("webdriver.edge.driver", "D:/ACC/Assignment3/msedgedriver.exe");
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--remote-allow-origins=*");
        return new EdgeDriver(options);
    }

    private static int readIntegerInputoftotalpages(Scanner scanner) {
        while (true) {
            try {
                System.out
                        .print(ANSI_CYAN + "\nEnter the total number of pages for Scraping (max 10): " + ANSI_RESET);
                String input = scanner.nextLine();
                int value = Integer.parseInt(input);

                if (value <= 0 || value > 10) {
                    throw new IllegalArgumentException(
                            ANSI_RED + "Invalid input. Please enter a positive integer between 1 and 10." + ANSI_RESET);
                }

                return value;
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + "Invalid input. Please enter a valid integer." + ANSI_RESET);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private static int readCategoryNumberInput(Scanner scanner, int maxNumber) {
        while (true) {
            try {
                System.out.print(ANSI_CYAN + "\nSelect a category by entering the corresponding number (1-"
                        + maxNumber + "): " + ANSI_RESET);
                int value = Integer.parseInt(scanner.nextLine());

                if (value >= 1 && value <= maxNumber) {
                    return value;
                } else {
                    System.out.println(ANSI_RED + "Invalid number. Please enter a valid number between 1 and "
                            + maxNumber + "." + ANSI_RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + "Invalid input. Please enter a valid number." + ANSI_RESET);
            }
        }
    }

    private static String getProductNameInput(Scanner inp) {
        String product;
        do {
            try {
                SearchFrequency.printFrequency();
                System.out.print(ANSI_CYAN + "\nEnter the product name (single word only): " + ANSI_RESET);
                product = inp.nextLine().toLowerCase().trim();

                if (!product.matches("^[a-zA-Z]+$")) {
                    throw new IllegalArgumentException(
                            ANSI_RED + "Product name must contain only letters (alphabets)." + ANSI_RESET);
                }

                if (product.length() < 3) {
                    throw new IllegalArgumentException(
                            ANSI_RED + "Product name must have at least three characters." + ANSI_RESET);
                }

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                product = ""; // Reset the product to trigger the loop
            }
        } while (product.isEmpty());

        return product;
    }

    private static void removeFiles(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                    // System.out.println("File deleted: " + file.getAbsolutePath());
                }
            }
        }
    }

    private static boolean getYesNoInput(String prompt, Scanner scanner) {
        boolean isValidInput = false;
        do {
            System.out.println(prompt + ": ");
            System.out.print("Do you want this functionality to perform ? (Y/N): ");
            String input = scanner.nextLine().toUpperCase();

            try {
                if (!input.matches("[YN]")) {
                    throw new IllegalArgumentException(
                            ANSI_RED + "Invalid input. Please enter 'Y' or 'N'.\n" + ANSI_RESET);
                }

                isValidInput = true; // exit the loop if the input is valid
                return "Y".equals(input); // return the boolean value directly
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (!isValidInput);

        return false; // this line should not be reached
    }

}
