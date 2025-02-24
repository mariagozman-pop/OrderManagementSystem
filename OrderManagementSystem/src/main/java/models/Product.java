package models;

/**
 * The Product class represents a product available in the inventory.
 */
public class Product {
    private int product_id;
    private String name;
    private double price;
    private int stock;

    public Product() {
    }

    /**
     * Constructs a new Product object with the specified attributes.
     *
     * @param product_id The ID of the product
     * @param name      The name of the product
     * @param price     The price of the product
     * @param stock     The stock quantity of the product
     */
    public Product(int product_id, String name, double price, int stock) {
        this.product_id = product_id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    /**
     * Retrieves the ID of the product.
     *
     * @return The product ID
     */
    public int getId() {
        return product_id;
    }

    /**
     * Sets the ID of the product.
     *
     * @param product_id The product ID
     */
    public void setId(int product_id) {
        this.product_id = product_id;
    }

    /**
     * Retrieves the name of the product.
     *
     * @return The product name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the price of the product.
     *
     * @return The product price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Retrieves the stock quantity of the product.
     *
     * @return The product stock quantity
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the stock quantity of the product.
     *
     * @param stock The product stock quantity
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Generates a string representation of the Product object.
     *
     * @return A string representation of the Product object
     */
    @Override
    public String toString() {
        return "Product{" +
                "id=" + product_id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}