package Mavericks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CombinedFoodpriceAnalysis {

    public static void showTopDeals(String product, String combinedFilePath) {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(combinedFilePath))) {
            // Skip the header
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String title = parts[0].trim();
                    String weight = parts[1].trim();
                    String priceStr = parts[2].replaceAll("\"", "").trim(); // Remove double quotes
                    double price = 0.0;
                    try {
                        price = Double.parseDouble(priceStr);
                    } catch (NumberFormatException e) {
                        // Skip this entry if price cannot be parsed
                        continue;
                    }
                    String filename = parts[3].trim();
                    products.add(new Product(title, weight, price, filename));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (products.isEmpty()) {
            System.out.println("No data found.");
            return;
        }

        products.sort(Comparator.comparingDouble(Product::getPrice));
        String st = "------------------------------------------------------";

        System.out.println(

                MainScraper.ANSI_GREEN + "\nTop 5 cheapest deals for '" + product + "':" + MainScraper.ANSI_RESET);

        System.out.println(st);
        int count = 0;
        for (Product p : products) {
            if (p.getTitle().toLowerCase().contains(product.toLowerCase())) {
                count++;
                System.out.println(count + ". " +
                        p.getTitle() + " - " + p.getWeight() + " - $" + p.getPrice() + " - " + p.getFilename());

                if (count >= 5) {
                    break;
                }
            }
        }
    }

    private static class Product {
        private String title;
        private String weight;
        private double price;
        private String filename;

        public Product(String title, String weight, double price, String filename) {
            this.title = title;
            this.weight = weight;
            this.price = price;
            this.filename = filename;
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

        public String getFilename() {
            return filename;
        }
    }
}
