package models;

import java.sql.Timestamp;

/**
 * The Bill class represents a bill in the system.
 */
public record Bill(int orderId, double totalAmount, Timestamp timestamp) {

}
