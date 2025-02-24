package business;

import data.ProductDAO;
import models.Product;

import java.sql.Connection;
import java.util.List;

/**
 * The ProductBLL class provides business logic methods for handling products.
 */
public class ProductBLL {
    private final ProductDAO productDAO;

    /**
     * Constructs a new ProductBLL instance with the specified database connection.
     *
     * @param connection the database connection
     */
    public ProductBLL(Connection connection) {
        this.productDAO = new ProductDAO(connection);
    }

    /**
     * Checks if the specified price is valid.
     *
     * @param price the price to validate
     * @return true if the price is valid, false otherwise
     */
    private boolean isValidPrice(double price) {
        return price > 0 && price < 10000;
    }

    /**
     * Adds a new product to the database.
     *
     * @param product the product to add
     * @return the ID of the added product, or a negative value indicating an error
     */
    public int addProduct(Product product) {
        if (productDAO.findObject(product.getId(), Product.class) != null) {
            return -1;
        }
        if (!isValidPrice(product.getPrice())) {
            return -2;
        }
        return productDAO.addObject(product);
    }

    /**
     * Updates an existing product in the database.
     *
     * @param product the product to update
     * @return 0 if the product was updated successfully, or a negative value indicating an error
     */
    public int updateProduct(Product product) {
        if (productDAO.findObject(product.getId(), Product.class) == null) {
            return -1;
        }
        productDAO.editObject(product.getId(), product);
        return 0;
    }

    /**
     * Deletes a product from the database.
     *
     * @param productId the ID of the product to delete
     * @return 0 if the product was deleted successfully, or a negative value indicating an error
     */
    public int deleteProduct(int productId) {
        if (productDAO.findObject(productId, Product.class) == null) {
            return -1;
        }
        productDAO.deleteObject(productId);
        return 0;
    }

    /**
     * Retrieves all products from the database.
     *
     * @return a list of Product objects representing all products
     */
    public List<Product> getAllProducts() {
        return productDAO.getAllObjects();
    }
}