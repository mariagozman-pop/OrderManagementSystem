package models;

/**
 * The Client class represents a client in the system.
 */
public class Client {
    private int client_id;
    private String name;
    private String email;
    private String phone;

    public Client() {
    }

    /**
     * Constructs a new Client object with the specified attributes.
     *
     * @param client_id The ID of the client
     * @param name     The name of the client
     * @param email    The email address of the client
     * @param phone    The phone number of the client
     */
    public Client(int client_id, String name, String email, String phone) {
        this.client_id = client_id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Retrieves the ID of the client.
     *
     * @return The client ID
     */
    public int getId() {
        return client_id;
    }

    /**
     * Sets the ID of the client.
     *
     * @param id The client ID
     */
    public void setId(int id) {
        this.client_id = id;
    }

    /**
     * Retrieves the name of the client.
     *
     * @return The client name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the email address of the client.
     *
     * @return The client email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retrieves the phone number of the client.
     *
     * @return The client phone number
     */
    public String getPhoneNumber() {
        return this.phone;
    }

    /**
     * Generates a string representation of the Client object.
     *
     * @return A string representation of the Client object
     */
    @Override
    public String toString() {
        return "Client{" +
                "id=" + client_id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}