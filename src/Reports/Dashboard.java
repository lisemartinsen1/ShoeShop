package Reports;

import Tables.Product;
import Utils.ProductUtil;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Dashboard {

    private final ProductUtil pu = new ProductUtil();
    private final List<Product> allProducts = pu.getAllProducts();
    private final Reports reports = new Reports();

    // -------------------- Higher order functions ---------------------
    private final ProductSearcher<String, Product> colorSearch = (term, p) -> p.getColor().equals(term);
    private final ProductSearcher<Double, Product> sizeSearch = (term, p) -> Objects.equals(p.getSizeOfShoe(), term);
    private final ProductSearcher<String, Product> brandSearch = (term, p) -> p.getBrand().equals(term);

    private List<Product> findProducts(String term, ProductSearcher<String, Product> mi) {
        return allProducts.stream().filter(product -> mi.findMatch(term, product)).toList();
    }

    private List<Product> findProducts(double term, ProductSearcher<Double, Product> mi) {
        return allProducts.stream().filter(product -> mi.findMatch(term, product)).toList();
    }
    // ----------------------------------------------------------------------

    private final List<String> colorsInDatabase = allProducts.stream().map(Product::getColor).distinct().toList();
    private final List<String> brandsInDatabase = allProducts.stream().map(Product::getBrand).distinct().toList();
    private final List<Double> shoeSizesInDatabase = allProducts.stream().map(Product::getSizeOfShoe).distinct().toList();

    Scanner sc = new Scanner(System.in);

    public Dashboard() {
        showReport();
    }

    private void showReport() {
        while (true) {
            int reportNr = askForReportNumber();

            if (reportNr == 1) {
                int numbOfChoice = getNumberForFiltration();
                filterByChoice(numbOfChoice);

            } else if (reportNr == 2) {
                reports.printOrdersPerCustomer();
                sc.nextLine();

            } else if (reportNr == 3) {
                reports.printOrderValuePerCustomer();
                sc.nextLine();

            } else if (reportNr == 4) {
                reports.printOrderValuePerCity();
                sc.nextLine();

            } else if (reportNr == 5) {
                reports.printTop5Sales();
                sc.nextLine();
            }


            System.out.println("Look at another report? Yes/No");

            while (true) {

                String yesOrNoInput = sc.nextLine().trim();

                if (yesOrNoInput.equalsIgnoreCase("yes")) {
                    break;
                } else if (yesOrNoInput.equalsIgnoreCase("no")) {
                    sc.close();
                    System.exit(0);
                } else {
                    System.out.println("Invalid answer. Please answer Yes or No.");
                }
            }
        }
    }

    private int askForReportNumber() {

        System.out.println("\n------------------------- REPORTS -------------------------");
        System.out.println("""

                1. Customers who bought shoes of certain Color / Size / Brand
                2. Amount of Orders / Customer
                3. Order Value / Customer
                4. Order Value / City
                5. Top 5 Most Sold Products""");

        while (true) {
            System.out.println("\nWhich report do you wish to see? Enter number. Type q to quit.");
            if (sc.hasNextInt()) {
                int reportNr = sc.nextInt();
                if (1 <= reportNr && reportNr <= 5) {
                    return reportNr;
                } else {
                    System.out.println("Invalid number. Number must be in range 1 - 5.");
                }
            } else {
                String input = sc.nextLine().trim();
                if (input.equalsIgnoreCase("q")) {
                    sc.close();
                    System.exit(0);
                } else {
                    System.out.println("Invalid number. Please try again.");
                }
            }
        }
    }


    private int getNumberForFiltration() {
        System.out.println("""
                1. Color
                2. Size
                3. Brand
                What do you want to filter by? Enter number. Type q to quit""");

        while (true) {
            if (sc.hasNextInt()) {
                int chosenNr = sc.nextInt();

                if (1 <= chosenNr && chosenNr <= 3) {
                    return chosenNr;
                } else {
                    System.out.println("Invalid number. Please enter a number in the range 1 - 3.");
                }

            } else {
                String input = sc.nextLine().trim();
                if (input.equalsIgnoreCase("q")) {
                    sc.close();
                    System.exit(0);
                } else {
                    System.out.println("Invalid number. Please try again");
                }
            }

        }
    }

    private void filterByChoice(int choice) {

        if (choice == 1) {
            filterByColor();
        } else if (choice == 2) {
            filterBySize();
        } else if (choice == 3) {
            filterByBrand();
        } else {
            System.out.println("Unexpected error while filtering.");
        }
    }

    private void filterByColor() {
        System.out.println(" >> Colors to choose among << ");
        colorsInDatabase.forEach(System.out::println);
        sc.nextLine();
        System.out.println("\nWhich color do you wish to search for? Type q to quit.");

        while (true) {
            String colorInput = sc.nextLine().trim();

            if (colorInput.equalsIgnoreCase("q")) {
                sc.close();
                System.exit(0);
            }

            boolean foundColor = colorsInDatabase.stream().anyMatch(color -> color.equalsIgnoreCase(colorInput));

            if (foundColor) {
                String colorStr = colorsInDatabase.stream().filter(color -> color.equalsIgnoreCase(colorInput)).findFirst().get();
                List<Product> productsInQuestion = findProducts(colorStr, colorSearch);
                reports.printCustomers(productsInQuestion);
                break;
            } else {
                System.out.println("Invalid color. Please try again.");
            }
        }
    }

    private void filterBySize() {
        sc.nextLine();
        System.out.println("\nWhich size do you wish to search for? Type q to quit.");
        double sizeInt = 0;

        while (true) {
            String sizeInput = sc.nextLine().trim();

            if (sizeInput.equalsIgnoreCase("q")) {
                sc.close();
                System.exit(0);
            }

            try {
                sizeInt = Double.parseDouble(sizeInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please try again.\nEx of valid input: 37 or 41. ");
            }

            double sizeIntFinal = sizeInt;
            boolean foundSize = shoeSizesInDatabase.stream().anyMatch(size -> size == sizeIntFinal);

            if (foundSize) {
                List<Product> productsInQuestion = findProducts(sizeIntFinal, sizeSearch);
                reports.printCustomers(productsInQuestion);
                break;
            } else {
                System.out.println("No matches found. Please try again.");
            }
        }
    }

    private void filterByBrand() {
        System.out.println(">> Brands to choose among <<");
        brandsInDatabase.forEach(System.out::println);
        sc.nextLine();
        System.out.println("\nWhich brand do you wish to search for?");

        while (true) {
            String brandInput = sc.nextLine().trim();

            if (brandInput.equalsIgnoreCase("q")) {
                sc.close();
                System.exit(0);
            }

            boolean foundBrand = brandsInDatabase.stream().anyMatch(brand -> brand.equalsIgnoreCase(brandInput));

            if (foundBrand) {
                String brandStr = brandsInDatabase.stream().filter(brand -> brand.equalsIgnoreCase(brandInput)).findFirst().get();
                List<Product> productsInQuestion = findProducts(brandStr, brandSearch);
                reports.printCustomers(productsInQuestion);
                break;

            } else {
                System.out.println("Invalid brand. Pleas try again.");
            }
        }
    }


}
