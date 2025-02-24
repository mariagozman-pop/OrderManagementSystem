package presentation;

import business.ClientBLL;
import business.ProductBLL;
import business.PurchaseBLL;
import models.Bill;
import models.Client;
import models.Product;
import models.Purchase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Controller class for managing the interaction between the view and the business logic.
 */
public class Controller {
    private final View view;
    private final Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/orders_management";
    private static final String USER = "root";
    private static final String PASSWORD = "maria";

    /**
     * Constructs a Controller with the specified view and database connection.
     *
     * @param view       the view to be controlled
     * @param connection the database connection to be used
     */
    public Controller(View view, Connection connection) {
        this.view = view;
        this.view.show();
        this.connection = connection;
        this.view.displayOptionsWindow(new ClientButtonListener(),
                new ProductButtonListener(),
                new OrderButtonListener());
    }

    /**
     * Listener for client-related actions.
     */
    class ClientButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Field[] fields = Client.class.getDeclaredFields();
            String[] columnNames = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                columnNames[i] = fields[i].getName();
            }
            view.displayClientsWindow(new ClientAddButtonListener(),
                    new ClientDeleteButtonListener(),
                    new ClientEditButtonListener(),
                    new ClientViewAllButtonListener(),
                    columnNames);
        }
    }

    /**
     * Listener for the add client button.
     */
    class ClientAddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.displayAddClientWindow(new AddClientSubmitButtonListener());
        }
    }

    /**
     * Listener for the submit button when adding a client.
     */
    class AddClientSubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = view.getClientName();
            String email = view.getClientEmail();
            String phoneNumber = view.getClientPhoneNumber();

            ClientBLL clientBLL = new ClientBLL(connection);

            Client newClient = new Client(-1, name, email, phoneNumber);
            int result = clientBLL.addClient(newClient);

            switch (result) {
                case -1:
                    JOptionPane.showMessageDialog(null, "Error: Client already exists!");
                    break;
                case -2:
                    JOptionPane.showMessageDialog(null, "Error: Invalid phone number!");
                    break;
                case -3:
                    JOptionPane.showMessageDialog(null, "Error: Invalid email address!");
                    break;
                default:
                    newClient.setId(result);
                    System.out.println("New client added: " + newClient);
                    break;
            }
            List<Client> clients = clientBLL.getAllClients();
            view.populateTableData((List<Object>) (List<?>) clients);
        }
    }

    /**
     * Listener for the delete client button.
     */
    class ClientDeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (view.getSelectedClient() != null) {
                deleteClient(view.getSelectedClient());
            }
        }
    }

    /**
     * Listener for the edit client button.
     */
    class ClientEditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (view.getSelectedClient() != null) {
                editClient(view.getSelectedClient());
            }
        }
    }

    /**
     * Deletes the specified client.
     *
     * @param client the client to be deleted
     */
    public void deleteClient(Client client) {
        ClientBLL clientBLL = new ClientBLL(connection);
        int result = clientBLL.deleteClient(client.getId());
        if(result == -1) {
            JOptionPane.showMessageDialog(null, "Error: Client does not exist!");
        }
        List<Client> clients = clientBLL.getAllClients();
        view.populateTableData((List<Object>) (List<?>) clients);
    }

    /**
     * Edits the specified client.
     *
     * @param client the client to be edited
     */

    public void editClient(Client client) {
        Client selectedClient = view.getSelectedClient();
        view.displayEditClientWindow(selectedClient, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = view.getClientName();
                String email = view.getClientEmail();
                String phoneNumber = view.getClientPhoneNumber();

                ClientBLL clientBLL = new ClientBLL(connection);

                Client editedClient = new Client(client.getId(), name, email, phoneNumber);
                int result = clientBLL.updateClient(editedClient);
                switch (result) {
                    case -1:
                        JOptionPane.showMessageDialog(null, "Error: Client does not exist!");
                        break;
                    case -2:
                        JOptionPane.showMessageDialog(null, "Error: Invalid phone number!");
                        break;
                    case -3:
                        JOptionPane.showMessageDialog(null, "Error: Invalid email address!");
                        break;
                    default:
                        System.out.println("Client updated: " + editedClient);
                        break;
                }
                List<Client> clients = clientBLL.getAllClients();
                view.populateTableData((List<Object>) (List<?>) clients);
            }
        });

    }

    /**
     * Listener for the view all clients button.
     */
    class ClientViewAllButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ClientBLL clientBLL = new ClientBLL(connection);
            List<Client> clients = clientBLL.getAllClients();
            view.displayAll((List<Object>) (List<?>) clients);
        }
    }

    /**
     * Listener for product-related actions.
     */
    private class ProductButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Field[] fields = Product.class.getDeclaredFields();
            String[] columnNames = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                columnNames[i] = fields[i].getName();
            }
            view.displayProductsWindow(new ProductAddButtonListener(), new ProductViewAllButtonListener(), new ProductDeleteButtonListener(), new ProductEditButtonListener(), columnNames);
        }
    }

    /**
     * Listener for the delete product button.
     */
    private class ProductDeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (view.getSelectedProduct() != null) {
                deleteProduct(view.getSelectedProduct());
            }
        }
    }

    /**
     * Deletes the specified product.
     *
     * @param product the product to be deleted
     */
    public void deleteProduct(Product product) {
        ProductBLL productBLL = new ProductBLL(connection);
        int result = productBLL.deleteProduct(product.getId());
        if(result == -1) {
            JOptionPane.showMessageDialog(null, "Error: Product does not exist!");
        }
        List<Product> products = productBLL.getAllProducts();
        view.populateTableData((List<Object>) (List<?>) products);
    }

    /**
     * Listener for the edit product button.
     */
    class ProductEditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (view.getSelectedProduct() != null) {
                editProduct(view.getSelectedProduct());
            }
        }
    }

    /**
     * Edits the specified product.
     *
     * @param product the product to be edited
     */
    public void editProduct(Product product) {
        Product selectedProduct = view.getSelectedProduct();
        view.displayEditProductWindow(selectedProduct, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = view.getProductName();
                double price = Double.parseDouble(view.getProductPrice());
                int stock = Integer.parseInt(view.getProductStock());

                ProductBLL productBLL = new ProductBLL(connection);

                Product editedProduct = new Product(product.getId(), name, price, stock);
                int result = productBLL.updateProduct(editedProduct);
                switch (result) {
                    case -1:
                        JOptionPane.showMessageDialog(null, "Error: Product does not exist!");
                        break;
                    case -2:
                        JOptionPane.showMessageDialog(null, "Error: Invalid price!");
                        break;
                    default:
                        System.out.println("Product updated: " + editedProduct);
                        break;
                }
                List<Product> products = productBLL.getAllProducts();
                view.populateTableData((List<Object>) (List<?>) products);
            }
        });
    }

    /**
     * Listener for the add product button.
     */
    class ProductAddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.displayAddProductWindow(new AddProductSubmitButtonListener());
        }
    }

    /**
     * Listener for the submit button when adding a product.
     */
    class AddProductSubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = view.getProductName();
            double price;
            int stock;

            try {
                price = Double.parseDouble(view.getProductPrice());
                stock = Integer.parseInt(view.getProductStock());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error: Invalid price or stock value!");
                return;
            }

            ProductBLL productBLL = new ProductBLL(connection);

            Product newProduct = new Product(-1, name, price, stock);
            int result = productBLL.addProduct(newProduct);

            switch (result) {
                case -1:
                    JOptionPane.showMessageDialog(null, "Error: Product already exists!");
                    break;
                case -2:
                    JOptionPane.showMessageDialog(null, "Error: Invalid price!");
                    break;
                default:
                    newProduct.setId(result);
                    System.out.println("New product added: " + newProduct);
                    break;
            }
            List<Product> products = productBLL.getAllProducts();
            view.populateTableData((List<Object>) (List<?>) products);
        }
    }

    /**
     * Listener for the view all products button.
     */
    class ProductViewAllButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ProductBLL productBLL = new ProductBLL(connection);
            List<Product> products = productBLL.getAllProducts();
            view.displayAll((List<Object>) (List<?>) products);
        }
    }

    /**
     * Listener for order-related actions.
     */
    class OrderButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Field[] clientFields = Client.class.getDeclaredFields();
            String[] clientColumnNames = new String[clientFields.length];
            for (int i = 0; i < clientFields.length; i++) {
                clientColumnNames[i] = clientFields[i].getName();
            }

            Field[] productFields = Product.class.getDeclaredFields();
            String[] productColumnNames = new String[productFields.length];
            for (int i = 0; i < productFields.length; i++) {
                productColumnNames[i] = productFields[i].getName();
            }

            Field[] purchaseFields = Purchase.class.getDeclaredFields();
            String[] purchaseColumnNames = new String[purchaseFields.length];
            for (int i = 0; i < purchaseFields.length; i++) {
                purchaseColumnNames[i] = purchaseFields[i].getName();
            }

            view.displayPurchaseWindow(new OrderAddButtonListener(), new OrderViewAllButtonListener(), new BillViewAllButtonListener(), clientColumnNames, productColumnNames, purchaseColumnNames);
            populateClientTable();
            populateProductTable();
        }
    }

    /**
     * Listener for the add order button.
     */
    class OrderAddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            Client client = view.getSelectedClient();
            Product product = view.getSelectedProduct();

            JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
            int result = JOptionPane.showConfirmDialog(null, quantitySpinner, "Select Quantity", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                int quantity = (int) quantitySpinner.getValue();

                PurchaseBLL purchaseBLL = new PurchaseBLL(connection);
                int createResult = purchaseBLL.createPurchase(client, product, quantity);

                switch (createResult) {
                    case -1:
                        JOptionPane.showMessageDialog(null, "Not enough products available.");
                        break;
                    case -2:
                        JOptionPane.showMessageDialog(null, "Product not found.");
                        break;
                    case -3:
                        JOptionPane.showMessageDialog(null, "Client not found.");
                        break;
                    default:
                        if (createResult > 0) {
                            int option = JOptionPane.showConfirmDialog(null, "Order created successfully. View bill?", "View Bill", JOptionPane.YES_NO_OPTION);
                            if (option == JOptionPane.YES_OPTION) {
                                Bill bill = purchaseBLL.getBill();
                                Object[][] billData = {{"Order ID", bill.orderId()},
                                        {"Total Amount", bill.totalAmount()},
                                        {"Order Date", bill.timestamp()}};

                                String[] columnNames = {"Attribute", "Value"};
                                JTable billTable = new JTable(billData, columnNames);
                                billTable.setEnabled(false);

                                JScrollPane scrollPane = new JScrollPane(billTable);
                                JOptionPane.showMessageDialog(null, scrollPane, "Bill Information", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Unknown error occurred.");
                        }
                        break;
                }
            }
            PurchaseBLL purchaseBLL = new PurchaseBLL(connection);
            List<Purchase> purchases = purchaseBLL.getAllPurchases();
            view.displayAll((List<Object>) (List<?>) purchases);
            populateProductTable();
        }
    }

    /**
     * Listener for the view all orders button.
     */
    class OrderViewAllButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            PurchaseBLL purchaseBLL = new PurchaseBLL(connection);
            List<Purchase> purchases = purchaseBLL.getAllPurchases();
            view.displayAll((List<Object>) (List<?>) purchases);
        }
    }

    /**
     * Listener for the view all bills button.
     */
    class BillViewAllButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            PurchaseBLL purchaseBLL = new PurchaseBLL(connection);
            List<Bill> allBills = purchaseBLL.getAllBills();

            Object[][] billData = new Object[allBills.size()][6];
            for (int i = 0; i < allBills.size(); i++) {
                Bill bill = allBills.get(i);
                billData[i] = new Object[]{bill.orderId(), bill.totalAmount(), bill.timestamp()};
            }

            String[] columnNames = {"Order ID", "Total Amount", "Order Date"};
            DefaultTableModel billTableModel = new DefaultTableModel(billData, columnNames);
            JTable billTable = new JTable(billTableModel);
            billTable.setEnabled(false);

            JScrollPane scrollPane = new JScrollPane(billTable);
            JOptionPane.showMessageDialog(null, scrollPane, "All Bills Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Populates the client table with data from the database.
     */
    private void populateClientTable() {
        ClientBLL clientBLL = new ClientBLL(connection);
        List<Client> clients = clientBLL.getAllClients();
        List<Object> clientObjects = new ArrayList<>(clients);
        view.populateClientTable(clientObjects);
    }

    /**
     * Populates the product table with data from the database.
     */
    private void populateProductTable() {
        ProductBLL productBLL = new ProductBLL(connection);
        List<Product> products = productBLL.getAllProducts();
        List<Object> productObjects = new ArrayList<>(products);
        view.populateProductTable(productObjects);
    }

    /**
     * Main method to start the application.
     *
     * @param args the command line arguments
     * @throws SQLException if a database access error occurs
     */
    public static void main(String[] args) throws SQLException {
        View view = new View();
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        new Controller(view, connection);
    }
}