package Tables;

public class ProductsInOrder {
    ShoeOrder shoeOrder;
    Product product;

    public ProductsInOrder(ShoeOrder shoeOrder, Product product) {
        this.shoeOrder = shoeOrder;
        this.product = product;
    }

    public ShoeOrder getShoeOrder() {
        return shoeOrder;
    }

    public void setShoeOrder(ShoeOrder shoeOrder) {
        this.shoeOrder = shoeOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
