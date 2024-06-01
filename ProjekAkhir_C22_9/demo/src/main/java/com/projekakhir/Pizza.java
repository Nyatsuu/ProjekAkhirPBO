package com.projekakhir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Abstract class for Pizza
abstract class Pizza {
    private final int id; // Using final for immutable field
    private String topping;
    private double price;

    // Constructor
    public Pizza(String topping, double price) {
        this.topping = topping;
        this.price = price;
        this.id = 0; // Assuming default id
    }

    // Getter and setter methods
    public String getTopping() {
        return topping;
    }

    public void setTopping(String topping) {
        this.topping = topping;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    // Static methods for database operations
    public static void addPizza(String table, String topping, double harga) {
        String sql = "INSERT INTO " + table + " (topping, harga) VALUES (?, ?)";
        try (Connection conn = new DB().con;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, topping);
            pstmt.setDouble(2, harga);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Overloaded method for adding pizza with description
    public static void addPizza(String table, String topping, double harga, String description) {
        String sql = "INSERT INTO " + table + " (topping, harga, description) VALUES (?, ?, ?)";
        try (Connection conn = new DB().con;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, topping);
            pstmt.setDouble(2, harga);
            pstmt.setString(3, description);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deletePizzaByIndex(String table, int index) throws SQLException {
        String sqlSelect = "SELECT id FROM " + table + " ORDER BY id LIMIT 1 OFFSET ?";
        String sqlDelete = "DELETE FROM " + table + " WHERE id = ?";
        try (Connection conn = new DB().con;
             PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect)) {
            pstmtSelect.setInt(1, index);
            ResultSet rs = pstmtSelect.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                try (PreparedStatement pstmtDelete = conn.prepareStatement(sqlDelete)) {
                    pstmtDelete.setInt(1, id);
                    int affectedRows = pstmtDelete.executeUpdate();
                    if (affectedRows == 0) {
                        throw new SQLException("Deleting pizza failed, no rows affected.");
                    }
                }
            } else {
                throw new SQLException("No pizza found at the given index.");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting pizza: " + e.getMessage());
            throw e;
        }
    }

    public static <T extends Pizza> List<T> loadAllPizzas(String table, Class<T> pizzaClass) {
        List<T> pizzas = new ArrayList<>();
        String sql = "SELECT topping, harga FROM " + table;
        try (Connection conn = new DB().con;
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String topping = rs.getString("topping");
                double harga = rs.getDouble("harga");
                T pizza = pizzaClass.getConstructor(String.class, double.class).newInstance(topping, harga);
                pizzas.add(pizza);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return pizzas;
    }

    public static void updatePizzaByIndex(String table, int index, String toppingBaru, double hargaBaru) {
        String sqlSelect = "SELECT id FROM " + table + " ORDER BY id LIMIT 1 OFFSET ?";
        String sqlUpdate = "UPDATE " + table + " SET topping = ?, harga = ? WHERE id = ?";
        try (Connection conn = new DB().con;
             PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect)) {
            pstmtSelect.setInt(1, index);
            ResultSet rs = pstmtSelect.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
                    pstmtUpdate.setString(1, toppingBaru);
                    pstmtUpdate.setDouble(2, hargaBaru);
                    pstmtUpdate.setInt(3, id);
                    pstmtUpdate.executeUpdate();
                }
            } else {
                throw new SQLException("No pizza found at the given index.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating pizza: " + e.getMessage());
        }
    }
}

// PizzaSpesial class
class PizzaSpesial extends Pizza {
    public PizzaSpesial(String topping, double price) {
        super(topping, price);
    }

    // Overriding a method to add specific behavior if necessary
    @Override
    public String getTopping() {
        return "Spesial: " + super.getTopping();
    }
}

// PizzaBiasa class
class PizzaBiasa extends Pizza {
    public PizzaBiasa(String topping, double price) {
        super(topping, price);
    }

    // Overriding a method to add specific behavior if necessary
    @Override
    public String getTopping() {
        return "Biasa: " + super.getTopping();
    }
}
