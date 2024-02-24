package Tables;

public class Product {
    private int id;
    private int amountInStock;
    private double sizeOfShoe;
    private String color;
    private double price;
    private String brand;

    public Product(int id, int amountInStock, double sizeOfShoe, String color, double price, String brand) {
        this.id = id;
        this.amountInStock = amountInStock;
        this.sizeOfShoe = sizeOfShoe;
        this.color = color;
        this.price = price;
        this.brand = brand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmountInStock() {
        return amountInStock;
    }

    public void setAmountInStock(int amountInStock) {
        this.amountInStock = amountInStock;
    }

    public Double getSizeOfShoe() {
        return sizeOfShoe;
    }

    public void setSizeOfShoe(double sizeOfShoe) {
        this.sizeOfShoe = sizeOfShoe;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


}
