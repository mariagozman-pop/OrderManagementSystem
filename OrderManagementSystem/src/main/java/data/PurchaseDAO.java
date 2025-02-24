package data;

import models.Purchase;

import java.sql.Connection;

/**
 * The PurchaseDAO class provides data access methods for interacting with the purchase table in the database.
 * It extends the AbstractDAO class.
 */
public class PurchaseDAO extends AbstractDAO<models.Purchase> {

    /**
     * Constructs a new PurchaseDAO instance with the specified database connection.
     *
     * @param connection the database connection
     */
    public PurchaseDAO(Connection connection) {
        super(connection);
    }

    /**
     * Retrieves the name of the purchase table in the database.
     *
     * @return the table name
     */
    @Override
    protected String getTableName() {
        return "purchase";
    }

    /**
     * Retrieves the primary key column name of the purchase table in the database.
     *
     * @return the primary key column name
     */
    @Override
    protected String getPrimaryKeyName() {
        return "order_id";
    }

    /**
     * Retrieves the model class representing Purchase objects.
     *
     * @return the model class for Purchase objects
     */
    @Override
    protected Class<Purchase> getObjectType() {
        return Purchase.class;
    }
}