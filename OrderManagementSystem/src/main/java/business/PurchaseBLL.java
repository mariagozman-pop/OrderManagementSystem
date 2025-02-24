package business;

import data.*;
import models.Bill;
import models.Client;
import models.Product;
import models.Purchase;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

/**
 * The PurchaseBLL class provides business logic methods for handling purchases.
 */
public class PurchaseBLL {
    private final PurchaseDAO purchaseDAO;
    private final ProductDAO productDAO;
    private final ClientDAO clientDAO;
    private final LogDAO logDAO;
    private Bill bill;

    /**
     * Constructs a new PurchaseBLL instance with the specified database connection.
     *
     * @param connection the database connection
     */
    public PurchaseBLL(Connection connection) {
        this.purchaseDAO = new PurchaseDAO(connection);
        this.productDAO = new ProductDAO(connection);
        this.clientDAO = new ClientDAO(connection);
        this.logDAO = new LogDAO(connection);
    }

    /**
     * Creates a new purchase with the given client, product, and quantity.
     *
     * @param client   the client making the purchase
     * @param product  the product being purchased
     * @param quantity the quantity of the product being purchased
     * @return the ID of the created purchase, or a negative value indicating an error
     */
    public int createPurchase(Client client, Product product, int quantity) {
        if (!isStockValid(product, quantity)) {
            System.out.println("Under-stock message: Not enough products available.");
            return -1;
        }
        if (productDAO.findObject(product.getId(), Product.class) == null) {
            return -2;
        }
        if (clientDAO.findObject(client.getId(), Client.class) == null) {
            return -3;
        }

        Timestamp date = new Timestamp(System.currentTimeMillis());
        Purchase purchase = new Purchase(-1, client.getId(), product.getId(), quantity, date);

        int id = purchaseDAO.addObject(purchase);
        purchase.setId(id);

        product.setStock(product.getStock() - quantity);
        productDAO.editObject(product.getId(), product);

        double totalAmount = quantity * product.getPrice();
        generateBill(logDAO, purchase, totalAmount);

        return id;
    }

    /**
     * Retrieves all purchases from the database.
     *
     * @return a list of Purchase objects representing all purchases
     */
    public List<Purchase> getAllPurchases() {
        return purchaseDAO.getAllObjects();
    }

    /**
     * Checks if the specified quantity of a product is available in stock.
     *
     * @param product  the product to check
     * @param quantity the quantity to purchase
     * @return true if the stock is sufficient, false otherwise
     */
    private boolean isStockValid(Product product, int quantity) {
        return product.getStock() >= quantity;
    }

    /**
     * Generates a bill for the specified purchase and total amount.
     *
     * @param logDAO       the LogDAO instance to add the bill to
     * @param purchase     the purchase for which the bill is generated
     * @param totalAmount  the total amount of the bill
     */
    private void generateBill(LogDAO logDAO, Purchase purchase, double totalAmount) {
        bill = new Bill(purchase.getId(), totalAmount, purchase.getOrderDate());
        logDAO.addBill(bill);
    }

    /**
     * Retrieves the current bill.
     *
     * @return the current bill
     */
    public Bill getBill() {
        return this.bill;
    }

    /**
     * Retrieves all bills from the log.
     *
     * @return a list of Bill objects representing all bills
     */
    public List<Bill> getAllBills(){
        return logDAO.getAllBills();
    }
}