package business;

import data.ClientDAO;
import models.Client;

import java.sql.Connection;
import java.util.List;

/**
 * The ClientBLL class provides business logic methods for handling clients.
 */
public class ClientBLL {
    private final ClientDAO clientDAO;

    /**
     * Constructs a new ClientBLL instance with the specified database connection.
     *
     * @param connection the database connection
     */
    public ClientBLL(Connection connection) {
        this.clientDAO = new ClientDAO(connection);
    }

    /**
     * Checks if the specified phone number is valid.
     *
     * @param phoneNumber the phone number to validate
     * @return true if the phone number is valid, false otherwise
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        return !phoneNumber.matches("\\d{10}");
    }

    /**
     * Checks if the specified email address is valid.
     *
     * @param email the email address to validate
     * @return true if the email address is valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        return !email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    /**
     * Adds a new client to the database.
     *
     * @param client the client to add
     * @return the ID of the added client, or a negative value indicating an error
     */
    public int addClient(Client client) {
        if (clientDAO.findObject(client.getId(), Client.class) != null) {
            return -1;
        }
        if (isValidPhoneNumber(client.getPhoneNumber())) {
            return -2;
        }
        if (isValidEmail(client.getEmail())) {
            return -3;
        }
        return clientDAO.addObject(client);
    }

    /**
     * Updates an existing client in the database.
     *
     * @param client the client to update
     * @return 0 if the client was updated successfully, or a negative value indicating an error
     */
    public int updateClient(Client client) {
        if (clientDAO.findObject(client.getId(), Client.class) == null) {
            return -1;
        }
        if (isValidPhoneNumber(client.getPhoneNumber())) {
            return -2;
        }
        if (isValidEmail(client.getEmail())) {
            return -3;
        }
        clientDAO.editObject(client.getId(), client);
        return 0;
    }

    /**
     * Deletes a client from the database.
     *
     * @param clientId the ID of the client to delete
     * @return 0 if the client was deleted successfully, or a negative value indicating an error
     */
    public int deleteClient(int clientId) {
        if (clientDAO.findObject(clientId, Client.class) == null) {
            return -1;
        }
        clientDAO.deleteObject(clientId);
        return 0;
    }

    /**
     * Retrieves all clients from the database.
     *
     * @return a list of Client objects representing all clients
     */
    public List<Client> getAllClients() {
        return clientDAO.getAllObjects();
    }
}
