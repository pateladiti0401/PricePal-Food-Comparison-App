package Mavericks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FoodPriceAnalysis {

    public void analyzeFoodPrices(String searchWord, int topN, List<String> fileNames) {
        for (String fileName : fileNames) {
            List<Product> products = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                // Skip the header
                br.readLine();

                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        String title = parts[0].trim();
                        String weight = parts[1].trim();
                        double price = extractPrice(parts[2].trim());
                        products.add(new Product(title, weight, price));
                    }
                }
            } catch (IOException e) {
                System.out.println(MainScraper.ANSI_RED + "Error reading file: " + fileName + MainScraper.ANSI_RESET);
            }
            if (products.isEmpty()) {
                System.out.println("No data available in file: " + fileName);
            } else {
                products.sort(Comparator.comparingDouble(Product::getPrice));
                String st = "------------------------------------------------------";

                System.out.println(MainScraper.ANSI_YELLOW + "\nTop " + topN + " cheapest product(s) for '" + searchWord
                        + "' in " + fileName + ":" + MainScraper.ANSI_RESET);
                System.out.println(st);

                int count = 0;
                for (Product product : products) {
                    if (product.getTitle().toLowerCase().contains(searchWord.toLowerCase())) {
                        count++;
                        System.out.println(count + ". " + product.getTitle() + " - " + product.getWeight() + " - $"
                                + product.getPrice());
                        if (count >= topN) {
                            break;
                        }
                    }
                }
            }
        }
    }

    private double extractPrice(String priceString) {
        try {
            // Remove any non-numeric characters
            priceString = priceString.replaceAll("[^\\d.]", "");
            return Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            return Double.MAX_VALUE; // Return a very large value if price cannot be parsed
        }
    }

    private static class Product {
        private String title;
        private String weight;
        private double price;

        public Product(String title, String weight, double price) {
            this.title = title;
            this.weight = weight;
            this.price = price;
        }

        public String getTitle() {
            return title;
        }

        public String getWeight() {
            return weight;
        }

        public double getPrice() {
            return price;
        }
    }
}