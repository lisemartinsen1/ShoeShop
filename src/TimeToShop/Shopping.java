package TimeToShop;

import Tables.Product;
import Tables.ShoeOrder;
import Utils.ProductUtil;
import Utils.ShoeOrderUtil;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Shopping {
    private final PlaceOrder r = new PlaceOrder();
    private final ProductUtil pu = new ProductUtil();
    private final ShoeOrderUtil su = new ShoeOrderUtil();
    String customerMail;
    String customerPassword;
    boolean isCustomer;
    int shoeNr;
    int customerId;
    int orderId;
    Scanner sc = new Scanner(System.in);

    public Shopping() {
        logIn();
        placeOrder();
    }

    public void logIn() {
        int attempts = 0;
        boolean isLoggedIn = false;
        System.out.println("\nWELCOME TO THE SHOE SHOP!\n");

        try  {
            while (attempts < 3 && !isLoggedIn) {

                System.out.println("Enter mail: ");
                customerMail = sc.nextLine().trim();
                System.out.println("Enter password: ");
                customerPassword = sc.nextLine().trim();

                isLoggedIn = r.authenticate(customerMail, customerPassword);

                if (isLoggedIn) {
                    System.out.println("\n-------------- SHOES --------------");
                    List<Product> products = pu.getAllProducts();
                    pu.printAllProducts(products);

                } else {
                    System.out.println("Wrong mail or password. Please try again.");
                    attempts++;
                }
            }
            if (!isLoggedIn) {
                System.out.println("You have reached the maximum login attempts.");
                System.exit(0);
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please try again.");
        } catch (Exception e) {
            System.out.println("Unknown error.");
            e.printStackTrace();
        }
    }

    public void placeOrder() {

        while (true) {
            System.out.println("\nWhich shoes do you want to order? Enter a number.\n(Type q to quite)");
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("q")) {
                sc.close();
                System.exit(0);
            }

            try {
                int shoeNr = Integer.parseInt(input);
                boolean isValid = pu.findMatch(shoeNr);
                if (!isValid) {
                    System.out.println("Invalid number. Please try again.");
                    continue;
                }

                customerId = r.getCustomerId(customerMail, customerPassword);
                orderId = r.getOrderId(customerId);

                if (orderId == 0) {

                    /* När kunden inte har en aktiv beställning (skapad idag) skapas en ny.
                      Därav hämtas ett nytt id (newOrderId) för beställningen för att anropa
                      stored procedure addToCart. */

                    List<ShoeOrder> shoeOrders = su.getAllOrders();
                    int newOrderId = su.getOrderNrForNewOrder(shoeOrders);
                    r.addToCart(customerId, newOrderId, shoeNr);

                } else {

                    //När en aktiv beställning (skapad idag) redan finns läggs produkter till i samma order
                    r.addToCart(customerId, orderId, shoeNr);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please try again");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please try again.");
            } catch (Exception e) {
                System.out.println("Unknown error");
                e.printStackTrace();
            }
        }

    }


}

