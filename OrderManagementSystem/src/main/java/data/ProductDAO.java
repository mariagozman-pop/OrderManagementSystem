package data;

import models.Product;

import java.sql.Connection;

/**
 * The ProductDAO class provides data access methods for interacting with the product table in the database.
 * It extends the AbstractDAO class.
 */
public class ProductDAO extends AbstractDAO<Product> {

    /**
     * Constructs a new ProductDAO instance with the specified database connection.
     *
     * @param connection the database connection
     */
    public ProductDAO(Connection connection) {
        super(connection);
    }

    /**
     * Retrieves the name of the product table in the database.
     *
     * @return the table name
     */
    @Override
    protected String getTableName() {
        return "product";
    }

    /**
     * Retrieves the primary key column name of the product table in the database.
     *
     * @return the primary key column name
     */
    @Override
    protected String getPrimaryKeyName() {
        return "product_id";
    }

    /**
     * Retrieves the model class representing Product objects.
     *
     * @return the model class for Product objects
     */
    @Override
    protected Class<Product> getObjectType() {
        return Product.class;
    }
}