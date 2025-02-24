package data;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The AbstractDAO class provides generic methods for interacting with a database.
 *
 * @param <T> The type of object the DAO interacts with.
 */
public abstract class AbstractDAO<T> {

    private final Connection connection;

    /**
     * Constructs an AbstractDAO with a database connection.
     *
     * @param connection The database connection.
     */
    public AbstractDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts a new object into the database.
     *
     * @param object The object to be inserted.
     * @return The auto-generated primary key of the inserted object.
     */
    public int addObject(T object) {
        String tableName = getTableName();
        String primaryKey = getPrimaryKeyName();
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (!field.getName().equals(primaryKey)) {
                    columns.append(field.getName()).append(",");
                    values.append("'").append(field.get(object)).append("',");
                }
            }
            columns.deleteCharAt(columns.length() - 1);
            values.deleteCharAt(values.length() - 1);

            String query = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Updates an existing object in the database.
     *
     * @param id     The primary key of the object to be updated.
     * @param object The updated object.
     */
    public void editObject(int id, T object) {
        String tableName = getTableName();
        String primaryKey = getPrimaryKeyName();
        StringBuilder setClause = new StringBuilder();

        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (!field.getName().equals(primaryKey)) {
                    setClause.append(field.getName()).append(" = '").append(field.get(object)).append("',");
                }
            }
            setClause.deleteCharAt(setClause.length() - 1);

            String query = "UPDATE " + tableName + " SET " + setClause + " WHERE " + primaryKey + " = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Object updated successfully.");
        } catch (IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes an object from the database.
     *
     * @param id The primary key of the object to be deleted.
     */
    public void deleteObject(int id) {
        String tableName = getTableName();
        String primaryKey = getPrimaryKeyName();

        try {
            String query = "DELETE FROM " + tableName + " WHERE " + primaryKey + " = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Object deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds an object in the database by its primary key.
     *
     * @param id     The primary key of the object to find.
     * @param tClass The class of the object.
     * @return The found object, or null if not found.
     */
    public T findObject(int id, Class<T> tClass) {
        String tableName = getTableName();
        String primaryKey = getPrimaryKeyName();

        try {
            String query = "SELECT * FROM " + tableName + " WHERE " + primaryKey + " = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                T object = tClass.newInstance();
                for (Field field : tClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    field.set(object, resultSet.getObject(field.getName()));
                }
                return object;
            }
        } catch (InstantiationException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all objects of a certain type from the database.
     *
     * @return A list of all objects in the database.
     */
    public List<T> getAllObjects() {
        List<T> objects = new ArrayList<>();
        String tableName = getTableName();

        try {
            String query = "SELECT * FROM " + tableName;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                T object = createObjectFromResultSet(resultSet);
                objects.add(object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return objects;
    }

    /**
     * Creates an object from a ResultSet obtained from the database.
     *
     * @param resultSet The ResultSet containing the object data.
     * @return The created object.
     * @throws SQLException if a database access error occurs.
     */
    protected T createObjectFromResultSet(ResultSet resultSet) throws SQLException {
        try {
            T object = getObjectType().newInstance();
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = resultSet.getObject(field.getName());
                if (value == null) {
                    if (field.getType() == int.class || field.getType() == Integer.class) {
                        field.set(object, -1);
                    } else {
                        field.set(object, null);
                    }
                } else {
                    field.set(object, value);
                }
            }
            return object;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the class type of the object the DAO interacts with.
     *
     * @return The class type of the object.
     */
    protected abstract Class<T> getObjectType();

    /**
     * Gets the name of the table associated with the DAO.
     *
     * @return The table name.
     */
    protected abstract String getTableName();

    /**
     * Gets the name of the primary key field in the table.
     *
     * @return The primary key field name.
     */
    protected abstract String getPrimaryKeyName();
}