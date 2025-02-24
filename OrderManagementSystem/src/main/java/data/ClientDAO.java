package data;

import models.Client;

import java.sql.Connection;

/**
 * This class provides data access methods for managing clients in the database.
 * It extends the AbstractDAO class.
 */
public class ClientDAO extends AbstractDAO<Client> {

    /**
     * Constructs a new ClientDAO with the specified database connection.
     *
     * @param connection The database connection.
     */
    public ClientDAO(Connection connection) {
        super(connection);
    }

    /**
     * Retrieves the name of the table associated with the Client model.
     *
     * @return The name of the client table.
     */
    @Override
    protected String getTableName() {
        return "client";
    }

    /**
     * Retrieves the name of the primary key column associated with the Client model.
     *
     * @return The name of the primary key column.
     */
    @Override
    protected String getPrimaryKeyName() {
        return "client_id";
    }

    /**
     * Retrieves the type of the Client class.
     *
     * @return The Client class type.
     */
    @Override
    protected Class<Client> getObjectType() {
        return Client.class;
    }
}