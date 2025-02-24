package models;

import java.sql.Timestamp;

/**
 * The Purchase class represents a purchase made by a client for a specific product.
 */
public class Purchase {
    private int order_id;
    private int client_id;
    private int product_id;
    private int quantity;
    private Timestamp order_date;

    public Purchase() {
    }
    /**
     * Constructs a new Purchase object with the specified values.
     *
     * @param orderId    The order ID
     * @param clientId   The ID of the client making the purchase
     * @param productId  The ID of the product being purchased
     * @param quantity   The quantity of the product being purchased
     * @param date       The date of the purchase
     */
    public Purchase(int orderId, int clientId, int productId, int quantity, Timestamp date) {
        this.order_id = orderId;
        this.client_id = clientId;
        this.product_id = productId;
        this.quantity = quantity;
        this.order_date = date;
    }

    /**
     * Retrieves the order ID.
     *
     * @return The order ID
     */
    public int getId() {
        return order_id;
    }

    /**
     * Sets the order ID.
     *
     * @param id The order ID
     */
    public void setId(int id) {
        this.order_id = id;
    }

    /**
     * Retrieves the date of the purchase.
     *
     * @return The date of the purchase
     */
    public Timestamp getOrderDate() {
        return order_date;
    }

    /**
     * Generates a string representation of the Purchase object.
     *
     * @return A string representation of the Purchase object
     */
    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + order_id +
                ", clientId=" + client_id +
                ", productId=" + product_id +
                ", quantity=" + quantity +
                '}';
    }
}