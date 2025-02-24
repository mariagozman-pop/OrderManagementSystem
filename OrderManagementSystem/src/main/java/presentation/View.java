package presentation;

import models.Client;
import models.Product;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.List;

/**
 * The {@code View} class represents the graphical user interface (GUI) for the application.
 * It provides methods to display different windows and manage clients, products, and orders.
 */
public class View {
    private final JFrame frame;
    private DefaultTableModel model;
    private DefaultTableModel clientTableModel;
    private DefaultTableModel productTableModel;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneNumberField;
    private JTextField priceField;
    private JTextField stockField;
    private JButton deleteButton;
    private JButton editButton;
    private Client selectedClient;
    private Product selectedProduct;

    /**
     * Constructs a new {@code View} and initializes the main application frame.
     */
    public View() {
        frame = new JFrame("Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
    }

    /**
     * Makes the main application frame visible.
     */
    public void show() {
        frame.setVisible(true);
    }

    /**
     * Displays the options window with buttons to manage clients, products, and orders.
     *
     * @param clientButtonListener   the action listener for the client button
     * @param productButtonListener  the action listener for the product button
     * @param orderButtonListener    the action listener for the order button
     */
    public void displayOptionsWindow(ActionListener clientButtonListener,
                                     ActionListener productButtonListener,
                                     ActionListener orderButtonListener) {
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        optionsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Welcome to Orders Management!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setForeground(new Color(7, 87, 152));
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 18f));
        optionsPanel.add(welcomeLabel);

        JButton clientButton = createButton("Manage Clients", clientButtonListener, new Color(40, 103, 160), new Color(9, 28, 87));
        optionsPanel.add(clientButton);

        JButton productButton = createButton("Manage Products", productButtonListener, new Color(99, 141, 187), new Color(9, 28, 87));
        optionsPanel.add(productButton);

        JButton orderButton = createButton("Manage Orders", orderButtonListener, new Color(157, 179, 208), new Color(9, 28, 87));
        optionsPanel.add(orderButton);

        optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(240, 248, 255));
        frame.getContentPane().add(optionsPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Creates a JButton with the specified text, action listener, background color, and text color.
     *
     * @param text           the text of the button
     * @param actionListener the action listener for the button
     * @param backgroundColor the background color of the button
     * @param textColor      the text color of the button
     * @return the created JButton
     */
    private JButton createButton(String text, ActionListener actionListener, Color backgroundColor, Color textColor) {
        JButton button = new JButton(text);
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
        button.setPreferredSize(new Dimension(150, 30));
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 14f));
        return button;
    }

    /**
     * Displays the window for managing clients with buttons to add, delete, edit, and view all clients.
     *
     * @param clientAddButtonListener   the action listener for the add client button
     * @param clientDeleteButtonListener the action listener for the delete client button
     * @param clientEditButtonListener   the action listener for the edit client button
     * @param clientViewAllButtonListener the action listener for the view all clients button
     * @param columnNames               the column names for the clients table
     */
    public void displayClientsWindow(ActionListener clientAddButtonListener,
                                     ActionListener clientDeleteButtonListener,
                                     ActionListener clientEditButtonListener,
                                     ActionListener clientViewAllButtonListener,
                                     String[] columnNames) {
        JFrame clientsFrame = createClientsFrame();
        JPanel clientsPanel = new JPanel(new BorderLayout());
        clientsFrame.getContentPane().add(clientsPanel);

        JPanel buttonPanel = createButtonPanelClient(clientAddButtonListener, clientDeleteButtonListener, clientEditButtonListener);

        JButton viewAllButton = createViewAllButtonClients(clientViewAllButtonListener);
        buttonPanel.add(viewAllButton);

        deleteButton.setEnabled(false);
        editButton.setEnabled(false);

        clientsPanel.add(buttonPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout());

        model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.setDefaultEditor(Object.class, null);

        JScrollPane scrollPane = new JScrollPane(table);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        clientsPanel.add(tablePanel, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    deleteButton.setEnabled(true);
                    editButton.setEnabled(true);

                    int selectedRowIndex = table.getSelectedRow();
                    Object[] rowData = new Object[model.getColumnCount()];
                    for (int i = 0; i < model.getColumnCount(); i++) {
                        rowData[i] = model.getValueAt(selectedRowIndex, i);
                    }
                    selectedClient = new Client((int) rowData[0], (String) rowData[1], (String) rowData[2], (String) rowData[3]); // Assuming the Client constructor takes name, email, and phoneNumber as arguments
                } else {
                    deleteButton.setEnabled(false);
                    editButton.setEnabled(false);

                    selectedClient = null;
                }
            }
        });

        clientsFrame.setVisible(true);
    }

    /**
     * Gets the currently selected client.
     *
     * @return the selected client
     */
    public Client getSelectedClient() {
        return selectedClient;
    }

    /**
     * Displays all clients in the table.
     *
     * @param objects the list of clients to display
     */
    public void displayAll(List<Object> objects) {
        model.setRowCount(0);
        populateTable(model, objects);
    }

    /**
     * Creates a new JFrame for managing clients.
     *
     * @return the created JFrame
     */
    private JFrame createClientsFrame() {
        JFrame clientsFrame = new JFrame("Manage Clients");
        clientsFrame.setSize(800, 300);
        clientsFrame.setLocationRelativeTo(frame);
        return clientsFrame;
    }

    /**
     * Creates a panel with buttons for adding, deleting, and editing clients.
     *
     * @param clientAddButtonListener   the action listener for the add client button
     * @param clientDeleteButtonListener the action listener for the delete client button
     * @param clientEditButtonListener   the action listener for the edit client button
     * @return the created button panel
     */
    private JPanel createButtonPanelClient(ActionListener clientAddButtonListener,
                                     ActionListener clientDeleteButtonListener, ActionListener clientEditButtonListener) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("Add Client");
        addButton.addActionListener(clientAddButtonListener);
        buttonPanel.add(addButton);

        deleteButton = new JButton("Delete Client");
        deleteButton.addActionListener(clientDeleteButtonListener);
        buttonPanel.add(deleteButton);

        editButton = new JButton("Edit Client");
        editButton.addActionListener(clientEditButtonListener);
        buttonPanel.add(editButton);
        return buttonPanel;
    }

    /**
     * Creates a button for viewing all clients.
     *
     * @param clientViewAllButtonListener the action listener for the view all clients button
     * @return the created view all button
     */
    private JButton createViewAllButtonClients(ActionListener clientViewAllButtonListener) {
        JButton viewAllButton = new JButton("View All Clients");
        viewAllButton.addActionListener(clientViewAllButtonListener);
        return viewAllButton;
    }

    /**
     * Displays the window for adding a new client.
     *
     * @param submitButtonListener the action listener for the submit button
     */
    public void displayAddClientWindow(ActionListener submitButtonListener) {
        JFrame addClientFrame = new JFrame("Add Client");
        addClientFrame.setSize(400, 300);
        addClientFrame.setLocationRelativeTo(frame);

        JPanel addClientPanel = new JPanel(new GridLayout(4, 1, 10, 10));

        nameField = new JTextField();
        emailField = new JTextField();
        phoneNumberField = new JTextField();

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            submitButtonListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
            addClientFrame.dispose();
        });

        addClientPanel.add(new JLabel("Name:"));
        addClientPanel.add(nameField);
        addClientPanel.add(new JLabel("Email:"));
        addClientPanel.add(emailField);
        addClientPanel.add(new JLabel("Phone Number:"));
        addClientPanel.add(phoneNumberField);
        addClientPanel.add(submitButton);

        addClientFrame.getContentPane().add(addClientPanel);
        addClientFrame.setVisible(true);
    }

    /**
     * Gets the text from the name field.
     *
     * @return the text from the name field
     */
    public String getClientName() {
        return nameField.getText();
    }

    /**
     * Gets the text from the email field.
     *
     * @return the text from the email field
     */
    public String getClientEmail() {
        return emailField.getText();
    }

    /**
     * Gets the text from the phone number field.
     *
     * @return the text from the phone number field
     */
    public String getClientPhoneNumber() {
        return phoneNumberField.getText();
    }

    /**
     * Displays the window for editing a client with the specified initial values.
     *
     * @param submitButtonListener the action listener for the submit button
     * @param client          the initial client
     */
    public void displayEditClientWindow(Client client, ActionListener submitButtonListener) {
        JFrame editClientFrame = new JFrame("Edit Client");
        editClientFrame.setSize(400, 300);
        editClientFrame.setLocationRelativeTo(frame);

        JPanel editClientPanel = new JPanel(new GridLayout(4, 1, 10, 10));

        nameField = new JTextField(client.getName());
        emailField = new JTextField(client.getEmail());
        phoneNumberField = new JTextField(client.getPhoneNumber());

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            submitButtonListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
            editClientFrame.dispose();
        });

        editClientPanel.add(new JLabel("Name:"));
        editClientPanel.add(nameField);
        editClientPanel.add(new JLabel("Email:"));
        editClientPanel.add(emailField);
        editClientPanel.add(new JLabel("Phone Number:"));
        editClientPanel.add(phoneNumberField);
        editClientPanel.add(submitButton);

        editClientFrame.getContentPane().add(editClientPanel);
        editClientFrame.setVisible(true);
    }

    /**
     * Creates a panel with buttons for adding, deleting, and editing products.
     *
     * @param productAddButtonListener    the action listener for the add product button
     * @param productDeleteButtonListener the action listener for the delete product button
     * @param productEditButtonListener   the action listener for the edit product button
     * @return the created button panel
     */
    private JPanel createButtonPanelProduct(ActionListener productAddButtonListener,
                                           ActionListener productDeleteButtonListener, ActionListener productEditButtonListener) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("Add Product");
        addButton.addActionListener(productAddButtonListener);
        buttonPanel.add(addButton);

        deleteButton = new JButton("Delete Product");
        deleteButton.addActionListener(productDeleteButtonListener);
        buttonPanel.add(deleteButton);

        editButton = new JButton("Edit Product");
        editButton.addActionListener(productEditButtonListener);
        buttonPanel.add(editButton);
        return buttonPanel;
    }

    /**
     * Creates a button for viewing all products.
     *
     * @param productViewAllButtonListener the action listener for the view all products button
     * @return the created view all button
     */
    private JButton createViewAllButtonProduct(ActionListener productViewAllButtonListener) {
        JButton viewAllButton = new JButton("View All Products");
        viewAllButton.addActionListener(productViewAllButtonListener);
        return viewAllButton;
    }

    /**
     * Displays the window for managing products with buttons to add, delete, edit, and view all products.
     *
     * @param productAddButtonListener    the action listener for the add product button
     * @param productDeleteButtonListener the action listener for the delete product button
     * @param productEditButtonListener   the action listener for the edit product button
     * @param productViewAllButtonListener the action listener for the view all products button
     * @param columnNames                 the column names for the products table
     */
    public void displayProductsWindow(ActionListener productAddButtonListener,
                                      ActionListener productViewAllButtonListener,
                                      ActionListener productDeleteButtonListener,
                                      ActionListener productEditButtonListener,
                                      String[] columnNames) {
        JFrame productsFrame = createProductsFrame();
        JPanel productsPanel = new JPanel(new BorderLayout());
        productsFrame.getContentPane().add(productsPanel);

        JPanel buttonPanel = createButtonPanelProduct(productAddButtonListener, productDeleteButtonListener, productEditButtonListener);

        JButton viewAllButton = createViewAllButtonProduct(productViewAllButtonListener);
        buttonPanel.add(viewAllButton);

        deleteButton.setEnabled(false);
        editButton.setEnabled(false);

        productsPanel.add(buttonPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout());

        model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.setDefaultEditor(Object.class, null);

        JScrollPane scrollPane = new JScrollPane(table);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        productsPanel.add(tablePanel, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    deleteButton.setEnabled(true);
                    editButton.setEnabled(true);

                    int selectedRowIndex = table.getSelectedRow();
                    Object[] rowData = new Object[model.getColumnCount()];
                    for (int i = 0; i < model.getColumnCount(); i++) {
                        rowData[i] = model.getValueAt(selectedRowIndex, i);
                    }
                    selectedProduct = new Product((int) rowData[0], (String) rowData[1], (double) rowData[2], (int) rowData[3]);
                } else {
                    deleteButton.setEnabled(false);
                    editButton.setEnabled(false);

                    selectedProduct = null;
                }
            }
        });

        productsFrame.setVisible(true);
    }

    /**
     * Creates a new JFrame for managing products.
     *
     * @return the created JFrame
     */
    private JFrame createProductsFrame() {
        JFrame productsFrame = new JFrame("Manage Products");
        productsFrame.setSize(800, 600);
        productsFrame.setLocationRelativeTo(frame);
        return productsFrame;
    }

    /**
     * Displays the window for adding a new product.
     *
     * @param submitButtonListener the action listener for the submit button
     */
    public void displayAddProductWindow(ActionListener submitButtonListener) {
        JFrame addProductFrame = new JFrame("Add Product");
        addProductFrame.setSize(400, 300);
        addProductFrame.setLocationRelativeTo(frame);

        JPanel addProductPanel = new JPanel(new GridLayout(4, 1, 10, 10));

        nameField = new JTextField();
        priceField = new JTextField();
        stockField = new JTextField();

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            submitButtonListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
            addProductFrame.dispose();
        });

        addProductPanel.add(new JLabel("Name:"));
        addProductPanel.add(nameField);
        addProductPanel.add(new JLabel("Price:"));
        addProductPanel.add(priceField);
        addProductPanel.add(new JLabel("Stock:"));
        addProductPanel.add(stockField);
        addProductPanel.add(submitButton);

        addProductFrame.getContentPane().add(addProductPanel);
        addProductFrame.setVisible(true);
    }

    /**
     * Gets the text from the name field.
     *
     * @return the text from the name field
     */
    public String getProductName() {
        return nameField.getText();
    }

    /**
     * Gets the text from the price field.
     *
     * @return the text from the price field
     */
    public String getProductPrice() {
        return priceField.getText();
    }

    /**
     * Gets the text from the stock field.
     *
     * @return the text from the stock field
     */
    public String getProductStock() {
        return stockField.getText();
    }

    /**
     * Gets the currently selected product.
     *
     * @return the selected product
     */
    public Product getSelectedProduct() {
        return this.selectedProduct;
    }

    /**
     * Displays the window for editing a product with the specified initial values.
     *
     * @param selectedProduct the product to be edited
     * @param actionListener  the action listener for the submit button
     */
    public void displayEditProductWindow(Product selectedProduct, ActionListener actionListener) {
        JFrame editProductFrame = new JFrame("Edit Product");
        editProductFrame.setSize(400, 300);
        editProductFrame.setLocationRelativeTo(frame);

        JPanel editProductPanel = new JPanel(new GridLayout(4, 1, 10, 10));

        nameField = new JTextField(selectedProduct.getName());
        priceField = new JTextField(String.valueOf(selectedProduct.getPrice()));
        stockField = new JTextField(String.valueOf(selectedProduct.getStock()));

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
            editProductFrame.dispose();
        });

        editProductPanel.add(new JLabel("Name:"));
        editProductPanel.add(nameField);
        editProductPanel.add(new JLabel("Price:"));
        editProductPanel.add(priceField);
        editProductPanel.add(new JLabel("Stock:"));
        editProductPanel.add(stockField);
        editProductPanel.add(submitButton);

        editProductFrame.getContentPane().add(editProductPanel);
        editProductFrame.setVisible(true);
    }

    /**
     * Displays the purchase window with tables for clients, products, and purchases.
     *
     * @param orderAddButtonListener    ActionListener for the "Add New Order" button.
     * @param orderViewAllButtonListener ActionListener for the "View All Orders" button.
     * @param viewAllBillsButtonListener ActionListener for the "View All Bills" button.
     * @param clientColumnNames          Column names for the client table.
     * @param productColumnNames         Column names for the product table.
     * @param purchaseColumnNames        Column names for the purchase table.
     */
    public void displayPurchaseWindow(ActionListener orderAddButtonListener, ActionListener orderViewAllButtonListener, ActionListener viewAllBillsButtonListener, String[] clientColumnNames, String[] productColumnNames, String[] purchaseColumnNames) {
        JFrame purchaseFrame = new JFrame("Manage Orders");
        purchaseFrame.setSize(900, 600);
        purchaseFrame.setLocationRelativeTo(frame);

        JPanel purchasePanel = new JPanel();
        purchasePanel.setLayout(new BoxLayout(purchasePanel, BoxLayout.Y_AXIS));
        purchaseFrame.getContentPane().add(purchasePanel);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton addOrderButton = new JButton("Add New Order");
        addOrderButton.addActionListener(orderAddButtonListener);
        addOrderButton.setEnabled(false);
        buttonPanel.add(addOrderButton);

        JButton viewAllOrdersButton = new JButton("View All Orders");
        viewAllOrdersButton.addActionListener(orderViewAllButtonListener);
        buttonPanel.add(viewAllOrdersButton);

        JButton viewAllBillsButton = new JButton("View All Bills");
        viewAllBillsButton.addActionListener(viewAllBillsButtonListener);
        buttonPanel.add(viewAllBillsButton);

        purchasePanel.add(buttonPanel);

        clientTableModel = new DefaultTableModel(clientColumnNames, 0);
        JTable clientTable = new JTable(clientTableModel);

        productTableModel = new DefaultTableModel(productColumnNames, 0);
        JTable productTable = new JTable(productTableModel);

        clientTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = clientTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedClient = getClientFromTable(clientTableModel, selectedRow);
                }
                addOrderButton.setEnabled(clientTable.getSelectedRow() != -1 && productTable.getSelectedRow() != -1);
            }
        });
        JScrollPane clientScrollPane = new JScrollPane(clientTable);
        purchasePanel.add(clientScrollPane);

        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedProduct = getProductFromTable(productTableModel, selectedRow);
                }
                addOrderButton.setEnabled(clientTable.getSelectedRow() != -1 && productTable.getSelectedRow() != -1);
            }
        });
        JScrollPane productScrollPane = new JScrollPane(productTable);
        purchasePanel.add(productScrollPane);

        model = new DefaultTableModel(purchaseColumnNames, 0);
        JTable purchaseTable = new JTable(model);
        JScrollPane purchaseScrollPane = new JScrollPane(purchaseTable);
        purchasePanel.add(purchaseScrollPane);

        purchaseFrame.setVisible(true);
    }

    /**
     * Retrieves a Client object from the client table model based on the selected row index.
     *
     * @param clientTableModel The table model containing client data.
     * @param rowIndex         The index of the selected row.
     * @return The Client object corresponding to the selected row.
     */
    private Client getClientFromTable(DefaultTableModel clientTableModel, int rowIndex) {
        int clientId = (int) clientTableModel.getValueAt(rowIndex, 0); // Assuming the first column is the ID
        String name = (String) clientTableModel.getValueAt(rowIndex, 1);
        String email = (String) clientTableModel.getValueAt(rowIndex, 2);
        String phoneNumber = (String) clientTableModel.getValueAt(rowIndex, 3);

        return new Client(clientId, name, email, phoneNumber);
    }

    /**
     * Retrieves a Product object from the product table model based on the selected row index.
     *
     * @param productTableModel The table model containing product data.
     * @param rowIndex          The index of the selected row.
     * @return The Product object corresponding to the selected row.
     */
    private Product getProductFromTable(DefaultTableModel productTableModel, int rowIndex) {
        int productId = (int) productTableModel.getValueAt(rowIndex, 0); // Assuming the first column is the ID
        String name = (String) productTableModel.getValueAt(rowIndex, 1);
        double price = (double) productTableModel.getValueAt(rowIndex, 2);
        int stock = (int) productTableModel.getValueAt(rowIndex, 3);

        return new Product(productId, name, price, stock);
    }

    /**
     * Populates the client table with a list of clients.
     *
     * @param clients The list of clients to populate the table with.
     */
    public void populateClientTable(List<Object> clients) {
        updateTableData(clientTableModel, clients);
    }

    /**
     * Populates the product table with a list of products.
     *
     * @param products The list of products to populate the table with.
     */
    public void populateProductTable(List<Object> products) {
        updateTableData(productTableModel, products);
    }

    /**
     * Populates a table with a list of objects.
     *
     * @param model   The table model to populate.
     * @param objects The list of objects to populate the table with.
     */
    public void populateTable(DefaultTableModel model, List<Object> objects) {
        model.setRowCount(0);

        if (!objects.isEmpty()) {
            Class<?> clazz = objects.get(0).getClass();

            for (Object object : objects) {
                Object[] rowData = new Object[model.getColumnCount()];
                for (int i = 0; i < model.getColumnCount(); i++) {
                    try {
                        String columnName = model.getColumnName(i);
                        java.lang.reflect.Field field = clazz.getDeclaredField(columnName);
                        field.setAccessible(true);
                        rowData[i] = field.get(object);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                model.addRow(rowData);
            }
        }
    }

    /**
     * Updates the table data with a list of data objects.
     *
     * @param model    The table model to update.
     * @param dataList The list of data objects to update the table with.
     * @param <T>      The type of data objects.
     */
    public <T> void updateTableData(DefaultTableModel model, List<T> dataList) {
        model.setRowCount(0);

        if (!dataList.isEmpty()) {
            Class<?> clazz = dataList.get(0).getClass();
            Field[] fields = clazz.getDeclaredFields();

            for (T data : dataList) {
                Object[] rowData = new Object[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    try {
                        fields[i].setAccessible(true);
                        rowData[i] = fields[i].get(data);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                model.addRow(rowData);
            }
        }
    }

    /**
     * Populates the purchase table with a list of purchase objects.
     *
     * @param objects The list of purchase objects to populate the table with.
     */
    public void populateTableData(List<Object> objects) {
        updateTableData(model, objects);
    }
}