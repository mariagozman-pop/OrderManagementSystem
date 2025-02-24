package data;

import models.Bill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * The LogDAO class provides data access methods for interacting with the log table in the database.
 */
public class LogDAO {

    private final Connection connection;

    /**
     * Constructs a new LogDAO instance with the specified database connection.
     *
     * @param connection the database connection
     */
    public LogDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Adds a bill to the log table in the database.
     *
     * @param bill the bill to add
     */
    public void addBill(Bill bill) {
        String query = "INSERT INTO Log (order_id, total_amount) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bill.orderId());
            statement.setDouble(2, bill.totalAmount());

            statement.executeUpdate();

            System.out.println("Bill added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all bills from the log table in the database.
     *
     * @return a list of Bill objects representing the bills in the log
     */
    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT * FROM Log";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                double totalAmount = resultSet.getDouble("total_amount");
                Timestamp createdAt = resultSet.getTimestamp("timestamp");

                Bill bill = new Bill(orderId, totalAmount, createdAt);
                bills.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bills;
    }
}