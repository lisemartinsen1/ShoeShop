package Reports;

import Utils.*;
import Tables.*;
import java.util.*;
import java.util.stream.Collectors;

public class Reports {

    private final ProductUtil productUtil = new ProductUtil();
    private final ShoeOrderUtil shoeOrderUtil = new ShoeOrderUtil();
    private final ProductsInOrderUtil productsInOrderUtil = new ProductsInOrderUtil();
    private final CustomerUtil customerUtil = new CustomerUtil();
    private final BelongsUtil belongsUtil = new BelongsUtil();
    private final List<Product> allProducts = productUtil.getAllProducts();
    private final List<ShoeOrder> shoeOrders = shoeOrderUtil.getAllOrders();
    private final List<ProductsInOrder> productsInOrders = productsInOrderUtil.getAllProductsInOrder();
    private final List<Customer> allCustomers = customerUtil.getAllCustomers();


    //----------------------------------------- RAPPORT 1 -----------------------------------------

    private Map<String, String> getCustomerAndCityMap() {
        return allCustomers.stream().collect(Collectors.toMap(Customer::getName, Customer::getCity));
    }

    private Set<String> findCustomers(List<Product> productsInQuestion) {
        Map<String, String> customersAndCities = getCustomerAndCityMap();
        return shoeOrders.stream()
                .filter(order -> productsInOrders.stream()
                        .anyMatch(prodInOrd -> prodInOrd.getShoeOrder().getId() == order.getId()
                                && productsInQuestion.stream().anyMatch(product -> product.getId() == prodInOrd.getProduct().getId())))
                .map(order -> {
                    String custName = order.getCustomer().getName();
                    String city = customersAndCities.getOrDefault(custName, "unknown");
                    return custName + ", " + city;
                }).collect(Collectors.toSet());
    }

    public void printCustomers(List<Product> products) {
        Set<String> customers = findCustomers(products);
        System.out.println("----------------------- RESULT -----------------------");
        customers.forEach(System.out::println);
        System.out.println();
    }


    //----------------------------------------- RAPPORT 2 -----------------------------------------

    private Map<Integer, Long> ordersPerCustomer() {
        return shoeOrders.stream().collect(Collectors.groupingBy(
                shoeOrder -> shoeOrder.getCustomer().getId(),
                Collectors.counting()));
    }

    public void printOrdersPerCustomer() {
        Map<Integer, Long> ordersPerCustomer = ordersPerCustomer();
        System.out.println("----------------------- AMOUNT OF ORDERS / CUSTOMER -----------------------");
        ordersPerCustomer.forEach((customerId, count) -> {
            String name = shoeOrders.stream()
                    .filter(shoeOrder -> shoeOrder.getCustomer().getId() == customerId)
                    .map(shoeOrder -> shoeOrder.getCustomer().getName())
                    .findFirst()
                    .orElse("unknown");

            System.out.println(name + ": " + count);
        });
        System.out.println();
    }


    //----------------------------------------- RAPPORT 3 -----------------------------------------

    private Map<Customer, Double> getOrderValuePerCustomer() {
        return allCustomers.stream().
                collect(Collectors.toMap(
                        customer -> customer,
                        this::calcOrderValueForCustomer
                ));
    }

    public void printOrderValuePerCustomer() {
        System.out.println("-------------------------- ORDER VALUE / CUSTOMER--------------------------");
        Map<Customer, Double> orderValuePerCustomer = getOrderValuePerCustomer();
        orderValuePerCustomer.forEach((customer, orderValue)
                -> System.out.println(customer.getName() + ", " + orderValue + "kr"));
        System.out.println();
    }


    //----------------------------------------- RAPPORT 4 -----------------------------------------

    private Map<String, Double> getOrderValuePerCity() {
        return allCustomers.stream()
                .collect(Collectors.groupingBy(Customer::getCity,
                        Collectors.summingDouble(this::calcOrderValueForCustomer)

                ));
    }

    public void printOrderValuePerCity() {
        System.out.println("-------------------------- ORDER VALUE / CITY --------------------------");
        Map<String, Double> orderValuePerCity = getOrderValuePerCity();
        orderValuePerCity.forEach((city, val) -> System.out.println(city + ": " + val + "kr"));
        System.out.println();
    }


    //----------------------------------------- RAPPORT 5 -----------------------------------------

    private Map<Product, Long> salesPerProductMap() {
        return allProducts.stream()
                .collect(Collectors.toMap(product -> product, this::countSalesPerProduct));
    }

    private long countSalesPerProduct(Product product) {
        return productsInOrders.stream().filter(prodInOrd -> prodInOrd.getProduct().getId() == product.getId())
                .count();
    }

    public void printTop5Sales() {
        List<Map.Entry<Product, Long>> top5Products = salesPerProductMap().entrySet().stream()
                .sorted(Map.Entry.<Product, Long>comparingByValue().reversed())
                .limit(5).toList();

        System.out.println("------------------------------- TOP 5 PRODUCTS -------------------------------");
        top5Products.forEach(entry -> {
            Product product = entry.getKey();
            Long sales = entry.getValue();
            List<String> categories = belongsUtil.getCategoriesForProduct(product);
            System.out.println("Id: " + product.getId() + "\nBrand: " + product.getBrand() + "\nColor: " + product.getColor() +
                    "\nSize: " + product.getSizeOfShoe() + "\nCategory: " + String.join(", ", categories));
            System.out.println("Amount of sales: " + sales);
            System.out.println();
        });
    }


    //----------------------------------------- OTHER -----------------------------------------

    private double calcOrderValueForCustomer(Customer customer) {
        int customerId = customer.getId();
        return shoeOrders.stream()
                .filter(order -> order.getCustomer().getId() == customerId)
                .flatMapToDouble(order -> productsInOrders.stream()
                        .filter(prodInOrd -> prodInOrd.getShoeOrder().getId() == order.getId())
                        .mapToDouble(prodInOrd -> allProducts.stream()
                                .filter(product -> product.getId() == prodInOrd.getProduct().getId())
                                .mapToDouble(Product::getPrice)
                                .sum()
                        )
                ).sum();
    }
}
